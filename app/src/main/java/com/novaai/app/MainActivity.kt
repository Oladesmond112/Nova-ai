package com.novaai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

private const val NOVA_API_URL = "https://nova-ai-backend.olamilekankolawole7.workers.dev/"

private val Night = Color(0xFF090B14)
private val Panel = Color(0xFF141827)
private val Violet = Color(0xFF8B75FF)
private val Cyan = Color(0xFF55D6E8)
private val SoftWhite = Color(0xFFF4F3FA)
private val Muted = Color(0xFF9CA3B8)

private data class ChatMessage(val role: String, val text: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NovaHome(
                    onSendMessage = { message, history ->
                        requestNovaReply(message, history)
                    }
                )
            }
        }
    }

    private suspend fun requestNovaReply(
        message: String,
        history: List<ChatMessage>
    ): Result<String> = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            connection = (URL(NOVA_API_URL).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 20000
                readTimeout = 60000
                doOutput = true
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
            }

            val historyJson = JSONArray()
            history.takeLast(20).forEach { item ->
                historyJson.put(
                    JSONObject()
                        .put("role", if (item.role == "assistant") "model" else "user")
                        .put("text", item.text)
                )
            }

            val payload = JSONObject()
                .put("message", message)
                .put("history", historyJson)

            connection.outputStream.use { output ->
                output.write(payload.toString().toByteArray(Charsets.UTF_8))
            }

            val status = connection.responseCode
            val responseStream = if (status in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }
            val responseText = responseStream?.bufferedReader()?.use { it.readText() }.orEmpty()
            val json = JSONObject(responseText)

            if (status !in 200..299 || !json.optBoolean("success", false)) {
                val error = json.optString("error", "NOVA could not complete that request.")
                val details = json.optJSONObject("details")?.optString("message").orEmpty()
                return@withContext Result.failure(
                    IllegalStateException(if (details.isNotBlank()) "$error\n$details" else error)
                )
            }

            val reply = json.optString("reply").trim()
            if (reply.isBlank()) {
                Result.failure(IllegalStateException("NOVA returned an empty response. Please try again."))
            } else {
                Result.success(reply)
            }
        } catch (error: Exception) {
            Result.failure(
                IllegalStateException(
                    error.message ?: "Couldn't connect to NOVA. Check your internet connection."
                )
            )
        } finally {
            connection?.disconnect()
        }
    }
}

@Composable
private fun NovaHome(
    onSendMessage: suspend (String, List<ChatMessage>) -> Result<String>
) {
    var prompt by remember { mutableStateOf("") }
    var selectedTool by remember { mutableStateOf("Chat") }
    var isSending by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize(), color = Night) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF15152B), Night, Night)))
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(42.dp).background(
                            Brush.linearGradient(listOf(Violet, Cyan)),
                            RoundedCornerShape(14.dp)
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "NOVA", tint = Color.White)
                    }
                    Spacer(Modifier.width(11.dp))
                    Column {
                        Text("NOVA AI", color = SoftWhite, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        Text("Your creative AI space", color = Muted, fontSize = 11.sp)
                    }
                }
                Text(
                    "ONLINE",
                    color = Cyan,
                    fontSize = 10.sp,
                    modifier = Modifier.background(Panel, CircleShape).padding(horizontal = 11.dp, vertical = 7.dp)
                )
            }

            if (messages.isEmpty()) {
                Spacer(Modifier.height(28.dp))
                Text("YOUR AI ASSISTANT", color = Cyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    "What can I help\nyou with today?",
                    color = SoftWhite,
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Ask a question, explore an idea, or get help with a task.",
                    color = Muted,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
                Spacer(Modifier.height(22.dp))
                Text("EXPLORE", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ToolCard("AI Chat", "Ask anything", selectedTool == "Chat", Modifier.weight(1f),
                        { Icon(Icons.Default.AutoAwesome, null, tint = Violet) }) { selectedTool = "Chat" }
                    ToolCard("Images", "Create visuals", selectedTool == "Images", Modifier.weight(1f),
                        { Icon(Icons.Default.Image, null, tint = Cyan) }) { selectedTool = "Images" }
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ToolCard("Video", "Make videos", selectedTool == "Video", Modifier.weight(1f),
                        { Icon(Icons.Default.VideoLibrary, null, tint = Cyan) }) { selectedTool = "Video" }
                    ToolCard("Voice", "Talk to NOVA", selectedTool == "Voice", Modifier.weight(1f),
                        { Icon(Icons.Default.Mic, null, tint = Violet) }) { selectedTool = "Voice" }
                }
                Spacer(Modifier.weight(1f))
            } else {
                Spacer(Modifier.height(14.dp))
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { message ->
                        MessageBubble(message)
                    }
                    if (isSending) {
                        item {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = Cyan,
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(10.dp))
                                Text("NOVA is thinking...", color = Muted, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            errorMessage?.let { error ->
                Text(error, color = Color(0xFFFF8A8A), fontSize = 12.sp, lineHeight = 17.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(22.dp)).padding(14.dp)
            ) {
                BasicTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSending,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = SoftWhite),
                    cursorBrush = Brush.verticalGradient(listOf(Violet, Cyan)),
                    decorationBox = { innerTextField ->
                        Box {
                            if (prompt.isEmpty()) Text("Message NOVA AI...", color = Muted, fontSize = 14.sp)
                            innerTextField()
                        }
                    }
                )
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("AI CHAT", color = Violet, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Box(
                        modifier = Modifier.size(40.dp).background(
                            Brush.linearGradient(listOf(Violet, Cyan)), CircleShape
                        ).clickable(enabled = !isSending && prompt.isNotBlank()) {
                            val question = prompt.trim()
                            val previousHistory = messages.toList()
                            prompt = ""
                            errorMessage = null
                            messages.add(ChatMessage("user", question))
                            isSending = true
                            scope.launch {
                                val result = onSendMessage(question, previousHistory)
                                result.onSuccess { answer ->
                                    messages.add(ChatMessage("assistant", answer))
                                }.onFailure { error ->
                                    errorMessage = error.message ?: "Something went wrong."
                                    messages.removeAt(messages.lastIndex)
                                    prompt = question
                                }
                                isSending = false
                            }
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSending) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Send, contentDescription = "Send message", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("NOVA AI can make mistakes. Check important information.", color = Muted, fontSize = 10.sp)
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isUser = message.role == "user"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .background(
                    if (isUser) Color(0xFF292448) else Panel,
                    RoundedCornerShape(18.dp)
                )
                .padding(14.dp)
        ) {
            Text(
                if (isUser) "YOU" else "NOVA AI",
                color = if (isUser) Cyan else Violet,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(message.text, color = SoftWhite, fontSize = 14.sp, lineHeight = 21.sp)
        }
    }
}

@Composable
private fun ToolCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(if (selected) Color(0xFF252342) else Panel, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        icon()
        Spacer(Modifier.height(13.dp))
        Text(title, color = SoftWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(3.dp))
        Text(subtitle, color = Muted, fontSize = 11.sp)
    }
}

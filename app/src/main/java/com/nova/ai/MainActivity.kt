package com.nova.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

private val Night = Color(0xFF080B14)
private val Panel = Color(0xFF121827)
private val Muted = Color(0xFF9AA6BC)
private val Cyan = Color(0xFF65E6F2)
private val Violet = Color(0xFF8C7BFF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NovaAIApp() }
    }
}

@Composable
fun NovaAIApp() {
    var prompt by remember { mutableStateOf("") }
    var sentPrompt by remember { mutableStateOf("") }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Night
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF11182A), Night, Color(0xFF090D19))
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Header()

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(Modifier.height(34.dp))
                    NovaOrb()
                    Spacer(Modifier.height(25.dp))
                    Text(
                        text = "Your ideas.\nA brighter intelligence.",
                        color = Color.White,
                        fontSize = 30.sp,
                        lineHeight = 37.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "I'm NOVA, your AI companion. What would you like to explore today?",
                        color = Muted,
                        fontSize = 15.sp,
                        lineHeight = 23.sp
                    )

                    Spacer(Modifier.height(28.dp))
                    Text(
                        text = "TRY ASKING",
                        color = Cyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Spacer(Modifier.height(12.dp))

                    SuggestionCard(
                        title = "Write something",
                        subtitle = "Messages, captions and creative ideas",
                        prompt = "Help me write a creative social media caption"
                    ) { prompt = it }

                    Spacer(Modifier.height(10.dp))
                    SuggestionCard(
                        title = "Learn something new",
                        subtitle = "Understand a topic step by step",
                        prompt = "Teach me something useful step by step"
                    ) { prompt = it }

                    Spacer(Modifier.height(10.dp))
                    SuggestionCard(
                        title = "Build an idea",
                        subtitle = "Plan a project and turn it into action",
                        prompt = "Help me plan and build a new project"
                    ) { prompt = it }

                    if (sentPrompt.isNotBlank()) {
                        Spacer(Modifier.height(22.dp))
                        Text("YOUR MESSAGE", color = Cyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        Spacer(Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Panel),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = sentPrompt,
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "The chat interface is ready. Connecting NOVA to an AI model is our next development step.",
                            color = Muted,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    }
                    Spacer(Modifier.height(18.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Panel, RoundedCornerShape(22.dp))
                        .padding(7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Message NOVA...", color = Muted) },
                        shape = RoundedCornerShape(18.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Cyan
                        )
                    )
                    Spacer(Modifier.width(5.dp))
                    Button(
                        onClick = {
                            if (prompt.isNotBlank()) {
                                sentPrompt = prompt.trim()
                                prompt = ""
                            }
                        },
                        enabled = prompt.isNotBlank(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Violet,
                            disabledContainerColor = Color(0xFF34364D)
                        ),
                        modifier = Modifier.height(52.dp)
                    ) {
                        Text("Send", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "NOVA AI  •  Early preview",
                    color = Color(0xFF65718A),
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        Brush.linearGradient(listOf(Cyan, Violet)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("N", color = Night, fontWeight = FontWeight.Black, fontSize = 20.sp)
            }
            Spacer(Modifier.width(10.dp))
            Text("NOVA", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text(" AI", color = Cyan, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Text("PREVIEW", color = Muted, fontSize = 10.sp, letterSpacing = 1.5.sp)
    }
}

@Composable
private fun NovaOrb() {
    Box(
        modifier = Modifier
            .size(94.dp)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF536DFF), Color(0xFF293D83), Color(0xFF172039))
                ),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .background(
                    Brush.linearGradient(listOf(Color.White, Cyan, Violet)),
                    CircleShape
                )
        )
    }
}

@Composable
private fun SuggestionCard(
    title: String,
    subtitle: String,
    prompt: String,
    onSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(prompt) },
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = Muted, fontSize = 12.sp)
        }
    }
}

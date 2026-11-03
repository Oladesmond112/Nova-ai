package com.novaai.app

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

private val Night = Color(0xFF090B14)
private val Panel = Color(0xFF141827)
private val Violet = Color(0xFF8B75FF)
private val Cyan = Color(0xFF55D6E8)
private val SoftWhite = Color(0xFFF4F3FA)
private val Muted = Color(0xFF9CA3B8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NovaHome()
            }
        }
    }
}

@Composable
private fun NovaHome() {
    var prompt by remember { mutableStateOf("") }
    var selectedTool by remember { mutableStateOf("Chat") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Night
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF15152B), Night, Night)
                    )
                )
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                Brush.linearGradient(listOf(Violet, Cyan)),
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "NOVA",
                            tint = Color.White
                        )
                    }
                    Spacer(Modifier.width(11.dp))
                    Column {
                        Text("NOVA AI", color = SoftWhite, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        Text("Your creative AI space", color = Muted, fontSize = 11.sp)
                    }
                }
                Text(
                    "0.1.0",
                    color = Cyan,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .background(Panel, CircleShape)
                        .padding(horizontal = 11.dp, vertical = 7.dp)
                )
            }

            Spacer(Modifier.height(34.dp))

            Text(
                "MAKE SOMETHING",
                color = Cyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "What can I help\nyou create today?",
                color = SoftWhite,
                fontSize = 31.sp,
                lineHeight = 37.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "Ask questions, explore ideas, and bring your creativity to life.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            Spacer(Modifier.height(25.dp))

            Text("EXPLORE TOOLS", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.6.sp)
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ToolCard(
                    title = "AI Chat",
                    subtitle = "Ask anything",
                    selected = selectedTool == "Chat",
                    modifier = Modifier.weight(1f),
                    icon = { Icon(Icons.Default.AutoAwesome, null, tint = Violet) }
                ) { selectedTool = "Chat" }
                ToolCard(
                    title = "Images",
                    subtitle = "Create visuals",
                    selected = selectedTool == "Images",
                    modifier = Modifier.weight(1f),
                    icon = { Icon(Icons.Default.Image, null, tint = Cyan) }
                ) { selectedTool = "Images" }
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ToolCard(
                    title = "Video",
                    subtitle = "Make videos",
                    selected = selectedTool == "Video",
                    modifier = Modifier.weight(1f),
                    icon = { Icon(Icons.Default.VideoLibrary, null, tint = Cyan) }
                ) { selectedTool = "Video" }
                ToolCard(
                    title = "Voice",
                    subtitle = "Talk to NOVA",
                    selected = selectedTool == "Voice",
                    modifier = Modifier.weight(1f),
                    icon = { Icon(Icons.Default.Mic, null, tint = Violet) }
                ) { selectedTool = "Voice" }
            }

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Panel, RoundedCornerShape(22.dp))
                    .padding(15.dp)
            ) {
                BasicTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = SoftWhite),
                    cursorBrush = Brush.verticalGradient(listOf(Violet, Cyan)),
                    decorationBox = { innerTextField ->
                        Box {
                            if (prompt.isEmpty()) {
                                Text(
                                    "Message NOVA AI...",
                                    color = Muted,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(selectedTool, color = Violet, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                Brush.linearGradient(listOf(Violet, Cyan)),
                                CircleShape
                            )
                            .clickable { prompt = "" },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(17.dp))
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "NOVA AI is getting ready. AI features will connect in a later build.",
                color = Muted,
                fontSize = 10.sp,
                lineHeight = 14.sp
            )
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
            .background(
                if (selected) Color(0xFF252342) else Panel,
                RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        icon()
        Spacer(Modifier.height(15.dp))
        Text(title, color = SoftWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(3.dp))
        Text(subtitle, color = Muted, fontSize = 11.sp)
    }
}

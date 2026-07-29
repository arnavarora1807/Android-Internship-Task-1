package com.example.a1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.a1.ui.theme.A1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AIAssistantApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

sealed class Screen {
    object Welcome : Screen()
    data class Chat(val question: String) : Screen()
}

@Composable
fun AIAssistantApp(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val screen = currentScreen) {
            is Screen.Welcome -> WelcomeScreen { question ->
                currentScreen = Screen.Chat(question)
            }
            is Screen.Chat -> ChatScreen(
                question = screen.question,
                onSendNew = { newQuestion ->
                    currentScreen = Screen.Chat(newQuestion)
                }
            )
        }
    }
}

@Composable
fun WelcomeScreen(onSend: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AI Assistant",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "How can I help you?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Ask me anything...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { if (text.isNotBlank()) onSend(text) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Send")
        }
    }
}

@Composable
fun ChatScreen(question: String, onSendNew: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Chat History Area
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // User Question
            ChatBubble(
                text = question,
                isUser = true,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // AI Response
            ChatBubble(
                text = "Kotlin is a programming language used for Android development.",
                isUser = false,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        // Bottom Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Ask me anything...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onSendNew(text)
                        text = ""
                    }
                },
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun ChatBubble(text: String, isUser: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = if (isUser) 16.dp else 0.dp,
            bottomEnd = if (isUser) 0.dp else 16.dp
        ),
        color = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

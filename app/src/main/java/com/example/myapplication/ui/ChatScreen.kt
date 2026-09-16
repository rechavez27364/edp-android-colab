package com.example.myapplication.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.domain.Message
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel(factory = ChatViewModel.Factory)
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("LiceoChat", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { viewModel.load() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        bottomBar = {
            ChatInputBar(
                name = viewModel.myName,
                onNameChange = { viewModel.onNameChange(it) },
                draft = viewModel.draft,
                onDraftChange = { viewModel.onDraftChange(it) },
                onSend = { viewModel.send() }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = viewModel.uiState) {
                is ChatUiState.Loading -> CircularProgressIndicator()
                is ChatUiState.Empty -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No messages yet.", color = Color.White)
                        Button(onClick = { viewModel.load() }) { Text("Refresh") }
                    }
                }
                is ChatUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.load() }) { Text("Retry") }
                    }
                }
                is ChatUiState.Ready -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        reverseLayout = true
                    ) {
                        items(state.messages) { message ->
                            ChatBubble(message = message, isMe = message.sender == viewModel.myName)
                        }
                    }
                }
            }
        }
    }

    viewModel.errorMessage?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) { Text("OK") }
            },
            title = { Text("Error") },
            text = { Text(error) }
        )
    }
}

@Composable
fun ChatBubble(message: Message, isMe: Boolean) {
    val alignment = if (isMe) Alignment.End else Alignment.Start
    val color = if (isMe) Color(0xFF001F3F) else Color(0xFF1B1B1B) // Navy vs Dark Gray
    val textColor = Color(0xFF0074D9) // Bright Blue

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        Card(
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (isMe) 12.dp else 0.dp,
                bottomEnd = if (isMe) 0.dp else 12.dp
            ),
            colors = CardDefaults.cardColors(containerColor = color),
            border = BorderStroke(1.dp, Color(0xFF004080))
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                if (!isMe) {
                    Text(
                        text = message.sender,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = message.text, 
                    color = textColor,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formatTime(message.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End),
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ChatInputBar(
    name: String,
    onNameChange: (String) -> Unit,
    draft: String,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        color = Color.Black,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Your Name", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0074D9),
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = Color.White
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = draft,
                    onValueChange = onDraftChange,
                    label = { Text("Message", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    maxLines = 3,
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0074D9),
                        unfocusedBorderColor = Color.DarkGray,
                        cursorColor = Color.White
                    )
                )
                IconButton(
                    onClick = onSend, 
                    enabled = name.isNotBlank() && draft.isNotBlank(),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFF0074D9))
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }
        }
    }
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

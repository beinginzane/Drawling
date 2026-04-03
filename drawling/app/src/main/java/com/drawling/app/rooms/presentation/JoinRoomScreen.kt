package com.drawling.app.rooms.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinRoomScreen(
    initialCode: String = "",
    onJoined: (String) -> Unit,
    viewModel: RoomViewModel = hiltViewModel()
) {
    var code by remember { mutableStateOf(initialCode) }
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Join Partner's Room") })
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("💕", fontSize = 64.sp)
            Spacer(Modifier.height(16.dp))
            Text("Enter invite code", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(32.dp))
            OutlinedTextField(
                value = code,
                onValueChange = { code = it.uppercase() },
                label = { Text("Invite Code") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            state.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { viewModel.joinRoom(code, onJoined) },
                enabled = code.isNotBlank() && !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                else Text("Join Room")
            }
        }
    }
}

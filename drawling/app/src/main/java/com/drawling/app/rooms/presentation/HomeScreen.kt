package com.drawling.app.rooms.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCanvas: (String, String) -> Unit,
    onNavigateToGallery: (String) -> Unit,
    onNavigateToJoin: () -> Unit,
    viewModel: RoomViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showInviteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.inviteCode) {
        if (state.inviteCode != null) showInviteDialog = true
    }

    if (showInviteDialog && state.inviteCode != null) {
        AlertDialog(
            onDismissRequest = { showInviteDialog = false; viewModel.clearInviteCode() },
            title = { Text("Invite Your Partner 💕") },
            text = {
                Column {
                    Text("Share this code with your partner:")
                    Spacer(Modifier.height(8.dp))
                    Text(
                        state.inviteCode!!,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showInviteDialog = false; viewModel.clearInviteCode() }) {
                    Text("Done")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Drawling ✏️", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToJoin) {
                        Icon(Icons.Default.Add, contentDescription = "Join Room")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // Couple Room Card
                RoomCard(
                    icon = "💕",
                    title = "Together Canvas",
                    subtitle = if (state.coupleRoom != null) "Draw with your partner" else "Start a couple room",
                    onClick = {
                        if (state.coupleRoom != null) onNavigateToCanvas(state.coupleRoom!!.id, "current")
                        else viewModel.createCoupleRoom()
                    },
                    onGalleryClick = state.coupleRoom?.let { { onNavigateToGallery(it.id) } }
                )

                // Personal Room Card
                RoomCard(
                    icon = "🎨",
                    title = "My Studio",
                    subtitle = if (state.personalRoom != null) "Your private canvases" else "Create your personal studio",
                    onClick = {
                        if (state.personalRoom != null) onNavigateToCanvas(state.personalRoom!!.id, "new")
                        else viewModel.createPersonalRoom()
                    },
                    onGalleryClick = state.personalRoom?.let { { onNavigateToGallery(it.id) } }
                )

                state.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun RoomCard(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    onGalleryClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 40.sp)
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
                Text(subtitle, style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)))
            }
            onGalleryClick?.let {
                IconButton(onClick = it) {
                    Icon(Icons.Default.Person, contentDescription = "Gallery", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

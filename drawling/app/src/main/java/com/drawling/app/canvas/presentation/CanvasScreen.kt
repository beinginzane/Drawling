package com.drawling.app.canvas.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

val brushColors = listOf(
    Color.Black, Color(0xFFE53935), Color(0xFF1E88E5), Color(0xFF43A047),
    Color(0xFFFDD835), Color(0xFFE91E63), Color(0xFF6200EA), Color(0xFFFF6D00),
    Color(0xFF795548), Color.White
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanvasScreen(
    roomId: String,
    canvasId: String,
    onNavigateBack: () -> Unit,
    onNavigateToGallery: () -> Unit,
    viewModel: CanvasViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isCouple = canvasId == "current"
    var showSurpriseDialog by remember { mutableStateOf(false) }

    LaunchedEffect(roomId) { viewModel.initialize(roomId, isCouple) }

    if (showSurpriseDialog) {
        AlertDialog(
            onDismissRequest = { showSurpriseDialog = false },
            title = { Text("Send as Surprise? 🎁") },
            text = { Text("This will seal the drawing and send it to your partner. You won't be able to edit it after.") },
            confirmButton = {
                Button(onClick = { showSurpriseDialog = false /* TODO: call deliver */ }) { Text("Deliver 💕") }
            },
            dismissButton = {
                TextButton(onClick = { showSurpriseDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (isCouple) "Together ✏️" else "My Studio 🎨")
                        if (state.partnerConnected) {
                            Spacer(Modifier.width(8.dp))
                            Text("• partner online", style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF43A047)))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.saveCanvas(roomId); onNavigateBack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (state.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp).padding(end = 8.dp))
                    }
                    IconButton(onClick = onNavigateToGallery) {
                        Icon(Icons.Default.FavoriteBorder, "Gallery")
                    }
                    if (!isCouple) {
                        IconButton(onClick = { showSurpriseDialog = true }) {
                            Icon(Icons.Default.Favorite, "Deliver Surprise")
                        }
                    }
                }
            )
        },
        bottomBar = {
            CanvasToolbar(
                brushSettings = state.brushSettings,
                onColorSelected = viewModel::setBrushColor,
                onSizeChanged = viewModel::setBrushSize,
                onToggleEraser = viewModel::toggleEraser,
                onUndo = viewModel::undo,
                onClear = viewModel::clearCanvas
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                DrawingCanvas(
                    strokes = state.strokes,
                    currentStroke = state.currentStroke,
                    onDrawStart = viewModel::onDrawStart,
                    onDrawMove = viewModel::onDrawMove,
                    onDrawEnd = viewModel::onDrawEnd
                )
            }
        }
    }
}

@Composable
fun CanvasToolbar(
    brushSettings: com.drawling.app.canvas.domain.model.BrushSettings,
    onColorSelected: (Color) -> Unit,
    onSizeChanged: (Float) -> Unit,
    onToggleEraser: () -> Unit,
    onUndo: () -> Unit,
    onClear: () -> Unit
) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface).padding(8.dp)) {
        // Color palette
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 4.dp)) {
            items(brushColors) { color ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (brushSettings.color == color && !brushSettings.isEraser) 2.dp else 0.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(color) }
                )
            }
        }
        // Brush size + tools
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Slider(
                value = brushSettings.strokeWidth,
                onValueChange = onSizeChanged,
                valueRange = 2f..40f,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            )
            IconButton(onClick = onToggleEraser) {
                Text(if (brushSettings.isEraser) "✏️" else "⬜", style = MaterialTheme.typography.bodyLarge)
            }
            IconButton(onClick = onUndo) { Icon(Icons.Default.ArrowBack, "Undo") }
            IconButton(onClick = onClear) { Icon(Icons.Default.Delete, "Clear") }
        }
    }
}

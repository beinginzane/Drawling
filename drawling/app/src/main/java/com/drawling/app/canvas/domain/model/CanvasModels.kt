package com.drawling.app.canvas.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset

data class DrawingStroke(
    val id: String = java.util.UUID.randomUUID().toString(),
    val points: List<Offset> = emptyList(),
    val color: Color = Color.Black,
    val strokeWidth: Float = 8f,
    val isEraser: Boolean = false
)

data class BrushSettings(
    val color: Color = Color.Black,
    val strokeWidth: Float = 8f,
    val isEraser: Boolean = false
)

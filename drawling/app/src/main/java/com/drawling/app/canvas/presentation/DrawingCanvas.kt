package com.drawling.app.canvas.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.drawling.app.canvas.domain.model.DrawingStroke

@Composable
fun DrawingCanvas(
    strokes: List<DrawingStroke>,
    currentStroke: DrawingStroke?,
    onDrawStart: (Offset) -> Unit,
    onDrawMove: (Offset) -> Unit,
    onDrawEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onDrawStart(it) },
                    onDrag = { change, _ -> onDrawMove(change.position) },
                    onDragEnd = { onDrawEnd() }
                )
            }
    ) {
        strokes.forEach { stroke -> drawStroke(stroke) }
        currentStroke?.let { drawStroke(it) }
    }
}

private fun DrawScope.drawStroke(stroke: DrawingStroke) {
    if (stroke.points.size < 2) {
        stroke.points.firstOrNull()?.let { point ->
            drawCircle(
                color = if (stroke.isEraser) Color.White else stroke.color,
                radius = stroke.strokeWidth / 2,
                center = point
            )
        }
        return
    }

    val path = Path().apply {
        moveTo(stroke.points.first().x, stroke.points.first().y)
        for (i in 1 until stroke.points.size) {
            val prev = stroke.points[i - 1]
            val curr = stroke.points[i]
            quadraticBezierTo(prev.x, prev.y, (prev.x + curr.x) / 2, (prev.y + curr.y) / 2)
        }
        stroke.points.lastOrNull()?.let { lineTo(it.x, it.y) }
    }

    drawPath(
        path = path,
        color = if (stroke.isEraser) Color.White else stroke.color,
        style = Stroke(
            width = stroke.strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        ),
        blendMode = if (stroke.isEraser) BlendMode.Clear else BlendMode.SrcOver
    )
}

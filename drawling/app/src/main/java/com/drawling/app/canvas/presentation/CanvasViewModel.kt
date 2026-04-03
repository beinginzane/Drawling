package com.drawling.app.canvas.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drawling.app.canvas.domain.model.BrushSettings
import com.drawling.app.canvas.domain.model.DrawingStroke
import com.drawling.app.canvas.domain.repository.CanvasRepository
import com.drawling.app.network.SocketManager
import com.drawling.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

data class CanvasState(
    val strokes: List<DrawingStroke> = emptyList(),
    val currentStroke: DrawingStroke? = null,
    val brushSettings: BrushSettings = BrushSettings(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val partnerConnected: Boolean = false
)

@HiltViewModel
class CanvasViewModel @Inject constructor(
    private val canvasRepository: CanvasRepository,
    private val socketManager: SocketManager
) : ViewModel() {

    private val _state = MutableStateFlow(CanvasState())
    val state: StateFlow<CanvasState> = _state

    fun initialize(roomId: String, isCouple: Boolean) {
        loadCanvasState(roomId)
        if (isCouple) {
            socketManager.joinRoom(roomId)
            listenForRemoteStrokes()
            listenForPartnerEvents()
            scheduleAutoSave(roomId)
        }
    }

    private fun loadCanvasState(roomId: String) = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true)
        when (val result = canvasRepository.getCanvasState(roomId)) {
            is Resource.Success -> _state.value = _state.value.copy(isLoading = false, strokes = result.data)
            is Resource.Error -> _state.value = _state.value.copy(isLoading = false, error = result.message)
            else -> Unit
        }
    }

    private fun listenForRemoteStrokes() = viewModelScope.launch {
        socketManager.strokeEvents.collectLatest { json ->
            val points = mutableListOf<Offset>()
            val pts = json.optJSONArray("points") ?: return@collectLatest
            for (i in 0 until pts.length()) {
                val p = pts.getJSONObject(i)
                points.add(Offset(p.getDouble("x").toFloat(), p.getDouble("y").toFloat()))
            }
            val stroke = DrawingStroke(
                id = json.optString("id"),
                points = points,
                color = try { Color(android.graphics.Color.parseColor(json.optString("color", "#000000"))) } catch (e: Exception) { Color.Black },
                strokeWidth = json.optDouble("strokeWidth", 8.0).toFloat(),
                isEraser = json.optBoolean("isEraser", false)
            )
            _state.value = _state.value.copy(strokes = _state.value.strokes + stroke)
        }
    }

    private fun listenForPartnerEvents() = viewModelScope.launch {
        socketManager.partnerJoined.collectLatest {
            _state.value = _state.value.copy(partnerConnected = true)
        }
    }

    fun onDrawStart(offset: Offset) {
        val stroke = DrawingStroke(
            points = listOf(offset),
            color = _state.value.brushSettings.color,
            strokeWidth = _state.value.brushSettings.strokeWidth,
            isEraser = _state.value.brushSettings.isEraser
        )
        _state.value = _state.value.copy(currentStroke = stroke)
    }

    fun onDrawMove(offset: Offset) {
        val current = _state.value.currentStroke ?: return
        _state.value = _state.value.copy(
            currentStroke = current.copy(points = current.points + offset)
        )
    }

    fun onDrawEnd() {
        val current = _state.value.currentStroke ?: return
        val updatedStrokes = _state.value.strokes + current
        _state.value = _state.value.copy(strokes = updatedStrokes, currentStroke = null)
        emitStroke(current)
    }

    private fun emitStroke(stroke: DrawingStroke) {
        val json = JSONObject().apply {
            put("id", stroke.id)
            put("color", String.format("#%06X", 0xFFFFFF and stroke.color.hashCode()))
            put("strokeWidth", stroke.strokeWidth)
            put("isEraser", stroke.isEraser)
            put("points", JSONArray().apply {
                stroke.points.forEach { put(JSONObject().apply { put("x", it.x); put("y", it.y) }) }
            })
        }
        socketManager.sendStroke(json)
    }

    fun undo() {
        if (_state.value.strokes.isNotEmpty()) {
            _state.value = _state.value.copy(strokes = _state.value.strokes.dropLast(1))
        }
    }

    fun clearCanvas() {
        _state.value = _state.value.copy(strokes = emptyList())
        socketManager.sendClear()
    }

    fun setBrushColor(color: Color) {
        _state.value = _state.value.copy(brushSettings = _state.value.brushSettings.copy(color = color, isEraser = false))
    }

    fun setBrushSize(size: Float) {
        _state.value = _state.value.copy(brushSettings = _state.value.brushSettings.copy(strokeWidth = size))
    }

    fun toggleEraser() {
        _state.value = _state.value.copy(brushSettings = _state.value.brushSettings.copy(isEraser = !_state.value.brushSettings.isEraser))
    }

    fun saveCanvas(roomId: String) = viewModelScope.launch {
        _state.value = _state.value.copy(isSaving = true)
        // In real implementation, render canvas to bitmap and convert to base64
        canvasRepository.saveCanvas(roomId, "")
        _state.value = _state.value.copy(isSaving = false)
    }

    private fun scheduleAutoSave(roomId: String) = viewModelScope.launch {
        while (true) {
            delay(5 * 60 * 1000L) // 5 minutes
            saveCanvas(roomId)
        }
    }
}

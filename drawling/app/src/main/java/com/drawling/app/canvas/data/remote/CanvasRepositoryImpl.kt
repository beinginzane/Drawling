package com.drawling.app.canvas.data.remote

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.drawling.app.canvas.data.remote.dto.CanvasStateDto
import com.drawling.app.canvas.domain.model.DrawingStroke
import com.drawling.app.canvas.domain.repository.CanvasRepository
import com.drawling.app.network.ApiService
import com.drawling.app.utils.Resource
import javax.inject.Inject

class CanvasRepositoryImpl @Inject constructor(
    private val api: ApiService
) : CanvasRepository {

    override suspend fun getCanvasState(roomId: String): Resource<List<DrawingStroke>> = try {
        val response = api.getCanvasState(roomId)
        if (response.isSuccessful && response.body() != null) {
            Resource.Success(response.body()!!.strokes.map { stroke ->
                DrawingStroke(
                    id = stroke.id,
                    points = stroke.points.map { Offset(it.x, it.y) },
                    color = try { Color(android.graphics.Color.parseColor(stroke.color)) } catch (e: Exception) { Color.Black },
                    strokeWidth = stroke.strokeWidth,
                    isEraser = stroke.isEraser
                )
            })
        } else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }

    override suspend fun saveCanvas(roomId: String, imageBase64: String): Resource<Unit> = try {
        val response = api.saveCanvas(roomId, mapOf("imageData" to imageBase64))
        if (response.isSuccessful) Resource.Success(Unit)
        else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }
}

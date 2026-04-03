package com.drawling.app.canvas.domain.repository

import com.drawling.app.canvas.domain.model.DrawingStroke
import com.drawling.app.utils.Resource

interface CanvasRepository {
    suspend fun getCanvasState(roomId: String): Resource<List<DrawingStroke>>
    suspend fun saveCanvas(roomId: String, imageBase64: String): Resource<Unit>
}

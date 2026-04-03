package com.drawling.app.domain.usecase.canvas

import com.drawling.app.domain.repository.CanvasRepository
import javax.inject.Inject

class CreateCanvasUseCase @Inject constructor(private val repo: CanvasRepository) {
    suspend operator fun invoke(roomId: String, title: String) = repo.createCanvas(roomId, title)
}

class GetCanvasesUseCase @Inject constructor(private val repo: CanvasRepository) {
    suspend operator fun invoke(roomId: String) = repo.getCanvasesByRoom(roomId)
}

class SaveCanvasUseCase @Inject constructor(private val repo: CanvasRepository) {
    suspend operator fun invoke(canvasId: String, imageData: ByteArray) =
        repo.saveCanvas(canvasId, imageData)
}

class DeleteCanvasUseCase @Inject constructor(private val repo: CanvasRepository) {
    suspend operator fun invoke(canvasId: String) = repo.deleteCanvas(canvasId)
}

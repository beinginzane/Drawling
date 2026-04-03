package com.drawling.app.domain.repository

import com.drawling.app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun sendOtp(phoneNumber: String): Result<Unit>
    suspend fun verifyOtp(phoneNumber: String, otp: String): Result<String> // returns JWT
    suspend fun registerPasskey(userId: String): Result<Unit>
    suspend fun authenticateWithPasskey(): Result<String>
    suspend fun joinAsGuest(inviteCode: String): Result<String>
    suspend fun getStoredToken(): String?
    suspend fun saveToken(token: String)
    suspend fun clearToken()
    fun getCurrentUser(): Flow<User?>
}

interface RoomRepository {
    suspend fun createCoupleRoom(): Result<Room>
    suspend fun createPersonalRoom(): Result<Room>
    suspend fun joinRoom(inviteCode: String): Result<Room>
    suspend fun getRoomById(roomId: String): Result<Room>
    suspend fun getMyRooms(): Result<List<Room>>
    fun observeRooms(): Flow<List<Room>>
}

interface CanvasRepository {
    suspend fun createCanvas(roomId: String, title: String): Result<Canvas>
    suspend fun getCanvasesByRoom(roomId: String): Result<List<Canvas>>
    suspend fun getCanvasById(canvasId: String): Result<Canvas>
    suspend fun saveCanvas(canvasId: String, imageData: ByteArray): Result<Canvas>
    suspend fun deleteCanvas(canvasId: String): Result<Unit>
    fun observeCanvases(roomId: String): Flow<List<Canvas>>
}

interface GalleryRepository {
    suspend fun getGallery(roomId: String): Result<List<Canvas>>
    suspend fun deleteFromGallery(canvasId: String): Result<Unit>
}

interface SurpriseRepository {
    suspend fun deliverSurprise(canvasId: String, receiverId: String): Result<Surprise>
    suspend fun getPendingSurprises(): Result<List<Surprise>>
    suspend fun openSurprise(surpriseId: String): Result<Surprise>
    fun observePendingSurprises(): Flow<List<Surprise>>
}

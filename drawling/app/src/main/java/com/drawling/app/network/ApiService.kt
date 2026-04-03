package com.drawling.app.network

import com.drawling.app.auth.data.remote.dto.AuthResponseDto
import com.drawling.app.auth.data.remote.dto.SendOtpRequestDto
import com.drawling.app.auth.data.remote.dto.VerifyOtpRequestDto
import com.drawling.app.canvas.data.remote.dto.CanvasStateDto
import com.drawling.app.gallery.data.remote.dto.GalleryItemDto
import com.drawling.app.rooms.data.remote.dto.CreateRoomResponseDto
import com.drawling.app.rooms.data.remote.dto.JoinRoomRequestDto
import com.drawling.app.rooms.data.remote.dto.RoomDto
import com.drawling.app.surprises.data.remote.dto.SurpriseDto
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/send-otp") suspend fun sendOtp(@Body request: SendOtpRequestDto): Response<Unit>
    @POST("auth/verify-otp") suspend fun verifyOtp(@Body request: VerifyOtpRequestDto): Response<AuthResponseDto>
    @POST("auth/refresh") suspend fun refreshToken(@Header("Authorization") token: String): Response<AuthResponseDto>
    @GET("rooms/mine") suspend fun getMyRooms(): Response<List<RoomDto>>
    @POST("rooms/couple") suspend fun createCoupleRoom(): Response<CreateRoomResponseDto>
    @POST("rooms/personal") suspend fun createPersonalRoom(): Response<RoomDto>
    @POST("rooms/join") suspend fun joinRoom(@Body request: JoinRoomRequestDto): Response<RoomDto>
    @GET("rooms/{roomId}") suspend fun getRoomById(@Path("roomId") roomId: String): Response<RoomDto>
    @GET("canvas/{roomId}/state") suspend fun getCanvasState(@Path("roomId") roomId: String): Response<CanvasStateDto>
    @POST("canvas/{roomId}/save") suspend fun saveCanvas(@Path("roomId") roomId: String, @Body data: Map<String, String>): Response<Unit>
    @GET("gallery/{roomId}") suspend fun getGallery(@Path("roomId") roomId: String): Response<List<GalleryItemDto>>
    @DELETE("gallery/{canvasId}") suspend fun deleteCanvas(@Path("canvasId") canvasId: String): Response<Unit>
    @POST("surprises/deliver") suspend fun deliverSurprise(@Body body: Map<String, String>): Response<SurpriseDto>
    @GET("surprises/pending") suspend fun getPendingSurprises(): Response<List<SurpriseDto>>
    @POST("surprises/{id}/open") suspend fun openSurprise(@Path("id") surpriseId: String): Response<SurpriseDto>
}

package com.drawling.app.data.remote.api

import com.drawling.app.data.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface AuthApi {
    @POST("auth/send-otp")
    suspend fun sendOtp(@Body body: SendOtpRequest): BaseResponse

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body body: VerifyOtpRequest): TokenResponse

    @POST("auth/guest")
    suspend fun joinAsGuest(@Body body: GuestJoinRequest): TokenResponse

    @GET("auth/me")
    suspend fun getMe(): UserDto
}

interface RoomApi {
    @POST("rooms/couple")
    suspend fun createCoupleRoom(): RoomDto

    @POST("rooms/personal")
    suspend fun createPersonalRoom(): RoomDto

    @POST("rooms/join")
    suspend fun joinRoom(@Body body: JoinRoomRequest): RoomDto

    @GET("rooms/{roomId}")
    suspend fun getRoomById(@Path("roomId") roomId: String): RoomDto

    @GET("rooms/mine")
    suspend fun getMyRooms(): List<RoomDto>
}

interface CanvasApi {
    @POST("canvases")
    suspend fun createCanvas(@Body body: CreateCanvasRequest): CanvasDto

    @GET("canvases/room/{roomId}")
    suspend fun getCanvasesByRoom(@Path("roomId") roomId: String): List<CanvasDto>

    @GET("canvases/{canvasId}")
    suspend fun getCanvasById(@Path("canvasId") canvasId: String): CanvasDto

    @Multipart
    @PUT("canvases/{canvasId}/save")
    suspend fun saveCanvas(
        @Path("canvasId") canvasId: String,
        @Part image: MultipartBody.Part
    ): CanvasDto

    @DELETE("canvases/{canvasId}")
    suspend fun deleteCanvas(@Path("canvasId") canvasId: String): BaseResponse
}

interface SurpriseApi {
    @POST("surprises/deliver")
    suspend fun deliverSurprise(@Body body: DeliverSurpriseRequest): SurpriseDto

    @GET("surprises/pending")
    suspend fun getPendingSurprises(): List<SurpriseDto>

    @POST("surprises/{surpriseId}/open")
    suspend fun openSurprise(@Path("surpriseId") surpriseId: String): SurpriseDto
}

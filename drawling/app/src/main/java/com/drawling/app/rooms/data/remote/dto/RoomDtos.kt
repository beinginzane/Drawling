package com.drawling.app.rooms.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RoomDto(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("ownerId") val ownerId: String,
    @SerializedName("partnerId") val partnerId: String?,
    @SerializedName("inviteCode") val inviteCode: String?,
    @SerializedName("createdAt") val createdAt: String
)

data class CreateRoomResponseDto(
    @SerializedName("room") val room: RoomDto,
    @SerializedName("inviteCode") val inviteCode: String
)

data class JoinRoomRequestDto(
    @SerializedName("inviteCode") val inviteCode: String
)

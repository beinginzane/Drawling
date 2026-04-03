package com.drawling.app.surprises.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SurpriseDto(
    @SerializedName("id") val id: String,
    @SerializedName("canvasId") val canvasId: String,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("receiverId") val receiverId: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("status") val status: String,
    @SerializedName("deliveredAt") val deliveredAt: String?,
    @SerializedName("openedAt") val openedAt: String?
)

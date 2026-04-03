package com.drawling.app.gallery.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GalleryItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("roomId") val roomId: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("title") val title: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("isDeleted") val isDeleted: Boolean
)

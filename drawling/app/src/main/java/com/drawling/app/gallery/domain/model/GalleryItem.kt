package com.drawling.app.gallery.domain.model

data class GalleryItem(
    val id: String,
    val roomId: String,
    val imageUrl: String,
    val title: String,
    val createdAt: String
)

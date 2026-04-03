package com.drawling.app.surprises.domain.model

data class Surprise(
    val id: String,
    val canvasId: String,
    val senderId: String,
    val receiverId: String,
    val imageUrl: String,
    val status: SurpriseStatus,
    val deliveredAt: String?,
    val openedAt: String?
)

enum class SurpriseStatus { PENDING, OPENED }

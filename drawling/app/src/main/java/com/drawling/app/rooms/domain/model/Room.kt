package com.drawling.app.rooms.domain.model

data class Room(
    val id: String,
    val type: RoomType,
    val ownerId: String,
    val partnerId: String?,
    val inviteCode: String?,
    val createdAt: String
)

enum class RoomType { COUPLE, PERSONAL }

package com.drawling.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val phoneNumber: String,
    val displayName: String?,
    val avatarUrl: String?,
    val isGuest: Boolean = false
) : Parcelable

@Parcelize
data class Room(
    val id: String,
    val type: RoomType,
    val ownerId: String,
    val partnerId: String?,
    val inviteCode: String?,
    val createdAt: Long
) : Parcelable

enum class RoomType { COUPLE, PERSONAL }

@Parcelize
data class Canvas(
    val id: String,
    val roomId: String,
    val title: String,
    val orderIndex: Int,
    val thumbnailUrl: String?,
    val imageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
) : Parcelable

@Parcelize
data class Stroke(
    val id: String,
    val canvasId: String,
    val userId: String,
    val points: List<StrokePoint>,
    val color: String,
    val strokeWidth: Float,
    val tool: DrawingTool,
    val timestamp: Long
) : Parcelable

@Parcelize
data class StrokePoint(
    val x: Float,
    val y: Float,
    val pressure: Float = 1f
) : Parcelable

enum class DrawingTool { BRUSH, ERASER }

@Parcelize
data class Surprise(
    val id: String,
    val canvasId: String,
    val senderId: String,
    val receiverId: String,
    val deliveredAt: Long?,
    val openedAt: Long?,
    val status: SurpriseStatus,
    val canvas: Canvas?
) : Parcelable

enum class SurpriseStatus { PENDING, OPENED }

data class AuthState(
    val isLoggedIn: Boolean = false,
    val isGuest: Boolean = false,
    val user: User? = null,
    val token: String? = null
)

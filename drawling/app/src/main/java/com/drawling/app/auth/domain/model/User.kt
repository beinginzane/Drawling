package com.drawling.app.auth.domain.model

data class User(val id: String, val phoneNumber: String, val accessToken: String, val refreshToken: String)

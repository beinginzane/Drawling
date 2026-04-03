package com.drawling.app.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SendOtpRequestDto(@SerializedName("phoneNumber") val phoneNumber: String)
data class VerifyOtpRequestDto(
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("otp") val otp: String,
    @SerializedName("firebaseToken") val firebaseToken: String
)
data class AuthResponseDto(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("isNewUser") val isNewUser: Boolean = false
)

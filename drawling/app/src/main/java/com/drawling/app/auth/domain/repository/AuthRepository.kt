package com.drawling.app.auth.domain.repository

import com.drawling.app.utils.Resource

interface AuthRepository {
    suspend fun sendOtp(phoneNumber: String): Resource<Unit>
    suspend fun verifyOtp(phoneNumber: String, otp: String): Resource<String>
    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
}

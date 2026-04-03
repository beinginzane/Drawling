package com.drawling.app.auth.data.remote

import com.drawling.app.auth.data.local.TokenDataStore
import com.drawling.app.auth.data.remote.dto.SendOtpRequestDto
import com.drawling.app.auth.data.remote.dto.VerifyOtpRequestDto
import com.drawling.app.auth.domain.repository.AuthRepository
import com.drawling.app.network.ApiService
import com.drawling.app.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val tokenStore: TokenDataStore,
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun sendOtp(phoneNumber: String): Resource<Unit> = try {
        val response = api.sendOtp(SendOtpRequestDto(phoneNumber))
        if (response.isSuccessful) Resource.Success(Unit) else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Network error") }

    override suspend fun verifyOtp(phoneNumber: String, otp: String): Resource<String> = try {
        val firebaseToken = firebaseAuth.currentUser?.getIdToken(false)?.await()?.token ?: ""
        val response = api.verifyOtp(VerifyOtpRequestDto(phoneNumber, otp, firebaseToken))
        if (response.isSuccessful && response.body() != null) {
            val body = response.body()!!
            tokenStore.saveTokens(body.accessToken, body.refreshToken, body.userId)
            Resource.Success(body.userId)
        } else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Verification failed") }

    override suspend fun isLoggedIn(): Boolean = try {
        tokenStore.accessToken.first() != null
    } catch (e: Exception) { false }

    override suspend fun logout() {
        tokenStore.clearTokens()
        firebaseAuth.signOut()
    }
}

package com.drawling.app.domain.usecase.auth

import com.drawling.app.domain.repository.AuthRepository
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(phoneNumber: String) = repo.sendOtp(phoneNumber)
}

class VerifyOtpUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(phoneNumber: String, otp: String) =
        repo.verifyOtp(phoneNumber, otp)
}

class RegisterPasskeyUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(userId: String) = repo.registerPasskey(userId)
}

class AuthenticatePasskeyUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke() = repo.authenticateWithPasskey()
}

class JoinAsGuestUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(inviteCode: String) = repo.joinAsGuest(inviteCode)
}

class GetStoredTokenUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke() = repo.getStoredToken()
}

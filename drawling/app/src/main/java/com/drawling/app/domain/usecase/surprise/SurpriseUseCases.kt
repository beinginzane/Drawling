package com.drawling.app.domain.usecase.surprise

import com.drawling.app.domain.repository.SurpriseRepository
import javax.inject.Inject

class DeliverSurpriseUseCase @Inject constructor(private val repo: SurpriseRepository) {
    suspend operator fun invoke(canvasId: String, receiverId: String) =
        repo.deliverSurprise(canvasId, receiverId)
}

class OpenSurpriseUseCase @Inject constructor(private val repo: SurpriseRepository) {
    suspend operator fun invoke(surpriseId: String) = repo.openSurprise(surpriseId)
}

class GetPendingSurprisesUseCase @Inject constructor(private val repo: SurpriseRepository) {
    suspend operator fun invoke() = repo.getPendingSurprises()
}

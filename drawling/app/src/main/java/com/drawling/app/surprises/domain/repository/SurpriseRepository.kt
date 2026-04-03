package com.drawling.app.surprises.domain.repository

import com.drawling.app.surprises.domain.model.Surprise
import com.drawling.app.utils.Resource

interface SurpriseRepository {
    suspend fun deliverSurprise(canvasId: String): Resource<Surprise>
    suspend fun getPendingSurprises(): Resource<List<Surprise>>
    suspend fun openSurprise(surpriseId: String): Resource<Surprise>
}

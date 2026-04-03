package com.drawling.app.surprises.data.remote

import com.drawling.app.network.ApiService
import com.drawling.app.surprises.domain.model.Surprise
import com.drawling.app.surprises.domain.model.SurpriseStatus
import com.drawling.app.surprises.domain.repository.SurpriseRepository
import com.drawling.app.utils.Resource
import javax.inject.Inject

class SurpriseRepositoryImpl @Inject constructor(
    private val api: ApiService
) : SurpriseRepository {

    override suspend fun deliverSurprise(canvasId: String): Resource<Surprise> = try {
        val response = api.deliverSurprise(mapOf("canvasId" to canvasId))
        if (response.isSuccessful && response.body() != null) Resource.Success(response.body()!!.toDomain())
        else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }

    override suspend fun getPendingSurprises(): Resource<List<Surprise>> = try {
        val response = api.getPendingSurprises()
        if (response.isSuccessful && response.body() != null) Resource.Success(response.body()!!.map { it.toDomain() })
        else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }

    override suspend fun openSurprise(surpriseId: String): Resource<Surprise> = try {
        val response = api.openSurprise(surpriseId)
        if (response.isSuccessful && response.body() != null) Resource.Success(response.body()!!.toDomain())
        else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }

    private fun com.drawling.app.surprises.data.remote.dto.SurpriseDto.toDomain() = Surprise(
        id = id, canvasId = canvasId, senderId = senderId, receiverId = receiverId,
        imageUrl = imageUrl,
        status = if (status == "opened") SurpriseStatus.OPENED else SurpriseStatus.PENDING,
        deliveredAt = deliveredAt, openedAt = openedAt
    )
}

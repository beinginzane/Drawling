package com.drawling.app.gallery.data.remote

import com.drawling.app.gallery.domain.model.GalleryItem
import com.drawling.app.gallery.domain.repository.GalleryRepository
import com.drawling.app.network.ApiService
import com.drawling.app.utils.Resource
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val api: ApiService
) : GalleryRepository {

    override suspend fun getGallery(roomId: String): Resource<List<GalleryItem>> = try {
        val response = api.getGallery(roomId)
        if (response.isSuccessful && response.body() != null) {
            Resource.Success(response.body()!!
                .filter { !it.isDeleted }
                .map { GalleryItem(it.id, it.roomId, it.imageUrl, it.title ?: "Untitled", it.createdAt) }
            )
        } else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }

    override suspend fun deleteCanvas(canvasId: String): Resource<Unit> = try {
        val response = api.deleteCanvas(canvasId)
        if (response.isSuccessful) Resource.Success(Unit) else Resource.Error(response.message())
    } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Error") }
}

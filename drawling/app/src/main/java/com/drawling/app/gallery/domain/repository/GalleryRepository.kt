package com.drawling.app.gallery.domain.repository

import com.drawling.app.gallery.domain.model.GalleryItem
import com.drawling.app.utils.Resource

interface GalleryRepository {
    suspend fun getGallery(roomId: String): Resource<List<GalleryItem>>
    suspend fun deleteCanvas(canvasId: String): Resource<Unit>
}

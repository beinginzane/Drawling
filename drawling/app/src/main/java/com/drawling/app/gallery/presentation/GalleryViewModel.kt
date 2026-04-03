package com.drawling.app.gallery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drawling.app.gallery.domain.model.GalleryItem
import com.drawling.app.gallery.domain.repository.GalleryRepository
import com.drawling.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GalleryState(
    val isLoading: Boolean = false,
    val items: List<GalleryItem> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryState())
    val state: StateFlow<GalleryState> = _state

    fun loadGallery(roomId: String) = viewModelScope.launch {
        _state.value = GalleryState(isLoading = true)
        when (val result = galleryRepository.getGallery(roomId)) {
            is Resource.Success -> _state.value = GalleryState(items = result.data)
            is Resource.Error -> _state.value = GalleryState(error = result.message)
            else -> Unit
        }
    }

    fun deleteItem(canvasId: String, roomId: String) = viewModelScope.launch {
        galleryRepository.deleteCanvas(canvasId)
        loadGallery(roomId)
    }
}

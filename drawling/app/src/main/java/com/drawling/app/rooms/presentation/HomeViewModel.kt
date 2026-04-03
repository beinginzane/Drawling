package com.drawling.app.rooms.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drawling.app.rooms.domain.model.Room
import com.drawling.app.rooms.domain.repository.RoomRepository
import com.drawling.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val isLoading: Boolean = false,
    val coupleRoom: Room? = null,
    val personalRoom: Room? = null,
    val error: String? = null,
    val inviteCode: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init { loadRooms() }

    fun loadRooms() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true)
        when (val result = roomRepository.getMyRooms()) {
            is Resource.Success -> {
                val couple = result.data.firstOrNull { it.type.name == "COUPLE" }
                val personal = result.data.firstOrNull { it.type.name == "PERSONAL" }
                _state.value = HomeState(coupleRoom = couple, personalRoom = personal)
            }
            is Resource.Error -> _state.value = HomeState(error = result.message)
            else -> Unit
        }
    }

    fun createCoupleRoom() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true)
        when (val result = roomRepository.createCoupleRoom()) {
            is Resource.Success -> _state.value = _state.value.copy(
                isLoading = false, coupleRoom = result.data.first, inviteCode = result.data.second
            )
            is Resource.Error -> _state.value = _state.value.copy(isLoading = false, error = result.message)
            else -> Unit
        }
    }

    fun createPersonalRoom() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true)
        when (val result = roomRepository.createPersonalRoom()) {
            is Resource.Success -> _state.value = _state.value.copy(isLoading = false, personalRoom = result.data)
            is Resource.Error -> _state.value = _state.value.copy(isLoading = false, error = result.message)
            else -> Unit
        }
    }

    fun clearInviteCode() { _state.value = _state.value.copy(inviteCode = null) }
}

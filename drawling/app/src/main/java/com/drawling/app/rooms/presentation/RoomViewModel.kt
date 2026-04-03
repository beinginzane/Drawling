package com.drawling.app.rooms.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drawling.app.rooms.domain.model.Room
import com.drawling.app.rooms.domain.model.RoomType
import com.drawling.app.rooms.domain.repository.RoomRepository
import com.drawling.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoomsState(
    val isLoading: Boolean = false,
    val coupleRoom: Room? = null,
    val personalRoom: Room? = null,
    val error: String? = null,
    val inviteCode: String? = null
)

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RoomsState())
    val state: StateFlow<RoomsState> = _state

    init { loadRooms() }

    fun loadRooms() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true)
        when (val result = roomRepository.getMyRooms()) {
            is Resource.Success -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    coupleRoom = result.data.firstOrNull { it.type == RoomType.COUPLE },
                    personalRoom = result.data.firstOrNull { it.type == RoomType.PERSONAL }
                )
            }
            is Resource.Error -> _state.value = _state.value.copy(isLoading = false, error = result.message)
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

    fun joinRoom(code: String, onJoined: (String) -> Unit) = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true)
        when (val result = roomRepository.joinRoom(code)) {
            is Resource.Success -> { _state.value = _state.value.copy(isLoading = false); onJoined(result.data.id) }
            is Resource.Error -> _state.value = _state.value.copy(isLoading = false, error = result.message)
            else -> Unit
        }
    }

    fun clearError() { _state.value = _state.value.copy(error = null) }
    fun clearInviteCode() { _state.value = _state.value.copy(inviteCode = null) }
}

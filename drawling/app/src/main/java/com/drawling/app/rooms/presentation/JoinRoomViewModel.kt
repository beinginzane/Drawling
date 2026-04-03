package com.drawling.app.rooms.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drawling.app.rooms.domain.repository.RoomRepository
import com.drawling.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JoinRoomState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val joinedRoomId: String? = null
)

@HiltViewModel
class JoinRoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow(JoinRoomState())
    val state: StateFlow<JoinRoomState> = _state

    fun joinRoom(code: String) = viewModelScope.launch {
        _state.value = JoinRoomState(isLoading = true)
        when (val result = roomRepository.joinRoom(code)) {
            is Resource.Success -> _state.value = JoinRoomState(joinedRoomId = result.data.id)
            is Resource.Error -> _state.value = JoinRoomState(error = result.message)
            else -> Unit
        }
    }
}

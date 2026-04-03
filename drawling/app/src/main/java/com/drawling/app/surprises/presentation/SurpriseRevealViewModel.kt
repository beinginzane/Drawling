package com.drawling.app.surprises.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drawling.app.surprises.domain.model.Surprise
import com.drawling.app.surprises.domain.repository.SurpriseRepository
import com.drawling.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SurpriseRevealState(
    val isLoading: Boolean = false,
    val surprise: Surprise? = null,
    val isRevealed: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SurpriseRevealViewModel @Inject constructor(
    private val surpriseRepository: SurpriseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SurpriseRevealState())
    val state: StateFlow<SurpriseRevealState> = _state

    fun loadSurprise(surpriseId: String) = viewModelScope.launch {
        _state.value = SurpriseRevealState(isLoading = true)
        when (val result = surpriseRepository.getPendingSurprises()) {
            is Resource.Success -> {
                val surprise = result.data.firstOrNull { it.id == surpriseId }
                _state.value = SurpriseRevealState(surprise = surprise)
            }
            is Resource.Error -> _state.value = SurpriseRevealState(error = result.message)
            else -> Unit
        }
    }

    fun reveal(surpriseId: String) = viewModelScope.launch {
        when (val result = surpriseRepository.openSurprise(surpriseId)) {
            is Resource.Success -> _state.value = _state.value.copy(surprise = result.data, isRevealed = true)
            is Resource.Error -> _state.value = _state.value.copy(error = result.message)
            else -> Unit
        }
    }
}

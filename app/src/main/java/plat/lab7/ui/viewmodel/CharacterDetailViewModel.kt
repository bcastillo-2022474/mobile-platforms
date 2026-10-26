package plat.lab7.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import plat.lab7.data.LibraryRepository
import plat.lab7.dto.Character
import plat.lab7.ui.state.ScreenState

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: LibraryRepository
) : ViewModel() {

    private val id =
        checkNotNull(savedStateHandle.get<Int>("id"))

    private val _uiState =
        MutableStateFlow(ScreenState<Character>())

    val uiState = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadingJob?.cancel()

        _uiState.value = ScreenState(isLoading = true)

        loadingJob = viewModelScope.launch {
            delay(2000)

            repository.observeCharacter(id)
                .catch {
                    _uiState.value = ScreenState(isLoading = false, hasError = true)
                }
                .collect { character ->
                    _uiState.value = if (character != null) {
                        ScreenState(isLoading = false, data = character)
                    } else {
                        ScreenState(isLoading = false, hasError = true)
                    }
                }
        }
    }

    fun toggleFavorite() {
        val character = _uiState.value.data ?: return
        viewModelScope.launch {
            repository.setCharacterFavorite(character.id, !character.isFavorite)
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            loadingJob?.cancel()
            _uiState.value = ScreenState(
                isLoading = false,
                hasError = true
            )
        }
    }
}

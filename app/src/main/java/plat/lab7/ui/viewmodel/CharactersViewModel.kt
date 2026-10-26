package plat.lab7.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab7.data.LibraryRepository
import plat.lab7.dto.Character
import plat.lab7.ui.state.ScreenState

class CharactersViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    private val favoritesOnly = MutableStateFlow(false)

    val showOnlyFavorites: StateFlow<Boolean> = favoritesOnly.asStateFlow()

    private val _uiState =
        MutableStateFlow(ScreenState<List<Character>>())

    val uiState = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        loadingJob?.cancel()

        _uiState.value = ScreenState(isLoading = true)

        loadingJob = viewModelScope.launch {
            delay(4000)

            favoritesOnly
                .flatMapLatest { onlyFavorites -> repository.observeCharacters(onlyFavorites) }
                .catch {
                    _uiState.value = ScreenState(isLoading = false, hasError = true)
                }
                .collect { characters ->
                    _uiState.value = ScreenState(isLoading = false, data = characters)
                }
        }
    }

    fun toggleFavoritesFilter() {
        favoritesOnly.update { !it }
    }

    fun toggleFavorite(character: Character) {
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

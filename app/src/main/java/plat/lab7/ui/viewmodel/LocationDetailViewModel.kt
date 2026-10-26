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
import plat.lab7.dto.Location
import plat.lab7.ui.state.ScreenState

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: LibraryRepository
) : ViewModel() {

    private val id =
        checkNotNull(savedStateHandle.get<Int>("id"))

    private val _uiState =
        MutableStateFlow(ScreenState<Location>())

    val uiState = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadingJob?.cancel()

        _uiState.value = ScreenState(isLoading = true)

        loadingJob = viewModelScope.launch {
            delay(2000)

            repository.observeLocation(id)
                .catch {
                    _uiState.value = ScreenState(isLoading = false, hasError = true)
                }
                .collect { location ->
                    _uiState.value = if (location != null) {
                        ScreenState(isLoading = false, data = location)
                    } else {
                        ScreenState(isLoading = false, hasError = true)
                    }
                }
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

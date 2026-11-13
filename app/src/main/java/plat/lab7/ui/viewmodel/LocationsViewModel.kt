package plat.lab7.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import plat.lab7.data.LibraryRepository
import plat.lab7.data.LocationFilter
import plat.lab7.dto.Location
import plat.lab7.ui.state.ScreenState

class LocationsViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    private val filter = MutableStateFlow(LocationFilter.ALL)

    val selectedFilter: StateFlow<LocationFilter> = filter.asStateFlow()

    private val _uiState =
        MutableStateFlow(ScreenState<List<Location>>())

    val uiState = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        loadingJob?.cancel()

        _uiState.value = ScreenState(isLoading = true)

        loadingJob = viewModelScope.launch {
            delay(4000)

            filter
                .flatMapLatest { repository.observeLocations(it) }
                .catch {
                    _uiState.value = ScreenState(isLoading = false, hasError = true)
                }
                .collect { locations ->
                    _uiState.value = ScreenState(isLoading = false, data = locations)
                }
        }
    }

    fun setFilter(newFilter: LocationFilter) {
        filter.value = newFilter
    }

    fun toggleFavorite(location: Location) {
        viewModelScope.launch {
            repository.setLocationFavorite(location.id, !location.isFavorite)
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

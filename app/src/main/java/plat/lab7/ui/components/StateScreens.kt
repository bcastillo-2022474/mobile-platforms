package plat.lab7.ui.components

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import plat.lab7.appContainer
import plat.lab7.screens.CharacterDetailScreen
import plat.lab7.screens.CharacterFormScreen
import plat.lab7.screens.CharactersScreen
import plat.lab7.screens.LocationDetailScreen
import plat.lab7.screens.LocationsScreen
import plat.lab7.ui.viewmodel.CharacterDetailViewModel
import plat.lab7.ui.viewmodel.CharacterFormViewModel
import plat.lab7.ui.viewmodel.CharactersViewModel
import plat.lab7.ui.viewmodel.LocationDetailViewModel
import plat.lab7.ui.viewmodel.LocationsViewModel

@Composable
fun CharactersStateScreen(
    onCharacterClick: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    val library = appContainer().library
    val viewModel: CharactersViewModel = viewModel(
        factory = viewModelFactory { initializer { CharactersViewModel(library) } }
    )
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val showOnlyFavorites = viewModel.showOnlyFavorites.collectAsStateWithLifecycle().value

    when {
        state.hasError -> {
            ErrorScreen(
                onRetryClick = viewModel::loadCharacters
            )
        }

        state.isLoading -> {
            LoadingScreen(
                onClick = viewModel::showError
            )
        }

        state.data != null -> {
            CharactersScreen(
                characters = state.data,
                showOnlyFavorites = showOnlyFavorites,
                onToggleFavoritesFilter = viewModel::toggleFavoritesFilter,
                onToggleFavorite = viewModel::toggleFavorite,
                onCharacterClick = onCharacterClick,
                onAddClick = onAddClick
            )
        }
    }
}

@Composable
fun CharacterDetailStateScreen(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleted: () -> Unit
) {
    val library = appContainer().library
    val viewModel: CharacterDetailViewModel = viewModel(
        factory = viewModelFactory {
            initializer { CharacterDetailViewModel(createSavedStateHandle(), library) }
        }
    )
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    when {
        state.hasError -> {
            ErrorScreen(
                onRetryClick = viewModel::loadCharacter
            )
        }

        state.isLoading -> {
            LoadingScreen(
                onClick = viewModel::showError
            )
        }

        state.data != null -> {
            CharacterDetailScreen(
                character = state.data,
                onBack = onBack,
                onToggleFavorite = viewModel::toggleFavorite,
                onEdit = onEdit,
                onDelete = { viewModel.delete(onDeleted) }
            )
        }
    }
}

@Composable
fun CharacterFormStateScreen(
    onDone: () -> Unit
) {
    val library = appContainer().library
    val viewModel: CharacterFormViewModel = viewModel(
        factory = viewModelFactory {
            initializer { CharacterFormViewModel(createSavedStateHandle(), library) }
        }
    )
    val draft = viewModel.draft.collectAsStateWithLifecycle().value

    CharacterFormScreen(
        draft = draft,
        isEditing = viewModel.isEditing,
        onDraftChange = viewModel::onDraftChange,
        onSave = { viewModel.save(onDone) },
        onBack = onDone
    )
}

@Composable
fun LocationsStateScreen(
    onLocationClick: (Int) -> Unit
) {
    val library = appContainer().library
    val viewModel: LocationsViewModel = viewModel(
        factory = viewModelFactory { initializer { LocationsViewModel(library) } }
    )
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val selectedFilter = viewModel.selectedFilter.collectAsStateWithLifecycle().value

    when {
        state.hasError -> {
            ErrorScreen(
                onRetryClick = viewModel::loadLocations
            )
        }

        state.isLoading -> {
            LoadingScreen(
                onClick = viewModel::showError
            )
        }

        state.data != null -> {
            LocationsScreen(
                locations = state.data,
                selectedFilter = selectedFilter,
                onFilterChange = viewModel::setFilter,
                onToggleFavorite = viewModel::toggleFavorite,
                onLocationClick = onLocationClick
            )
        }
    }
}

@Composable
fun LocationDetailStateScreen(
    onBack: () -> Unit
) {
    val library = appContainer().library
    val viewModel: LocationDetailViewModel = viewModel(
        factory = viewModelFactory {
            initializer { LocationDetailViewModel(createSavedStateHandle(), library) }
        }
    )
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    when {
        state.hasError -> {
            ErrorScreen(
                onRetryClick = viewModel::loadLocation
            )
        }

        state.isLoading -> {
            LoadingScreen(
                onClick = viewModel::showError
            )
        }

        state.data != null -> {
            LocationDetailScreen(
                location = state.data,
                onBack = onBack,
                onToggleFavorite = viewModel::toggleFavorite
            )
        }
    }
}

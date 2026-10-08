package plat.lab7.ui.components

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab7.screens.CharactersScreen
import plat.lab7.screens.CharacterDetailScreen
import plat.lab7.screens.LocationsScreen
import plat.lab7.screens.LocationDetailScreen
import plat.lab7.ui.viewmodel.CharactersViewModel
import plat.lab7.ui.viewmodel.CharacterDetailViewModel
import plat.lab7.ui.viewmodel.LocationsViewModel
import plat.lab7.ui.viewmodel.LocationDetailViewModel

@Composable
fun CharactersStateScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharactersViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

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
                onCharacterClick = onCharacterClick
            )
        }
    }
}

@Composable
fun CharacterDetailStateScreen(
    onBack: () -> Unit,
    viewModel: CharacterDetailViewModel = viewModel()
) {
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
                onBack = onBack
            )
        }
    }
}

@Composable
fun LocationsStateScreen(
    onLocationClick: (Int) -> Unit,
    viewModel: LocationsViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

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
                onLocationClick = onLocationClick
            )
        }
    }
}

@Composable
fun LocationDetailStateScreen(
    onBack: () -> Unit,
    viewModel: LocationDetailViewModel = viewModel()
) {
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
                onBack = onBack
            )
        }
    }
}

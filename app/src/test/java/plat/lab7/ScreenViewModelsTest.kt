package plat.lab7

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import plat.lab7.ui.viewmodel.*

@OptIn(ExperimentalCoroutinesApi::class)
class ScreenViewModelsTest {
    private val dispatcher = StandardTestDispatcher()

    @Before fun setUp() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }

    @Test fun listsLoadAfterFourSeconds() = runTest(dispatcher) {
        val characters = CharactersViewModel()
        val locations = LocationsViewModel()
        advanceTimeBy(3999)
        assertTrue(characters.uiState.value.isLoading)
        assertTrue(locations.uiState.value.isLoading)
        advanceTimeBy(1)
        runCurrent()
        assertFalse(characters.uiState.value.isLoading)
        assertFalse(locations.uiState.value.isLoading)
        assertEquals(20, characters.uiState.value.data!!.size)
        assertEquals(20, locations.uiState.value.data!!.size)
    }

    @Test fun detailsUseSavedIdsAndLoadAfterTwoSeconds() = runTest(dispatcher) {
        val character = CharacterDetailViewModel(SavedStateHandle(mapOf("id" to 2)))
        val location = LocationDetailViewModel(SavedStateHandle(mapOf("id" to 3)))
        advanceTimeBy(1999)
        assertTrue(character.uiState.value.isLoading)
        assertTrue(location.uiState.value.isLoading)
        advanceTimeBy(1)
        runCurrent()
        assertEquals(2, character.uiState.value.data!!.id)
        assertEquals(3, location.uiState.value.data!!.id)
    }

    @Test fun errorsCancelPendingLoadsAndRetryWaitsAgain() = runTest(dispatcher) {
        val characters = CharactersViewModel()
        val locations = LocationsViewModel()
        val character = CharacterDetailViewModel(SavedStateHandle(mapOf("id" to 2)))
        val location = LocationDetailViewModel(SavedStateHandle(mapOf("id" to 3)))
        runCurrent()
        advanceTimeBy(1000)
        characters.showError()
        locations.showError()
        character.showError()
        location.showError()
        advanceTimeBy(5000)
        runCurrent()
        assertTrue(characters.uiState.value.hasError)
        assertTrue(locations.uiState.value.hasError)
        assertTrue(character.uiState.value.hasError)
        assertTrue(location.uiState.value.hasError)
        characters.loadCharacters()
        locations.loadLocations()
        character.loadCharacter()
        location.loadLocation()
        assertFalse(characters.uiState.value.hasError)
        assertFalse(locations.uiState.value.hasError)
        assertFalse(character.uiState.value.hasError)
        assertFalse(location.uiState.value.hasError)
        advanceTimeBy(1999)
        assertTrue(character.uiState.value.isLoading)
        assertTrue(location.uiState.value.isLoading)
        advanceTimeBy(1)
        runCurrent()
        assertNotNull(character.uiState.value.data)
        assertNotNull(location.uiState.value.data)
        assertTrue(characters.uiState.value.isLoading)
        assertTrue(locations.uiState.value.isLoading)
        advanceTimeBy(2000)
        runCurrent()
        assertNotNull(characters.uiState.value.data)
        assertNotNull(locations.uiState.value.data)
    }

    @Test fun invalidDetailIdsShowError() = runTest(dispatcher) {
        val character = CharacterDetailViewModel(SavedStateHandle(mapOf("id" to -1)))
        val location = LocationDetailViewModel(SavedStateHandle(mapOf("id" to -1)))
        advanceTimeBy(2000)
        runCurrent()
        assertTrue(character.uiState.value.hasError)
        assertTrue(location.uiState.value.hasError)
        assertFalse(character.uiState.value.isLoading)
        assertFalse(location.uiState.value.isLoading)
    }
}

package plat.lab7

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import plat.lab7.data.CharacterDraft
import plat.lab7.data.LibraryRepository
import plat.lab7.data.LocationFilter
import plat.lab7.dto.Character
import plat.lab7.dto.Location

// In-memory stand-in for the Room repository so the ViewModels can be tested on the JVM.
class FakeLibraryRepository : LibraryRepository {
    private val characters = MutableStateFlow(
        (1..20).map { Character(it, "Character $it", "Alive", "Human", "Male", "") }
    )
    private val locations = MutableStateFlow(
        (1..20).map { Location(it, "Location $it", "Planet", "Dimension $it") }
    )

    override fun observeCharacters(favoritesOnly: Boolean): Flow<List<Character>> =
        characters.map { list -> if (favoritesOnly) list.filter { it.isFavorite } else list }

    override fun observeCharacter(id: Int): Flow<Character?> =
        characters.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun setCharacterFavorite(id: Int, isFavorite: Boolean) {
        characters.update { list -> list.map { if (it.id == id) it.copy(isFavorite = isFavorite) else it } }
    }

    override suspend fun createCharacter(draft: CharacterDraft) {
        characters.update { list ->
            list + Character(list.maxOf { it.id } + 1, draft.name, draft.status, draft.species, draft.gender, draft.image)
        }
    }

    override suspend fun updateCharacter(id: Int, draft: CharacterDraft) {
        characters.update { list ->
            list.map { if (it.id == id) it.copy(name = draft.name, status = draft.status, species = draft.species, gender = draft.gender, image = draft.image) else it }
        }
    }

    override suspend fun deleteCharacter(id: Int) {
        characters.update { list -> list.filterNot { it.id == id } }
    }

    override fun observeLocations(filter: LocationFilter): Flow<List<Location>> =
        locations.map { list ->
            when (filter) {
                LocationFilter.ALL -> list
                LocationFilter.FAVORITES -> list.filter { it.isFavorite }
                LocationFilter.NOT_FAVORITES -> list.filterNot { it.isFavorite }
            }
        }

    override fun observeLocation(id: Int): Flow<Location?> =
        locations.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun setLocationFavorite(id: Int, isFavorite: Boolean) {
        locations.update { list -> list.map { if (it.id == id) it.copy(isFavorite = isFavorite) else it } }
    }

    override suspend fun syncFromApi() = Unit

    override suspend fun clearAll() {
        characters.value = emptyList()
        locations.value = emptyList()
    }
}

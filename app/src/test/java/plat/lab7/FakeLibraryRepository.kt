package plat.lab7

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import plat.lab7.data.LibraryRepository
import plat.lab7.dto.Character
import plat.lab7.dto.Location
import plat.lab7.repository.CharacterRepository
import plat.lab7.repository.LocationDb

// In-memory stand-in for the Room repository so the ViewModels can be tested on the JVM.
class FakeLibraryRepository : LibraryRepository {
    private val characters = MutableStateFlow(CharacterRepository().getAllCharacters())
    private val locations = MutableStateFlow(LocationDb().getAllLocations())

    override fun observeCharacters(favoritesOnly: Boolean): Flow<List<Character>> =
        characters.map { list -> if (favoritesOnly) list.filter { it.isFavorite } else list }

    override fun observeCharacter(id: Int): Flow<Character?> =
        characters.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun setCharacterFavorite(id: Int, isFavorite: Boolean) {
        characters.update { list ->
            list.map { if (it.id == id) it.copy(isFavorite = isFavorite) else it }
        }
    }

    override fun observeLocations(): Flow<List<Location>> = locations

    override fun observeLocation(id: Int): Flow<Location?> =
        locations.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun syncInitialData() = Unit
}

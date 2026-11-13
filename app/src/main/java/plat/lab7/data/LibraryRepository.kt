package plat.lab7.data

import kotlinx.coroutines.flow.Flow
import plat.lab7.dto.Character
import plat.lab7.dto.Location

enum class LocationFilter { ALL, FAVORITES, NOT_FAVORITES }

data class CharacterDraft(
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val gender: String = "",
    val image: String = ""
)

// Source of truth for the screens. Only this layer knows about Room and the API.
interface LibraryRepository {
    fun observeCharacters(favoritesOnly: Boolean): Flow<List<Character>>
    fun observeCharacter(id: Int): Flow<Character?>
    suspend fun setCharacterFavorite(id: Int, isFavorite: Boolean)
    suspend fun createCharacter(draft: CharacterDraft)
    suspend fun updateCharacter(id: Int, draft: CharacterDraft)
    suspend fun deleteCharacter(id: Int)

    fun observeLocations(filter: LocationFilter): Flow<List<Location>>
    fun observeLocation(id: Int): Flow<Location?>
    suspend fun setLocationFavorite(id: Int, isFavorite: Boolean)

    // Downloads the API data once (login). Does nothing if the local data already exists.
    suspend fun syncFromApi()

    // Logout: the app starts from zero the next time
    suspend fun clearAll()
}

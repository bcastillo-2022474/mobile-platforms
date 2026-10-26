package plat.lab7.data

import kotlinx.coroutines.flow.Flow
import plat.lab7.dto.Character
import plat.lab7.dto.Location

// Source of truth for the screens. Only this layer knows about Room.
interface LibraryRepository {
    fun observeCharacters(favoritesOnly: Boolean): Flow<List<Character>>
    fun observeCharacter(id: Int): Flow<Character?>
    suspend fun setCharacterFavorite(id: Int, isFavorite: Boolean)
    fun observeLocations(): Flow<List<Location>>
    fun observeLocation(id: Int): Flow<Location?>

    // Copies CharacterRepository and LocationDb into Room. Does nothing if already done.
    suspend fun syncInitialData()
}

package plat.lab7.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.lab7.data.local.AppDatabase
import plat.lab7.data.local.CharacterEntity
import plat.lab7.data.local.toEntity
import plat.lab7.data.local.toModel
import plat.lab7.data.remote.fetchAllPages
import plat.lab7.data.remote.RickAndMortyApi
import plat.lab7.dto.Character
import plat.lab7.dto.Location

class RoomLibraryRepository(
    private val database: AppDatabase,
    private val api: RickAndMortyApi
) : LibraryRepository {

    private val characterDao = database.characterDao()
    private val locationDao = database.locationDao()

    override fun observeCharacters(favoritesOnly: Boolean): Flow<List<Character>> {
        val source = if (favoritesOnly) characterDao.observeFavorites() else characterDao.observeAll()
        return source.map { entities -> entities.map { it.toModel() } }
    }

    override fun observeCharacter(id: Int): Flow<Character?> =
        characterDao.observeById(id).map { it?.toModel() }

    override suspend fun setCharacterFavorite(id: Int, isFavorite: Boolean) {
        characterDao.updateFavorite(id, isFavorite)
    }

    override suspend fun createCharacter(draft: CharacterDraft) {
        characterDao.insert(
            CharacterEntity(
                id = characterDao.nextId(),
                name = draft.name.trim(),
                status = draft.status.trim(),
                species = draft.species.trim(),
                gender = draft.gender.trim(),
                image = draft.image.trim()
            )
        )
    }

    override suspend fun updateCharacter(id: Int, draft: CharacterDraft) {
        characterDao.updateDetails(
            id = id,
            name = draft.name.trim(),
            status = draft.status.trim(),
            species = draft.species.trim(),
            gender = draft.gender.trim(),
            image = draft.image.trim()
        )
    }

    override suspend fun deleteCharacter(id: Int) {
        characterDao.deleteById(id)
    }

    override fun observeLocations(filter: LocationFilter): Flow<List<Location>> {
        val source = when (filter) {
            LocationFilter.ALL -> locationDao.observeAll()
            LocationFilter.FAVORITES -> locationDao.observeFavorites()
            LocationFilter.NOT_FAVORITES -> locationDao.observeNotFavorites()
        }
        return source.map { entities -> entities.map { it.toModel() } }
    }

    override fun observeLocation(id: Int): Flow<Location?> =
        locationDao.observeById(id).map { it?.toModel() }

    override suspend fun setLocationFavorite(id: Int, isFavorite: Boolean) {
        locationDao.updateFavorite(id, isFavorite)
    }

    override suspend fun syncFromApi() {
        if (hasLocalData()) return

        // Network first, database second: the transaction below only holds the writes
        val characters = fetchAllPages(RickAndMortyApi.CHARACTERS_URL) { api.getCharacterPage(it) }
        val locations = fetchAllPages(RickAndMortyApi.LOCATIONS_URL) { api.getLocationPage(it) }

        database.withTransaction {
            if (hasLocalData()) return@withTransaction
            characterDao.insertAll(characters.map { it.toEntity() })
            locationDao.insertAll(locations.map { it.toEntity() })
        }
    }

    override suspend fun clearAll() {
        database.withTransaction {
            characterDao.deleteAll()
            locationDao.deleteAll()
        }
    }

    private suspend fun hasLocalData(): Boolean =
        characterDao.count() > 0 && locationDao.count() > 0
}

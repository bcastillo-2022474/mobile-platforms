package plat.lab7.data

import androidx.room.withTransaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.lab7.data.local.AppDatabase
import plat.lab7.data.local.toEntity
import plat.lab7.data.local.toModel
import plat.lab7.dto.Character
import plat.lab7.dto.Location
import plat.lab7.repository.CharacterRepository
import plat.lab7.repository.LocationDb

class RoomLibraryRepository(private val database: AppDatabase) : LibraryRepository {

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

    override fun observeLocations(): Flow<List<Location>> =
        locationDao.observeAll().map { entities -> entities.map { it.toModel() } }

    override fun observeLocation(id: Int): Flow<Location?> =
        locationDao.observeById(id).map { it?.toModel() }

    override suspend fun syncInitialData() {
        if (hasInitialData()) return

        delay(4000)

        // One transaction for both tables: either all data lands or none does.
        // Syncs are serialized by SessionViewModel.isSyncing, and IGNORE makes a repeat insert a no-op.
        database.withTransaction {
            if (hasInitialData()) return@withTransaction
            characterDao.insertAll(CharacterRepository().getAllCharacters().map { it.toEntity() })
            locationDao.insertAll(LocationDb().getAllLocations().map { it.toEntity() })
        }
    }

    private suspend fun hasInitialData(): Boolean =
        characterDao.count() > 0 && locationDao.count() > 0
}

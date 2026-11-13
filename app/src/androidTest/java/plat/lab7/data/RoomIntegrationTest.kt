package plat.lab7.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import plat.lab7.data.local.AppDatabase
import plat.lab7.data.local.CharacterEntity
import plat.lab7.data.local.LocationEntity
import plat.lab7.data.remote.RickAndMortyApi

// Runs on a device/emulator: real Room + SQLite, in memory.
class RoomIntegrationTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: RoomLibraryRepository

    private fun sampleCharacters() =
        (1..20).map { CharacterEntity(it, "Character $it", "Alive", "Human", "Male", "") }

    private fun sampleLocations() =
        (1..5).map { LocationEntity(it, "Location $it", "Planet", "Dimension $it") }

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        repository = RoomLibraryRepository(database, RickAndMortyApi.create())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun favoriteUpdatePersistsAndFiltersList() = runBlocking {
        database.characterDao().insertAll(sampleCharacters())

        repository.setCharacterFavorite(5, true)

        assertTrue(repository.observeCharacter(5).first()!!.isFavorite)
        assertEquals(listOf(5), repository.observeCharacters(favoritesOnly = true).first().map { it.id })
    }

    @Test
    fun reinsertingDoesNotResetFavoritesOrEdits() = runBlocking {
        database.characterDao().insertAll(sampleCharacters())
        repository.setCharacterFavorite(3, true)
        repository.updateCharacter(3, CharacterDraft("Edited", "Dead", "Robot", "unknown", ""))

        database.characterDao().insertAll(sampleCharacters()) // same keys: IGNORE

        val edited = repository.observeCharacter(3).first()!!
        assertEquals("Edited", edited.name)
        assertTrue(edited.isFavorite)
        assertEquals(20, repository.observeCharacters(favoritesOnly = false).first().size)
    }

    @Test
    fun createUsesNextIdAndDeleteRemovesRow() = runBlocking {
        database.characterDao().insertAll(sampleCharacters())

        repository.createCharacter(CharacterDraft("New", "Alive", "Human", "Male", ""))
        assertEquals("New", repository.observeCharacter(21).first()!!.name)

        repository.deleteCharacter(21)
        assertNull(repository.observeCharacter(21).first())
    }

    @Test
    fun locationFilterSeparatesFavorites() = runBlocking {
        database.locationDao().insertAll(sampleLocations())
        repository.setLocationFavorite(2, true)

        assertEquals(listOf(2), repository.observeLocations(LocationFilter.FAVORITES).first().map { it.id })
        assertEquals(4, repository.observeLocations(LocationFilter.NOT_FAVORITES).first().size)
        assertEquals(5, repository.observeLocations(LocationFilter.ALL).first().size)
    }

    @Test
    fun syncIsSkippedWhenLocalDataExists() = runBlocking {
        database.characterDao().insertAll(sampleCharacters())
        database.locationDao().insertAll(sampleLocations())

        val start = System.currentTimeMillis()
        repository.syncFromApi() // must not touch the network when data already exists
        assertTrue(System.currentTimeMillis() - start < 1000)
        assertEquals(20, repository.observeCharacters(false).first().size)
    }

    @Test
    fun clearAllEmptiesBothTables() = runBlocking {
        database.characterDao().insertAll(sampleCharacters())
        database.locationDao().insertAll(sampleLocations())

        repository.clearAll()

        assertEquals(0, repository.observeCharacters(false).first().size)
        assertEquals(0, repository.observeLocations(LocationFilter.ALL).first().size)
    }
}

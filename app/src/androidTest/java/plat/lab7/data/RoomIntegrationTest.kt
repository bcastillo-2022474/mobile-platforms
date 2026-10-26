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
import plat.lab7.data.local.toEntity
import plat.lab7.repository.CharacterRepository
import plat.lab7.repository.LocationDb

class RoomIntegrationTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: RoomLibraryRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        repository = RoomLibraryRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun characterInsertKeepsAllRowsAndMapsFields() = runBlocking {
        database.characterDao().insertAll(CharacterRepository().getAllCharacters().map { it.toEntity() })

        val characters = repository.observeCharacters(favoritesOnly = false).first()

        assertEquals(20, characters.size)
        assertEquals(1, characters.first().id)
        assertEquals("Rick Sanchez", characters.first().name)
        assertTrue(characters.none { it.isFavorite })
    }

    @Test
    fun favoriteUpdatePersistsAndFiltersList() = runBlocking {
        database.characterDao().insertAll(CharacterRepository().getAllCharacters().map { it.toEntity() })

        repository.setCharacterFavorite(id = 5, isFavorite = true)

        assertTrue(repository.observeCharacter(5).first()!!.isFavorite)
        assertEquals(listOf(5), repository.observeCharacters(favoritesOnly = true).first().map { it.id })
    }

    @Test
    fun reinsertingDoesNotResetFavorites() = runBlocking {
        database.characterDao().insertAll(CharacterRepository().getAllCharacters().map { it.toEntity() })
        repository.setCharacterFavorite(id = 3, isFavorite = true)

        // Same primary keys again: IGNORE must keep the existing rows untouched
        database.characterDao().insertAll(CharacterRepository().getAllCharacters().map { it.toEntity() })

        assertEquals(20, repository.observeCharacters(favoritesOnly = false).first().size)
        assertTrue(repository.observeCharacter(3).first()!!.isFavorite)
    }

    @Test
    fun missingIdEmitsNull() = runBlocking {
        assertNull(repository.observeCharacter(-1).first())
        assertNull(repository.observeLocation(-1).first())
    }

    @Test
    fun syncFillsBothTablesOnceAndDoesNotDuplicate() = runBlocking {
        repository.syncInitialData()
        assertEquals(20, repository.observeCharacters(false).first().size)
        assertEquals(LocationDb().getAllLocations().size, repository.observeLocations().first().size)

        repository.setCharacterFavorite(id = 2, isFavorite = true)

        val start = System.currentTimeMillis()
        repository.syncInitialData() // second run must return early: no 4s delay, no duplicates, no favorite reset
        assertTrue(System.currentTimeMillis() - start < 1000)

        assertEquals(20, repository.observeCharacters(false).first().size)
        assertTrue(repository.observeCharacter(2).first()!!.isFavorite)
    }

    @Test
    fun insertedEntityIsReadBackAsModel() = runBlocking {
        database.characterDao().insertAll(listOf(CharacterEntity(99, "X", "Alive", "Human", "Male", "")))
        val model = repository.observeCharacter(99).first()
        assertEquals("X", model!!.name)
    }
}

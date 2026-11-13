package plat.lab7

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import plat.lab7.data.remote.fetchAllPages
import plat.lab7.data.remote.RickAndMortyApi

// Hits the real API from the JVM: checks the DTOs parse and that paging yields the full catalog.
class RickAndMortyApiLiveTest {

    private val api = RickAndMortyApi.create()

    @Test
    fun charactersPagingReturnsWholeCatalog() = runBlocking {
        assertEquals(826, fetchAllPages(RickAndMortyApi.CHARACTERS_URL) { api.getCharacterPage(it) }.size)
    }

    @Test
    fun locationsPagingReturnsWholeCatalog() = runBlocking {
        assertEquals(126, fetchAllPages(RickAndMortyApi.LOCATIONS_URL) { api.getLocationPage(it) }.size)
    }
}

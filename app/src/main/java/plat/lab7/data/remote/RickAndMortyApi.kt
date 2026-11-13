package plat.lab7.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface RickAndMortyApi {

    @GET
    suspend fun getCharacterPage(@Url url: String): ApiPage<CharacterDto>

    @GET
    suspend fun getLocationPage(@Url url: String): ApiPage<LocationDto>

    companion object {
        const val CHARACTERS_URL = "https://rickandmortyapi.com/api/character"
        const val LOCATIONS_URL = "https://rickandmortyapi.com/api/location"

        fun create(): RickAndMortyApi = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApi::class.java)
    }
}

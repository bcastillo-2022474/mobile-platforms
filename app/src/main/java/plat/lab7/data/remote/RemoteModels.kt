package plat.lab7.data.remote

// Shapes of the Rick and Morty API responses. Only the fields the app uses are declared.
data class ApiPage<T>(
    val info: ApiInfo,
    val results: List<T>
)

data class ApiInfo(
    val next: String?
)

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)

data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

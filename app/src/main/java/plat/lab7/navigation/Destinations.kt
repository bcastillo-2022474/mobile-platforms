package plat.lab7.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object CharactersGraph

@Serializable
object Characters

@Serializable
data class CharacterDetail(val id: Int)

@Serializable
object LocationsGraph

@Serializable
object Locations

@Serializable
data class LocationDetail(val id: Int)

@Serializable
object Profile

package plat.lab7.navigation

import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
object Login

@Serializable
object CharactersGraph

@Serializable
object Characters

@Serializable
data class CharacterDetail(val id: Int)

// id == null opens the form to create a character
@Serializable
data class CharacterForm(val id: Int? = null)

@Serializable
object LocationsGraph

@Serializable
object Locations

@Serializable
data class LocationDetail(val id: Int)

@Serializable
object Profile

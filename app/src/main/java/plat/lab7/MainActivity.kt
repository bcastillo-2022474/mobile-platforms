package plat.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.lab7.navigation.CharacterDetail
import plat.lab7.navigation.Characters
import plat.lab7.navigation.CharactersGraph
import plat.lab7.navigation.LocationDetail
import plat.lab7.navigation.Locations
import plat.lab7.navigation.LocationsGraph
import plat.lab7.navigation.Login
import plat.lab7.navigation.Profile
import plat.lab7.screens.CharacterDetailScreen
import plat.lab7.screens.CharactersScreen
import plat.lab7.screens.LocationDetailScreen
import plat.lab7.screens.LocationsScreen
import plat.lab7.screens.LoginScreen
import plat.lab7.screens.ProfileScreen
import plat.lab7.ui.theme.Lab7Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Lab7Theme {
                AppNavHost()
            }
        }
    }
}

private data class BottomTab(val graph: Any, val label: String, val icon: ImageVector)

private val bottomTabs = listOf(
    BottomTab(CharactersGraph, "Characters", Icons.Default.Group),
    BottomTab(LocationsGraph, "Locations", Icons.Default.Public),
    BottomTab(Profile, "Profile", Icons.Default.Person)
)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val showBottomBar = currentDestination != null && !currentDestination.hasRoute<Login>()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    bottomTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentDestination.isInGraph(tab.graph),
                            onClick = {
                                navController.navigate(tab.graph) {
                                    popUpTo(CharactersGraph) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                indicatorColor = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Login,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Login> {
                LoginScreen(
                    onStart = {
                        navController.navigate(CharactersGraph) {
                            popUpTo(Login) { inclusive = true }
                        }
                    }
                )
            }
            navigation<CharactersGraph>(startDestination = Characters) {
                composable<Characters> {
                    CharactersScreen(
                        onCharacterClick = { id ->
                            navController.navigate(CharacterDetail(id))
                        }
                    )
                }
                composable<CharacterDetail> { backStackEntry ->
                    val detail: CharacterDetail = backStackEntry.toRoute()
                    CharacterDetailScreen(
                        id = detail.id,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
            navigation<LocationsGraph>(startDestination = Locations) {
                composable<Locations> {
                    LocationsScreen(
                        onLocationClick = { id ->
                            navController.navigate(LocationDetail(id))
                        }
                    )
                }
                composable<LocationDetail> { backStackEntry ->
                    val detail: LocationDetail = backStackEntry.toRoute()
                    LocationDetailScreen(
                        id = detail.id,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
            composable<Profile> {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Login) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

private fun NavDestination?.isInGraph(graph: Any): Boolean =
    this?.hierarchy?.any { it.hasRoute(graph::class) } == true

@Preview(
    name = "Modo claro",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ScreenLightPreview() {
    Lab7Theme(
        darkTheme = false,
        dynamicColor = false
    ) {
        AppNavHost()
    }
}

@Preview(
    name = "Modo oscuro",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ScreenDarkPreview() {
    Lab7Theme(
        darkTheme = true,
        dynamicColor = false
    ) {
        AppNavHost()
    }
}

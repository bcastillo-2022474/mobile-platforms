package plat.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.lab7.navigation.CharacterDetail
import plat.lab7.navigation.Characters
import plat.lab7.navigation.Login
import plat.lab7.screens.CharacterDetailScreen
import plat.lab7.screens.CharactersScreen
import plat.lab7.screens.LoginScreen
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

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                onStart = {
                    navController.navigate(Characters) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }
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
}

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

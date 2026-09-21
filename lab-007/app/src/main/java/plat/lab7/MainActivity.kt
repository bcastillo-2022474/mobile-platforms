package plat.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import plat.lab7.ui.theme.Lab7Theme
import plat.lab7.screens.LoginScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Lab7Theme {
                LoginScreen()
            }
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
        LoginScreen()
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
        LoginScreen()
    }
}
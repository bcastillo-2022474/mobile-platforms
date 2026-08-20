    package plat.lab1.lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import plat.lab1.lab4.ui.theme.Lab4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab4Theme {
                LabScreen()
            }
        }
    }
}

@Composable
fun LabScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(
                8.dp,
                Color(0xFF146B2E)
            )
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .size(310.dp),
            painter = painterResource(id = R.drawable.escudo_uvg),
            contentDescription = "Escudo UVG",
            alpha = 0.22f
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 250.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Universidad del Valle\nde Guatemala",
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 42.sp
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            Text(
                text = "Programación de plataformas\nmóviles, Sección 20",
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                lineHeight = 36.sp
            )
            Spacer(
                modifier = Modifier.height(25.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "INTEGRANTES",
                    fontWeight = FontWeight.Bold
                )
                Column {
                    Text("Joao Castillo")
                    Text("Franco Paiz")
                    Text("Weslly Alexander")
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "CATEDRÁTICO",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Juan Carlos Durini"
                )
            }

            Spacer(
                modifier = Modifier.height(35.dp)
            )

            Text("Joao Castillo")
            Text("25776")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab4Theme {
        LabScreen()
    }
}
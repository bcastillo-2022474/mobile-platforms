package plat.lab1.lab5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import plat.lab1.lab5.ui.theme.Lab5Theme
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Lab5Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    RestaurantScreen()
                }
            }
        }
    }
}

@Composable
fun RestaurantScreen() {

    val context = LocalContext.current

    val nombreCompleto = "Brandon Joao Castillo Muñoz"

    val restaurante = "La Berna: Panes y Licuados"
    val direccion = "Spazio, Zona 16"
    val horario = "8:00 AM - 7:00 PM"

    val tipoComida = "Desayunos"
    val precio = "Q"

    val latitud = 14.6052537
    val longitud = -90.4948059

    val cumple = LocalDate.of(
        LocalDate.now().year,
        Month.JUNE,
        7
    )

    val locale = Locale("es", "GT")

    val diaSemana = cumple.dayOfWeek
        .getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { it.uppercase() }

    val mes = cumple.month
        .getDisplayName(TextStyle.FULL, locale)

    val fechaTexto = "${cumple.dayOfMonth} de $mes"

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Actualizar",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = "Actualización disponible",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                TextButton(
                    onClick = {

                        val playStoreUrl =
                            "https://play.google.com/store/apps/details?id=com.whatsapp"

                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(playStoreUrl)
                        )

                        context.startActivity(intent)
                    }
                ) {
                    Text("Descargar")
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            Text(
                text = diaSemana,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = fechaTexto,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.inverseSurface
                )

                OutlinedButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Jornada terminada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Text("Terminar jornada", color = MaterialTheme.colorScheme.surface)
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = restaurante,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        IconButton(
                            onClick = {

                                val mapsUri = Uri.parse(
                                    "geo:$latitud,$longitud?q=$latitud,$longitud($restaurante)"
                                )

                                val mapsIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    mapsUri
                                )

                                context.startActivity(mapsIntent)
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Directions,
                                contentDescription = "Abrir ubicación",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Text(
                        text = direccion,
                        fontSize = 18.sp,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = horario,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Button(
                            onClick = {

                                Toast.makeText(
                                    context,
                                    nombreCompleto,
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Iniciar")
                        }

                        TextButton(
                            onClick = {

                                Toast.makeText(
                                    context,
                                    "$tipoComida\n$precio",
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Detalles")
                        }
                    }
                }
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
fun RestaurantScreenLightPreview() {
    Lab5Theme(
        darkTheme = false,
        dynamicColor = false
    ) {
        RestaurantScreen()
    }
}

@Preview(
    name = "Modo oscuro",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RestaurantScreenDarkPreview() {
    Lab5Theme(
        darkTheme = true,
        dynamicColor = false
    ) {
        RestaurantScreen()
    }
}
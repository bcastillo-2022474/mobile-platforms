package plat.lab7.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import plat.lab7.data.LocationFilter
import plat.lab7.dto.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    locations: List<Location>,
    selectedFilter: LocationFilter,
    onFilterChange: (LocationFilter) -> Unit,
    onToggleFavorite: (Location) -> Unit,
    onLocationClick: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text("Locations")
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == LocationFilter.ALL,
                    onClick = { onFilterChange(LocationFilter.ALL) },
                    label = { Text("Todas") }
                )
                FilterChip(
                    selected = selectedFilter == LocationFilter.FAVORITES,
                    onClick = { onFilterChange(LocationFilter.FAVORITES) },
                    label = { Text("Favoritas") }
                )
                FilterChip(
                    selected = selectedFilter == LocationFilter.NOT_FAVORITES,
                    onClick = { onFilterChange(LocationFilter.NOT_FAVORITES) },
                    label = { Text("No favoritas") }
                )
            }
            if (locations.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay elementos para mostrar")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(locations, key = { it.id }) { location ->
                        LocationComponent(
                            location = location,
                            onClick = { onLocationClick(location.id) },
                            onToggleFavorite = { onToggleFavorite(location) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationComponent(
    location: Location,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(location.name, fontSize = 4.em)
            Text(location.type, fontWeight = FontWeight.Bold)
        }
        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (location.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Marcar como favorita",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
fun LocationsScreenPreview() {
    LocationsScreen(
        locations = emptyList(),
        selectedFilter = LocationFilter.ALL,
        onFilterChange = {},
        onToggleFavorite = {},
        onLocationClick = {}
    )
}

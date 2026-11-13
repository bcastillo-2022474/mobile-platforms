package plat.lab7.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import plat.lab7.data.CharacterDraft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFormScreen(
    draft: CharacterDraft,
    isEditing: Boolean,
    onDraftChange: (CharacterDraft) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopAppBar(
                title = {
                    Text(if (isEditing) "Editar personaje" else "Nuevo personaje")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = draft.name,
                    onValueChange = { onDraftChange(draft.copy(name = it)) },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = draft.status,
                    onValueChange = { onDraftChange(draft.copy(status = it)) },
                    label = { Text("Estado (Alive, Dead, unknown)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = draft.species,
                    onValueChange = { onDraftChange(draft.copy(species = it)) },
                    label = { Text("Especie") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = draft.gender,
                    onValueChange = { onDraftChange(draft.copy(gender = it)) },
                    label = { Text("Género") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = draft.image,
                    onValueChange = { onDraftChange(draft.copy(image = it)) },
                    label = { Text("URL de imagen (opcional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = onSave,
                    enabled = draft.name.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

@Preview
@Composable
fun CharacterFormScreenPreview() {
    CharacterFormScreen(
        draft = CharacterDraft(name = "Rick"),
        isEditing = false,
        onDraftChange = {},
        onSave = {},
        onBack = {}
    )
}

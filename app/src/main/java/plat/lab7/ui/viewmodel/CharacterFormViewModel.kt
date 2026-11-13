package plat.lab7.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.lab7.data.CharacterDraft
import plat.lab7.data.LibraryRepository

// id == null means "new character"
class CharacterFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: LibraryRepository
) : ViewModel() {

    private val editingId: Int? = savedStateHandle.get<Int>("id")

    val isEditing: Boolean = editingId != null

    private val _draft = MutableStateFlow(CharacterDraft())

    val draft: StateFlow<CharacterDraft> = _draft.asStateFlow()

    init {
        val id = editingId
        if (id != null) {
            viewModelScope.launch {
                repository.observeCharacter(id).first()?.let { character ->
                    _draft.value = CharacterDraft(
                        name = character.name,
                        status = character.status,
                        species = character.species,
                        gender = character.gender,
                        image = character.image
                    )
                }
            }
        }
    }

    fun onDraftChange(newDraft: CharacterDraft) {
        _draft.value = newDraft
    }

    fun save(onDone: () -> Unit) {
        val draft = _draft.value
        if (draft.name.isBlank()) return

        viewModelScope.launch {
            val id = editingId
            if (id == null) {
                repository.createCharacter(draft)
            } else {
                repository.updateCharacter(id, draft)
            }
            onDone()
        }
    }
}

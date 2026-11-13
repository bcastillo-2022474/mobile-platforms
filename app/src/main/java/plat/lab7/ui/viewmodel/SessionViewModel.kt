package plat.lab7.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import plat.lab7.data.LibraryRepository
import plat.lab7.data.UserPreferences

class SessionViewModel(
    private val preferences: UserPreferences,
    private val library: LibraryRepository
) : ViewModel() {

    val userName: StateFlow<String?> = preferences.userName.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _isSyncing = MutableStateFlow(false)

    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _syncFailed = MutableStateFlow(false)

    val syncFailed: StateFlow<Boolean> = _syncFailed.asStateFlow()

    suspend fun storedUserName(): String? = preferences.userName.first()

    fun login(name: String, onDone: () -> Unit) {
        if (_isSyncing.value) return

        viewModelScope.launch {
            _syncFailed.value = false
            _isSyncing.value = true

            val synced = try {
                library.syncFromApi()
                true
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                false
            } finally {
                _isSyncing.value = false
            }

            // The name is saved only after the data is in Room, so a failed sync never logs the user in
            if (!synced) {
                _syncFailed.value = true
                return@launch
            }

            preferences.saveUserName(name.trim())
            onDone()
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            library.clearAll()
            preferences.clearUserName()
            onDone()
        }
    }
}

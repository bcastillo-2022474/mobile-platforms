package plat.lab7.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import plat.lab7.data.UserPreferences

class SessionViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = UserPreferences(application)

    val userName: StateFlow<String?> = preferences.userName.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    suspend fun storedUserName(): String? = preferences.userName.first()

    fun login(name: String, onDone: () -> Unit) {
        viewModelScope.launch {
            preferences.saveUserName(name.trim())
            onDone()
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            preferences.clearUserName()
            onDone()
        }
    }
}

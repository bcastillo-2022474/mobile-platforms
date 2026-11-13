package plat.lab7

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import plat.lab7.data.LibraryRepository
import plat.lab7.data.RoomLibraryRepository
import plat.lab7.data.UserPreferences
import plat.lab7.data.local.AppDatabase
import plat.lab7.data.remote.RickAndMortyApi

class RickAndMortyApp : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}

class AppContainer(context: Context) {
    private val database: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "rick_and_morty.db"
    )
        // POC: a schema change simply rebuilds the local cache, which is refilled from the API on login
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()

    val library: LibraryRepository = RoomLibraryRepository(database, RickAndMortyApi.create())
    val preferences = UserPreferences(context)
}

@Composable
fun appContainer(): AppContainer =
    (LocalContext.current.applicationContext as RickAndMortyApp).container

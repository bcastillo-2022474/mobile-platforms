package plat.lab7.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM locations ORDER BY id")
    fun observeAll(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE isFavorite = 1 ORDER BY id")
    fun observeFavorites(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE isFavorite = 0 ORDER BY id")
    fun observeNotFavorites(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id")
    fun observeById(id: Int): Flow<LocationEntity?>

    @Query("UPDATE locations SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(locations: List<LocationEntity>)

    @Query("DELETE FROM locations")
    suspend fun deleteAll()
}

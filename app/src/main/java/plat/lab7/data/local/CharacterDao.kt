package plat.lab7.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters ORDER BY id")
    fun observeAll(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE isFavorite = 1 ORDER BY id")
    fun observeFavorites(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun observeById(id: Int): Flow<CharacterEntity?>

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    // Only the editable columns: an edit must not overwrite isFavorite
    @Query("UPDATE characters SET name = :name, status = :status, species = :species, gender = :gender, image = :image WHERE id = :id")
    suspend fun updateDetails(id: Int, name: String, status: String, species: String, gender: String, image: String)

    @Query("SELECT COALESCE(MAX(id), 0) + 1 FROM characters")
    suspend fun nextId(): Int

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(character: CharacterEntity)

    // IGNORE keeps existing rows, so a repeated sync never resets favorites or local edits
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}

package si.uni_lj.fri.pbd.classproject3.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import si.uni_lj.fri.pbd.classproject3.database.entity.RecipeDetails

@Dao
interface RecipeDao {
    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :idMeal")
    suspend fun getRecipeById(idMeal: String?): RecipeDetails?

    // TODO: Add the missing methods

    @Query("SELECT * FROM RecipeDetails WHERE isFavorite = 1")
    fun getFavoriteRecipes(): Flow<List<RecipeDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: RecipeDetails)

    @Query("UPDATE RecipeDetails SET isFavorite = :flag WHERE idMeal = :id")
    suspend fun setFavoriteFlag(id: String, flag: Boolean)

    @Delete
    suspend fun delete(recipe: RecipeDetails)
}
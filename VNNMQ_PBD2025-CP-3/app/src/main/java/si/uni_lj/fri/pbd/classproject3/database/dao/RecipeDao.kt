package si.uni_lj.fri.pbd.classproject3.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import si.uni_lj.fri.pbd.classproject3.database.entity.RecipeDetails

@Dao
interface RecipeDao {
    /** Insert or replace a single recipe details row */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: RecipeDetails)

    /** Insert or replace multiple recipe details */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg recipes: RecipeDetails)

    /** Get a recipe by its ID (meal ID) */
    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :mealId LIMIT 1")
    suspend fun getById(mealId: String): RecipeDetails?

    /** Stream favorite recipes */
    @Query("SELECT * FROM RecipeDetails WHERE isFavorite = 1")
    fun favorites(): Flow<List<RecipeDetails>>
}

package si.uni_lj.fri.pbd.classproject3.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import si.uni_lj.fri.pbd.classproject3.database.entity.RecipeDetails

@Dao
interface RecipeDao {
    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :idMeal")
    suspend fun getRecipeById(idMeal: String?): RecipeDetails?

    @Query("SELECT * FROM RecipeDetails")
    fun getAllRecipes(): Flow<List<RecipeDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeDetails)

    @Query("DELETE FROM RecipeDetails WHERE idMeal = :idMeal")
    suspend fun deleteRecipeById(idMeal: String)

    // DONE: Add the missing methods

}
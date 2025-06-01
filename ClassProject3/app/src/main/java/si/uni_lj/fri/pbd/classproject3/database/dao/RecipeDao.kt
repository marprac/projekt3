package si.uni_lj.fri.pbd.classproject3.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import si.uni_lj.fri.pbd.classproject3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeDetailsDTO

@Dao
interface RecipeDao {
    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :idMeal")
    fun getRecipeById(idMeal: String?): RecipeDetails?

    //Insert recipe into database
    @Insert
    fun insertRecipe(recipe: RecipeDetails)

    //Delete recipe from database
    @Query("DELETE FROM RecipeDetails WHERE idMeal = :idMeal")
    fun deleteRecipe(idMeal: String?)

    //Get all recipes in favourites to show on favourites screen
    @Query("SELECT * FROM RecipeDetails")
    fun getAllRecipes(): List<RecipeDetails>

    //Return count of recipe by id to check recipe is favourite or not
    @Query("SELECT COUNT(*) FROM RecipeDetails WHERE idMeal = :id")
    fun isRecipeInFavorites(id: String): Int

}
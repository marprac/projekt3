package si.uni_lj.fri.pbd.classproject3.repository

import android.app.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response
import si.uni_lj.fri.pbd.classproject3.database.RecipeDatabase
import si.uni_lj.fri.pbd.classproject3.database.dao.RecipeDao
import si.uni_lj.fri.pbd.classproject3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.classproject3.models.dto.IngredientDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeDetailsDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeSummaryDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipesByIdDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipesByIngredientsDTO
import si.uni_lj.fri.pbd.classproject3.rest.RestAPI
import si.uni_lj.fri.pbd.classproject3.rest.ServiceGenerator

class RecipeRepository(
    application: Application?
    ) {

    // DONE: Add DAO reference
    private val recipeDao: RecipeDao?

    private val restAPI: RestAPI = ServiceGenerator.createService(RestAPI::class.java)

    suspend fun getAllIngredients(): Result<List<IngredientDTO>>? {
        return try {
            val response = restAPI.getAllIngredients()
            if (response.isSuccessful) {
                val ingredientsDTO = response.body()
                val ingredientsList = ingredientsDTO?.ingredients ?: emptyList()
                Result.success(ingredientsList)
            } else {
                Result.failure(Exception("Failed to fetch ingredients: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipesByMainIngredient(mainIngredient: String): Result<List<RecipeSummaryDTO>>{
        return try {
            val response: Response<RecipesByIngredientsDTO> = restAPI.filterByMainIngredient(mainIngredient)
            if (response.isSuccessful) {
                val recipes = response.body()?.meals
                if (recipes != null) {
                    Result.success(recipes)
                } else {
                    Result.failure(Exception("No recipes found"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipeById(recipeId: String): Result<RecipeDetailsDTO> {
        return try {
            val response: Response<RecipesByIdDTO> = restAPI.lookupRecipeById(recipeId)
            if (response.isSuccessful) {
                val recipe = response.body()?.meals?.firstOrNull()
                if (recipe != null) {
                    Result.success(recipe)
                } else {
                    Result.failure(Exception("No recipe found"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getFavoriteRecipes(): Flow<List<RecipeDetails>> {
        return recipeDao?.getAllRecipes() ?: flowOf(emptyList())
    }

    suspend fun insertRecipe(recipe: RecipeDetails) {
        recipeDao?.insertRecipe(recipe)
    }

    suspend fun deleteRecipeById(idMeal: String) {
        recipeDao?.deleteRecipeById(idMeal)
    }

    suspend fun getRecipeByIdFromDB(idMeal: String): RecipeDetails? {
        return recipeDao?.getRecipeById(idMeal)
    }

    init {
        val database = RecipeDatabase.getDatabase(application!!)
        recipeDao = database.recipeDao()
    }

}
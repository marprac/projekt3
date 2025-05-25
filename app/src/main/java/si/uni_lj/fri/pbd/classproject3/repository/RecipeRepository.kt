package si.uni_lj.fri.pbd.classproject3.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import si.uni_lj.fri.pbd.classproject3.database.dao.RecipeDao
import si.uni_lj.fri.pbd.classproject3.models.Mapper
import si.uni_lj.fri.pbd.classproject3.models.RecipeDetailsIM
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.models.dto.IngredientDTO
import si.uni_lj.fri.pbd.classproject3.rest.RestAPI
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val api: RestAPI,
    private val dao: RecipeDao
) {


    suspend fun loadAllIngredients(): List<IngredientDTO> {
        try {
            return api.getAllIngredients()?.ingredients.orEmpty()
        } catch (e: IOException) {
            throw IOException("Network error fetching ingredients: ${e.message}", e)
        } catch (e: Exception) {
            throw Exception("Error fetching ingredients: ${e.message}", e)
        }
    }

    suspend fun loadRecipes(ingredient: String): List<RecipeSummaryIM> {
        try {
            return api.getRecipesByIngredient(ingredient)
                ?.recipes.orEmpty()
                .map { Mapper.mapRecipSummaryDtoToRecipeSummaryIm(it) }
        } catch (e: IOException) {
            throw IOException("Network error fetching recipes for $ingredient: ${e.message}", e)
        } catch (e: Exception) {
            throw Exception("Error fetching recipes for $ingredient: ${e.message}", e)
        }
    }


    suspend fun loadRecipeDetails(id: String, isOnline: Boolean): RecipeDetailsIM {
        val localRecipe = dao.getRecipeById(id)
        val isFavoriteCurrentState = localRecipe?.isFavorite

        if (isOnline) {
            try {
                val dto = api.getRecipeDetailsById(id)?.recipes?.firstOrNull()
                    ?: throw IllegalStateException("Recipe $id not found on server")
                val detailsToStore = Mapper.mapRecipeDetailsDtoToRecipeDetails(isFavoriteCurrentState ?: false, dto)
                detailsToStore.id = localRecipe?.id
                dao.insert(detailsToStore)
                return Mapper.mapRecipeDetailsDtoToRecipeDetailsIm(isFavoriteCurrentState, dto)
            } catch (e: IOException) {
                localRecipe?.let { return Mapper.mapRecipeDetailsToRecipeDetailsIm(it.isFavorite, it) }
                throw IOException("Network error fetching details for $id. No local data available: ${e.message}", e)
            } catch (e: Exception) {
                localRecipe?.let { return Mapper.mapRecipeDetailsToRecipeDetailsIm(it.isFavorite, it) }
                throw Exception("Error fetching details for $id. No local data available: ${e.message}", e)
            }
        } else {
            localRecipe?.let {
                return Mapper.mapRecipeDetailsToRecipeDetailsIm(it.isFavorite, it)
            } ?: throw Exception("Recipe $id not found in local database for offline viewing.")
        }
    }

    fun favoriteRecipes(): Flow<List<RecipeSummaryIM>> =
        dao.getFavoriteRecipes()
            .map { list -> list.map { Mapper.mapRecipeDetailsToRecipeSummaryIm(it) } }

    suspend fun toggleFavorite(details: RecipeDetailsIM) {
        val recipeId = details.idMeal ?: return

        val localVersion = dao.getRecipeById(recipeId)
        val newFavoriteState = !(localVersion?.isFavorite ?: details.isFavorite ?: false)

        val recipeToStore = localVersion?.apply {
            isFavorite = newFavoriteState
        } ?: Mapper.mapRecipeDetailsImToRecipeDetails(newFavoriteState, details)

        recipeToStore.isFavorite = newFavoriteState
        dao.insert(recipeToStore)
    }
}
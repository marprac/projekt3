package si.uni_lj.fri.pbd.classproject3.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import si.uni_lj.fri.pbd.classproject3.database.dao.RecipeDao
import si.uni_lj.fri.pbd.classproject3.models.Mapper
import si.uni_lj.fri.pbd.classproject3.models.RecipeDetailsIM
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.models.dto.IngredientDTO
import si.uni_lj.fri.pbd.classproject3.rest.RestAPI
import si.uni_lj.fri.pbd.classproject3.rest.ServiceGenerator

class RecipeRepository(
    private val api: RestAPI = ServiceGenerator.createService(RestAPI::class.java),
    private val dao: RecipeDao
) {

    /* ---------- INGREDIENTS ---------- */
    suspend fun loadAllIngredients(): List<IngredientDTO> =
        api.getAllIngredients()?.ingredients.orEmpty()

    /* ---------- RECIPES BY INGREDIENT ---------- */
    suspend fun loadRecipes(ingredient: String): List<RecipeSummaryIM> =
        api.getRecipesByIngredient(ingredient)
            ?.recipes.orEmpty()
            .map { Mapper.mapRecipSummaryDtoToRecipeSummaryIm(it) }

    /* ---------- SINGLE RECIPE ---------- */
    suspend fun loadRecipeDetails(id: String): RecipeDetailsIM {
        val fav = dao.getRecipeById(id)?.isFavorite
        val dto = api.getRecipeDetailsById(id)?.recipes?.firstOrNull()
            ?: throw IllegalStateException("Recipe not found on server")
        return Mapper.mapRecipeDetailsDtoToRecipeDetailsIm(fav, dto)
    }

    /* ---------- FAVORITES ---------- */
    fun favoriteRecipes(): Flow<List<RecipeSummaryIM>> =
        dao.getFavoriteRecipes()
            .map { list -> list.map { Mapper.mapRecipeDetailsToRecipeSummaryIm(it) } }

    suspend fun toggleFavorite(details: RecipeDetailsIM) {
        val currentlyFav = dao.getRecipeById(details.idMeal)?.isFavorite == true
        dao.insert(Mapper.mapRecipeDetailsImToRecipeDetails(!currentlyFav, details))
    }
}

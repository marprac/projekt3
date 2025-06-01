package si.uni_lj.fri.pbd.classproject3.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import si.uni_lj.fri.pbd.classproject3.database.RecipeDatabase
import si.uni_lj.fri.pbd.classproject3.models.RecipeDetailsIM
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeDetailsDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeSummaryDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipesByIngredientDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipesByIdDTO
import si.uni_lj.fri.pbd.classproject3.models.Mapper
import si.uni_lj.fri.pbd.classproject3.rest.RestAPI
import si.uni_lj.fri.pbd.classproject3.rest.ServiceGenerator

class RecipeRepository(context: Context) {

    private val api: RestAPI by lazy {
        ServiceGenerator.createService(RestAPI::class.java)
    }

    val db  = RecipeDatabase.getInstance(context)
    val dao = db.recipeDao()

    suspend fun getIngredients(): List<String> = withContext(Dispatchers.IO) {
        api.getAllIngredients()
            ?.ingredients
            .orEmpty()
            .mapNotNull { it.strIngredient }
            .sorted()
    }

    suspend fun getRecipesByIngredient(ingredient: String): List<RecipeSummaryIM> =
        withContext(Dispatchers.IO) {
            val dtoWrapper: RecipesByIngredientDTO =
                api.getRecipesByIngredient(ingredient)
            dtoWrapper.meals
                .orEmpty()
                .map { summaryDto: RecipeSummaryDTO ->
                    Mapper.mapRecipSummaryDtoToRecipeSummaryIm(summaryDto)
                }
        }

    suspend fun getRecipeDetails(id: String): RecipeDetailsIM = withContext(Dispatchers.IO) {
        dao.getById(id)?.let { entity ->
            return@withContext Mapper.mapRecipeDetailsToRecipeDetailsIm(entity.isFavorite,  entity)
        }
        val dtoWrapper: RecipesByIdDTO = api.getRecipeDetails(id)
        val dto: RecipeDetailsDTO = dtoWrapper.meals
            .orEmpty()
            .firstOrNull()
            ?: throw IllegalArgumentException("Unknown recipe ID: $id")

        val entity = Mapper.mapRecipeDetailsDtoToRecipeDetails(
            isFavorite = false,
            dto = dto         )

        dao.insert(entity)

        Mapper.mapRecipeDetailsToRecipeDetailsIm(entity.isFavorite, entity)
    }

    fun getFavoriteRecipes(): Flow<List<RecipeSummaryIM>> =
        dao.favorites()
            .map { listOfEntities ->
                listOfEntities.map { ent ->
                    Mapper.mapRecipeDetailsToRecipeSummaryIm(ent)
                }
            }

    suspend fun toggleFavorite(id: String) = withContext(Dispatchers.IO) {
        dao.getById(id)?.let { ent ->
            val updated = ent.copy(isFavorite = !(ent.isFavorite ?: false))
            dao.insert(updated)
        }
    }
    suspend fun getRecipeDetailsFromCache(id: String): RecipeDetailsIM? =
        withContext(Dispatchers.IO) {
            dao.getById(id)?.let { Mapper.mapRecipeDetailsToRecipeDetailsIm(it.isFavorite, it) }
        }
}
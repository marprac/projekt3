package si.uni_lj.fri.pbd.classproject3.rest

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import si.uni_lj.fri.pbd.classproject3.models.dto.*

interface RestAPI {
    @GET("list.php?i=list")
    suspend fun getAllIngredients(): Response<IngredientsDTO>

    // DONE: Add missing endpoints

    @GET("filter.php")
    suspend fun filterByMainIngredient(
        @Query("i") mainIngredient: String
    ): Response<RecipesByIngredientsDTO>

    @GET("lookup.php")
    suspend fun lookupRecipeById(
        @Query("i") recipeId: String
    ): Response<RecipesByIdDTO>

}
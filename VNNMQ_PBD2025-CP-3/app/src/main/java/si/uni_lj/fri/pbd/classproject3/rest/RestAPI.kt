package si.uni_lj.fri.pbd.classproject3.rest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import si.uni_lj.fri.pbd.classproject3.models.dto.*

interface RestAPI {
    @GET("list.php?i=list")
    suspend fun getAllIngredients(): IngredientsDTO?

    @GET("filter.php")
    suspend fun getRecipesByIngredient(
        @Query("i") ingredient: String
    ): RecipesByIngredientDTO

    @GET("lookup.php")
    suspend fun getRecipeDetails(
        @Query("i") id: String
    ): RecipesByIdDTO

}
package si.uni_lj.fri.pbd.classproject3.rest

import retrofit2.http.GET
import retrofit2.http.Query
import si.uni_lj.fri.pbd.classproject3.models.dto.*
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipesByIdDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipesByIngredientDTO

interface RestAPI {

    @GET("list.php?i=list")
    suspend fun getAllIngredients(): IngredientsDTO?


    @GET("filter.php")
    suspend fun getRecipesByIngredient(@Query("i") ingredient: String): RecipesByIngredientDTO?

    @GET("lookup.php")
    suspend fun getRecipeDetailsById(@Query("i") id: String): RecipesByIdDTO?

}
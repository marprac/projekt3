package si.uni_lj.fri.pbd.classproject3.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.database.dao.RecipeDao
import si.uni_lj.fri.pbd.classproject3.models.Mapper.mapRecipeDetailsDtoToRecipeDetails
import si.uni_lj.fri.pbd.classproject3.models.Mapper.mapRecipeDetailsToRecipeDetailsDTO
import si.uni_lj.fri.pbd.classproject3.models.Mapper.mapRecipeDetailsToRecipeDetailsIm
import si.uni_lj.fri.pbd.classproject3.models.Mapper.mapRecipeDetailsToRecipeSummaryIm
import si.uni_lj.fri.pbd.classproject3.models.RecipeDetailsIM
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.models.dto.IngredientsDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeDetailsDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipesByIdDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipesByIngredientDTO
import si.uni_lj.fri.pbd.classproject3.rest.RestAPI
import si.uni_lj.fri.pbd.classproject3.rest.ServiceGenerator

class RecipeRepository(application: Application?) {
    //List of ingredients
    private val _ingredients = MutableLiveData<IngredientsDTO?>()
    val ingredients: LiveData<IngredientsDTO?> = _ingredients

    //Recipes containing ingredient to show on search screen
    private val _recipesByIngredient = MutableLiveData<RecipesByIngredientDTO?>()
    val recipesByIngredient = _recipesByIngredient

    //Recipe details to show on recipe details screen
    private val _recipesById = MutableLiveData<RecipeDetailsDTO?>()
    val recipesById = _recipesById

    //Recipes favourited to show on favourites screen
    private val _recipesFavourited = MutableLiveData<List<RecipeSummaryIM>>()
    val recipesFavourited = _recipesFavourited

    //Loading state for recipe details
    val isLoadingId = MutableLiveData<Boolean>()

    //Search result telling if currently selected recipe is favourited
    val searchResult = MutableLiveData<Boolean>()

    //Loading state for ingredients to use on splash screen
    val ingredientsLoading = MutableLiveData<Boolean>()

    //Loading state for search screen
    val isRefreshing = MutableLiveData<Boolean>()

    private val repositoryScope = CoroutineScope(Dispatchers.IO)
    private val restAPI: RestAPI
    private val recipeDao: RecipeDao?

    //Get recipes containing ingredient
    fun getRecipesByIngredient(ingredient: String){
        Log.d("RecipeRepository", "getRecipesByIngredient: $ingredient")
        isRefreshing.postValue(true)
        repositoryScope.launch {
            try {
                val recipesByIngredientData = restAPI.getRecipesByIngredient(ingredient)
                _recipesByIngredient.postValue(recipesByIngredientData)
            }catch (e: Exception){
                Log.e("Error", e.message.toString())
                isRefreshing.postValue(false)
            }finally {
                isRefreshing.postValue(false)
            }
        }
    }

    //Get recipe details from rest api
    fun getRecipesById(id: String){
        Log.d("RecipeRepository", "getRecipesById: $id")
        isLoadingId.postValue(true)
        repositoryScope.launch {
            try {
                val recipesByIdData = restAPI.getRecipesById(id)
                _recipesById.postValue(recipesByIdData?.recipes?.get(0))
            }catch (e: Exception){
                Log.e("Error", e.message.toString())
            }finally {
                isLoadingId.postValue(false)
            }
        }
    }

    //Get recipe details from database
    fun getRecipesByIdOffline(id: String){
        Log.d("RecipeRepository", "getRecipesByIdOffline: $id")
        isLoadingId.postValue(true)
        RecipeDatabase.databaseWriteExecutor.execute(Runnable {
            val recipe = recipeDao?.getRecipeById(id)
            val recipeDTO : RecipeDetailsDTO?
            if (recipe != null){
                recipeDTO = mapRecipeDetailsToRecipeDetailsDTO(recipe?.isFavorite, recipe!!)
            }else{
                recipeDTO = null
            }
            _recipesById.postValue(recipeDTO)
            isLoadingId.postValue(false)
        })
    }

    //Check if recipe is favourited
    fun isRecipeInFavorites(id: String){
        Log.d("RecipeRepository", "isRecipeInFavorites: $id")
        RecipeDatabase.databaseWriteExecutor.execute(Runnable{
            val recipe = recipeDao?.isRecipeInFavorites(id)
            if (recipe != null) {
                if(recipe > 0){
                    searchResult.postValue(true)
                }else{
                    searchResult.postValue(false)
                }
            }else{
                searchResult.postValue(false)
            }
        })
    }

    //Add selected recipe to favourites
    fun addRecipeToFavorites(recipe: RecipeDetailsDTO){
        Log.d("RecipeRepository", "addRecipeToFavorites: $recipe")
        RecipeDatabase.databaseWriteExecutor.execute(Runnable{
            recipeDao?.insertRecipe(mapRecipeDetailsDtoToRecipeDetails(true,recipe))
        })
    }

    //Remove selected recipe from favourites
    fun removeRecipeFromFavorites(id: String){
        Log.d("RecipeRepository", "removeRecipeFromFavorites: $id")
        RecipeDatabase.databaseWriteExecutor.execute(Runnable{
            recipeDao?.deleteRecipe(id)
        })
    }

    //Get all favourited recipes
    fun getRecipesFavourited(){
        RecipeDatabase.databaseWriteExecutor.execute(Runnable{
            val recipes = recipeDao?.getAllRecipes()
            val recipesFavouritedList = mutableListOf<RecipeSummaryIM>()
            recipes?.map { recipe -> recipesFavouritedList.add(mapRecipeDetailsToRecipeSummaryIm(recipe)) }
            _recipesFavourited.postValue(recipesFavouritedList)
        })
    }

    //Get all ingredients from rest api
    fun getAllIngredients() {
        repositoryScope.launch {
            try {
                val ingredientsData = restAPI.getAllIngredients()
                _ingredients.postValue(ingredientsData)
            }catch (e: Exception){
                Log.e("Error", e.message.toString())
            }finally {
                ingredientsLoading.postValue(false)
            }
        }
    }

    //Reset recipe details screen
    fun resetRecipesById(){
        _recipesById.postValue(null)
    }

    init {
        restAPI = ServiceGenerator.createService(RestAPI::class.java)
        isLoadingId.value = false
        ingredientsLoading.value = true
        isRefreshing.value = false

        val db: RecipeDatabase? = application?.let {
            RecipeDatabase.getDatabase(it.applicationContext) }

        recipeDao = db?.recipeDao()

        //Get all ingredients
        getAllIngredients()
    }
}
package si.uni_lj.fri.pbd.classproject3.models.dto

import com.google.gson.annotations.SerializedName

class RecipesByIngredientDTO(
    @SerializedName("meals") val recipes: List<RecipeSummaryDTO>? = null
)
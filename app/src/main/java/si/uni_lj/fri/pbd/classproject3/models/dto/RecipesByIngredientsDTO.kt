package si.uni_lj.fri.pbd.classproject3.models.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecipesByIngredientsDTO(
    @SerializedName("meals")
    @Expose
    val meals: List<RecipeSummaryDTO>? = null

)
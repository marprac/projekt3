package si.uni_lj.fri.pbd.classproject3.models.dto

import com.google.gson.annotations.SerializedName

class RecipesByIdDTO(
    @SerializedName("meals") val recipes: List<RecipeDetailsDTO>? = null
)
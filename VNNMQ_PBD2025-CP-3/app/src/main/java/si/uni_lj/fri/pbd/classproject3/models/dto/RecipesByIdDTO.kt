// RecipesByIdDTO.kt
package si.uni_lj.fri.pbd.classproject3.models.dto

import com.google.gson.annotations.SerializedName

data class RecipesByIdDTO(
    @SerializedName("meals")
    val meals: List<RecipeDetailsDTO>?
)
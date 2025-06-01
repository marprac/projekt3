package si.uni_lj.fri.pbd.classproject3.models.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecipeSummaryDTO (
    @SerializedName("strMeal")
    @Expose
    val strMeal: String? = null,

    @SerializedName("strMealThumb")
    @Expose
    val strMealThumb: String? = null,

    @SerializedName("idMeal")
    @Expose
    val idMeal: String? = null
)
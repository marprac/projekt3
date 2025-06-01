package si.uni_lj.fri.pbd.classproject3.models.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecipeSummaryDTO (
    @SerializedName("strMeal")
    @Expose
    val strMeal: String,

    @SerializedName("strMealThumb")
    @Expose
    val strMealThumb: String,

    @SerializedName("idMeal")
    @Expose
    val idMeal: String
)
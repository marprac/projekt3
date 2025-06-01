package si.uni_lj.fri.pbd.classproject3.models.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecipeDetailsDTO(
    @SerializedName("idMeal")
    @Expose
    val idMeal: String? = null,

    @SerializedName("strMeal")
    @Expose
    val strMeal: String? = null,


    @SerializedName("strDrinkAlternate")
    @Expose
    val strDrinkAlternate: String? = null,

    @SerializedName("strCategory")
    @Expose
    val strCategory: String? = null,

    @SerializedName("strArea")
    @Expose
    val strArea: String? = null,

    @SerializedName("strInstructions")
    @Expose
    val strInstructions: String? = null,

    @SerializedName("strMealThumb")
    @Expose
    val strMealThumb: String? = null,

    @SerializedName("strTags")
    @Expose
    val strTags: String? = null,

    @SerializedName("strYoutube")
    @Expose
    val strYoutube: String? = null,

    // Ingredients
    @SerializedName("strIngredient1") @Expose val strIngredient1: String? = null,
    @SerializedName("strIngredient2") @Expose val strIngredient2: String? = null,
    @SerializedName("strIngredient3") @Expose val strIngredient3: String? = null,
    @SerializedName("strIngredient4") @Expose val strIngredient4: String? = null,
    @SerializedName("strIngredient5") @Expose val strIngredient5: String? = null,
    @SerializedName("strIngredient6") @Expose val strIngredient6: String? = null,
    @SerializedName("strIngredient7") @Expose val strIngredient7: String? = null,
    @SerializedName("strIngredient8") @Expose val strIngredient8: String? = null,
    @SerializedName("strIngredient9") @Expose val strIngredient9: String? = null,
    @SerializedName("strIngredient10") @Expose val strIngredient10: String? = null,
    @SerializedName("strIngredient11") @Expose val strIngredient11: String? = null,
    @SerializedName("strIngredient12") @Expose val strIngredient12: String? = null,
    @SerializedName("strIngredient13") @Expose val strIngredient13: String? = null,
    @SerializedName("strIngredient14") @Expose val strIngredient14: String? = null,
    @SerializedName("strIngredient15") @Expose val strIngredient15: String? = null,
    @SerializedName("strIngredient16") @Expose val strIngredient16: String? = null,
    @SerializedName("strIngredient17") @Expose val strIngredient17: String? = null,
    @SerializedName("strIngredient18") @Expose val strIngredient18: String? = null,
    @SerializedName("strIngredient19") @Expose val strIngredient19: String? = null,
    @SerializedName("strIngredient20") @Expose val strIngredient20: String? = null,

    // Measures
    @SerializedName("strMeasure1") @Expose val strMeasure1: String? = null,
    @SerializedName("strMeasure2") @Expose val strMeasure2: String? = null,
    @SerializedName("strMeasure3") @Expose val strMeasure3: String? = null,
    @SerializedName("strMeasure4") @Expose val strMeasure4: String? = null,
    @SerializedName("strMeasure5") @Expose val strMeasure5: String? = null,
    @SerializedName("strMeasure6") @Expose val strMeasure6: String? = null,
    @SerializedName("strMeasure7") @Expose val strMeasure7: String? = null,
    @SerializedName("strMeasure8") @Expose val strMeasure8: String? = null,
    @SerializedName("strMeasure9") @Expose val strMeasure9: String? = null,
    @SerializedName("strMeasure10") @Expose val strMeasure10: String? = null,
    @SerializedName("strMeasure11") @Expose val strMeasure11: String? = null,
    @SerializedName("strMeasure12") @Expose val strMeasure12: String? = null,
    @SerializedName("strMeasure13") @Expose val strMeasure13: String? = null,
    @SerializedName("strMeasure14") @Expose val strMeasure14: String? = null,
    @SerializedName("strMeasure15") @Expose val strMeasure15: String? = null,
    @SerializedName("strMeasure16") @Expose val strMeasure16: String? = null,
    @SerializedName("strMeasure17") @Expose val strMeasure17: String? = null,
    @SerializedName("strMeasure18") @Expose val strMeasure18: String? = null,
    @SerializedName("strMeasure19") @Expose val strMeasure19: String? = null,
    @SerializedName("strMeasure20") @Expose val strMeasure20: String? = null,

    @SerializedName("strSource")
    @Expose
    val strSource: String? = null,

    @SerializedName("strImageSource")
    @Expose
    val strImageSource: String? = null,

    @SerializedName("strCreativeCommonsConfirmed")
    @Expose
    val strCreativeCommonsConfirmed: String? = null,

    @SerializedName("dateModified")
    @Expose
    val dateModified: String? = null
)

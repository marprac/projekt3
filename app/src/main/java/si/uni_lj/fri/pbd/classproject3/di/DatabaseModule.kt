package si.uni_lj.fri.pbd.classproject3.di
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun db(@ApplicationContext ctx: Context): RecipeDatabase =
        RecipeDatabase.getDatabase(ctx)

    @Provides
    fun dao(db: RecipeDatabase): RecipeDao = db.recipeDao()
}
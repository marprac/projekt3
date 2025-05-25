package si.uni_lj.fri.pbd.classproject3.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import si.uni_lj.fri.pbd.classproject3.database.RecipeDatabase
import si.uni_lj.fri.pbd.classproject3.database.dao.RecipeDao
import si.uni_lj.fri.pbd.classproject3.Constants

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRecipeDatabase(@ApplicationContext appContext: Context): RecipeDatabase {
        return Room.databaseBuilder(
            appContext,
            RecipeDatabase::class.java,
            Constants.DB_NAME
        )
            // .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideRecipeDao(database: RecipeDatabase): RecipeDao {
        return database.recipeDao()
    }
}

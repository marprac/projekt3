package si.uni_lj.fri.pbd.classproject3.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import si.uni_lj.fri.pbd.classproject3.Constants
import si.uni_lj.fri.pbd.classproject3.database.dao.RecipeDao
import si.uni_lj.fri.pbd.classproject3.database.entity.RecipeDetails
import java.util.concurrent.Executors

@androidx.room.Database(entities = [RecipeDetails::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null


        fun getDatabase(context: Context): RecipeDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                            RecipeDatabase::class.java, Constants.DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

    }
}
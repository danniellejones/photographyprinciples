package cp3406.a2.lenslearn.data

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [CategoryEntity::class, ImageEntity::class, TaskEntity::class,
        UserImageEntity::class, UserProgress::class],
    version = 1,
    exportSchema = false
)
abstract class CategoryDatabase : RoomDatabase() {

    abstract val categoryDao: CategoryDao  // Return instance of DAO interface

    companion object {
        @Volatile
        private var INSTANCE: CategoryDatabase? = null

        fun getInstance(context: Context): CategoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CategoryDatabase::class.java,
                    "category_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Read and parse JSON data, then insert it into the database
                            val categoriesJson = readJsonFile(context, "data.json")
                            val categories = parseCategoriesJson(categoriesJson)
                            INSTANCE?.let { insertCategoriesIntoDatabase(it, categories) }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /** Read .json file */
        private fun readJsonFile(context: Context, fileName: String): String {
            return context.assets.open(fileName)
                .bufferedReader()
                .use { it.readText() }
        }

        /** Parse .json data into CategoryEntity */
        private fun parseCategoriesJson(jsonString: String): List<CategoryEntity> {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val listType = Types.newParameterizedType(
                List::class.java, CategoryEntity::class.java
            )
            val adapter: JsonAdapter<List<CategoryEntity>> = moshi.adapter(listType)
            return adapter.fromJson(jsonString) ?: emptyList()
        }

        /** Insert CategoryEntity into Room Database */
        private fun insertCategoriesIntoDatabase(
            database: CategoryDatabase,
            categories: List<CategoryEntity>
        ) {
            val categoryDao = database.categoryDao
            CoroutineScope(Dispatchers.IO).launch {
                for (category in categories) {
                    categoryDao.insert(category)
                }
            }
        }
    }
}
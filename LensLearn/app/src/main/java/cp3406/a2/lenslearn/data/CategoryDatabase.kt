/**
 * Room Database, abstract class, and companion.
 */

package cp3406.a2.lenslearn.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CategoryEntity::class, ImageEntity::class, TaskEntity::class,
        UserImageEntity::class, UserProgress::class],
    version = 1,
    exportSchema = false
)
abstract class CategoryDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao  // Return instance of DAO interface

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
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package cp3406.a2.lenslearn.repository

import android.content.Context
import android.util.Log
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cp3406.a2.lenslearn.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val FILENAME = "data.json"
private const val TAG = "CategoryRepository"

//class CategoryRepository(private val categoryDao: CategoryDao) {
class CategoryRepository(private val context: Context) {

    private val categoryDao = CategoryDatabase.getInstance(context).categoryDao()

    init {
//        val categoryDatabase = CategoryDatabase.getInstance(context)
//        categoryDao = categoryDatabase.categoryDao()

        val jsonDataString = readJsonFile(context, FILENAME)
        Log.i(TAG, "JSON STRING: $jsonDataString")
//        insertDataFromJson(jsonDataString)
        parseAndAddToRoomDatabase(jsonDataString)
    }

    /** Read .json file return as string */
    private fun readJsonFile(context: Context, fileName: String): String {
        Log.d(TAG, "Read Json File")
        return context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }


//

    private fun parseAndAddToRoomDatabase(json: String) {
        Log.d(TAG, "Enter Parse")
        val moshi = Moshi.Builder().build()
        Log.d(TAG, "Moshi")
        // Create JSON adapters for each entity

        val adapter: JsonAdapter<CategoryEntity> = moshi.adapter(CategoryEntity::class.java)
        val categoryEntity = adapter.fromJson(categoriesJson)


        val listType = Types.newParameterizedType(List::class.java, CategoryEntity::class.java)
        val categoryAdapter: JsonAdapter<List<CategoryEntity>> = moshi.adapter(listType)
//        val categoryAdapter = moshi.adapter<List<CategoryEntity>>()

//        val categoryAdapter: JsonAdapter<List<CategoryEntity>> =
//            moshi.adapter(Types.newParameterizedType(List::class.java, CategoryEntity::class.java))
        Log.d(TAG, "Category")
//        val imageAdapter: JsonAdapter<List<ImageEntity>> =
//            moshi.adapter(Types.newParameterizedType(List::class.java, ImageEntity::class.java))
//        val taskAdapter: JsonAdapter<List<TaskEntity>> =
//            moshi.adapter(Types.newParameterizedType(List::class.java, TaskEntity::class.java))
//
        val data = moshi.adapter<Map<String, List<Any>>>(
            Types.newParameterizedType(
                Map::class.java,
                String::class.java,
                List::class.java
            )
        )
            .fromJson(json)
//
        val categoryList: List<CategoryEntity>? =
            data?.get("CategoryEntity")?.let { categoryAdapter.fromJsonValue(it) }
//        val imageList: List<ImageEntity>? =
//            data?.get("ImageEntity")?.let { imageAdapter.fromJsonValue(it) }
//        val taskList: List<TaskEntity>? =
//            data?.get("TaskEntity")?.let { taskAdapter.fromJsonValue(it) }
//
//        // Insert data into the database using DAO methods
        CoroutineScope(Dispatchers.IO).launch {
            if (categoryList != null) {
                categoryDao.insertCategories(categoryList)
            }
//            if (imageList != null) {
//                categoryDao.insertImages(imageList)
//            }
//            if (taskList != null) {
//                categoryDao.insertTasks(taskList)
//            }
        }
    }

    private suspend fun insertCategories(categories: List<CategoryEntity>) {
        for (category in categories) {
            categoryDao.insertCategory(category)
        }
    }

    private suspend fun insertImages(images: List<ImageEntity>) {
        for (image in images) {
            categoryDao.insertImage(image)
        }
    }

    private suspend fun insertTasks(tasks: List<TaskEntity>) {
        for (task in tasks) {
            categoryDao.insertTask(task)
        }
    }


    // Get all categories
    suspend fun getAllCategories(): List<CategoryEntity> {
        return categoryDao.getAllCategories()
    }

    // Get a category by category Id
    suspend fun getCategoryById(categoryId: Int): CategoryEntity? {
        Log.d("CategoryRepository", "categoryId passed: $categoryId")
        return categoryDao.get(categoryId)
    }

    // Get x images from category == selected category for identify phase
    suspend fun getCorrectIdentifyImages(selectedCategoryId: Int, limit: Int): List<ImageEntity> {
        return categoryDao.getCorrectIdentifyImages(selectedCategoryId, limit)
    }

    // Get x images from category != selected category for identify phase
    suspend fun getIncorrectIdentifyImages(selectedCategoryId: Int, limit: Int): List<ImageEntity> {
        return categoryDao.getIncorrectIdentifyImages(selectedCategoryId, limit)
    }

    // Get a random task from the selected category for do phase
    suspend fun getRandomTask(selectedCategoryId: Int): TaskEntity? {
        return categoryDao.getRandomTask(selectedCategoryId)
    }

    // Update the progress of a category
    suspend fun updateCategoryProgress(
        categoryId: Int,
        hasShared: Boolean,
        hasCompletedTask: Boolean,
        progressPercentage: Int
    ) {
        val progress = UserProgress(
            categoryId = categoryId,
            hasShared = hasShared,
            hasCompletedTask = hasCompletedTask,
            progressPercentage = progressPercentage
        )
        categoryDao.insertOrUpdateProgress(progress)
    }

    // Retrieve the last photograph taken by the user for the last task entered
    suspend fun getLastUserImageForLastTask(): UserImageEntity? {
        return categoryDao.getLastUserImageForLastTask()
    }

    // Retrieve the second last photograph taken by the user for the last task entered
    suspend fun getSecondLastUserImageForLastTask(): UserImageEntity? {
        return categoryDao.getSecondLastUserImageForLastTask()
    }

    // Retrieve the third last photograph taken by the user for the last task entered
    suspend fun getThirdLastUserImageForLastTask(): UserImageEntity? {
        return categoryDao.getThirdLastUserImageForLastTask()
    }

    // Insert a user image into the user image table
    suspend fun insertUserImage(userImageEntity: UserImageEntity) {
        categoryDao.insertUserImage(userImageEntity)
    }




//        private fun parseCategoriesJson(jsonString: String): List<CategoryEntity> {
//            Log.d("CategoryDatabase", "Parse Json File")
//            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//            val adapter: JsonAdapter<CategoryJsonData> = moshi.adapter(CategoryJsonData::class.java)
//            val jsonData = adapter.fromJson(jsonString) ?: return emptyList()
//
//            val categories: MutableList<CategoryEntity> = mutableListOf()
//
//            // Process CategoryEntity data
//            jsonData.categoryEntity?.let { categories.addAll(it) }
//
//            return categories
//        }
//
//        /** Parse .json data into ImageEntity */
//        private fun parseImagesJson(jsonString: String): List<ImageEntity> {
//            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//            val adapter: JsonAdapter<List<ImageEntity>> = moshi.adapter(Types.newParameterizedType(List::class.java, ImageEntity::class.java))
//            return adapter.fromJson(jsonString) ?: emptyList()
//        }
//
//        /** Parse .json data into TaskEntity */
//        private fun parseTasksJson(jsonString: String): List<TaskEntity> {
//            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//            val adapter: JsonAdapter<List<TaskEntity>> = moshi.adapter(Types.newParameterizedType(List::class.java, TaskEntity::class.java))
//            return adapter.fromJson(jsonString) ?: emptyList()
//        }
//
//        private fun insertAllEntitiesIntoDatabase(
//            database: CategoryDatabase,
//            categories: List<CategoryEntity>,
//            images: List<ImageEntity>,
//            tasks: List<TaskEntity>
//        ) {
//            Log.d("CategoryDatabase", "Inserting Categories, Images, and Tasks into Database...")
//            val categoryDao = database.categoryDao
//            CoroutineScope(Dispatchers.IO).launch {
//                for (category in categories) {
//                    // Insert CategoryEntity
//                    categoryDao.insert(category)
//                    Log.d("CategoryDatabase", "Inserted Category: $category")
//
//                    // Insert ImageEntities associated with the category
//                    val categoryImages = images.filter { it.categoryId == category.id }
//                    for (image in categoryImages) {
//                        categoryDao.insert(image)
//                        Log.d("CategoryDatabase", "Inserted Image: $image")
//                    }
//
//                    // Insert TaskEntities associated with the category
//                    val categoryTasks = tasks.filter { it.categoryId == category.id }
//                    for (task in categoryTasks) {
//                        categoryDao.insert(task)
//                        Log.d("CategoryDatabase", "Inserted Task: $task")
//                    }
//                }
//            }
//        }
//
//        /** Insert CategoryEntity into Room Database */
//        private fun insertCategoriesIntoDatabase(
//            database: CategoryDatabase,
//            categories: List<CategoryEntity>
//        ) {
//            Log.d("CategoryDatabase", "Inserting Categories into Database...")
//            val categoryDao = database.categoryDao
//            CoroutineScope(Dispatchers.IO).launch {
//                for (category in categories) {
//                    categoryDao.insert(category)
//                    Log.d("CategoryDatabase", "Inserted Category: $category")
//                }
//            }
//        }

    /** Parse .json data into Room Database */
//    private fun insertDataFromJson(jsonString: String) {
//        // Use Moshi to parse JSON data
//        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
//        Log.i(TAG, "Parse Start")
//
//        // Parse CategoryEntity list from JSON
//        val categoryAdapter: JsonAdapter<List<CategoryEntity>> =
//            moshi.adapter(Types.newParameterizedType(List::class.java, CategoryEntity::class.java))
//        val categoryEntities = categoryAdapter.fromJson(jsonString) ?: emptyList()
//        Log.i(TAG, "Parsed ${categoryEntities.size} CategoryEntity objects")
//
//        // Parse ImageEntity list from JSON
//        val imageAdapter: JsonAdapter<List<ImageEntity>> =
//            moshi.adapter(Types.newParameterizedType(List::class.java, ImageEntity::class.java))
//        val imageEntities = imageAdapter.fromJson(jsonString) ?: emptyList()
//        Log.i(TAG, "Parsed ${imageEntities.size} ImageEntity objects")
//
//        // Parse TaskEntity list from JSON
//        val taskAdapter: JsonAdapter<List<TaskEntity>> =
//            moshi.adapter(Types.newParameterizedType(List::class.java, TaskEntity::class.java))
//        val taskEntities = taskAdapter.fromJson(jsonString) ?: emptyList()
//        Log.i(TAG, "Parsed ${taskEntities.size} TaskEntity objects")
//
//        // Insert data into the database using DAO methods
//        CoroutineScope(Dispatchers.IO).launch {
//            insertCategories(categoryEntities)
//            insertImages(imageEntities)
//            insertTasks(taskEntities)
//        }
//    }
}
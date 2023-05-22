/**
 * Category Repository is the intermediary between the Room Database and the View Model.
 * It contains the methods for accessing the data, with correlation to the DAO interface.
 */

package cp3406.a2.lenslearn.repository

import android.content.Context
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import cp3406.a2.lenslearn.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val FILENAME_CATEGORIES = "category_data.json"
const val FILENAME_IMAGES = "image_data.json"
const val FILENAME_TASKS = "task_data.json"
private const val TAG = "CategoryRepository"

class CategoryRepository(private val context: Context) {

    // Connect Room database to Repository
    private val categoryDao = CategoryDatabase.getInstance(context).categoryDao()

    init {
        // Connect Room database to Repository
//        val categoryDatabase = CategoryDatabase.getInstance(context)
//        categoryDao = categoryDatabase.categoryDao()

        // Read Json Files
        val jsonStringCategory = readJsonFile(context, FILENAME_CATEGORIES)
        val jsonStringImage = readJsonFile(context, FILENAME_IMAGES)
        val jsonStringTask = readJsonFile(context, FILENAME_TASKS)
//        Log.i(TAG, "JSON STRING: $jsonStringCategory \n $jsonStringImage \n $jsonStringTask")

        // Parse Json String data into Entities List
        val categories : List<CategoryEntity> = parseJsonToEntity(jsonStringCategory)
        val images : List<ImageEntity> = parseJsonToEntity(jsonStringImage)
        val tasks : List<TaskEntity> = parseJsonToEntity(jsonStringTask)

        // Insert Entities into Room Database
        insertCategoryEntityData(categories, images, tasks)
    }

    /** Read .json file return as string */
    private fun readJsonFile(context: Context, fileName: String): String {
        Log.d(TAG, "Read Json File")
        return context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }

    /** TESTING ONLY Parse Json string and derive category entity */
    private fun parseJsonToCategoryEntity(jsonString: String): List<CategoryEntity> {
        val moshi = Moshi.Builder().build()
        Log.d(TAG, "Enter Parse")
        val listType = Types.newParameterizedType(List::class.java, CategoryEntity::class.java)
        Log.d(TAG, "List Type Created")
        val categoryAdapter: JsonAdapter<List<CategoryEntity>> = moshi.adapter(listType)
        Log.d(TAG, "Category Adapter done")
        return categoryAdapter.fromJson(jsonString) ?: emptyList()
    }

    /** Parse Json string and derive entities */
    private inline fun <reified T> parseJsonToEntity(jsonString: String): List<T> {
        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, T::class.java)
        val entityAdapter: JsonAdapter<List<T>> = moshi.adapter(listType)
        return entityAdapter.fromJson(jsonString) ?: emptyList()
    }

    /** Insert entities into Room Database */
    private fun insertCategoryEntityData(categories: List<CategoryEntity>, images: List<ImageEntity>, tasks: List<TaskEntity>) {

        Log.d(TAG, "Categories: $categories")
        Log.d(TAG, "Images: $images")
        Log.d(TAG, "Tasks: $tasks")
        CoroutineScope(Dispatchers.IO).launch {
            if (categories != null) {
                categoryDao.insertCategories(categories)
            }
            if (images != null) {
                categoryDao.insertImages(images)
            }
            if (tasks != null) {
                categoryDao.insertTasks(tasks)
            }
        }
    }

    /** Get all categories */
    suspend fun getAllCategories(): List<CategoryEntity> {
        return categoryDao.getAllCategories()
    }

    /** Get a category by category Id */
    suspend fun getCategoryById(categoryId: Int): CategoryEntity? {
        Log.d("CategoryRepository", "categoryId passed: $categoryId")
        return categoryDao.getCategory(categoryId)
    }

    /** Get the selected category by using category Id */
    suspend fun getSelectedCategory(selectedCategoryId: Int): CategoryEntity? {
        Log.d("CategoryRepository", "categoryId passed: $selectedCategoryId")
        return categoryDao.getSelectedCategory(selectedCategoryId)
    }

    /** Get x images from category == selected category for identify phase */
    suspend fun getCorrectIdentifyImages(selectedCategoryId: Int, limit: Int): List<ImageEntity> {
        return categoryDao.getCorrectIdentifyImages(selectedCategoryId, limit)
    }

    /** Get x images from category != selected category for identify phase */
    suspend fun getIncorrectIdentifyImages(selectedCategoryId: Int, limit: Int): List<ImageEntity> {
        return categoryDao.getIncorrectIdentifyImages(selectedCategoryId, limit)
    }

    /** Get a random task from the selected category for do phase */
    suspend fun getRandomTask(selectedCategoryId: Int): TaskEntity? {
        return categoryDao.getRandomTask(selectedCategoryId)
    }

    /** Update the progress of a category */
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

    /** Retrieve the last photograph taken by the user for the last task entered */
    suspend fun getLastUserImageForLastTask(): UserImageEntity? {
        return categoryDao.getLastUserImageForLastTask()
    }

    /** Retrieve the second last photograph taken by the user for the last task entered */
    suspend fun getSecondLastUserImageForLastTask(): UserImageEntity? {
        return categoryDao.getSecondLastUserImageForLastTask()
    }

    /** Retrieve the third last photograph taken by the user for the last task entered */
    suspend fun getThirdLastUserImageForLastTask(): UserImageEntity? {
        return categoryDao.getThirdLastUserImageForLastTask()
    }

    /** Insert a user image into the user image table */
    suspend fun insertUserImage(userImageEntity: UserImageEntity) {
        categoryDao.insertUserImage(userImageEntity)
    }
}
package cp3406.a2.lenslearn.data

import androidx.room.*

@Dao
interface CategoryDao {

    /** Initialise Categories */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categoryEntityList: List<CategoryEntity>)

    /** Initialise Images */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImages(imageEntityList: List<ImageEntity>)

    /** Initialise Tasks */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTasks(taskEntityList: List<TaskEntity>)

    /** Add New Category */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categoryEntity: CategoryEntity)

    /** Add New Image */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(imageEntity: ImageEntity)

    /** Add New Task */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskEntity: TaskEntity)

    /** Add New User Image */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserImage(userImageEntity: UserImageEntity)

    /** Update User Progress */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: UserProgress)

    /** Update Category Entity */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(categoryEntity: CategoryEntity)

    /** Get All Categories */
    @Query("SELECT * FROM category")
    suspend fun getAllCategories(): List<CategoryEntity>

    /** Get Category that matches the Specified Category Id */
    @Query("SELECT * FROM category WHERE id = :categoryId")
    suspend fun getCategory(categoryId: Int): CategoryEntity?

    /** Get Category that matches the Selected Category Id */
    @Query("SELECT * FROM category WHERE id = :selectedCategoryId")
    suspend fun getSelectedCategory(selectedCategoryId: Int): CategoryEntity?

    /** Get x Images == Selected Category Id */
    @Query("SELECT * FROM image WHERE categoryId = :selectedCategoryId LIMIT :limit")
    suspend fun getCorrectIdentifyImages(selectedCategoryId: Int, limit: Int): List<ImageEntity>

    /** Get x Images != Selected Category Id */
    @Query("SELECT * FROM image WHERE categoryId != :selectedCategoryId LIMIT :limit")
    suspend fun getIncorrectIdentifyImages(selectedCategoryId: Int, limit: Int): List<ImageEntity>

    /** Get Random Task == Selected Category Id */
    @Query("SELECT * FROM task WHERE categoryId = :selectedCategoryId ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomTask(selectedCategoryId: Int): TaskEntity?

    /** Get Last User Image Taken */
    @Query("SELECT * FROM user_image WHERE taskId = (SELECT MAX(id) FROM task) ORDER BY id DESC LIMIT 1")
    suspend fun getLastUserImageForLastTask(): UserImageEntity?

    /** Get Second Last User Image Taken */
    @Query("SELECT * FROM user_image WHERE taskId = (SELECT MAX(id) FROM task ORDER BY id DESC LIMIT 1, 1) ORDER BY id DESC LIMIT 1")
    suspend fun getSecondLastUserImageForLastTask(): UserImageEntity?

    /** Get Third Last User Image Taken */
    @Query("SELECT * FROM user_image WHERE taskId = (SELECT MAX(id) FROM task ORDER BY id DESC LIMIT 2, 1) ORDER BY id DESC LIMIT 1")
    suspend fun getThirdLastUserImageForLastTask(): UserImageEntity?

    /** Delete All Records */
    @Query("DELETE FROM category")
    suspend fun deleteAll()

}
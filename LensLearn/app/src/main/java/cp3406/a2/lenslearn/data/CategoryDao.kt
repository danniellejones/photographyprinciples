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

    /** Add New User Progress */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProgress(progress: UserProgress)

    /** Update User Progress */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserProgress(progress: UserProgress)

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
    @Query("SELECT * FROM user_image ORDER BY id DESC LIMIT 1")
    suspend fun getLastUserImageForLastTask(): UserImageEntity?

    /** Get Second Last User Image Taken */
    @Query("SELECT * FROM user_image ORDER BY id DESC LIMIT 1 OFFSET 1")
    suspend fun getSecondLastUserImageForLastTask(): UserImageEntity?

    /** Get Third Last User Image Taken */
    @Query("SELECT * FROM user_image ORDER BY id DESC LIMIT 1 OFFSET 2")
    suspend fun getThirdLastUserImageForLastTask(): UserImageEntity?

    /** Get User Progress by Id */
    @Query("SELECT * FROM progress WHERE categoryId = :categoryId")
    suspend fun getUserProgressByCategoryId(categoryId: Int): UserProgress?

    /** Get All User Progress for All Categories */
    @Query("SELECT * FROM progress")
    suspend fun getAllUserProgress(): List<UserProgress>

    /** Delete All Category Records */
    @Query("DELETE FROM category")
    suspend fun deleteAll()

}
package cp3406.a2.lenslearn.data

import androidx.room.*

@Dao
interface CategoryDao {

    @Insert
    suspend fun insert(categoryEntity: CategoryEntity)

    @Insert
    suspend fun insertUserImage(userImageEntity: UserImageEntity)

    @Update
    suspend fun update(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM category")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query("SELECT * FROM category WHERE id = :key")
    suspend fun get(key: Int): CategoryEntity?

    @Query("SELECT * FROM image WHERE categoryId = :selectedCategoryId LIMIT :limit")
    suspend fun getCorrectIdentifyImages(selectedCategoryId: Int, limit: Int): List<ImageEntity>

    @Query("SELECT * FROM image WHERE categoryId != :selectedCategoryId LIMIT :limit")
    suspend fun getIncorrectIdentifyImages(selectedCategoryId: Int, limit: Int): List<ImageEntity>

    @Query("SELECT * FROM task WHERE categoryId = :selectedCategoryId ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomTask(selectedCategoryId: Int): TaskEntity?

    @Query("SELECT * FROM user_image WHERE taskId = (SELECT MAX(id) FROM task) ORDER BY id DESC LIMIT 1")
    suspend fun getLastUserImageForLastTask(): UserImageEntity?

    @Query("SELECT * FROM user_image WHERE taskId = (SELECT MAX(id) FROM task ORDER BY id DESC LIMIT 1, 1) ORDER BY id DESC LIMIT 1")
    suspend fun getSecondLastUserImageForLastTask(): UserImageEntity?

    @Query("SELECT * FROM user_image WHERE taskId = (SELECT MAX(id) FROM task ORDER BY id DESC LIMIT 2, 1) ORDER BY id DESC LIMIT 1")
    suspend fun getThirdLastUserImageForLastTask(): UserImageEntity?

    @Query("DELETE FROM category")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: UserProgress)

}
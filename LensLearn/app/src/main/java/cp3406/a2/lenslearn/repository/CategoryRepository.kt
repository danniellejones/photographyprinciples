package cp3406.a2.lenslearn.repository

import cp3406.a2.lenslearn.data.*

class CategoryRepository(private val categoryDao: CategoryDao) {

    // Get all categories
    suspend fun getAllCategories(): List<CategoryEntity> {
        return categoryDao.getAllCategories()
    }

    // Get a category by category Id
    suspend fun getCategoryById(categoryId: Int): CategoryEntity? {
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

}
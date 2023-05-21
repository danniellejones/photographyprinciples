package cp3406.a2.lenslearn

import cp3406.a2.lenslearn.data.*
import cp3406.a2.lenslearn.repository.CategoryRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CategoryRepositoryTest {

    @Mock
    private lateinit var categoryDao: CategoryDao

    private lateinit var categoryRepository: CategoryRepository

    @Before
    fun setup() {
        categoryRepository = CategoryRepository(categoryDao)
    }

    @Test
    fun testGetAllCategories() = runBlocking {
        val categories = listOf(
            CategoryEntity(1, "Category 1", "filename1", "definition1", "detailedInfo1"),
            CategoryEntity(2, "Category 2", "filename2", "definition2", "detailedInfo2")
        )

        // Mock the CategoryDao's getAllCategories function
        `when`(categoryDao.getAllCategories()).thenReturn(categories)

        // Call the repository method
        val result = categoryRepository.getAllCategories()

        // Verify the result
        assertEquals(categories, result)
    }

    @Test
    fun testGetCategoryById() = runBlocking {
        val categoryId = 1
        val category = CategoryEntity(categoryId, "Category 1", "filename1", "definition1", "detailedInfo1")

        // Mock the CategoryDao's get function
        `when`(categoryDao.get(categoryId)).thenReturn(category)

        // Call the repository method
        val result = categoryRepository.getCategoryById(categoryId)

        // Verify the result
        assertEquals(category, result)
    }

    @Test
    fun testGetCorrectIdentifyImages() = runBlocking {
        val selectedCategoryId = 1
        val limit = 2
        val images = listOf(
            ImageEntity(1, 1,"image1.jpg"),
            ImageEntity(2, 1,"image2.jpg"),
            ImageEntity(3, 1,"image3.jpg"),
            ImageEntity(4, 2,"image1.jpg"),
            ImageEntity(5, 2,"image2.jpg")
        )

        `when`(categoryDao.getCorrectIdentifyImages(selectedCategoryId, limit)).thenReturn(images)

        val result = categoryRepository.getCorrectIdentifyImages(selectedCategoryId, limit)

        assertEquals(images, result)
    }

    @Test
    fun testGetIncorrectIdentifyImages() = runBlocking {
        val selectedCategoryId = 1
        val limit = 2
        val images = listOf(
            ImageEntity(6, 2,"image3.jpg"),
            ImageEntity(7, 2,"image4.jpg"),
            ImageEntity(8, 2,"image5.jpg"),
            ImageEntity(9, 1,"image5.jpg"),
            ImageEntity(10,1,"image6.jpg")
        )

        `when`(categoryDao.getIncorrectIdentifyImages(selectedCategoryId, limit)).thenReturn(images)

        val result = categoryRepository.getIncorrectIdentifyImages(selectedCategoryId, limit)

        assertEquals(images, result)
    }

    @Test
    fun testGetRandomTask() = runBlocking {
        val selectedCategoryId = 1
        val tasks = listOf(
        TaskEntity(1, 1,"Task 1", "overlayImage01.png"),
        TaskEntity(1, 1,"Task 2", "overlayImage02.png"),
        TaskEntity(1, 1,"Task 3", "overlayImage03.png")
        )

        val taskCaptor = ArgumentCaptor.forClass(Int::class.java)
        `when`(categoryDao.getRandomTask(taskCaptor.capture())).thenReturn(tasks.random())

        val result = categoryRepository.getRandomTask(selectedCategoryId)

        // Verify that the captured argument is the expected category ID
        assertEquals(selectedCategoryId, taskCaptor.value)

        assertTrue(tasks.contains(result))
    }

    @Test
    fun testUpdateCategoryProgress() = runBlocking {
        val categoryId = 1
        val hasShared = true
        val hasCompletedTask = true
        val progressPercentage = 50

        val progress = UserProgress(
            categoryId = categoryId,
            hasShared = hasShared,
            hasCompletedTask = hasCompletedTask,
            progressPercentage = progressPercentage
        )

        categoryRepository.updateCategoryProgress(categoryId, hasShared, hasCompletedTask, progressPercentage)

        // Verify that the insertOrUpdateProgress method is called with the correct progress object
        verify(categoryDao).insertOrUpdateProgress(progress)
    }
}
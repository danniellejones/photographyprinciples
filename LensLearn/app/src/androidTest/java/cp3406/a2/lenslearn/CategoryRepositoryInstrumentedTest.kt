package cp3406.a2.lenslearn

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import cp3406.a2.lenslearn.data.CategoryDao
import cp3406.a2.lenslearn.data.CategoryDatabase
import cp3406.a2.lenslearn.data.CategoryEntity
import cp3406.a2.lenslearn.repository.CategoryRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryRepositoryInstrumentedTest {

    private lateinit var categoryDao: CategoryDao
    private lateinit var database: CategoryDatabase
    private lateinit var categoryRepository: CategoryRepository

    // Create test in memory
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, CategoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        categoryDao = database.categoryDao()
        categoryRepository = CategoryRepository(context)
    }

    // Clean up after test is run
    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun testGetCategory() = runBlocking {

        // Insert test data into the database
        val categories = listOf(
            CategoryEntity(1, "Balance", "img_example_balance.jpg", "Balance refers to the visual distribution of elements within a composition. It aims to achieve a sense of equilibrium and stability.", "In photography, balance plays a crucial role in creating harmonious and pleasing images. There are two types of balance: symmetrical and asymmetrical. Symmetrical balance involves dividing the frame into equal parts, creating a mirror-like effect. Asymmetrical balance, on the other hand, involves distributing visual weight unevenly to create interest and tension. Understanding and utilizing balance can help you create visually compelling photographs with a sense of stability or dynamic tension."),
            CategoryEntity(2, "Contrast", "img_example_contrast.jpg", "Contrast refers to the difference between light and dark areas in an image. It adds visual interest and helps emphasize important elements.", "In photography, contrast is an essential tool to create impact and draw attention to specific subjects. It can be achieved through variations in brightness, color, or texture. High contrast images have a significant difference between light and dark areas, resulting in bold and dramatic visuals. Low contrast images have minimal differences, leading to a softer and more subtle appearance. Understanding how to manipulate contrast effectively allows you to control the mood, focus, and visual hierarchy in your photographs.")
        )

        // Call the repository method
        val result = categoryRepository.getCategoryById(1)

        val expected = categories[0]

        // Verify the result
        assertThat(result, equalTo(expected))

        println("Result: $result")
        println("Expected: $expected")
    }
}
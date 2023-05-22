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
    fun testGetAllCategories() = runBlocking {
        // Insert test data into the database
        val categories = listOf(
            CategoryEntity(1, "Category 1", "filename1", "definition1", "detailedInfo1"),
            CategoryEntity(2, "Category 2", "filename2", "definition2", "detailedInfo2")
        )
        categories.forEach { categoryDao.insert(it) }

        // Call the repository method
        val result = categoryRepository.getAllCategories()

        // Verify the result
        assertThat(result, equalTo(categories))
    }
}
package cp3406.a2.lenslearn.model

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cp3406.a2.lenslearn.data.CategoryDatabase
import cp3406.a2.lenslearn.data.CategoryEntity
import cp3406.a2.lenslearn.data.UserImageEntity
import cp3406.a2.lenslearn.repository.CategoryRepository
import kotlinx.coroutines.launch


private const val LOG_TAG = "CategoryViewModel"

class CategoryViewModel(app: Application) : AndroidViewModel(app) {

    private val categoryRepository: CategoryRepository

    // CATEGORY - Select Category from on Click Event
    private val _selectedCategoryId: MutableLiveData<Int> = MutableLiveData()  // Private
    val selectedCategoryId: LiveData<Int> = _selectedCategoryId  // Public

    // LEARN -
    private val _category = MutableLiveData<CategoryEntity?>()
    val category: MutableLiveData<CategoryEntity?> get() = _category

    private val _isShaken: MutableLiveData<Boolean> = MutableLiveData()  // Private
    val isShaken: LiveData<Boolean> = _isShaken  // Public

    // SHARE - Last three images taken by the user
    private val _lastUserImageForLastTask: MutableLiveData<UserImageEntity?> = MutableLiveData()
    private val _secondLastUserImageForLastTask: MutableLiveData<UserImageEntity?> =
        MutableLiveData()
    private val _thirdLastUserImageForLastTask: MutableLiveData<UserImageEntity?> =
        MutableLiveData()


    /** Check size of study timer list and creates any missing timers */
    init {
        Log.i(LOG_TAG, "Category View Model Init")
        _selectedCategoryId.value = 0

        val categoryDao = CategoryDatabase.getInstance(app).categoryDao
        categoryRepository = CategoryRepository(categoryDao)
    }

    /** Set new Category Id */
    fun setCategoryId(newCategoryId: Int) {
        _selectedCategoryId.value = newCategoryId
    }

    /** Retrieve the category by the selected category Id */
    fun retrieveCategoryById(categoryId: Int) {
        viewModelScope.launch {
            val category = categoryRepository.getCategoryById(categoryId)
            _category.value = category
        }
    }

    /** Get last image taken by user for share fragment */
    fun getLastUserImageForLastTask(): LiveData<UserImageEntity?> {
        return _lastUserImageForLastTask
    }

    /** Get second last image taken by user for share fragment */
    fun getSecondLastUserImageForLastTask(): LiveData<UserImageEntity?> {
        return _secondLastUserImageForLastTask
    }

    /** Get third last image taken by user for share fragment */
    fun getThirdLastUserImageForLastTask(): LiveData<UserImageEntity?> {
        return _thirdLastUserImageForLastTask
    }

    suspend fun retrieveUserImagesForLastTask() {
        val lastImage = categoryRepository.getLastUserImageForLastTask()
        val secondLastImage = categoryRepository.getSecondLastUserImageForLastTask()
        val thirdLastImage = categoryRepository.getThirdLastUserImageForLastTask()

        // Update LiveData with image results
        _lastUserImageForLastTask.postValue(lastImage)
        _secondLastUserImageForLastTask.postValue(secondLastImage)
        _thirdLastUserImageForLastTask.postValue(thirdLastImage)
    }

    suspend fun getCategoryById(categoryId: Int): CategoryEntity? {
        return categoryRepository.getCategoryById(categoryId)
    }



    // Get the image resource ID based on the file name
    @SuppressLint("DiscouragedApi")
    fun getImageResId(fileName: String): Int {
        val resources = getApplication<Application>().resources
        val packageName = getApplication<Application>().packageName

        // Assuming your images are stored in the "drawable" directory
        return resources.getIdentifier(fileName, "drawable", packageName)
    }

}

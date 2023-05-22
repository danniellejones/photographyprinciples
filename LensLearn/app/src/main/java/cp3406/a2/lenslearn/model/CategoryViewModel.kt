/**
 * Category View Model links the Repository and the View (Activity/Fragment).
 * This is a shared view model between the category activity and all fragments.
 * The purpose is to retrieve and perform operations on the data ready for display.
 *
 * Notes:
 *
 * Use viewModelScope.launch to perform asynchronous operation across multiple fragments.
 * This will be linked to the view model lifecycle.
 *
 * Use suspend to perform asynchronous operation in a single fragment.
 * Prevents the main thread from being blocked when retrieving/loading is slower.
 */

package cp3406.a2.lenslearn.model

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cp3406.a2.lenslearn.data.CategoryEntity
import cp3406.a2.lenslearn.data.ImageEntity
import cp3406.a2.lenslearn.data.UserImageEntity
import cp3406.a2.lenslearn.repository.CategoryRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val LOG_TAG = "CategoryViewModel"

class CategoryViewModel(app: Application) : AndroidViewModel(app) {

    private val categoryRepository: CategoryRepository

//    private var currentIndex: Int = 0

    /** Set Up the Live Data */

    // CATEGORY - Select Category from on Click Event
    private val _selectedCategoryId: MutableLiveData<Int> = MutableLiveData()  // Private
    val selectedCategoryId: LiveData<Int> = _selectedCategoryId  // Public

    // LEARN - Retrieve selected category information and track if shaken
    private val _currentCategory = MutableLiveData<CategoryEntity>()
    val currentCategory: MutableLiveData<CategoryEntity> = _currentCategory

    private val _isShaken: MutableLiveData<Boolean> = MutableLiveData()  // Private
    val isShaken: LiveData<Boolean> = _isShaken  // Public

    // IDENTIFY - Retrieve both correct and incorrect images, track correct answers over total
    private val _identifyImagesList: MutableLiveData<List<ImageEntity>> = MutableLiveData()
    val identifyImagesList: LiveData<List<ImageEntity>> = _identifyImagesList

    private val _currentImageFileName: MutableLiveData<String> = MutableLiveData()
    val currentImageFileName: LiveData<String> = _currentImageFileName

    private val _correctCount: MutableLiveData<Int> = MutableLiveData()
    val correctCount: LiveData<Int> = _correctCount

    private val _totalImages: MutableLiveData<Int> = MutableLiveData()
    val totalImages: LiveData<Int> = _totalImages


    // SHARE - Last three images taken by the user
    private val _lastUserImageForLastTask: MutableLiveData<UserImageEntity?> = MutableLiveData()
    private val _secondLastUserImageForLastTask: MutableLiveData<UserImageEntity?> =
        MutableLiveData()
    private val _thirdLastUserImageForLastTask: MutableLiveData<UserImageEntity?> =
        MutableLiveData()

    /** Initialise the connection between the View Model and the Repository */
    init {
        Log.i(LOG_TAG, "Category View Model Init")
        _selectedCategoryId.value = 0
        _isShaken.value = false
        _currentImageFileName.value = "img_default"
        _correctCount.value = 0
        _totalImages.value = 0

//        val categoryDao = CategoryDatabase.getInstance(app).categoryDao()
        categoryRepository = CategoryRepository(app)
    }

    /** Set new Category Id */
    fun setCategoryId(newCategoryId: Int) {
        _selectedCategoryId.value = newCategoryId
    }

    /** Set new Category Id */
    fun toggleShaken() {
        _isShaken.value = !_isShaken.value!!
        Log.i(LOG_TAG, "Shake Change: ${_isShaken.value.toString()}")
    }

    /** Retrieve the category by a specified category Id */
    fun retrieveCategoryById(categoryId: Int) {
        viewModelScope.launch {
            val category = categoryRepository.getCategoryById(categoryId)
            _currentCategory.value = category!!
        }
    }

    /** Retrieve the category by the selected category Id */
    fun retrieveSelectedCategory() {
        viewModelScope.launch {
            val category =
                selectedCategoryId.value?.let { categoryRepository.getSelectedCategory(it) }
            _currentCategory.value = category!!
        }
    }

    /** Create Identify Images List with correct and incorrect images */
    fun getIdentifyImagesList(
        correctLimit: Int,
        incorrectLimit: Int
    ) {
        Log.i(LOG_TAG, "Getting Identify Images List")
        runBlocking {
            val correctImages =
                _selectedCategoryId.value?.let {
                    categoryRepository.getCorrectIdentifyImages(
                        it,
                        correctLimit
                    )
                }
            Log.i(LOG_TAG, "Correct Images: $correctImages")
            val incorrectImages =
                _selectedCategoryId.value?.let {
                    categoryRepository.getIncorrectIdentifyImages(
                        it,
                        incorrectLimit
                    )
                }
            Log.i(LOG_TAG, "Incorrect Images: $incorrectImages")

            // Combined incorrect and correct images
            val combinedList = mutableListOf<ImageEntity>()
            if (correctImages != null) {
                combinedList.addAll(correctImages)
            }
            if (incorrectImages != null) {
                combinedList.addAll(incorrectImages)
            }
            Log.i(LOG_TAG, "Combined Images: $combinedList")

            _identifyImagesList.value = combinedList
            Log.i(LOG_TAG, "IdentifyImagesList Images: ${_identifyImagesList.value}")

            updateTotalImages()
        }
    }

    private fun updateTotalImages() {
        _totalImages.value = _identifyImagesList.value?.size
        Log.i(LOG_TAG, "Total Size of Identify Images: ${_totalImages.value}")
    }

    /** Go to next Identify Image from Identify Image List */
    fun setImageFileNameToIndex(index: Int) {
        _currentImageFileName.value = identifyImagesList.value?.get(index)?.filename
    }

    /** Check correct images by comparing selected category id to image category id */
    fun checkImageCorrect(index: Int, leftOrRight: String) {
        if (leftOrRight == "right") {
            if (_selectedCategoryId.value == identifyImagesList.value?.get(index)?.categoryId) {
                _correctCount.value = _correctCount.value!! + 1
            }
        }
        else if(leftOrRight == "left") {
            if (_selectedCategoryId.value != identifyImagesList.value?.get(index)?.categoryId) {
                _correctCount.value = _correctCount.value!! + 1
            }
        }
        else {
            Log.i(LOG_TAG, "Error in checking image correct")
        }
        Log.i(LOG_TAG, "Correct Answers: ${_correctCount.value} / ${_totalImages.value}")
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

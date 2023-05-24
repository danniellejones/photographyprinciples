/**
 * Category View Model links the Repository and the View (Activity/Fragment).
 * This is a shared view model between the category activity and all fragments.
 * The purpose is to retrieve and perform operations on the data ready for display.
 */

package cp3406.a2.lenslearn.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import cp3406.a2.lenslearn.data.*
import cp3406.a2.lenslearn.repository.CategoryRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val LOG_TAG = "CategoryViewModel"

class CategoryViewModel(app: Application) : AndroidViewModel(app) {

    private val categoryRepository: CategoryRepository

    /** Set Up the Live Data */

    // CATEGORY - Select Category from on Click Event
    private val _selectedCategoryId: MutableLiveData<Int> = MutableLiveData()  // Private
    val selectedCategoryId: LiveData<Int> = _selectedCategoryId  // Public

    // LEARN - Retrieve selected category information and track if shaken
    private val _currentCategory = MutableLiveData<CategoryEntity>()
    val currentCategory: MutableLiveData<CategoryEntity> = _currentCategory

    private val _isShaken: MutableLiveData<Boolean> = MutableLiveData()
    val isShaken: LiveData<Boolean> = _isShaken

    // IDENTIFY - Retrieve both correct and incorrect images, track correct answers over total
    private val _identifyImagesList: MutableLiveData<List<ImageEntity>> = MutableLiveData()
    val identifyImagesList: LiveData<List<ImageEntity>> = _identifyImagesList

    private val _currentImageFileName: MutableLiveData<String> = MutableLiveData()
    val currentImageFileName: LiveData<String> = _currentImageFileName

    private val _correctCount: MutableLiveData<Int> = MutableLiveData()
    val correctCount: LiveData<Int> = _correctCount

    private val _totalImages: MutableLiveData<Int> = MutableLiveData()
    val totalImages: LiveData<Int> = _totalImages

    // DO - Retrieve random task matching the selected category
    private val _randomTask: MutableLiveData<TaskEntity> = MutableLiveData()
    val randomTask: LiveData<TaskEntity> = _randomTask

    // SHARE - Last three images taken by the user
    private val _lastUserImageForLastTask: MutableLiveData<UserImageEntity> = MutableLiveData()
    val lastUserImageForLastTask: LiveData<UserImageEntity> = _lastUserImageForLastTask

    private val _secondLastUserImageForLastTask: MutableLiveData<UserImageEntity> =
        MutableLiveData()
    val secondLastUserImageForLastTask: LiveData<UserImageEntity> = _secondLastUserImageForLastTask

    private val _thirdLastUserImageForLastTask: MutableLiveData<UserImageEntity> =
        MutableLiveData()
    val thirdLastUserImageForLastTask: LiveData<UserImageEntity> = _thirdLastUserImageForLastTask

    private val _imagePathToShare: MutableLiveData<String> = MutableLiveData()
    var imagePathToShare: LiveData<String> = _imagePathToShare

    // STATS - Progress, has Shared and has Completed Task
    private val _progressEntities: MutableLiveData<List<UserProgress>> = MutableLiveData()
    var progressEntities: LiveData<List<UserProgress>> = _progressEntities


    /** Initialise the connection between the View Model and the Repository */
    init {
        categoryRepository = CategoryRepository(app)

        // Initialise start values
        _selectedCategoryId.value = 0
        _isShaken.value = false
        _currentImageFileName.value = "img_default"
        _correctCount.value = 0
        _totalImages.value = 0
    }

    /** Set new Category Id */
    fun setCategoryId(newCategoryId: Int) {
        _selectedCategoryId.value = newCategoryId
    }

    /** Set image path */
    fun setImagePath(selectedPath: String) {
        _imagePathToShare.value = selectedPath
    }

    /** Retrieve a random task */
    fun retrieveRandomTask() {
        viewModelScope.launch {
            val task = categoryRepository.getRandomTask(_selectedCategoryId.value!!)
            _randomTask.value = task!!
        }
    }

    /** Take path from fragment and insert into database*/
    fun addNewUserImage(path: String) {
        viewModelScope.launch {
            val newUserImageEntity = UserImageEntity(taskId = randomTask.value!!.id, path = path)
            categoryRepository.insertUserImage(newUserImageEntity)
        }
    }

    /** Set new Category Id */
    fun toggleShaken() {
        _isShaken.value = !_isShaken.value!!
        Log.i(LOG_TAG, "Shake Change: ${_isShaken.value.toString()}")
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
        runBlocking {
            val correctImages =
                _selectedCategoryId.value?.let {
                    categoryRepository.getCorrectIdentifyImages(
                        it,
                        correctLimit
                    )
                }
            val incorrectImages =
                _selectedCategoryId.value?.let {
                    categoryRepository.getIncorrectIdentifyImages(
                        it,
                        incorrectLimit
                    )
                }

            // Combined incorrect and correct images
            val combinedList = mutableListOf<ImageEntity>()
            if (correctImages != null) {
                combinedList.addAll(correctImages)
            }
            if (incorrectImages != null) {
                combinedList.addAll(incorrectImages)
            }

            _identifyImagesList.value = combinedList
            Log.i(LOG_TAG, "IdentifyImagesList Images: ${_identifyImagesList.value}")

            updateTotalImages()
        }
    }

    /** Update total size of Identify Image List */
    private fun updateTotalImages() {
        _totalImages.value = _identifyImagesList.value?.size
    }

    /** Go to next Identify Image from Identify Image List */
    fun setImageFileNameToIndex(index: Int) {
        _currentImageFileName.value = identifyImagesList.value?.get(index)?.filename
    }

    /** Check correct images by comparing selected category id to image category id */
    fun checkImageCorrect(index: Int, leftOrRight: String): Boolean {
        var isCorrect = false
        if (leftOrRight == "right") {
            if (_selectedCategoryId.value == identifyImagesList.value?.get(index)?.categoryId) {
                _correctCount.value = _correctCount.value!! + 1
                isCorrect = true
            }
        } else if (leftOrRight == "left") {
            if (_selectedCategoryId.value != identifyImagesList.value?.get(index)?.categoryId) {
                _correctCount.value = _correctCount.value!! + 1
                isCorrect = true
            }
        } else {
            Log.i(LOG_TAG, "Error: Check Correct Image")
        }

        Log.i(LOG_TAG, "Correct Answers: ${_correctCount.value} / ${_totalImages.value}")
        return isCorrect
    }


    /** Get last image taken by user for share fragment */
    fun getLastUserImageForLastTask(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            // Retrieve and check
            val userImageEntity = categoryRepository.getLastUserImageForLastTask()
            val hasImage = userImageEntity != null
            Log.d(LOG_TAG, "Last User Image Entity: $userImageEntity")

            // Assign value
            if (hasImage) {
                _lastUserImageForLastTask.value = userImageEntity!!
            } else {
                Log.d(LOG_TAG, "Error: No User Image")
            }
            callback.invoke(hasImage)
        }
    }

    /** Get second last image taken by user for share fragment */
    fun getSecondLastUserImageForLastTask(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            // Retrieve and check
            val userImageEntity = categoryRepository.getSecondLastUserImageForLastTask()
            val hasImage = userImageEntity != null
            Log.d(LOG_TAG, "Second Last User Image Entity: $userImageEntity")

            // Assign value
            if (hasImage) {
                _secondLastUserImageForLastTask.value = userImageEntity!!
            } else {
                Log.d(LOG_TAG, "Error: No User Image")
            }
            callback.invoke(hasImage)
        }
    }

    /** Get third last image taken by user for share fragment */
    fun getThirdLastUserImageForLastTask(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            // Retrieve and check
            val userImageEntity = categoryRepository.getThirdLastUserImageForLastTask()
            val hasImage = userImageEntity != null
            Log.d(LOG_TAG, "Third Last User Image Entity: $userImageEntity")

            // Assign value
            if (hasImage) {
                _thirdLastUserImageForLastTask.value = userImageEntity!!
            } else {
                Log.d(LOG_TAG, "Error: No User Image")
            }
            callback.invoke(hasImage)
        }
    }

    /** Get all user progress for stats */
    fun getAllUserProgress() {
        viewModelScope.launch {
            val allUserProgress = categoryRepository.getAllUserProgress()
            _progressEntities.value = allUserProgress
        }
    }

    /** Update the user progress percentage */
    fun updateProgressPercentage() {
        viewModelScope.launch {
            // Calculate progress
            val correctCount = _correctCount.value ?: 0
            val totalImages = _totalImages.value ?: 0
            val userProgress =
                categoryRepository.getUserProgressByCategoryId(selectedCategoryId.value!!)
            val progressNumber = ((correctCount.toFloat() / totalImages) * 100).toInt()

            // Assign value by update or insert
            if (userProgress != null) {
                userProgress.progressPercentage = progressNumber
                categoryRepository.updateUserProgress(userProgress)
            } else {
                val newProgress = UserProgress(
                    categoryId = selectedCategoryId.value!!,
                    hasShared = false,
                    hasCompletedTask = false,
                    progressPercentage = progressNumber
                )
                categoryRepository.insertUserProgress(newProgress)
            }
        }

        _correctCount.value = 0  // Reset correct count
    }

    /** Update the user progress has shared */
    fun updateHasShared(hasShared: Boolean) {
        viewModelScope.launch {
            val userProgress =
                categoryRepository.getUserProgressByCategoryId(selectedCategoryId.value!!)
            if (userProgress != null) {
                userProgress.hasShared = hasShared
                categoryRepository.updateUserProgress(userProgress)
            } else {
                val newProgress = UserProgress(
                    categoryId = selectedCategoryId.value!!,
                    hasShared = hasShared,
                    hasCompletedTask = false,
                    progressPercentage = 0
                )
                categoryRepository.insertUserProgress(newProgress)
            }
        }
    }

    /** Update the user progress has completed task */
    fun updateHasCompletedTask(hasCompletedTask: Boolean) {
        viewModelScope.launch {
            val userProgress =
                categoryRepository.getUserProgressByCategoryId(selectedCategoryId.value!!)
            if (userProgress != null) {
                userProgress.hasCompletedTask = hasCompletedTask
                categoryRepository.updateUserProgress(userProgress)
            } else {
                val newProgress = UserProgress(
                    categoryId = selectedCategoryId.value!!,
                    hasShared = false,
                    hasCompletedTask = hasCompletedTask,
                    progressPercentage = 0
                )
                categoryRepository.insertUserProgress(newProgress)
            }
        }
    }

    /** Method to get specific category for stats data binding */
    fun getUserProgressByCategoryId(categoryId: Int): LiveData<UserProgress?> {
        return Transformations.map(progressEntities) { userProgressList ->
            userProgressList.find { it.categoryId == categoryId }
        }
    }
}

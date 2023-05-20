package cp3406.a2.lenslearn.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.data.CategoryEntity
import cp3406.a2.lenslearn.repository.CategoryRepository


private const val LOG_TAG = "CategoryViewModel"

//class CategoryViewModel(app: Application) : AndroidViewModel(app) {
class CategoryViewModel : ViewModel() {
    private val _categoryId : MutableLiveData<Int> = MutableLiveData()  // Private for data
    val categoryId: LiveData<Int> = _categoryId  // Public accessible variable
//    var categoryRepository: CategoryRepository = CategoryRepository()

    // Get list of category information
//    val categoryInformation: MutableLiveData<List<CategoryEntity>> = MutableLiveData()

    /** Check size of study timer list and creates any missing timers */
    init {
        Log.i(LOG_TAG, "Category View Model Init")
        _categoryId.value = 0

//        // Data from raw
//        val data = categoryRepository.getTextFromResource(app, R.raw.data)
//        Log.i(LOG_TAG, "Data from json file: $data")

        // Data from assets
//        val data = categoryRepository.getTextFromAsset(app, "data.json")
//        Log.i(LOG_TAG, "Data from json file: $data")

        // Data from assets parsed with Moshi
//        val data = categoryRepository.getCategoryInformation(app, "data.json")
//        data?.forEach {
//            Log.i(LOG_TAG, "Category Entity: ${it.name}")

        // Data from category repository
//        val data = categoryRepository.getCategoryInformation(app,"data.json")
//        data?.let {
//            categoryInformation.value = it  // Update mutable live data instance
//        }
//        }
    }

    /** Load Category */
    fun loadData() {
        _categoryId.value = _categoryId.value!!  // !! to indicate never null
        Log.i(LOG_TAG, "Load Data")
        Log.i(LOG_TAG, "Load Data ${categoryId.value}")
    }

    /** Set new Category Id */
    fun setCategoryId(newCategoryId : Int) {
        _categoryId.value = newCategoryId
    }
}

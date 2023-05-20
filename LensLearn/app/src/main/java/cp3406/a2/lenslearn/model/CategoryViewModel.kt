package cp3406.a2.lenslearn.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


const val LOG_TAG = "CategoryViewModel"

class CategoryViewModel : ViewModel() {
    private val _categoryId : MutableLiveData<Int> = MutableLiveData()  // Private for data
    val categoryId: LiveData<Int> = _categoryId  // Public accessible variable


    /** Check size of study timer list and creates any missing timers */
    init {
        Log.i(LOG_TAG, "Category View Model Init")
        _categoryId.value = 0
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

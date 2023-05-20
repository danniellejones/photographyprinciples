package cp3406.a2.lenslearn.repository

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cp3406.a2.lenslearn.data.CategoryEntity

class CategoryRepository {

//    // Read .json file from raw resource folder
//    fun getTextFromResource(context: Context, resourceId: Int): String {
//        return context.resources.openRawResource(resourceId)
//            .bufferedReader()
//            .use{ it.readText() }  // Closes file after operation
//    }
//
//    // Read .json file from assets resource folder
//    fun getTextFromAsset(context: Context, fileName: String): String {
//        return context.resources.assets.open(fileName)
//            .bufferedReader()
//            .use{ it.readText() }  // Closes file after operation
//    }
//
//    fun getCategoryInformation(context: Context, fileName: String): List<CategoryEntity>? {
//        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//        val listType = Types.newParameterizedType(
//            List::class.java, CategoryEntity::class.java
//        )
//        val adapter: JsonAdapter<List<CategoryEntity>> = moshi.adapter(listType)
//        return adapter.fromJson(getTextFromAsset(context, fileName))
//    }
}
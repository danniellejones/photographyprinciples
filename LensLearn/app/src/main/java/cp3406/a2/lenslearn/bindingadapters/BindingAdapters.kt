package cp3406.a2.lenslearn.bindingadapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import cp3406.a2.lenslearn.R

//@BindingAdapter("imageFromFilename")
//fun ImageView.setImageFromFilename(filename: String?) {
//    filename?.let {
//        val drawableResId = when (filename) {
//            "filename1" -> R.drawable.drawable1
//            "filename2" -> R.drawable.drawable2
//            // Add more cases for other filenames and their respective drawable resource IDs
//            else -> R.drawable.default_image // Default drawable resource ID if filename doesn't match any cases
//        }
//        setImageResource(drawableResId)
//    }
//}

@SuppressLint("DiscouragedApi")
@BindingAdapter("imageFromFilename")
fun ImageView.setImageFromFilename(filename: String?) {
    filename?.let {
        val context = this.context
        val drawableResId = try {
            val packageName = context.packageName
            val resName = "drawable/$filename"
            context.resources.getIdentifier(resName, null, packageName)
        } catch (e: Exception) {
            e.printStackTrace()
            R.drawable.img_default // Default drawable resource ID if filename is invalid or not found
        }
        setImageResource(drawableResId)
    }
}


//@BindingAdapter("app:hideIfNull")
//fun hideIfNull(view: View, value: Int) {
//
//    // app:hideIfNull="@{categoryViewModel.value}"
//    view.visibility = if (value == 0) View.GONE else View.VISIBLE
//}
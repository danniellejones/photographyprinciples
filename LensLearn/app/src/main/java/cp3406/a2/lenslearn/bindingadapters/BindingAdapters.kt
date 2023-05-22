package cp3406.a2.lenslearn.bindingadapters

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import cp3406.a2.lenslearn.R

@SuppressLint("DiscouragedApi")
@BindingAdapter("imageFromFilename")
fun ImageView.setImageFromFilename(filename: String?) {
    try {
        val resources = context.resources
        val packageName = context.packageName

        val drawableResId = filename?.let {
            resources.getIdentifier(filename, "drawable", packageName)
        } ?: 0  // Null or empty filename

        if (drawableResId != 0) {
            setImageResource(drawableResId)
        } else {
            setImageResource(R.drawable.img_default)  // File not found, set default image
        }

    } catch (e: Exception) {
        e.printStackTrace()
        setImageResource(R.drawable.img_default)  // Exception set default image
    }
}

//@BindingAdapter("app:hideIfNull")
//fun hideIfNull(view: View, value: Int) {
//
//    // app:hideIfNull="@{categoryViewModel.value}"
//    view.visibility = if (value == 0) View.GONE else View.VISIBLE
//}
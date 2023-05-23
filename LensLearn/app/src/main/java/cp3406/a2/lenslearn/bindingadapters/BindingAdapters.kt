package cp3406.a2.lenslearn.bindingadapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import cp3406.a2.lenslearn.R

/** Read filename from Room Data and set image to image view */
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

/** Use Glide image library to set photographs from storage device */
@BindingAdapter("imagePath")
fun ImageView.setImagePath(imagePath: String?) {
    if (!imagePath.isNullOrEmpty()) {
        Glide.with(context)
            .load(Uri.parse(imagePath))
            .placeholder(R.drawable.img_default)
            .error(R.drawable.img_broken)
            .into(this)
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}
package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import cp3406.a2.lenslearn.data.UserImageEntity
import cp3406.a2.lenslearn.databinding.FragmentDoBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import java.io.File

class DoFragment : Fragment() {

    private lateinit var binding: FragmentDoBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate with data binding
        binding = FragmentDoBinding.inflate(inflater, container, false)

        return binding.root
    }

}

// Create a variable to store the captured image file path
//private var capturedImagePath: String? = null
// Access CameraX file paths by implementing a ImageCapture.OnImageSavedCallback
//private val imageSavedCallback = object : ImageCapture.OnImageSavedCallback {
//    override fun onImageSaved(file: File) {
//        // Store the file path of the captured image
//        capturedImagePath = file.absolutePath
//
//        // After the image is saved, insert it into the user_image table
//        insertUserImage()
//    }
//override fun onError(imageCaptureError: ImageCapture.ImageCaptureError, message: String, cause: Throwable?) {
//    // Handle error cases
//}
//}
//// Create a function to insert the user image into the database
//private fun insertUserImage() {
//    // Retrieve the auto-generated taskId after inserting the task
//    val taskId = categoryRepository.insertTask(categoryId, description, capturedImagePath)
//
//    // Create a UserImageEntity instance with the retrieved taskId and capturedImagePath
//    val userImageEntity = UserImageEntity(taskId = taskId.toInt(), path = capturedImagePath ?: "")
//
//    // Insert the user image into the user_image table
//    categoryRepository.insertUserImage(userImageEntity)
//}

/// UserImageEntity instance
//val userImageEntity = UserImageEntity(taskId = taskId, path = photoPath)
package cp3406.a2.lenslearn.view

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.data.UserImageEntity
import cp3406.a2.lenslearn.databinding.FragmentDoBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import java.io.File

private const val MAX_PHOTOS = 3

class DoFragment : Fragment() {

    // Data binding and view model
    private lateinit var binding: FragmentDoBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    // Camerax
    private val imageCapture: ImageCapture? = null
//    private val imageCapture = ImageCapture.Builder().build()
    private var capturedImagePath: String? = null
    private var photoCount = 0
    private lateinit var cameraController: LifecycleCameraController


    /** Initialise data binding, connect view model, retrieve task and set up button actions */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate with data binding, set lifecycle owner and attach shared view model
        binding = FragmentDoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.categoryViewModel = categoryViewModel

        // Get a random task from the selected category
        categoryViewModel.retrieveRandomTask()
        Log.i("DoFragment", "Random task: ${categoryViewModel.randomTask.value}")

        // Show and hide the task description
        binding.toggleDescriptionButton.setOnClickListener {
            binding.taskDescription.visibility =
                if (binding.taskDescription.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        // Check if permissions were granted in previous session
        if(!hasPermissions(baseContext)) {
            activityResultLauncher.launch(REQUIRED_PERMISSIONS)
        } else {
            startCamera()
        }

        // Take the photograph and save path to Room database
        binding.takePhotoButton.setOnClickListener {
            Log.i("DoFragment", "Take Photo")

            val photoFile = File(externalMediaDirs.firstOrNull(), "photo.jpg")

            imageCapture.takePicture(photoFile, object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(file: File) {
                    capturedImagePath = file.absolutePath
                    categoryViewModel.addNewUserImage(capturedImagePath)
                    photoCount++
                    if (photoCount == MAX_PHOTOS) {
                        findNavController().navigate(R.id.action_doFragment_to_shareFragment)
                    }
                }

                override fun onError(imageCaptureError: ImageCapture.ImageCaptureError, message: String, cause: Throwable?) {
                    // Photo capture failed, handle the error
                }
            })
        }
        return binding.root
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if(it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if(!permissionGranted) {
                Toast.makeText(requireContext(), "Permission Request Denied", Toast.LENGTH_LONG).show()
            } else {
                startCamera()
            }
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(android.Manifest.permission.CAMERA).apply {
            }.toTypedArray()
        fun hasPermissions(context: Context) = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}

package cp3406.a2.lenslearn.view

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.FragmentDoBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val MAX_PHOTOS = 3

class DoFragment : Fragment() {

    // Data binding and view model
    private lateinit var binding: FragmentDoBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    // CameraX
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
//        binding.overlayImage.rotation = 90f

        // Show and hide the task description
        binding.toggleDescriptionButton.setOnClickListener {
            binding.taskDescription.visibility =
                if (binding.taskDescription.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        }
        // Hold down my task button to skip task
        binding.toggleDescriptionButton.setOnLongClickListener {
            findNavController().navigate(R.id.action_doFragment_to_shareFragment)
            true
        }

        // Check if permissions were granted in previous session
        if (!hasPermissions(requireContext())) {
            activityResultLauncher.launch(REQUIRED_PERMISSIONS)
        } else {
            startCamera()
        }

        // Take the photograph and save path to Room database
        binding.takePhotoButton.setOnClickListener {
            takePhoto()

        }
        return binding.root
    }

    /** Handle taking a photo and save images */
    private fun takePhoto() {
        Log.i("DoFragment", "Take Photo")
        val name =
            SimpleDateFormat(FILENAME_FORMAT, Locale.ENGLISH).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }

        // Create output options object which contains file and metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            requireContext().contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        cameraController.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                    // Save filepath and show toast
                    capturedImagePath = output.savedUri?.toString()  // Absolute Path
                    Log.d(TAG, "Photo capture successful: ${output.savedUri?.path}")
                    if (photoCount < 2) {
                        Toast.makeText(
                            requireContext(),
                            R.string.capture_success_message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    // Add path to room database
                    capturedImagePath?.let { it1 -> categoryViewModel.addNewUserImage(it1) }
                    Log.i("DoFragment", "Added Capture Path: $capturedImagePath")

                    // After three photographs are taken, move to share fragment
                    photoCount++
                    if (photoCount == MAX_PHOTOS) {
                        Toast.makeText(
                            requireContext(),
                            R.string.capture_success_final_message,
                            Toast.LENGTH_LONG
                        ).show()
                        photoCount = 0
                        categoryViewModel.updateHasCompletedTask(true)
                        findNavController().navigate(R.id.action_doFragment_to_shareFragment)
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }
            }
        )
    }

    /** Start Camera connected to preview view */
    private fun startCamera() {
        val previewView: PreviewView = binding.previewView
        cameraController = LifecycleCameraController((requireContext()))
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        previewView.controller = cameraController
        Log.i(TAG, "Camera Started")
    }

    /** Launch camera activity if permissions are granted */
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(requireContext(), "Permission Request Denied", Toast.LENGTH_LONG)
                    .show()
            } else {
                startCamera()
            }
        }

    /** Set companion for permissions and format */
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

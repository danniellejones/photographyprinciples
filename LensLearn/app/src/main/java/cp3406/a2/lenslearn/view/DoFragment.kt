package cp3406.a2.lenslearn.view

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.media.Image
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.data.UserImageEntity
import cp3406.a2.lenslearn.databinding.FragmentDoBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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

        // Check if permissions were granted in previous session
        if (!hasPermissions(requireContext())) {
            activityResultLauncher.launch(REQUIRED_PERMISSIONS)
        } else {
            startCamera()
        }

//        // Camera provider
//        if (!hasPermissions(requireContext())) {
//            activityResultLauncher.launch(REQUIRED_PERMISSIONS)
//        } else {
//            lifecycleScope.launch {
//                startCamera()
//            }
//        }

        // Take the photograph and save path to Room database
        binding.takePhotoButton.setOnClickListener {
            Log.i("DoFragment", "Take Photo")
            takePhoto()
            photoCount++
            capturedImagePath?.let { it1 -> categoryViewModel.addNewUserImage(it1) }
            if (photoCount == MAX_PHOTOS) {
                findNavController().navigate(R.id.action_doFragment_to_shareFragment)
            }
        }
        return binding.root
    }

    private fun takePhoto() {
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
                    val msg = "Photo capture successful: ${output.savedUri}"
                    capturedImagePath = "${output.savedUri}"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                    Log.d(TAG, msg)
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }
            }
        )
    }

//    // Camera Provider
//    private suspend fun starCamera() {
//        val cameraProvider = ProcessCameraProvider.getInstance(requireContext()).await()
//
//        val preview = Preview.Builder().build()
//        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
//        imageCapture = imageCapture.Builder().build()
//        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
//
//        try {
//            cameraProvider.unbindAll()
//            var camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
//        }
//        catch (e: java.lang.Exception) {
//            Log.e(TAG, "UseCase binding failed", e)
//        }
//    }

    // Camera Controller
    private fun startCamera() {
        val previewView: PreviewView = binding.previewView
        cameraController = LifecycleCameraController((requireContext()))
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        previewView.controller = cameraController
    }

//    // Camera Provider
//    private val activityResultLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            var permissionGranted = true
//            permissions.entries.forEach {
//                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
//                    permissionGranted = false
//            }
//            if (!permissionGranted) {
//                Toast.makeText(requireContext(), "Permission Request Denied", Toast.LENGTH_LONG)
//                    .show()
//            } else {
//                lifecycleScope.launch{
//                    startCamera()
//                }
//            }
//        }

    // Camera Controller
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(requireContext(), "Permission Request Denied", Toast.LENGTH_LONG)
                    .show()
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

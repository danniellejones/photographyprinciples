package cp3406.a2.lenslearn.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.FragmentIdentifyBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import cp3406.a2.lenslearn.sensors.Accelerometer
import cp3406.a2.lenslearn.sensors.SwipeGestureDetector
import cp3406.a2.lenslearn.sensors.GestureEventListener

private const val TAG = "IdentifyFragment"

class IdentifyFragment : Fragment(), GestureEventListener {

    private lateinit var binding: FragmentIdentifyBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    // Gesture Detector
    private var currentIndex: Int = 0
    private lateinit var gestureDetector: SwipeGestureDetector
//    val swipeGestureDetector = SwipeGestureDetector(requireContext(), this)
    private val requestCode = 1
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate with data binding, set lifecycle owner and attach shared view model
        binding = FragmentIdentifyBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.categoryViewModel = categoryViewModel

        categoryViewModel.selectedCategoryId.observe(viewLifecycleOwner) { categoryId ->
            categoryId?.let {
                Log.d(TAG, "Inside observer of selected cat id")
                categoryViewModel.getIdentifyImagesList(2, 4)
                Log.d(TAG, "Images: ${categoryViewModel.identifyImagesList.value}")
            }
        }


        categoryViewModel.identifyImagesList.observe(viewLifecycleOwner) {
            currentIndex = 0  // When list changes, reset index to first image
            categoryViewModel.setImageFileNameToIndex(currentIndex)
            Log.d(TAG, "Current Filename: ${categoryViewModel.currentImageFileName.value}")
        }

        requestPermissions()
        gestureDetector = SwipeGestureDetector(requireContext(), this)

        binding.identifyImage.setOnTouchListener(gestureDetector)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onSwipeRight() {
        // Compare category id to selected category id if selectedCategoryId == currentCategoryId = correct
        showToast(requireContext(), "Swiped Right!")
        categoryViewModel.checkImageCorrect(currentIndex, "right")
//        currentIndex++
//        categoryViewModel.setImageFileNameToIndex(currentIndex)
    }

    override fun onSwipeLeft() {
        // Compare category id to selected category id if selectedCategoryId != currentCategoryId = correct
        showToast(requireContext(), "Swiped Left!")
        categoryViewModel.checkImageCorrect(currentIndex, "left")
//        currentIndex++
//        categoryViewModel.setImageFileNameToIndex(currentIndex)
        // If no more images navigate to the next view
//        if (categoryViewModel.totalImages.value?.minus(1) == currentIndex) {
//            findNavController().navigate(R.id.action_identifyFragment_to_doFragment)
//        }
    }

    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    /** Request accelerometer permissions and initialise start */
    private fun requestPermissions() {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toTypedArray(),
                requestCode
            )
        }
    }
}




package cp3406.a2.lenslearn.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.data.CategoryEntity
import cp3406.a2.lenslearn.databinding.FragmentLearningBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import cp3406.a2.lenslearn.sensors.Accelerometer
import kotlinx.coroutines.launch

class LearningFragment : Fragment() {

    // Data binding and view model
    private lateinit var binding: FragmentLearningBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    // Accelerometer Sensor
    private lateinit var accelerometer: Accelerometer
    private val requestCode = 1
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    /** Create View, Initialise data binding, connect view model, retrieve selected category */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LearningFragment", "Learning Fragment Start of OnCreateView")

        // Inflate with data binding, set lifecycle owner and attach shared view model
        binding = FragmentLearningBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.categoryViewModel = categoryViewModel

        // Observe the selected category Id and retrieve the corresponding category information
        categoryViewModel.selectedCategoryId.observe(viewLifecycleOwner) { categoryId ->
            categoryId?.let {
                categoryViewModel.retrieveSelectedCategory()
                Log.d("LearningFragment", "Inside observer of selected category Id")
            }
        }

        // Set Click Listener for Floating Action Button to move to Identify Fragment
        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.action_learningFragment_to_identifyFragment)
        }

        return binding.root
    }

    /** Set up accelerometer, start listening for changes */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check permission, set up and start accelerometer
        requestPermissions()

        // Observe device shake change and display Log when shake detected
        categoryViewModel.isShaken.observe(viewLifecycleOwner) { shakeDetected ->
            if (shakeDetected) {
                Log.d("LearningFragment", "Is Shaken: $shakeDetected")
            }
        }
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
        } else {
            startAccelerometer()
        }
    }

    /** Start accelerometer listening */
    private fun startAccelerometer() {
        accelerometer = Accelerometer(requireContext(), categoryViewModel)
        accelerometer.startListening()
    }

    /** Stop accelerometer from listening */
    override fun onDestroyView() {
        super.onDestroyView()

        if (::accelerometer.isInitialized) {
            accelerometer.stopListening()
        }
    }
}
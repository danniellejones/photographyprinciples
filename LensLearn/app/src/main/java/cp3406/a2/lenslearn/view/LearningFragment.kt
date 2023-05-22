package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import cp3406.a2.lenslearn.data.CategoryEntity
import cp3406.a2.lenslearn.databinding.FragmentLearningBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import cp3406.a2.lenslearn.sensors.Accelerometer
import kotlinx.coroutines.launch

class LearningFragment : Fragment() {

    private lateinit var binding: FragmentLearningBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    private lateinit var accelerometer: Accelerometer

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

        categoryViewModel.isShaken.observe(viewLifecycleOwner) { shakeDetected ->
            if (shakeDetected) {
                // Update UI
            }
        }

//        categoryViewModel.isShaken.observe(viewLifecycleOwner) {
//
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accelerometer = Accelerometer(requireContext(), categoryViewModel)

    }
}
package cp3406.a2.lenslearn.view

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cp3406.a2.lenslearn.databinding.FragmentIdentifyBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import cp3406.a2.lenslearn.sensors.GestureDetector
import cp3406.a2.lenslearn.sensors.GestureEventListener

class IdentifyFragment : Fragment(), GestureEventListener {

    private lateinit var binding: FragmentIdentifyBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    // Gesture Detector
    private var currentIndex: Int = 0
    private lateinit var gestureDetector: GestureDetector

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
                Log.d("LearningFragment", "Inside observer of selected cat id")
                categoryViewModel.getIdentifyImagesList(2, 4)
                categoryViewModel.setImageFileNameToIndex(currentIndex)
            }
        }

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gestureDetector = GestureDetector(this)

        binding.identifyImage.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }

        categoryViewModel.currentImageFileName.observe(viewLifecycleOwner) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onSwipeRight() {
        // Compare category id to selected category id if selectedCategoryId == currentCategoryId = correct
        currentIndex++
        categoryViewModel.setImageFileNameToIndex(currentIndex)
    }

    override fun onSwipeLeft() {
        // Compare category id to selected category id if selectedCategoryId != currentCategoryId = correct
        currentIndex++
        categoryViewModel.setImageFileNameToIndex(currentIndex)
    }
}

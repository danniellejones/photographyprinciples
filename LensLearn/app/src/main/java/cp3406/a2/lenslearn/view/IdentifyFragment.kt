package cp3406.a2.lenslearn.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.FragmentIdentifyBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import cp3406.a2.lenslearn.sensors.GestureEventListener
import cp3406.a2.lenslearn.sensors.SwipeGestureDetector

private const val TAG = "IdentifyFragment"
private const val CORRECT_LIMIT = 3
private const val INCORRECT_LIMIT = 3

class IdentifyFragment : Fragment(), GestureEventListener {

    // Data binding and view model
    private lateinit var binding: FragmentIdentifyBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    // Gesture Detector
    private var currentIndex: Int = 0
    private lateinit var gestureDetector: SwipeGestureDetector

    /** Bind data to view, initialise view model and observe live data */
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate with data binding, set lifecycle owner and attach shared view model
        binding = FragmentIdentifyBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.categoryViewModel = categoryViewModel

        // Observe category id changes and generate a new set of images
        categoryViewModel.selectedCategoryId.observe(viewLifecycleOwner) { categoryId ->
            categoryId?.let {
                categoryViewModel.getIdentifyImagesList(CORRECT_LIMIT, INCORRECT_LIMIT)
                Log.d(TAG, "Images: ${categoryViewModel.identifyImagesList.value}")
            }
        }

        // Observe changes to the images list and reset index
        categoryViewModel.identifyImagesList.observe(viewLifecycleOwner) {
            currentIndex = 0  // When list changes, reset index to first image
            categoryViewModel.setImageFileNameToIndex(currentIndex)
            Log.d(TAG, "Current Filename: ${categoryViewModel.currentImageFileName.value}")
        }

        // Set up gesture detection and touch listener
        gestureDetector = SwipeGestureDetector(requireContext(), this)
        binding.identifyImage.setOnTouchListener(gestureDetector)

        return binding.root
    }

    /** Handle swipe right gesture */
    override fun onSwipeRight() {
        // Compare selectedCategoryId == currentCategoryId = correct
        val isCorrect = categoryViewModel.checkImageCorrect(currentIndex, "right")
        if (isCorrect) { showToast(requireContext(), "Correct!")}
        // Show results
        if (categoryViewModel.totalImages.value?.minus(1) == currentIndex) {
            showResultDialog(categoryViewModel.correctCount.value!!, categoryViewModel.totalImages.value!!)
        }
        else {
            currentIndex++
            categoryViewModel.setImageFileNameToIndex(currentIndex)
        }
    }

    /** Handle swipe left gesture */
    override fun onSwipeLeft() {
        // Compare selectedCategoryId != currentCategoryId = correct
        val isCorrect = categoryViewModel.checkImageCorrect(currentIndex, "left")
        if (isCorrect) { showToast(requireContext(), "Correct!")}
        // Show results
        if (categoryViewModel.totalImages.value?.minus(1) == currentIndex) {
            showResultDialog(categoryViewModel.correctCount.value!!, categoryViewModel.totalImages.value!!)
        }
        else {
            currentIndex++
            categoryViewModel.setImageFileNameToIndex(currentIndex)
        }
    }

    /** Display Toast of swipe direction */
    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    /** Display custom dialog box to display results and navigate to next screen */
    private fun showResultDialog(correctCount: Int, totalImages: Int) {
        val message = getString(R.string.results_message).format(correctCount, totalImages)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.results_title))
            .setMessage(message)
            .setPositiveButton(getString(R.string.next)) { dialog, _ ->
                dialog.dismiss()
                categoryViewModel.updateProgressPercentage()
                findNavController().navigate(R.id.action_identifyFragment_to_doFragment)
            }

        // Customize dialog appearance
        val dialog = dialogBuilder.create()
        dialog.apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawableResource(android.R.color.white)
            show()

            // Modify dialog text color, button color, etc.
            val messageTextView = findViewById<TextView>(android.R.id.message)
            messageTextView!!.setTextColor(ContextCompat.getColor(context, R.color.black))
            messageTextView.gravity = Gravity.CENTER
            val positiveButton = getButton(DialogInterface.BUTTON_POSITIVE)
            positiveButton.setTextColor(ContextCompat.getColor(context, R.color.white))
            positiveButton.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_purple))
        }
    }
}




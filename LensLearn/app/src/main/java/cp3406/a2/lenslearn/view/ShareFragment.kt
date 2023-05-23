package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.FragmentShareBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import java.io.File

private const val TAG = "ShareFragment"

class ShareFragment : Fragment() {

    private lateinit var binding: FragmentShareBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate with data binding, set lifecycle owner and attach shared view model
        binding = FragmentShareBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.categoryViewModel = categoryViewModel

        // Retrieve images from the saved filepath and display
        categoryViewModel.getLastUserImageForLastTask { hasImage ->
            Log.i(TAG, "Has image = $hasImage")
            binding.thumbnailImageTop.visibility = if (hasImage) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        categoryViewModel.getSecondLastUserImageForLastTask { hasImage ->
            if (hasImage) {
                Log.i(TAG, "Has image = $hasImage")
                binding.thumbnailImageMiddle.visibility = if (hasImage) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
        categoryViewModel.getThirdLastUserImageForLastTask { hasImage ->
            if (hasImage) {
                Log.i(TAG, "Has image = $hasImage")
                binding.thumbnailImageBottom.visibility = if (hasImage) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        // TODO: Set on click listeners to know which to use for share action
        binding.thumbnailImageTop.setOnClickListener {}
        binding.thumbnailImageMiddle.setOnClickListener {}
        binding.thumbnailImageBottom.setOnClickListener {}

        return binding.root
    }
}
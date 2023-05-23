package cp3406.a2.lenslearn.view

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
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

        // Retrieve images from the saved filepath and set click listeners for share
        categoryViewModel.getLastUserImageForLastTask { hasImage ->
            Log.i(TAG, "Last Has image = $hasImage")
            if (hasImage) {
                binding.thumbnailImageTop.setOnClickListener {
                    categoryViewModel.setImagePath(categoryViewModel.lastUserImageForLastTask.value?.path.toString())
                    Log.i(TAG, "Image Path to Share: ${categoryViewModel.imagePathToShare}")
                }
            }
        }
        categoryViewModel.getSecondLastUserImageForLastTask { hasImage ->
            Log.i(TAG, "Second Last Has image = $hasImage")
            if (hasImage) {
                binding.thumbnailImageMiddle.setOnClickListener {
                    categoryViewModel.setImagePath(categoryViewModel.secondLastUserImageForLastTask.value?.path.toString())
                    Log.i(TAG, "Image Path to Share: ${categoryViewModel.imagePathToShare}")
                }
            }
        }
        categoryViewModel.getThirdLastUserImageForLastTask { hasImage ->
            Log.i(TAG, "Third Last Has image = $hasImage")
            if (hasImage) {
                binding.thumbnailImageBottom.setOnClickListener {
                    categoryViewModel.setImagePath(categoryViewModel.thirdLastUserImageForLastTask.value?.path.toString())
                    Log.i(TAG, "Image Path to Share: ${categoryViewModel.imagePathToShare}")
                }
            }
        }

        return binding.root
    }
}
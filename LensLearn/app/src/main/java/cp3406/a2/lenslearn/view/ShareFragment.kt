package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.FragmentShareBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import java.io.File

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
            if (!hasImage) {
                binding.thumbnailImageBottom.setImageResource(R.drawable.img_default)
            } else {
                Glide.with(requireContext())
                    .load(categoryViewModel.lastUserImageForLastTask.value?.path?.let { File(it) })
                    .placeholder(R.drawable.img_default)
                    .error(R.drawable.img_default)
                    .into(binding.thumbnailImageBottom)
            }
        }
        categoryViewModel.getSecondLastUserImageForLastTask { hasImage ->
            if (!hasImage) {
                binding.thumbnailImageMiddle.setImageResource(R.drawable.img_default)
            } else {
                Glide.with(requireContext())
                    .load(categoryViewModel.secondLastUserImageForLastTask.value?.path?.let { File(it) })
                    .placeholder(R.drawable.img_default)
                    .error(R.drawable.img_default)
                    .into(binding.thumbnailImageMiddle)
            }
        }
        categoryViewModel.getThirdLastUserImageForLastTask { hasImage ->
            if (!hasImage) {
                binding.thumbnailImageTop.setImageResource(R.drawable.img_default)
            } else {
                Glide.with(requireContext())
                    .load(categoryViewModel.thirdLastUserImageForLastTask.value?.path?.let { File(it) })
                    .placeholder(R.drawable.img_default)
                    .error(R.drawable.img_default)
                    .into(binding.thumbnailImageTop)
            }
        }

        // TODO: Set on click listeners to know which to use for share action
        binding.thumbnailImageTop.setOnClickListener {}
        binding.thumbnailImageMiddle.setOnClickListener {}
        binding.thumbnailImageBottom.setOnClickListener {}

        return binding.root
    }
}
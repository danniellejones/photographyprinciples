package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.FragmentShareBinding
import cp3406.a2.lenslearn.model.CategoryViewModel

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

        categoryViewModel.getLastUserImageForLastTask { hasImage ->
            if(!hasImage) {
                binding.thumbnailImageBottom.setImageResource(R.drawable.img_default)
            }
        }
        categoryViewModel.getSecondLastUserImageForLastTask { hasImage ->
            if(!hasImage) {
                binding.thumbnailImageMiddle.setImageResource(R.drawable.img_default)
            }
        }
        categoryViewModel.getThirdLastUserImageForLastTask { hasImage ->
            if(!hasImage) {
                binding.thumbnailImageTop.setImageResource(R.drawable.img_default)
            }
        }

        return binding.root
    }

//    categoryViewModel.getLastUserImageForLastTask().observe(viewLifecycleOwner, Observer { userImageEntity ->
//        userImageEntity?.let { // Use the safe call operator ?. and let function
//            // Update your views with the user image entity
//        } ?: run {
//            // Handle the case when there is no last user image
//        }
//    })
//
//    categoryViewModel.getSecondLastUserImageForLastTask().observe(viewLifecycleOwner, Observer { userImageEntity ->
//        userImageEntity?.let { // Use the safe call operator ?. and let function
//            // Update your views with the user image entity
//        } ?: run {
//            // Handle the case when there is no second last user image
//        }
//    })
//
//    categoryViewModel.getThirdLastUserImageForLastTask().observe(viewLifecycleOwner, Observer { userImageEntity ->
//        userImageEntity?.let { // Use the safe call operator ?. and let function
//            // Update your views with the user image entity
//        } ?: run {
//            // Handle the case when there is no third last user image
//        }
//    })
}
package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cp3406.a2.lenslearn.databinding.FragmentIdentifyBinding
import cp3406.a2.lenslearn.model.CategoryViewModel

class IdentifyFragment : Fragment() {

    private lateinit var binding: FragmentIdentifyBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate with data binding
        binding = FragmentIdentifyBinding.inflate(inflater, container, false)

//        categoryViewModel.selectedCategoryId.observe(viewLifecycleOwner) { categoryId ->
//            categoryId?.let {
//                Log.d("LearningFragment", "Inside observer of selected cat id")
//                retrieveCategoryById(categoryId)
//            }
//        }
//        categoryViewModel.getIdentifyImagesList(selectedCategoryId, correctLimit, incorrectLimit)

        return binding.root
    }


}
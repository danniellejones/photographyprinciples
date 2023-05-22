package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import cp3406.a2.lenslearn.data.CategoryEntity
import cp3406.a2.lenslearn.databinding.FragmentLearningBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import kotlinx.coroutines.launch

class LearningFragment : Fragment() {

    private lateinit var binding: FragmentLearningBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }
    private lateinit var category: CategoryEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate with data binding
        binding = FragmentLearningBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        categoryViewModel.selectedCategoryId.observe(viewLifecycleOwner) { categoryId ->
            categoryId?.let {
                retrieveCategoryById(categoryId)
            }
        }
//        categoryViewModel.isShaken.observe(viewLifecycleOwner) {
//
//        }

        return binding.root
    }

    private fun retrieveCategoryById(categoryId: Int) {
        categoryViewModel.retrieveCategoryById(categoryId)
    }
}
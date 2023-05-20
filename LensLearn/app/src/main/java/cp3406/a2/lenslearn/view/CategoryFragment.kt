package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.FragmentCategoryBinding
import cp3406.a2.lenslearn.model.CategoryViewModel

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate with data binding
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        // Set click listeners for each category
//        binding.cardCategoryBalance.setOnClickListener{
//            categoryViewModel.setCategoryId(1)
//            findNavController().navigate(R.id.action_categoryFragment_to_learningFragment)
//        }
//        binding.cardCategoryContrast.setOnClickListener{
//            categoryViewModel.setCategoryId(2)
//        }
//        binding.cardCategoryEmphasis.setOnClickListener{
//            categoryViewModel.setCategoryId(3)
//        }
//        binding.cardCategoryPattern.setOnClickListener{
//            categoryViewModel.setCategoryId(4)
//        }
//        binding.cardCategoryRhythm.setOnClickListener{
//            categoryViewModel.setCategoryId(5)
//        }
//        binding.cardCategorySpace.setOnClickListener{
//            categoryViewModel.setCategoryId(6)
//        }
//        binding.cardCategoryUnity.setOnClickListener{
//            categoryViewModel.setCategoryId(7)
//        }

        val categoryClickListener: (Int) -> Unit = { categoryId ->
            categoryViewModel.setCategoryId(categoryId)
            findNavController().navigate(R.id.action_categoryFragment_to_learningFragment)
        }

        binding.apply {
            cardCategoryBalance.setOnClickListener { categoryClickListener(1) }
            cardCategoryContrast.setOnClickListener { categoryClickListener(2) }
            cardCategoryEmphasis.setOnClickListener { categoryClickListener(3) }
            cardCategoryPattern.setOnClickListener { categoryClickListener(4) }
            cardCategoryRhythm.setOnClickListener { categoryClickListener(5) }
            cardCategorySpace.setOnClickListener { categoryClickListener(6) }
            cardCategoryUnity.setOnClickListener { categoryClickListener(7) }
        }

       



        categoryViewModel.categoryId.observe(viewLifecycleOwner) {
            displaySnackbar(it)
        }

        return binding.root
    }

    private fun displaySnackbar(catId: Int) {
        Snackbar.make(binding.root, "current value: $catId", Snackbar.LENGTH_LONG).show()
    }
}
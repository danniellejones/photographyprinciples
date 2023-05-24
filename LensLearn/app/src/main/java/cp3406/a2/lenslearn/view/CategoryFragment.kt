/** Displays the categories for selection. */
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate with data binding
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        // Listen for click event and handle selection of category Id
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

        // Observe the Selected Category Id
        categoryViewModel.selectedCategoryId.observe(viewLifecycleOwner) {
            displaySnackBar(it)
        }
        return binding.root
    }

    /** Display the selected category */
    private fun displaySnackBar(categoryId: Int) {
        val categoryName = when (categoryId) {
            1 -> R.string.category_balance
            2 -> R.string.category_contrast
            3 -> R.string.category_emphasis
            4 -> R.string.category_pattern
            5 -> R.string.category_rhythm
            6 -> R.string.category_space
            7 -> R.string.category_unity
            else -> R.string.unknown
        }
        var messagePrefix = ""
        if (R.string.unknown != categoryName) {
            messagePrefix = getString(R.string.selected_category_message)
        }
        val message = messagePrefix + " " + getString(categoryName)
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cp3406.a2.lenslearn.databinding.FragmentLearningBinding
import cp3406.a2.lenslearn.model.CategoryViewModel

class LearningFragment : Fragment() {

    private lateinit var binding: FragmentLearningBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate with data binding
        binding = FragmentLearningBinding.inflate(inflater, container, false)

        // Retrieve category information
//        viewModel.categoryInformation.observe(viewLifecycleOwner, { categoryInformation
//            val categoryNames = StringBuilder()
//            categoryInformation.foreach {
//                categoryNames.appendLine(it.name)
//            }
//            binding.cartContextText.text = productNames.toString()
//        })

        return binding.root
    }
}
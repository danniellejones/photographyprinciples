package cp3406.a2.lenslearn.view

import android.content.pm.ActivityInfo
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.ActivityCategoryBinding
import cp3406.a2.lenslearn.databinding.FragmentLearningBinding
import cp3406.a2.lenslearn.model.CategoryFragmentViewModel
import cp3406.a2.lenslearn.model.CategoryViewModel
import cp3406.a2.lenslearn.model.LearningViewModel

class LearningFragment : Fragment() {

//    companion object {
//        fun newInstance() = LearningFragment()
//    }

    private lateinit var binding: FragmentLearningBinding
    private val viewModel: LearningViewModel by viewModels()
//    private lateinit var viewModel: LearningViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate with data binding
        binding = FragmentLearningBinding.inflate(inflater, container, false)

        // Get reference to view model
//        binding.lifecycleOwner = this
//        binding.learningViewModel = viewModel

        // Retrieve category information
//        viewModel.categoryInformation.observe(viewLifecycleOwner, { categoryInformation
//            val categoryNames = StringBuilder()
//            categoryInformation.foreach {
//                categoryNames.appendLine(it.name)
//            }
//            binding.cartContextText.text = productNames.toString()
//        })

//        val selectedCategory = arguments?.getString("categoryId")
//        binding.textIndividualCategoryTitle.text = categoryId
//        Log.i("LearningFragment", "$categoryId")
//        binding.learningViewModel = viewModel

        return binding.root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this)[LearningViewModel::class.java]
//         TODO: Use the ViewModel
//    }

//    // Destroy binding to prevent memory leaks - not necessary with data binding
//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding.unbind()
//    }

}
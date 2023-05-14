package cp3406.a2.lenslearn.view

import android.content.pm.ActivityInfo
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.ActivityCategoryBinding
import cp3406.a2.lenslearn.databinding.FragmentLearningBinding
import cp3406.a2.lenslearn.model.LearningViewModel

class LearningFragment : Fragment() {

//    companion object {
//        fun newInstance() = LearningFragment()
//    }

    private lateinit var binding: FragmentLearningBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = DataBindingUtil.inflate<FragmentLearningBinding>(inflater, R.layout.fragment_learning, container, false)
        val selectedCategory = arguments?.getString("selectedCategory")
        binding.textIndividualCategoryTitle.text = selectedCategory
        Log.i("LearningFragment", "$selectedCategory")
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
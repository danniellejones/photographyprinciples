/** Display progress to user. */
package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.FragmentStatsBinding
import cp3406.a2.lenslearn.model.CategoryViewModel

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    /** Set up binding, get progress data and start click listeners */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Inflate with data binding, set lifecycle owner and attach shared view model
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.categoryViewModel = categoryViewModel

        categoryViewModel.getAllUserProgress()

        // Navigate back to categories fragment
        binding.returnToCategories.setOnClickListener {
            findNavController().navigate(R.id.action_statsFragment_to_categoryFragment)
        }

        return binding.root
    }
}
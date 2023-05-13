package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.model.IdentifyViewModel

class IdentifyFragment : Fragment() {

    companion object {
        fun newInstance() = IdentifyFragment()
    }

    private lateinit var viewModel: IdentifyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_identify, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(IdentifyViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
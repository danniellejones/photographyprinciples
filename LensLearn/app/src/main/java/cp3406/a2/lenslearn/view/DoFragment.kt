package cp3406.a2.lenslearn.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cp3406.a2.lenslearn.data.UserImageEntity
import cp3406.a2.lenslearn.databinding.FragmentDoBinding
import cp3406.a2.lenslearn.model.CategoryViewModel
import java.io.File

class DoFragment : Fragment() {

    private lateinit var binding: FragmentDoBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate with data binding
        binding = FragmentDoBinding.inflate(inflater, container, false)

        return binding.root
    }

}

//val imageFile: File = // Obtained file of the captured image
//
//// Save the image path in the database
//val userImageEntity = UserImageEntity(taskId = taskId, imagePath = imageFile.absolutePath)
//yourDao.insertUserImage(userImageEntity)
package cp3406.a2.lenslearn.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cp3406.a2.lenslearn.databinding.FragmentShareBinding
import cp3406.a2.lenslearn.model.CategoryViewModel

private const val TAG = "ShareFragment"

class ShareFragment : Fragment() {

    // Data binding and view model
    private lateinit var binding: FragmentShareBinding
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

    // Permissions for share
    private val requestCode = 2
    @RequiresApi(Build.VERSION_CODES.R)
    private val permissions = arrayOf(
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate with data binding, set lifecycle owner and attach shared view model
        binding = FragmentShareBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.categoryViewModel = categoryViewModel

        // Retrieve images from the saved filepath and set click listeners for share
        categoryViewModel.getLastUserImageForLastTask { hasImage ->
            Log.i(TAG, "Last Has image = $hasImage")
            if (hasImage) {
                binding.thumbnailImageTop.setOnClickListener {
                    categoryViewModel.setImagePath(categoryViewModel.lastUserImageForLastTask.value?.path.toString())
                    Log.i(TAG, "Image Path to Share: ${categoryViewModel.imagePathToShare.value}")
                }
            }
        }
        categoryViewModel.getSecondLastUserImageForLastTask { hasImage ->
            Log.i(TAG, "Second Last Has image = $hasImage")
            if (hasImage) {
                binding.thumbnailImageMiddle.setOnClickListener {
                    categoryViewModel.setImagePath(categoryViewModel.secondLastUserImageForLastTask.value?.path.toString())
                    Log.i(TAG, "Image Path to Share: ${categoryViewModel.imagePathToShare.value}")
                }
            }
        }
        categoryViewModel.getThirdLastUserImageForLastTask { hasImage ->
            Log.i(TAG, "Third Last Has image = $hasImage")
            if (hasImage) {
                binding.thumbnailImageBottom.setOnClickListener {
                    categoryViewModel.setImagePath(categoryViewModel.thirdLastUserImageForLastTask.value?.path.toString())
                    Log.i(TAG, "Image Path to Share: ${categoryViewModel.imagePathToShare.value}")
                }
            }
        }

        // Handle share with share button click
        binding.share2Button.setOnClickListener{
            requestPermissions()
            handleShare()
        }

        return binding.root
    }

    /** Request permissions if required */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestPermissions() {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toTypedArray(),
                requestCode
            )
        }
    }

    /** Handle share image on social media menu item */
    private fun handleShare(): Boolean {
        try {
            Log.i(TAG, "Enter Share")
            val pathName = binding.categoryViewModel?.imagePathToShare.toString()
            if (pathName.isNotEmpty()) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "image/jpg"
                    putExtra(Intent.EXTRA_STREAM, Uri.parse(pathName))  // TODO: Fix filetype
                }
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.i(TAG, "No Images Available")
        }
        return true
    }
}
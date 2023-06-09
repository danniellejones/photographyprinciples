/** Select an image from photography task and share on social media. */
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
import androidx.navigation.fragment.findNavController
import cp3406.a2.lenslearn.R
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Inflate with data binding, set lifecycle owner and attach shared view model
        binding = FragmentShareBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.categoryViewModel = categoryViewModel

        // Retrieve images from the saved filepath and set click listeners for share
        categoryViewModel.getLastUserImageForLastTask { hasImage ->
            if (hasImage) {
                binding.thumbnailImageTop.setOnClickListener {
                    categoryViewModel.setImagePath(categoryViewModel.lastUserImageForLastTask.value?.path.toString())
                    Log.i(TAG, "Image Path to Share: ${categoryViewModel.imagePathToShare.value}")
                    binding.thumbnailImageTop.scaleX = 1.2f
                    binding.thumbnailImageTop.scaleY = 1.2f
                    binding.thumbnailImageMiddle.scaleX = 1.0f
                    binding.thumbnailImageMiddle.scaleY = 1.0f
                    binding.thumbnailImageBottom.scaleX = 1.0f
                    binding.thumbnailImageBottom.scaleY = 1.0f
                }
            }
        }
        categoryViewModel.getSecondLastUserImageForLastTask { hasImage ->
            if (hasImage) {
                binding.thumbnailImageMiddle.setOnClickListener {
                    categoryViewModel.setImagePath(categoryViewModel.secondLastUserImageForLastTask.value?.path.toString())
                    Log.i(TAG, "Image Path to Share: ${categoryViewModel.imagePathToShare.value}")
                    binding.thumbnailImageTop.scaleX = 1.0f
                    binding.thumbnailImageTop.scaleY = 1.0f
                    binding.thumbnailImageMiddle.scaleX = 1.2f
                    binding.thumbnailImageMiddle.scaleY = 1.2f
                    binding.thumbnailImageBottom.scaleX = 1.0f
                    binding.thumbnailImageBottom.scaleY = 1.0f
                }
            }
        }
        categoryViewModel.getThirdLastUserImageForLastTask { hasImage ->
            if (hasImage) {
                binding.thumbnailImageBottom.setOnClickListener {
                    categoryViewModel.setImagePath(categoryViewModel.thirdLastUserImageForLastTask.value?.path.toString())
                    Log.i(TAG, "Image Path to Share: ${categoryViewModel.imagePathToShare.value}")
                    binding.thumbnailImageTop.scaleX = 1.0f
                    binding.thumbnailImageTop.scaleY = 1.0f
                    binding.thumbnailImageMiddle.scaleX = 1.0f
                    binding.thumbnailImageMiddle.scaleY = 1.0f
                    binding.thumbnailImageBottom.scaleX = 1.2f
                    binding.thumbnailImageBottom.scaleY = 1.2f
                }
            }
        }

        // Handle share with share button click
        binding.share2Button.setOnClickListener {
            requestPermissions()
            handleShare()
            categoryViewModel.updateHasShared(true)
        }

        // Navigate to stats fragment
        binding.toStatsFragmentButton.setOnClickListener {
            findNavController().navigate(R.id.action_shareFragment_to_statsFragment)
        }
        return binding.root
    }

    /** Request permissions if required */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestPermissions() {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(), it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(), permissionsToRequest.toTypedArray(), requestCode
            )
        }
    }

    /** Handle share image on social media menu item */
    private fun handleShare(): Boolean {
        try {
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
            Log.i(TAG, "Error: No Image Available")
        }
        return true
    }
}
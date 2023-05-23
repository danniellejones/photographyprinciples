package cp3406.a2.lenslearn.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.ActivityCategoryBinding
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import cp3406.a2.lenslearn.model.CategoryViewModel

private const val LOG_TAG2 = "CategoryActivity"

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private val categoryVewModel: CategoryViewModel by viewModels()
    private lateinit var navController: NavController

    /** Connect data binding and set up navigation */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        Log.d(LOG_TAG2, "Category Activity is reached")

        // Add Navigation Bar and Navigation Graph
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    /** Create options for main menu */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Only show share option if there is a path to share
        val shareMenuItem = menu?.findItem(R.id.shareImplicitIntent)
        val imagePathToShare = binding.categoryViewModel?.imagePathToShare.toString()
        Log.i(LOG_TAG2, "Image path to share from menu: $imagePathToShare")
        shareMenuItem?.isVisible = !(imagePathToShare != null && imagePathToShare != "")
        return true
    }

    /** Determine actions for menu item select in main menu */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shareImplicitIntent -> {
                handleShare()
                true
            }
            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }


    /** Handle share image on social media menu item */
    private fun handleShare(): Boolean {
        try {
            val pathName = binding.categoryViewModel?.imagePathToShare.toString()
            if (pathName.isEmpty()) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "image/jpeg"
                    putExtra(Intent.EXTRA_STREAM, Uri.parse(pathName))
                }
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.i(LOG_TAG2, "No Images Available")
        }
        return true
    }

    /** Create navigation up buttons on all fragments except category */
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return if (navController.currentDestination?.id == R.id.categoryFragment) {
            false
        } else {
            navController.navigateUp() || super.onSupportNavigateUp()
        }
    }
}
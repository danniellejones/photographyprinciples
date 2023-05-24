/** Category Activity hosts all fragments and the controls menu navigation */
package cp3406.a2.lenslearn.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.ActivityCategoryBinding

private const val LOG_TAG = "CategoryActivity"

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private lateinit var navController: NavController

    /** Connect data binding and set up navigation */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)

        // Add Navigation Bar and Navigation Graph
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    /** Create options for main menu */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // TODO: Dynamic menu - Show if path to share, hidden but doesn't show up dynamically
        val shareMenuItem = menu?.findItem(R.id.shareImplicitIntent)
        val imagePathToShare = binding.categoryViewModel?.imagePathToShare.toString()
        // Null check required at runtime
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
                    putExtra(Intent.EXTRA_STREAM, Uri.parse(pathName))  // TODO: Fix file format
                }
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.i(LOG_TAG, "Error: No Image Available")
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
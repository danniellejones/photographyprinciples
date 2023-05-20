package cp3406.a2.lenslearn.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.ActivityCategoryBinding
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import cp3406.a2.lenslearn.databinding.FragmentLearningBinding
import cp3406.a2.lenslearn.model.CategoryViewModel

private const val LOG_TAG2 = "CategoryActivity"

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        Log.d(LOG_TAG2, "Category Activity is reached")

        // Add Navigation Bar and Navigation Graph
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)

        // Get reference to the view model
//        binding.lifecycleOwner = this
//        binding.categoryViewModel = viewModel

        // Load and observe view model data
//        viewModel.loadData()
//        viewModel.categoryId.observe(this) {
//            displaySnackbar(it)
//        }

        // Set click listeners for each category
//        binding.cardCategoryBalance.setOnClickListener{viewModel.setCategoryId(1)}
//        binding.cardCategoryContrast.setOnClickListener{viewModel.setCategoryId(2)}
//        binding.cardCategoryEmphasis.setOnClickListener{viewModel.setCategoryId(3)}
//        binding.cardCategoryPattern.setOnClickListener{viewModel.setCategoryId(4)}
//        binding.cardCategoryRhythm.setOnClickListener{viewModel.setCategoryId(5)}
//        binding.cardCategorySpace.setOnClickListener{viewModel.setCategoryId(6)}
//        binding.cardCategoryUnity.setOnClickListener{viewModel.setCategoryId(7)}

//        binding.cardCategoryContrast.setOnClickListener {
//            supportFragmentManager.commit {
//                add<LearningFragment>(R.id.categoryActivityMain, null)
//            navController.navigate(action)
//        findNavController().navigate(R.id.action_doFragment_to_shareFragment)
//        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when(item.itemId) {
//            R.id.shareFragment -> handleShare()
////            R.id.settingsFragment-> navigateToSettings()
////            R.id.progressFragment-> navigateToProgress()
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
//
//    private fun handleShare(): Boolean {
//        val intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            type = "text/plain"
//            // Data to share
//            putExtra(Intent.EXTRA_TEXT, "Text to share")
////            putExtra(Intent.EXTRA_TEXT, "Text to share ${viewModel.categoryId.value}")
//        }
//        startActivity(intent)
//        return true
//    }
//
//    private fun navigateToProgress() : Boolean {
//        supportFragmentManager.commit {
//            replace<StatsFragment>(R.id.categoryActivityMain, null, null)
////            supportFragmentManager.commit {
////                add<StatsFragment>(R.id.categoryActivityMain, null)
//        }
//        return true
//    }
//
//    private fun navigateToSettings() : Boolean {
//        supportFragmentManager.commit {
//            replace<StatsFragment>(R.id.categoryActivityMain, null, null)
//        }
//        return true
//    }


    /** Create navigation up buttons */
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || super.onSupportNavigateUp()
    }


//    override fun onClick(view: View) {
//        // Determine which category card was clicked
//        val categoryId = when (view.id) {
//            R.id.card_category_balance -> 1
//            R.id.card_category_contrast -> 2
//            R.id.card_category_emphasis -> 3
//            R.id.card_category_pattern -> 4
//            R.id.card_category_rhythm -> 5
//            R.id.card_category_space -> 6
//            R.id.card_category_unity -> 7
//            else -> return
//        }
//
//        // Create the action using the generated directions class
////        val action = ActivityCategoryDirections.actionCategoryActivityToLearningFragment(categoryId)
//
//        // Navigate to the LearningFragment
////        findNavController(R.id.nav_host_fragment).navigate(action)
//
//    }

//    private fun displaySnackbar(catId: Int) {
//        Snackbar.make(binding.root, "current value: $catId", Snackbar.LENGTH_LONG).show()
//    }
}




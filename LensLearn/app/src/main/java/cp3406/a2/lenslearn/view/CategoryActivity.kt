package cp3406.a2.lenslearn.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.ActivityCategoryBinding
//import cp3406.a2.lenslearn.databinding.ActivityCategoryDirections
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import cp3406.a2.lenslearn.model.CategoryViewModel

class CategoryActivity : AppCompatActivity() {

    val categoryId = -1
    private lateinit var binding: ActivityCategoryBinding
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityCategoryBinding>(
            this,
            R.layout.activity_category
        )
//        binding = ActivityCategoryBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        // Add Navigation Bar and Navigation Graph
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)

        // Get reference to the view model
//        val viewModel by viewModels<CategoryViewModel>()
        binding.lifecycleOwner = this
        binding.categoryViewModel = viewModel

        // Load and observe view model data
        viewModel.loadData()
        viewModel.categoryId.observe(this) {
            displaySnackbar(it)
        }

//        val view = binding.root
//

        // Set click listeners for each category
        binding.cardCategoryBalance.setOnClickListener{viewModel.setCategoryId(1)}
        binding.cardCategoryContrast.setOnClickListener{viewModel.setCategoryId(2)}
        binding.cardCategoryEmphasis.setOnClickListener{viewModel.setCategoryId(3)}
        binding.cardCategoryPattern.setOnClickListener{viewModel.setCategoryId(4)}
        binding.cardCategoryRhythm.setOnClickListener{viewModel.setCategoryId(5)}
        binding.cardCategorySpace.setOnClickListener{viewModel.setCategoryId(6)}
        binding.cardCategoryUnity.setOnClickListener{viewModel.setCategoryId(7)}


//        binding.cardCategoryBalance.setOnClickListener(this)
//        binding.cardCategoryContrast.setOnClickListener(this)
//        binding.cardCategoryEmphasis.setOnClickListener(this)
//        binding.cardCategoryPattern.setOnClickListener(this)
//        binding.cardCategoryRhythm.setOnClickListener(this)
//        binding.cardCategorySpace.setOnClickListener(this)
//        binding.cardCategoryUnity.setOnClickListener(this)


//        // Select a category
//        binding.cardCategoryBalance.setOnClickListener {
//            val action = CategoryActivityDirections.actionCategoryActivityToLearningFragment(categoryId = 1)
//            navController.navigate(action)
//            startBalancePlay()
//        }
//
//        binding.cardCategoryContrast.setOnClickListener {
//            val action = CategoryActivityDirections.actionCategoryActivityToLearningFragment(categoryId = 2)
//            navController.navigate(action)
//        }
//
//        binding.cardCategoryEmphasis.setOnClickListener {
//            val action = CategoryActivityDirections.actionCategoryActivityToLearningFragment(categoryId = 3)
//            navController.navigate(action)
//        }
//
//        binding.cardCategoryPattern.setOnClickListener {
//            val action = CategoryActivityDirections.actionCategoryActivityToLearningFragment(categoryId = 4)
//            navController.navigate(action)
//        }
//
//        binding.cardCategoryRhythm.setOnClickListener {
//            val action = CategoryActivityDirections.actionCategoryActivityToLearningFragment(categoryId = 5)
//            navController.navigate(action)
//        }
//
//        binding.cardCategorySpace.setOnClickListener {
//            val action = CategoryActivityDirections.actionCategoryActivityToLearningFragment(categoryId = 6)
//            navController.navigate(action)
//        }
//
//        binding.cardCategoryUnity.setOnClickListener {
//            val action = CategoryActivityDirections.actionCategoryActivityToLearningFragment(categoryId = 7)
//            navController.navigate(action)
//        }
    }

    /** Create navigation up buttons */
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || super.onSupportNavigateUp()
    }

    private fun startBalancePlay() {
        TODO("Not yet implemented")
//        supportFragmentManager.beginTransaction().add(R.id.).commit()
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

    private fun displaySnackbar(catId: Int) {
        Snackbar.make(binding.root, "current value: $catId", Snackbar.LENGTH_LONG).show()
    }
}




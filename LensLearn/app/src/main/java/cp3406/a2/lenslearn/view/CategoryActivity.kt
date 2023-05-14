package cp3406.a2.lenslearn.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cp3406.a2.lenslearn.R
import cp3406.a2.lenslearn.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {

    val selectedCategoryId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCategoryBinding>(
            this,
            R.layout.activity_category
        )

        // Find NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)


//        val view = binding.root
//        binding.lifecycleOwner = this
//        binding.categoryViewModel = viewModel

        binding.textCategoryBalance.setOnClickListener {
            startBalancePlay()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || super.onSupportNavigateUp()
    }

    private fun startBalancePlay() {
        TODO("Not yet implemented")
//        supportFragmentManager.beginTransaction().add(R.id.).commit()
    }


}




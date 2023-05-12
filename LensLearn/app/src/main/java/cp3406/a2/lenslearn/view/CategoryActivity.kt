package cp3406.a2.lenslearn.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cp3406.a2.lenslearn.R

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val binding = DataBindingUtil.setContentView<ActivityCategoryBinding>(this, R.layout.activity_category)
        setContentView(R.layout.activity_category)
    }
}
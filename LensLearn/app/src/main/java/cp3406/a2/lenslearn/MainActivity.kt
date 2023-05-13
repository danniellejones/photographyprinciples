package cp3406.a2.lenslearn

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import cp3406.a2.lenslearn.view.CategoryActivity

class MainActivity : AppCompatActivity() {

    /** On Create Inflate Layout and Set Click Listener */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton = findViewById<Button>(R.id.playButton)
        playButton.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in)
        }
    }

    /** On Pause Continue Transition */
    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}


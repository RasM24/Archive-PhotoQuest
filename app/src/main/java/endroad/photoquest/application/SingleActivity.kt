package endroad.photoquest.application

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import endroad.photoquest.R
import endroad.photoquest.component.changeRoot
import endroad.photoquest.view.MainFragment

class SingleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupOrientation()
        setContentView(R.layout.base_activity)

        savedInstanceState ?: openMainFragment()
    }

    private fun openMainFragment() {
        supportFragmentManager.changeRoot(MainFragment())
    }


    private fun setupOrientation() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
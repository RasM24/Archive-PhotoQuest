package endroad.photoquest.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import endroad.photoquest.R
import endroad.photoquest.component.changeRoot
import endroad.photoquest.view.MainFragment

class SingleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        savedInstanceState ?: openMainFragment()
    }

    private fun openMainFragment() {
        supportFragmentManager.changeRoot(MainFragment())
    }
}
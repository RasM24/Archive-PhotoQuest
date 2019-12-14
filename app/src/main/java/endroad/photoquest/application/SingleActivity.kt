package endroad.photoquest.application

import endroad.photoquest.R
import endroad.photoquest.view.MainFragment
import ru.endroad.arena.viewlayer.activity.BaseActivity
import ru.endroad.navigation.changeRoot

class SingleActivity : BaseActivity() {

	override val layout = R.layout.base_activity

	override fun onFirstCreate() {
		fragmentManager.changeRoot(MainFragment.newInstance())
	}
}
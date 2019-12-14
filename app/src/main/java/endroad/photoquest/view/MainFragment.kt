package endroad.photoquest.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import androidx.fragment.app.Fragment
import endroad.photoquest.Quest.ListQuest
import endroad.photoquest.R
import endroad.photoquest.data.PlaceDataSource
import endroad.photoquest.places.ListPlacesActivity
import kotlinx.android.synthetic.main.main_fragment.*
import ru.endroad.arena.viewlayer.extension.startScreen
import ru.endroad.arena.viewlayer.fragment.BaseFragment
import ru.endroad.navigation.finish

class MainFragment : BaseFragment() {

	override val layout = R.layout.main_fragment

	private val placeDataSource by lazy { PlaceDataSource(requireContext()) }

	override fun setupViewComponents() {
		bt_place.setOnClickListener { requireContext().startScreen(ListPlacesActivity::class.java) }
		bt_quest.setOnClickListener { requireContext().startScreen(ListQuest::class.java) }
		bt_setting.setOnClickListener { clearProgress() }
		bt_exit.setOnClickListener { finish() }

		setAnimationBackground()
	}

	private fun setAnimationBackground() {
		val animation = img_animation.background as AnimationDrawable
		animation.start()
	}

	private fun clearProgress() {
		val sPref = requireContext().getSharedPreferences("Places", Context.MODE_PRIVATE)
		val ed = sPref.edit()
		placeDataSource.getList().forEach {
			ed.putBoolean(it.openName, false)
		}
		ed.apply()
	}

	companion object {
		fun newInstance(): Fragment =
			MainFragment()
	}
}
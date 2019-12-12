package endroad.photoquest

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import endroad.photoquest.Quest.ListQuest
import endroad.photoquest.data.PlaceDataSource
import endroad.photoquest.places.ListPlacesActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.endroad.arena.viewlayer.extension.startScreen

class MainActivity : AppCompatActivity() {

	private val placeDataSource by lazy { PlaceDataSource(this) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		bt_place.setOnClickListener { startScreen(ListPlacesActivity::class.java) }
		bt_quest.setOnClickListener { startScreen(ListQuest::class.java) }
		bt_setting.setOnClickListener { clearProgress() }
		bt_exit.setOnClickListener { finish() }

		setAnimationBackground()
	}

	private fun setAnimationBackground() {
		val animation = img_animation.background as AnimationDrawable
		animation.start()
	}

	private fun clearProgress() {
		val sPref = getSharedPreferences("Places", Context.MODE_PRIVATE)
		val ed = sPref.edit()
		placeDataSource.getList().forEach {
			ed.putBoolean(it.openName, false)
		}
		ed.apply()
	}
}
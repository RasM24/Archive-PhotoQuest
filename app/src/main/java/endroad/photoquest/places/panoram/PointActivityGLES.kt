package endroad.photoquest.places.panoram

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import endroad.photoquest.R
import endroad.photoquest.data.PlaceDataSource
import kotlinx.android.synthetic.main.activity_point1.*
import kotlin.math.hypot

class PointActivityGLES : AppCompatActivity() {

	private val placeId: Int by lazy { intent.getIntExtra("id", -1) }

	//TODO криво и костыльно, будет в таком виде до перехода на фрагменты
	private val point by lazy { PlaceDataSource(this).getList()[placeId] }

	private val gps by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

	private val locationListener: LocationListener = object : LocationListener {
		override fun onLocationChanged(location: Location) {
			val x = location.latitude
			val y = location.longitude
			val distance = hypot(x - point.posX, y - point.posY)
			if (distance < 0.00021 && !point.opened) //если выполняется это условие, то вы открыли точку
			{
				openPoint()
			} else {
				bt_map_img.setImageResource(point.getIdRes(distance))
			}
		}

		override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
		override fun onProviderEnabled(provider: String) {}
		override fun onProviderDisabled(provider: String) {}
	}

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setupOrientation()

		setContentView(R.layout.activity_point1)

		gl_surface_view.start(this)
		gl_surface_view.setTexturePath(point.pathTexture)

		bt_point_fullscreen.setOnClickListener { changeOrientation() }
		map_img.setOnClickListener { map_img.visibility = View.INVISIBLE }
		bt_map_img.setOnClickListener {
			bt_map_img.visibility = View.VISIBLE
			openPoint()
		}
		bt_open_img.setOnClickListener {
			bt_open_img.visibility = View.INVISIBLE
			finish()
		}

		map_img.setImageDrawable(loadImage("${point.pathTexture}map.jpg"))

		//TODO добавить запрос разрешения на геолокацию
		gps.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
								   1000, 0f, locationListener)
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

	private fun changeOrientation() {
		if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
			requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
		} else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
		}
	}

	private fun loadImage(path: String): Drawable? =
		runCatching { Drawable.createFromStream(assets.open(path), null) }
			.getOrNull()

	override fun onStart() {
		super.onStart()
		if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
			bt_point_fullscreen.setImageResource(R.drawable.full)
		} else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			bt_point_fullscreen.setImageResource(R.drawable.nofull)
		}
	}

	override fun onResume() {
		super.onResume()
		bt_map_img.setImageResource(R.drawable.dist_0)
	}

	override fun onDestroy() {
		super.onDestroy()
		gps.removeUpdates(locationListener)
	}

	fun openPoint() {
		point.opened = true
		val sPref = getSharedPreferences("Places", Context.MODE_PRIVATE)
		val ed = sPref.edit()
		ed.putBoolean(point.openName, true)
		ed.apply()
		val anim = AnimationUtils.loadAnimation(this, R.anim.anim_alpha)
		bt_open_img.startAnimation(anim)
		bt_open_img.visibility = View.VISIBLE
	}

	override fun onBackPressed() {
		finish()
	}
}
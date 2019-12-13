package endroad.photoquest.places.panoram

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import endroad.photoquest.R
import endroad.photoquest.data.PlaceDataSource
import endroad.photoquest.model.Place
import kotlinx.android.synthetic.main.activity_point1.*
import java.io.IOException
import kotlin.math.hypot

class PointActivityGLES : AppCompatActivity(), View.OnClickListener {

	lateinit var placeDataSource: PlaceDataSource

	var point: Place? = null

	var gps: LocationManager? = null

	private val locationListener: LocationListener = object : LocationListener {
		override fun onLocationChanged(location: Location) {
			val x = location.latitude
			val y = location.longitude
			val distance = hypot(x - point!!.posX, y - point!!.posY)
			if (distance < 0.00021 && !point!!.opened) //если выполняется это условие, то вы открыли точку
			{
				openPoint()
			} else {
				bt_map_img.setImageResource(point!!.getIdRes(distance))
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

		try {
			placeDataSource = PlaceDataSource(this)
			point = placeDataSource.getList()[intent.getIntExtra("id", -1)]
			gl_surface_view.setTexturePath(point!!.pathTexture)
		} catch (e: Exception) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
			finish()
		}
		bt_point_fullscreen.setOnClickListener(this)
		bt_map_img.setOnClickListener(this)
		map_img.setOnClickListener(this)
		map_img.visibility = View.INVISIBLE
		try {
			val ims = assets.open(point!!.pathTexture + "map.jpg")
			val d = Drawable.createFromStream(ims, null)
			map_img.setImageDrawable(d)
		} catch (ex: IOException) {
			return
		}
		bt_open_img.visibility = View.INVISIBLE
		bt_open_img.setBackgroundColor(Color.TRANSPARENT)
		bt_open_img.setImageResource(R.drawable.test)
		bt_open_img.setOnClickListener(this)
		gps = getSystemService(Context.LOCATION_SERVICE) as LocationManager
		//TODO добавить запрос разрешения на геолокацию
		gps!!.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
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
		gps!!.removeUpdates(locationListener)
	}

	override fun onClick(v: View) {
		when (v.id) {
			R.id.bt_point_fullscreen -> if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

			R.id.bt_map_img          -> {
				bt_map_img.visibility = View.VISIBLE
				openPoint()
			}

			R.id.map_img             -> map_img.visibility = View.INVISIBLE

			R.id.bt_open_img         -> {
				bt_open_img.visibility = View.INVISIBLE
				finish()
			}
		}
	}

	fun openPoint() {
		point!!.opened = true
		val sPref = getSharedPreferences("Places", Context.MODE_PRIVATE)
		val ed = sPref.edit()
		ed.putBoolean(point!!.openName, true)
		ed.apply()
		val anim = AnimationUtils.loadAnimation(this, R.anim.anim_alpha)
		bt_open_img.startAnimation(anim)
		bt_open_img.visibility = View.VISIBLE
	}

	override fun onBackPressed() {
		finish()
	}
}
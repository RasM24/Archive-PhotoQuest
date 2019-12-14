package endroad.photoquest.places

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import endroad.photoquest.R
import endroad.photoquest.data.PlaceDataSource
import kotlinx.android.synthetic.main.panoramic_place_fragment.*
import org.koin.android.ext.android.inject
import ru.endroad.arena.viewlayer.extension.argument
import ru.endroad.arena.viewlayer.extension.hideViews
import ru.endroad.arena.viewlayer.extension.showViews
import ru.endroad.arena.viewlayer.extension.withArgument
import ru.endroad.arena.viewlayer.fragment.BaseFragment
import ru.endroad.navigation.finish
import ru.endroad.panorama.TexturePathes

class PanoramicPlaceFragment : BaseFragment() {

	override val layout = R.layout.panoramic_place_fragment

	private val placeId: Int by argument(PLACE_ID)

	private val placeDataSource: PlaceDataSource by inject()

	private val point get() = placeDataSource.getList()[placeId]

	private val gps by lazy { requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager }

	private val locationListener: LocationListener = object : LocationListener {
		override fun onLocationChanged(location: Location) {
			bt_map_img.setImageResource(point.getDistanceIcon(location.latLng))
		}

		val Location.latLng get() = LatLng(latitude, longitude)

		override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
		override fun onProviderEnabled(provider: String) {}
		override fun onProviderDisabled(provider: String) {}
	}

	override fun setupViewComponents() {
		setupOrientation()
		val pathes = TexturePathes(top = "${point.pathTexture}top.jpg",
								   bottom = "${point.pathTexture}bottom.jpg",
								   right = "${point.pathTexture}right.jpg",
								   left = "${point.pathTexture}left.jpg",
								   front = "${point.pathTexture}front.jpg",
								   back = "${point.pathTexture}back.jpg")

		gl_surface_view.start(this, pathes)

		bt_point_fullscreen.setOnClickListener { changeOrientation() }
		map_img.setOnClickListener { showViews(map_img) }
		bt_map_img.setOnClickListener {
			showViews(bt_map_img)
			openPoint()
		}
		bt_open_img.setOnClickListener {
			hideViews(bt_open_img)
			finish()
		}

		map_img.setImageDrawable(loadImage("${point.pathTexture}map.jpg"))

		//TODO добавить запрос разрешения на геолокацию
//		gps.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
//								   1000, 0f, locationListener)
	}

	private fun setupOrientation() {
		if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
			requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
			requireActivity().requestWindowFeature(Window.FEATURE_NO_TITLE)
		} else {
			requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
		}
	}

	private fun changeOrientation() {
		if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
			requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
		} else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
		}
	}

	private fun loadImage(path: String): Drawable? =
		runCatching { Drawable.createFromStream(requireContext().assets.open(path), null) }
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

	private fun openPoint() {
		val sPref = requireContext().getSharedPreferences("Places", Context.MODE_PRIVATE)
		val ed = sPref.edit()
		ed.putBoolean(point.openName, true)
		ed.apply()
		val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_alpha)
		bt_open_img.startAnimation(anim)
		showViews(bt_open_img)
	}

	companion object {
		private const val PLACE_ID = "placeId"

		fun newInstance(id: Int): Fragment =
			PanoramicPlaceFragment().withArgument {
				putInt(PLACE_ID, id)
			}
	}
}
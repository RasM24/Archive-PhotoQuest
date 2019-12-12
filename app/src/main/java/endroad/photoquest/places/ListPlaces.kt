package endroad.photoquest.places

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import endroad.photoquest.R
import endroad.photoquest.data.PlaceDataSource
import endroad.photoquest.places.panoram.PointActivityGLES
import kotlinx.android.synthetic.main.listplaces.*

class ListPlaces : AppCompatActivity() {

	private val placeDataSource by lazy { PlaceDataSource(this) }

	private val adapter by lazy { AdapterListPlaces(this, placeDataSource.getList()) }

	//TODO memory leak - убрать зависимость AdapterListPlaces и x,y
	@JvmField
	var x = 0.0
	@JvmField
	var y = 0.0
	private val locationListener: LocationListener = object : LocationListener {
		override fun onLocationChanged(location: Location) {
			x = location.latitude
			y = location.longitude
			adapter.notifyDataSetChanged()
		}

		override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
		override fun onProviderEnabled(s: String) {}
		override fun onProviderDisabled(s: String) {}
	}

	/**
	 * Called when the activity is first created.
	 */
	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.listplaces)
		val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
		//TODO добавить запрос разрешения на геолокацию
		locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
											   1000, 0f, locationListener)
		lvMain.adapter = adapter
		lvMain.setOnItemClickListener { _, _, position, _ ->
			val intent = Intent(application.baseContext, PointActivityGLES::class.java)
			intent.putExtra("id", position)
			startActivity(intent)
		}
	}

	override fun onResume() {
		super.onResume()
		adapter.notifyDataSetChanged()
	}
}
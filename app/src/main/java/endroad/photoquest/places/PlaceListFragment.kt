package endroad.photoquest.places

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mikepenz.fastadapter.IModelItem
import endroad.photoquest.R
import endroad.photoquest.data.PlaceDataSource
import endroad.photoquest.model.Place
import org.koin.android.ext.android.inject
import ru.endroad.arena.viewlayer.fragment.ListFragment
import ru.endroad.navigation.forwardTo

class PlaceListFragment : ListFragment() {

	override val layout = R.layout.place_list_fragment

	private val placeDataSource: PlaceDataSource by inject()

	//TODO memory leak - убрать зависимость AdapterListPlaces и x,y
	@JvmField
	var x = 0.0
	@JvmField
	var y = 0.0
	private val locationListener: LocationListener = object : LocationListener {
		override fun onLocationChanged(location: Location) {
			x = location.latitude
			y = location.longitude
			//adapter.notifyDataSetChanged()
		}

		override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
		override fun onProviderEnabled(s: String) {}
		override fun onProviderDisabled(s: String) {}
	}

	override fun setupViewComponents() {
		placeDataSource.getList()
			.map(::PlaceItem)
			.setItems()

		//TODO добавить запрос разрешения на геолокацию
//		val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//		locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
//											   1000, 0f, locationListener)
	}

	override fun onClickItem(item: IModelItem<*, *>): Boolean {
		val panoramicPlace = item.model as? Place ?: return false

		//TODO костыль, временно.. надеюсь :)
		val position: Int = placeDataSource.getList().indexOf(panoramicPlace)

		requireFragmentManager().forwardTo(PanoramicPlaceFragment.newInstance(position))

		return true
	}

	companion object {
		fun newInstance(): Fragment =
			PlaceListFragment()
	}
}
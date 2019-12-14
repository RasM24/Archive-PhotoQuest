package endroad.photoquest.model

import com.google.android.gms.maps.model.LatLng
import endroad.photoquest.R
import kotlin.math.hypot

class Place(val openName: String,
			val area: Int,
			val posX: Double,
			val posY: Double,
			val pathTexture: String) {

	//TODO убрать магические числа
	fun getDistanceIcon(userPosition: LatLng): Int {
		val distance = calculateDistance(userPosition)

		return when {
			distance < 2e-4 -> R.drawable.dist_1
			distance < 3e-4 -> R.drawable.dist_2
			distance < 5e-4 -> R.drawable.dist_3
			distance < 8e-4 -> R.drawable.dist_4
			else            -> R.drawable.dist_5
		}
	}

	//TODO взять нормальный алгоритм определения расстояния
	private fun calculateDistance(userPosition: LatLng): Double =
		hypot(userPosition.latitude - posX, userPosition.longitude - posY)

}
package endroad.photoquest.model

import endroad.photoquest.Data
import endroad.photoquest.R

class Place(private val hiddenName: String,
			val openName: String, //public int id;
			private val area: Int,
			private val point: Int,
			val posX: Double,
			val posY: Double,
			val pathTexture: String) {


	@JvmField
	var opened = false

	fun name(): String {
		return if (opened) openName else hiddenName
	}

	fun nameDiff(): String? {
		return if (opened) "открыта" else when (point) {
			Data.POINT_DIFF1 -> Data.PLACE_DIFF1
			Data.POINT_DIFF2 -> Data.PLACE_DIFF2
			Data.POINT_DIFF3 -> Data.PLACE_DIFF3
			Data.POINT_DIFF4 -> Data.PLACE_DIFF4
			else             -> null
		}
	}

	fun getArea(): Int {
		return if (!opened) area else when (area) {
			Data.AREA_CENTR -> Data.AREA_CENTR_O
			Data.AREA_KIROV -> Data.AREA_KIROV_O
			Data.AREA_LENIN -> Data.AREA_LENIN_O
			Data.AREA_OKTYB -> Data.AREA_OKTYB_O
			Data.AREA_SOVET -> Data.AREA_SOVET_O
			Data.AREA_SVERD -> Data.AREA_SVERD_O
			Data.AREA_ZHELZ -> Data.AREA_ZHELZ_O
			else            -> 0
		}
	}

	//TODO убрать магические числа
	fun getIdRes(dist: Double): Int {
		return when {
			opened          -> R.drawable.dist_0
			dist < 0.00021f -> R.drawable.dist_1
			dist < 0.00033f -> R.drawable.dist_2
			dist < 0.0005f  -> R.drawable.dist_3
			dist < 0.0008f  -> R.drawable.dist_4
			else            -> R.drawable.dist_5
		}
	}
}
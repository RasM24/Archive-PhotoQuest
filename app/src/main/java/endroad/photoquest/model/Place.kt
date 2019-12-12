package endroad.photoquest.model

import endroad.photoquest.*

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
			POINT_DIFF1 -> PLACE_DIFF1
			POINT_DIFF2 -> PLACE_DIFF2
			POINT_DIFF3 -> PLACE_DIFF3
			POINT_DIFF4 -> PLACE_DIFF4
			else        -> null
		}
	}

	fun getArea(): Int {
		return if (!opened) area else when (area) {
			AREA_CENTR -> AREA_CENTR_O
			AREA_KIROV -> AREA_KIROV_O
			AREA_LENIN -> AREA_LENIN_O
			AREA_OKTYB -> AREA_OKTYB_O
			AREA_SOVET -> AREA_SOVET_O
			AREA_SVERD -> AREA_SVERD_O
			AREA_ZHELZ -> AREA_ZHELZ_O
			else       -> 0
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
package endroad.photoquest.data

import endroad.photoquest.AREA_CENTR
import endroad.photoquest.model.Place

class PlaceDataSource {

	private val hardcoreData = listOf(
		Place("Двор в центре", AREA_CENTR, 56.01031335, 92.87928364, "point/c1/"),
		Place("Суд вроде)", AREA_CENTR, 56.01207307, 92.88627617, "point/c2/"),
		Place("КИЦ", AREA_CENTR, 56.010752, 92.894623, "point/c3/"),
		Place("Почти KFC", AREA_CENTR, 56.01115317, 92.87620894, "point/c4/"),
		Place("Дом", AREA_CENTR, 56.0148301, 92.88149703, "point/c5/"),
		Place("Дом", AREA_CENTR, 56.00965500, 92.88623530, "point/c6/"),
		Place("Дворец им. Ярыгина", AREA_CENTR, 55.99456135, 92.87342037, "point/c7/"),
		Place("Дворец им. Ярыгина", AREA_CENTR, 56.014805, 92.866301, "point/c8/"),
		Place("Дворец им. Ярыгина", AREA_CENTR, 56.01516280, 92.85833529, "point/c9/"),
		Place("Дворец им. Ярыгина", AREA_CENTR, 56.01265808, 92.855563736, "point/c10/"),
		Place("Дворец им. Ярыгина", AREA_CENTR, 56.01175705, 92.856535, "point/c11/"),
		Place("Дворец им. Ярыгина", AREA_CENTR, 56.01137021, 92.8547404, "point/c12/"),
		Place("Дворец им. Ярыгина", AREA_CENTR, 56.01154, 92.861269, "point/c13/")
	)

	fun getList(): List<Place> = hardcoreData
}
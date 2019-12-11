package endroad.photoquest.data

import android.content.Context
import endroad.photoquest.Data
import endroad.photoquest.Places.DataPlaces

//TODO убрать контекст
// пересмотреть загрузку данных
class PlaceDataSource(context: Context) {

	private val hardcoreData = listOf(
		DataPlaces("Дворы 1", "Двор в центре", Data.AREA_CENTR, Data.POINT_DIFF4, 56.01031335f, 92.87928364f, "point/c1/"),
		DataPlaces("Площадь 1", "Суд вроде)", Data.AREA_CENTR, Data.POINT_DIFF2, 56.01207307f, 92.88627617f, "point/c2/"),
		DataPlaces("Места 1", "КИЦ", Data.AREA_CENTR, Data.POINT_DIFF1, 56.010752f, 92.894623f, "point/c3/"),
		DataPlaces("Места 2", "Почти KFC", Data.AREA_CENTR, Data.POINT_DIFF2, 56.01115317f, 92.87620894f, "point/c4/"),
		DataPlaces("Дворы 2", "Дом", Data.AREA_CENTR, Data.POINT_DIFF4, 56.0148301f, 92.88149703f, "point/c5/"),
		DataPlaces("Дворы 3", "Дом", Data.AREA_CENTR, Data.POINT_DIFF4, 56.00965500f, 92.88623530f, "point/c6/"),
		DataPlaces("Места 3", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 55.99456135f, 92.87342037f, "point/c7/"),
		DataPlaces("Места 6", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.014805f, 92.866301f, "point/c8/"),
		DataPlaces("Дворы 4", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.01516280f, 92.85833529f, "point/c9/"),
		DataPlaces("Дворы 5", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.01265808f, 92.855563736f, "point/c10/"),
		DataPlaces("Места 4", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.01175705f, 92.856535f, "point/c11/"),
		DataPlaces("Площадь 2", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.01137021f, 92.8547404f, "point/c12/"),
		DataPlaces("Места 5", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.01154f, 92.861269f, "point/c13/")
	)

	init {
		val sPref = context.getSharedPreferences("Places", Context.MODE_PRIVATE)

		hardcoreData.forEach {
			it.opened = sPref.getBoolean(it.openName, false)
		}
	}

	fun getList(): List<DataPlaces> = hardcoreData
}
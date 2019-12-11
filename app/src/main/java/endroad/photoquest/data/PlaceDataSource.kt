package endroad.photoquest.data

import android.content.Context
import endroad.photoquest.Data
import endroad.photoquest.model.Place

//TODO убрать контекст
// пересмотреть загрузку данных
class PlaceDataSource(context: Context) {

	private val hardcoreData = listOf(
		Place("Дворы 1",
			  "Двор в центре",
			  Data.AREA_CENTR,
			  Data.POINT_DIFF4,
			  56.01031335,
			  92.87928364,
			  "point/c1/"),
		Place("Площадь 1",
			  "Суд вроде)",
			  Data.AREA_CENTR,
			  Data.POINT_DIFF2,
			  56.01207307,
			  92.88627617,
			  "point/c2/"),
		Place("Места 1", "КИЦ", Data.AREA_CENTR, Data.POINT_DIFF1, 56.010752, 92.894623, "point/c3/"),
		Place("Места 2",
			  "Почти KFC",
			  Data.AREA_CENTR,
			  Data.POINT_DIFF2,
			  56.01115317,
			  92.87620894,
			  "point/c4/"),
		Place("Дворы 2", "Дом", Data.AREA_CENTR, Data.POINT_DIFF4, 56.0148301, 92.88149703, "point/c5/"),
		Place("Дворы 3", "Дом", Data.AREA_CENTR, Data.POINT_DIFF4, 56.00965500, 92.88623530, "point/c6/"),
		Place("Места 3",
			  "Дворец им. Ярыгина",
			  Data.AREA_CENTR,
			  Data.POINT_DIFF3,
			  55.99456135,
			  92.87342037,
			  "point/c7/"),
		Place("Места 6",
			  "Дворец им. Ярыгина",
			  Data.AREA_CENTR,
			  Data.POINT_DIFF3,
			  56.014805,
			  92.866301,
			  "point/c8/"),
		Place("Дворы 4",
			  "Дворец им. Ярыгина",
			  Data.AREA_CENTR,
			  Data.POINT_DIFF3,
			  56.01516280,
			  92.85833529,
			  "point/c9/"),
		Place("Дворы 5",
			  "Дворец им. Ярыгина",
			  Data.AREA_CENTR,
			  Data.POINT_DIFF3,
			  56.01265808,
			  92.855563736,
			  "point/c10/"),
		Place("Места 4",
			  "Дворец им. Ярыгина",
			  Data.AREA_CENTR,
			  Data.POINT_DIFF3,
			  56.01175705,
			  92.856535,
			  "point/c11/"),
		Place("Площадь 2",
			  "Дворец им. Ярыгина",
			  Data.AREA_CENTR,
			  Data.POINT_DIFF3,
			  56.01137021,
			  92.8547404,
			  "point/c12/"),
		Place("Места 5",
			  "Дворец им. Ярыгина",
			  Data.AREA_CENTR,
			  Data.POINT_DIFF3,
			  56.01154,
			  92.861269,
			  "point/c13/")
	)

	init {
		val sPref = context.getSharedPreferences("Places", Context.MODE_PRIVATE)

		hardcoreData.forEach {
			it.opened = sPref.getBoolean(it.openName, false)
		}
	}

	fun getList(): List<Place> = hardcoreData
}
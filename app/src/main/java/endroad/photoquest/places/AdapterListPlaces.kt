package endroad.photoquest.places

import android.content.Context.*
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import endroad.photoquest.R
import endroad.photoquest.model.Place
import kotlin.math.pow
import kotlin.math.sqrt

class AdapterListPlaces internal constructor(private val listPlacesActivityActivity: ListPlacesActivity, private val data: List<Place>) : BaseAdapter() {

	private val colorOpenedPlace = Color.argb(200, 180, 180, 180)
	private val colorDontOpenedPlace = Color.argb(200, 255, 255, 255)

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val inflater = parent.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
		val view = convertView ?: inflater.inflate(R.layout.item, parent, false)

		val distance = sqrt((listPlacesActivityActivity.x - getItem(position).posX).pow(2.0) + (listPlacesActivityActivity.y - getItem(position).posY).pow(2.0))

		return view.apply {
			if (getItem(position).opened)
				setBackgroundColor(colorOpenedPlace)
			else
				setBackgroundColor(colorDontOpenedPlace)

			findViewById<ImageView>(R.id.ivDist).setImageResource(getItem(position).getIdRes(distance))
			findViewById<TextView>(R.id.tvPlaceAbout).text = getItem(position).nameDiff()
			findViewById<ImageView>(R.id.ivPlace).setImageResource(getItem(position).getArea())
			findViewById<TextView>(R.id.tvPlace).text = getItem(position).name()
		}
	}

	override fun getItemId(position: Int): Long {
		return 0
	}

	override fun getItem(position: Int): Place =
		data[position]

	override fun getCount(): Int =
		data.size
}
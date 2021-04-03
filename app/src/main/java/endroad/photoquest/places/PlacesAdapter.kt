package endroad.photoquest.places

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import endroad.photoquest.model.Place

class PlacesAdapter(private val onPlaceClick: (item: Place) -> Boolean) : RecyclerView.Adapter<PlaceViewHolder>() {

	var items: List<Place> = listOf()
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder =
			PlaceViewHolder(parent, onPlaceClick)

	override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
		holder.bind(items[position])
	}

	override fun getItemCount(): Int =
			items.size
}
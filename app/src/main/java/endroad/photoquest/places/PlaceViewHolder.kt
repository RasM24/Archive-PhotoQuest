package endroad.photoquest.places

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import endroad.photoquest.R
import endroad.photoquest.component.inflate
import endroad.photoquest.model.Place
import kotlinx.android.synthetic.main.panoramic_place_item.view.*

class PlaceViewHolder(
		parent: ViewGroup,
		private val onPlaceClick: (item: Place) -> Boolean
) : RecyclerView.ViewHolder(parent.inflate(R.layout.panoramic_place_item)) {

	fun bind(item: Place) {
		itemView.setOnClickListener { onPlaceClick(item) }

		//TODO придумать, что сделать с дистанцией
		//itemView.ivPlace.setImageResource(model.getIdRes(distance))
		itemView.tvPlace.text = item.openName
		itemView.ivDist.setImageResource(item.area)
	}
}
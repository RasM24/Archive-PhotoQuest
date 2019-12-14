package endroad.photoquest.places

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.items.ModelAbstractItem
import endroad.photoquest.R
import endroad.photoquest.model.Place

class PlaceItem(item: Place) : ModelAbstractItem<Place, PlaceItem.ViewHolder>(item) {

	override val layoutRes = R.layout.panoramic_place_item

	override val type = R.id.panoramic_place_item

	override fun getViewHolder(v: View) = ViewHolder(v)

	override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
		super.bindView(holder, payloads)

		//TODO придумать, что сделать с дистанцией
		//val distance = sqrt((listPlacesActivityActivity.x - getItem(position).posX).pow(2.0) + (listPlacesActivityActivity.y - getItem(position).posY).pow(2.0))

		holder.name.text = model.openName
		holder.description.text = model.nameDiff()
		//holder.icon.setImageResource(model.getIdRes(distance))
		holder.iconDistance.setImageResource(model.getArea())

	}

	class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
		val name: TextView = root.findViewById(R.id.tvPlace)
		val icon: ImageView = root.findViewById(R.id.ivPlace)
		val description: TextView = root.findViewById(R.id.tvPlaceAbout)
		val iconDistance: ImageView = root.findViewById(R.id.ivDist)
	}
}
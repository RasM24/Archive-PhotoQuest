package endroad.photoquest.places;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import endroad.photoquest.R;
import endroad.photoquest.model.Place;

public class AdapterListPlaces extends BaseAdapter {
	private LayoutInflater lInflater;
	private List<Place> data;
	private ListPlaces lp;

	AdapterListPlaces(ListPlaces lp_, List<Place> data_) {
		Context ctx = lp_.getBaseContext();
		lp = lp_;
		data = data_;
		lInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Place getItem(int position) {
		return data.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.item, parent, false);
		}

		double distance = Math.sqrt(Math.pow(lp.x - getItem(position).getPosX(), 2) + Math.pow(lp.y - getItem(position).getPosY(), 2));

		if (getItem(position).opened)
			view.setBackgroundColor(Color.argb(200, 180, 180, 180));
		else
			view.setBackgroundColor(Color.argb(200, 255, 255, 255));

		((ImageView) view.findViewById(R.id.ivDist))
				.setImageResource(getItem(position).getIdRes(distance));


		((TextView) view.findViewById(R.id.tvPlaceAbout))
				.setText(getItem(position).nameDiff());

		((ImageView) view.findViewById(R.id.ivPlace))
				.setImageResource(getItem(position).getArea());
		((TextView) view.findViewById(R.id.tvPlace))
				.setText(getItem(position).name());

		return view;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
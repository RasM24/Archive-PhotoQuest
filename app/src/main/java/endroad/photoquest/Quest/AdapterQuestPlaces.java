package endroad.photoquest.Quest;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import endroad.photoquest.R;

/**
 * Created by OleG on 18.01.2015.
 */
public class AdapterQuestPlaces extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Quest> data;
    ListQuest lp;

    AdapterQuestPlaces(ListQuest lp_, ArrayList<Quest> data_) {
        ctx = lp_.getBaseContext();
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
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        if (data.get(position).nowTaskId == -1) {
            view.setBackgroundColor(Color.argb(127, 180, 180, 180));
            ((ImageView) view.findViewById(R.id.ivDist))
                    .setImageResource(R.drawable.dist_1);
        } else {
            view.setBackgroundColor(Color.argb(127, 255, 255, 255));

            ((ImageView) view.findViewById(R.id.ivDist))
                    .setImageResource(R.drawable.dist_0);
        }

        ((TextView) view.findViewById(R.id.tvPlace))
                .setText(data.get(position).questname);

        ((TextView) view.findViewById(R.id.tvPlaceAbout))
                .setText(data.get(position).about);


        return view;
    }
}

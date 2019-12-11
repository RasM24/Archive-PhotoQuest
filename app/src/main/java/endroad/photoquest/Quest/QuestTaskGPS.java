package endroad.photoquest.Quest;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import endroad.photoquest.Data;
import endroad.photoquest.R;

/**
 * Created by OleG on 03.03.2015.
 */
public class QuestTaskGPS extends AppCompatActivity implements View.OnClickListener {

	TextView textView;
	Button bt_reply;
	ImageView img;
	Quest quest;
	LocationManager locM;
	LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			double x = location.getLatitude();
			double y = location.getLongitude();
			double distance = Math.hypot((x - quest.getTask().reply.GPSX), (y - quest.getTask().reply.GPSY));
			if (distance < 0.0003) //если выполняется это условие, то вы открыли точку
			{
				Intent intent = new Intent();
				intent.putExtra("finish", true);
				setResult(RESULT_OK, intent);
				finish();
			}

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quest_task_gps);
		quest = Data.quest.get(getIntent().getIntExtra("id", -1));
		textView = findViewById(R.id.tv_quest_gps);
		bt_reply = findViewById(R.id.bt_reply);
		img = findViewById(R.id.img_quest_gps);
		textView.setText(quest.textTask());
		img.setImageDrawable(quest.drawableSec());
		bt_reply.setOnClickListener(this);
		locM = (LocationManager) getSystemService(LOCATION_SERVICE);
		//TODO добавить запрос разрешения на геолокацию
		locM.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
				1000, 0, locationListener);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.putExtra("finish", true);
		setResult(RESULT_OK, intent);
		finish();
	}
}

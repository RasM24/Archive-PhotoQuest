package endroad.photoquest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import endroad.photoquest.Places.ListPlaces;
import endroad.photoquest.Quest.ListQuest;
import endroad.photoquest.Quest.Quest;
import endroad.photoquest.data.PlaceDataSource;

public class MainActivity extends AppCompatActivity implements OnClickListener {

	Button bt_place;
	Button bt_achiv;
	Button bt_quest;
	Button bt_setting;
	Button bt_exit;

	AnimationDrawable animation;
	ImageView img_anim;
	LocationManager locM;

	PlaceDataSource placeDataSource;

	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {

		}

		@Override
		public void onStatusChanged(String s, int i, Bundle bundle) {

		}

		@Override
		public void onProviderEnabled(String s) {

		}

		@Override
		public void onProviderDisabled(String s) {

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bt_place = findViewById(R.id.bt_place);
		bt_achiv = findViewById(R.id.bt_achiv);
		bt_quest = findViewById(R.id.bt_quest);
		bt_setting = findViewById(R.id.bt_setting);
		bt_exit = findViewById(R.id.bt_exit);

		bt_place.setOnClickListener(this);
		bt_achiv.setOnClickListener(this);
		bt_quest.setOnClickListener(this);
		bt_setting.setOnClickListener(this);
		bt_exit.setOnClickListener(this);

		img_anim = findViewById(R.id.img_animation);
		//img_anim.setBackgroundResource(R.drawable.fontan);
		animation = (AnimationDrawable) img_anim.getBackground();
		//animation.setOneShot(false);
		animation.start();

		locM = (LocationManager) getSystemService(LOCATION_SERVICE);
		//TODO добавить запрос разрешения на геолокацию
		locM.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 0, locationListener);
		loadData();
	}

	private void loadData() {
		placeDataSource = new PlaceDataSource(this);

		Data.quest.clear();
		Data.quest.add(new Quest(getBaseContext(), "quest/q1/"));
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.bt_place:
				intent = new Intent(this, ListPlaces.class);
				break;

			case R.id.bt_achiv:
				//intent = new Intent(this, GooglePlayServicesActivity.class);

				break;
			// test
			case R.id.bt_quest:
				intent = new Intent(this, ListQuest.class);
				break;

			// intent = new Intent(this, LookingForActivity.class);

			//TODO на кнопку "настройки" было забинден сброс открытых точек. Пересмотреть этот подход
			case R.id.bt_setting:
				SharedPreferences sPref = getSharedPreferences("Places", MODE_PRIVATE);
				SharedPreferences.Editor ed = sPref.edit();

				for (int i = 0; i < placeDataSource.getList().size(); i++) {
					ed.putBoolean(placeDataSource.getList().get(i).openName, false);
				}
				ed.apply();

				break;
			case R.id.bt_exit:
				finish();
				break;
		}

		if (intent != null)
			startActivity(intent);
	}

}

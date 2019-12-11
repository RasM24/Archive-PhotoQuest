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
import endroad.photoquest.Places.dataPlaces;
import endroad.photoquest.Quest.ListQuest;
import endroad.photoquest.Quest.Quest;

public class MainActivity extends AppCompatActivity implements OnClickListener {

	Button bt_place;
	Button bt_achiv;
	Button bt_quest;
	Button bt_setting;
	Button bt_exit;

	AnimationDrawable animation;
	ImageView img_anim;
	LocationManager locM;
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
		SharedPreferences sPref;
		sPref = getSharedPreferences("Places", MODE_PRIVATE);
		Data.place.clear();

		Data.place.add(new dataPlaces("Дворы 1", "Двор в центре", Data.AREA_CENTR, Data.POINT_DIFF4, 56.01031335f, 92.87928364f, "point/c1/"));
		Data.place.add(new dataPlaces("Площадь 1", "Суд вроде)", Data.AREA_CENTR, Data.POINT_DIFF2, 56.01207307f, 92.88627617f, "point/c2/"));
		Data.place.add(new dataPlaces("Места 1", "КИЦ", Data.AREA_CENTR, Data.POINT_DIFF1, 56.010752f, 92.894623f, "point/c3/"));
		Data.place.add(new dataPlaces("Места 2", "Почти KFC", Data.AREA_CENTR, Data.POINT_DIFF2, 56.01115317f, 92.87620894f, "point/c4/"));
		Data.place.add(new dataPlaces("Дворы 2", "Дом", Data.AREA_CENTR, Data.POINT_DIFF4, 56.0148301f, 92.88149703f, "point/c5/"));
		Data.place.add(new dataPlaces("Дворы 3", "Дом", Data.AREA_CENTR, Data.POINT_DIFF4, 56.00965500f, 92.88623530f, "point/c6/"));
		Data.place.add(new dataPlaces("Места 3", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 55.99456135f, 92.87342037f, "point/c7/"));

		Data.place.add(new dataPlaces("Места 6", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.014805f, 92.866301f, "point/c8/"));
		Data.place.add(new dataPlaces("Дворы 4", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.01516280f, 92.85833529f, "point/c9/"));
		Data.place.add(new dataPlaces("Дворы 5", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.01265808f, 92.855563736f, "point/c10/"));
		Data.place.add(new dataPlaces("Места 4", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.01175705f, 92.856535f, "point/c11/"));
		Data.place.add(new dataPlaces("Площадь 2", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.01137021f, 92.8547404f, "point/c12/"));
		Data.place.add(new dataPlaces("Места 5", "Дворец им. Ярыгина", Data.AREA_CENTR, Data.POINT_DIFF3, 56.01154f, 92.861269f, "point/c13/"));

		for (int i = 0; i < Data.place.size(); i++)
			Data.place.get(i).opened = sPref.getBoolean(Data.place.get(i).openName, false);


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

			case R.id.bt_setting:
				SharedPreferences sPref = getSharedPreferences("Places", MODE_PRIVATE);
				SharedPreferences.Editor ed = sPref.edit();
				for (int i = 0; i < Data.place.size(); i++) {
					ed.putBoolean(Data.place.get(i).openName, false);
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

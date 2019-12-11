package endroad.photoquest.Places.panoram;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;
import endroad.photoquest.Places.DataPlaces;
import endroad.photoquest.R;
import endroad.photoquest.data.PlaceDataSource;


public class PointActivityGLES extends AppCompatActivity implements View.OnClickListener {

	PlaceDataSource placeDataSource;
	DataPlaces point;
	LocationManager gps;
	private SurfaceView mGLSurfaceView;
	private ImageButton bt_fullscreen;
	private ImageButton bt_map;
	private ImageButton bt_open_catch;
	private ImageButton img_map;
	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			double x = location.getLatitude();
			double y = location.getLongitude();
			double distance = Math.hypot((x - point.posX), (y - point.posY));
			if ((distance < 0.00021) && (!point.opened)) //если выполняется это условие, то вы открыли точку
			{
				OpenPoint();
			} else
				bt_map.setImageResource(point.getIdRes(distance));
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
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		} else
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


		setContentView(R.layout.activity_point1);
		mGLSurfaceView = findViewById(R.id.gl_surface_view);
		mGLSurfaceView.setEGLContextClientVersion(2);
		Render mRender = new Render(this);
		mGLSurfaceView.setRenderer(mRender);

		try {
			placeDataSource = new PlaceDataSource(this);
			point = placeDataSource.getList().get(getIntent().getIntExtra("id", -1));
			mRender.path = point.pathTexture;
		} catch (Exception e) {
			Toast t = Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
			t.show();
			finish();
		}
		bt_fullscreen = findViewById(R.id.bt_point_fullscreen);
		bt_fullscreen.setOnClickListener(this);

		bt_map = findViewById(R.id.bt_map_img);
		bt_map.setOnClickListener(this);

		img_map = findViewById(R.id.map_img);
		img_map.setOnClickListener(this);
		img_map.setVisibility(View.INVISIBLE);
		try {
			InputStream ims = getAssets().open(point.pathTexture + "map.jpg");
			Drawable d = Drawable.createFromStream(ims, null);
			img_map.setImageDrawable(d);
		} catch (IOException ex) {
			return;
		}

		bt_open_catch = findViewById(R.id.bt_open_img);
		bt_open_catch.setVisibility(View.INVISIBLE);
		bt_open_catch.setBackgroundColor(Color.TRANSPARENT);
		bt_open_catch.setImageResource(R.drawable.test);
		bt_open_catch.setOnClickListener(this);

		gps = (LocationManager) getSystemService(LOCATION_SERVICE);
		//TODO добавить запрос разрешения на геолокацию
		gps.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
				1000, 0, locationListener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			bt_fullscreen.setImageResource(R.drawable.full);
		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			bt_fullscreen.setImageResource(R.drawable.nofull);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
		bt_map.setImageResource(R.drawable.dist_0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLSurfaceView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		gps.removeUpdates(locationListener);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.bt_point_fullscreen:
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;
			case R.id.bt_map_img:
				img_map.setVisibility(View.VISIBLE);
				OpenPoint();
				break;
			case R.id.map_img:
				img_map.setVisibility(View.INVISIBLE);
				break;
			case R.id.bt_open_img:
				bt_open_catch.setVisibility(View.INVISIBLE);
				finish();
				break;
		}
	}

	void OpenPoint() {
		point.opened = true;
		SharedPreferences sPref = getSharedPreferences("Places", MODE_PRIVATE);
		SharedPreferences.Editor ed = sPref.edit();
		ed.putBoolean(point.openName, true);
		ed.apply();

		Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
		bt_open_catch.startAnimation(anim);
		bt_open_catch.setVisibility(View.VISIBLE);
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
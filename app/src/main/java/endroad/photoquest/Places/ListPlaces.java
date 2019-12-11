package endroad.photoquest.Places;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import endroad.photoquest.Places.panoram.PointActivityGLES;
import endroad.photoquest.R;
import endroad.photoquest.data.PlaceDataSource;

public class ListPlaces extends AppCompatActivity {
	PlaceDataSource placeDataSource;
	AdapterListPlaces adapter;
	ListView lvMain;
	LocationManager locM;
	double x, y;
	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			x = location.getLatitude();
			y = location.getLongitude();
			adapter.notifyDataSetChanged();
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

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listplaces);
		locM = (LocationManager) getSystemService(LOCATION_SERVICE);
		//TODO добавить запрос разрешения на геолокацию
		locM.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
				1000, 0, locationListener);
		placeDataSource = new PlaceDataSource(this);

		adapter = new AdapterListPlaces(this, placeDataSource.getList());
		lvMain = findViewById(R.id.lvMain);
		lvMain.setAdapter(adapter);
		lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Intent intent = new Intent(getApplication().getBaseContext(), PointActivityGLES.class);
				intent.putExtra("id", position);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}
}

package endroad.photoquest.Places;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import endroad.photoquest.Data;
import endroad.photoquest.MapsActivity;
import endroad.photoquest.Places.panoram.PointActivityGLES;
import endroad.photoquest.R;

public class ListPlaces extends AppCompatActivity {
    AdapterListPlaces adapter;
    ListView lvMain;
    LocationManager locM;
    Button bt_map;
    double x, y;
    public static ArrayList<dataPlaces> place = new ArrayList<dataPlaces>();

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listplaces);
        locM = (LocationManager) getSystemService(LOCATION_SERVICE);
        locM.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
                1000, 0, locationListener);
        place = Data.place;
        bt_map= (Button)findViewById(R.id.bt_showopen);
        bt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
        adapter = new AdapterListPlaces(this, place);
        lvMain = (ListView) findViewById(R.id.lvMain);
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
}

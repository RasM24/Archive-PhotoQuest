package endroad.photoquest.Quest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import endroad.photoquest.R;
import endroad.photoquest.data.QuestDataSource;

/**
 * Created by OleG on 18.01.2015.
 */
public class ListQuest extends AppCompatActivity {
	AdapterQuestPlaces adapter;
	ListView lvMain;

	QuestDataSource questDataSource;

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_list_fragment);

		questDataSource = new QuestDataSource(this);

		adapter = new AdapterQuestPlaces(this, questDataSource.getList());
		lvMain = findViewById(R.id.list);
		lvMain.setAdapter(adapter);
		lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Intent intent = new Intent(getApplication().getBaseContext(), QuestActivity.class);
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

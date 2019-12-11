package endroad.photoquest.Quest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import endroad.photoquest.Data;
import endroad.photoquest.R;

/**
 * Created by OleG on 18.01.2015.
 */
public class ListQuest extends AppCompatActivity {
	public static ArrayList<Quest> quest = new ArrayList<>();
	AdapterQuestPlaces adapter;
	ListView lvMain;

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listplaces);

		quest = Data.quest;

		adapter = new AdapterQuestPlaces(this, quest);
		lvMain = findViewById(R.id.lvMain);
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

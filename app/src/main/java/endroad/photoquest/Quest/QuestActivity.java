package endroad.photoquest.Quest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import endroad.photoquest.Data;
import endroad.photoquest.R;

/**
 * Created by OleG on 11.01.2015.
 */
public class QuestActivity extends Activity implements View.OnClickListener {


    TextView textView;
    ImageView imgView;
    ScrollView myScroll;
    Button bt_next;
    Quest quest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        quest = Data.quest.get(getIntent().getIntExtra("id", -1));

        myScroll = (ScrollView) findViewById(R.id.scrollView);

        textView = (TextView) findViewById(R.id.tv_quest);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(quest.text());

        imgView = (ImageView) findViewById(R.id.img_quest);
        imgView.setImageDrawable(quest.drawable());

        bt_next = (Button) findViewById(R.id.bt_next_task);
        if (quest.complete())
            bt_next.setText("Завершить");
        else if (quest.nowTaskId == -1)
            bt_next.setText("Заново");
        bt_next.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (quest.complete()) {
            quest.finish();
            textView.setText(quest.text());
            imgView.setImageDrawable(quest.drawable());
            bt_next.setText("Заново");
            myScroll.scrollTo(0, 0);

        } else if (quest.nowTaskId == -1) {
            quest.re();
            textView.setText(quest.text());
            imgView.setImageDrawable(quest.drawable());
            bt_next.setText("Задание");
            myScroll.scrollTo(0, 0);

        } else {
            Intent intent = new Intent(this, QuestTaskGPS.class);
            intent.putExtra("id", getIntent().getIntExtra("id", -1));
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (data.getBooleanExtra("finish", false))
            taskComplete();
    }

    void taskComplete() {
        quest.nextTask();
        textView.setText(quest.text());
        imgView.setImageDrawable(quest.drawable());
        if (quest.complete())
            bt_next.setText("Завершить");
        myScroll.scrollTo(0, 0);
    }
}

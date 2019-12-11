package endroad.photoquest.Quest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by OleG on 11.01.2015.
 */
public class Quest {

    private static ArrayList<Task> task = new ArrayList<>();
    int nowTaskId;

    boolean complete() {
        return nowTaskId + 1 == task.size();
    }

    private Context context;
    String questname;
    String about;
    private String path;

    Task getTask() {
        return task.get(nowTaskId);
    }

    public Quest(Context context_, String path_) {
        context = context_;
        path = path_;
        SharedPreferences sPref = context.getSharedPreferences("quest", Context.MODE_PRIVATE);
        nowTaskId = sPref.getInt(path, 0);
        try {
            AssetManager ass = context.getAssets(); // :D
            InputStreamReader is = new InputStreamReader(ass.open(path
                    + "header"));
            BufferedReader in = new BufferedReader(is);
            questname = in.readLine();
            about = in.readLine();
            while (in.readLine() != null) {
                Task temp = new Task();
                temp.pathText = in.readLine();
                temp.pathImageMain = in.readLine();
                temp.pathTask = in.readLine();
                temp.pathImageSec = in.readLine();
                temp.reply = new Reply(in.readLine());
                task.add(temp);
            }
            in.close();
        } catch (FileNotFoundException e) {
            // FileNotFoundExcpeption
        } catch (IOException e) {
            // IOException
        }
    }

    void nextTask() {
        SharedPreferences sPref = context.getSharedPreferences("quest", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        nowTaskId++;
        editor.putInt(path, nowTaskId);
        editor.apply();
    }

    void finish() {
        SharedPreferences sPref = context.getSharedPreferences("quest", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        nowTaskId = -1;
        editor.putInt(path, nowTaskId);
        editor.apply();
    }

    String text() {
        InputStream is;
        if (nowTaskId != -1) {
            try {
                is = context.getAssets().open(path + task.get(nowTaskId).pathText);
                String s = convertStreamToString(is);
                is.close();
                return s;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                is = context.getAssets().open(path + "final");
                String s = convertStreamToString(is);
                is.close();
                return s;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    String textTask() {
        InputStream is;
        try {
            is = context.getAssets().open(path + task.get(nowTaskId).pathTask);
            String s = convertStreamToString(is);
            is.close();
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String convertStreamToString(InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }

    Drawable drawable() {
        if (nowTaskId != -1) {
            try {
                InputStream is;
                is = context.getAssets().open(path + task.get(nowTaskId).pathImageMain);
                return Drawable.createFromStream(is, null);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                InputStream is;
                is = context.getAssets().open(path + "final");
                return Drawable.createFromStream(is, null);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    Drawable drawableSec() {
        try {
            InputStream is;
            is = context.getAssets().open(path + task.get(nowTaskId).pathImageSec);
            return Drawable.createFromStream(is, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void re() {

        SharedPreferences sPref = context.getSharedPreferences("quest", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        nowTaskId = 0;
        editor.putInt(path, nowTaskId);
        editor.apply();
    }


    class Reply {
        static final int COMPLETE = 0;
        static final int GPS = 1;
        static final int TEXT = 2;
        short type;
        double GPSX, GPSY;
        String text;

        Reply(String aa) {
            String[] temp = aa.split(":");
            if (temp[0].equals("Complete")) {
                type = COMPLETE;
                text = "Complete";
                //Записать очки
            } else if (temp[0].equals("gps")) {
                type = GPS;
                GPSX = Double.valueOf(temp[1]);
                GPSY = Double.valueOf(temp[2]);
            }
        }
    }

    class Task {
        String pathImageMain;
        String pathText;
        String pathImageSec;
        String pathTask;
        Reply reply;

    }
}

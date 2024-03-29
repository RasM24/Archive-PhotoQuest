package endroad.photoquest.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by OleG on 11.01.2015.
 */
//TODO разобраться с классом, перед тем как конвертировать на котлин
public class Quest {

	private static ArrayList<Task> task = new ArrayList<>();
	public int nowTaskId;
	public String questname;
	public String about;
	private Context context;
	private String path;
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

	private static String convertStreamToString(InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}

	public boolean complete() {
		return nowTaskId + 1 == task.size();
	}

	public Task getTask() {
		return task.get(nowTaskId);
	}

	public void nextTask() {
		SharedPreferences sPref = context.getSharedPreferences("quest", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sPref.edit();
		nowTaskId++;
		editor.putInt(path, nowTaskId);
		editor.apply();
	}

	public void finish() {
		SharedPreferences sPref = context.getSharedPreferences("quest", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sPref.edit();
		nowTaskId = -1;
		editor.putInt(path, nowTaskId);
		editor.apply();
	}

	public String text() {
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

	public String textTask() {
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

	public Drawable drawable() {
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

	public Drawable drawableSec() {
		try {
			InputStream is;
			is = context.getAssets().open(path + task.get(nowTaskId).pathImageSec);
			return Drawable.createFromStream(is, null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void re() {

		SharedPreferences sPref = context.getSharedPreferences("quest", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sPref.edit();
		nowTaskId = 0;
		editor.putInt(path, nowTaskId);
		editor.apply();
	}


	public class Reply {
		static final int COMPLETE = 0;
		static final int GPS = 1;
		static final int TEXT = 2;
		short type;
		public double GPSX, GPSY;
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

	public class Task {
		String pathImageMain;
		String pathText;
		String pathImageSec;
		String pathTask;
		public Reply reply;

	}
}

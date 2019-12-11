package endroad.photoquest;

import java.util.ArrayList;

import endroad.photoquest.Places.dataPlaces;
import endroad.photoquest.Quest.Quest;

/**
 * Created by OleG on 20.11.2014.
 * p.s. я аккуратно =)
 */
public class Data {

	public final static int AREA_CENTR = R.drawable.met_centralniy;
	public final static int AREA_KIROV = R.drawable.met_kirovskiy;
	public final static int AREA_LENIN = R.drawable.met_leninskiy;
	public final static int AREA_OKTYB = R.drawable.met_oktyabrskiy;
	public final static int AREA_SOVET = R.drawable.met_sovietskiy;
	public final static int AREA_SVERD = R.drawable.met_sverdlovskiy;
	public final static int AREA_ZHELZ = R.drawable.met_zheleznodorozhniy;
	public final static int AREA_CENTR_O = R.drawable.meto_centralniy;
	public final static int AREA_KIROV_O = R.drawable.meto_kirovskiy;
	public final static int AREA_LENIN_O = R.drawable.meto_leninskiy;
	public final static int AREA_OKTYB_O = R.drawable.meto_oktyabrskiy;
	public final static int AREA_SOVET_O = R.drawable.meto_sovietskiy;
	public final static int AREA_SVERD_O = R.drawable.meto_sverdlovskiy;
	public final static int AREA_ZHELZ_O = R.drawable.meto_zheleznodorozhniy;

	public final static int POINT_DIFF1 = 100;
	public final static int POINT_DIFF2 = 200;
	public final static int POINT_DIFF3 = 400;
	public final static int POINT_DIFF4 = 800;

	public final static String PLACE_DIFF1 = "ИЗИ";
	public final static String PLACE_DIFF2 = "Норм так";
	public final static String PLACE_DIFF3 = "Попотей";
	public final static String PLACE_DIFF4 = "Хер найдешь";

	public static ArrayList<dataPlaces> place = new ArrayList<>();
	public static ArrayList<Quest> quest = new ArrayList<>();
}

package endroad.photoquest.Places;

import endroad.photoquest.Data;
import endroad.photoquest.R;

public class dataPlaces {

    public dataPlaces(String name_, String opName_, int area_, int point_, float posX_, float posY_, String pathTexture_) {
        hiddenName = name_;
        openName = opName_;
        area = area_;
        posX = posX_;
        posY = posY_;
        point = point_;
        pathTexture = pathTexture_;
    }

    private String hiddenName;
    public String openName;
    //public int id;
    private int area;
    public String pathTexture;

    String name() {
        if (opened)
            return openName;
        else return hiddenName;
    }

    String nameDiff() {
        if (opened)
            return "открыта";
        else
            switch (point) {
                case Data.POINT_DIFF1:
                    return Data.PLACE_DIFF1;
                case Data.POINT_DIFF2:
                    return Data.PLACE_DIFF2;
                case Data.POINT_DIFF3:
                    return Data.PLACE_DIFF3;
                case Data.POINT_DIFF4:
                    return Data.PLACE_DIFF4;
                default:
                    return null;
            }
    }

    int getArea() {
        if (!opened)
            return area;
        else
            switch (area) {
                case Data.AREA_CENTR:
                    return Data.AREA_CENTR_O;
                case Data.AREA_KIROV:
                    return Data.AREA_KIROV_O;
                case Data.AREA_LENIN:
                    return Data.AREA_LENIN_O;
                case Data.AREA_OKTYB:
                    return Data.AREA_OKTYB_O;
                case Data.AREA_SOVET:
                    return Data.AREA_SOVET_O;
                case Data.AREA_SVERD:
                    return Data.AREA_SVERD_O;
                case Data.AREA_ZHELZ:
                    return Data.AREA_ZHELZ_O;

                default:
                    return 0;
            }
    }

    public int getIdRes(double dist) {
        if (opened)
            return R.drawable.dist_0;
        else if (dist < 0.00021f)
            return R.drawable.dist_1;
        else if (dist < 0.00033f)
            return R.drawable.dist_2;
        else if (dist < 0.0005f)
            return R.drawable.dist_3;
        else if (dist < 0.0008f)
            return R.drawable.dist_4;
        else
            return R.drawable.dist_5;
    }

    public double posX;
    public double posY;
    int point;

    public boolean opened;

}

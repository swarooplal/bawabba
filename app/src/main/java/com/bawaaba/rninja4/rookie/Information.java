package com.bawaaba.rninja4.rookie;

/**
 * Created by rninja4 on 8/29/17.
 */

public class Information {

    public int imageID;
    public String titile;

    public int getImageID() {
        return imageID;
    }

    public String getTitile() {
        return titile==null?"":titile;
    }
}

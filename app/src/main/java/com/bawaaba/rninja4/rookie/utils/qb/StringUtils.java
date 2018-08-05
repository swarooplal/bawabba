package com.bawaaba.rninja4.rookie.utils.qb;

/**
 * Created by rninja4 on 12/11/17.
 */

public class StringUtils {

    private static final String NULL_TEXT = "null";

    public static boolean textIsNull(String text){
        return NULL_TEXT.equals(text);
    }
}

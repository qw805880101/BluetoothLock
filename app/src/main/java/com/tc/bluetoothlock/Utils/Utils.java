package com.tc.bluetoothlock.Utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by tianchao on 2018/4/3.
 */

public class Utils {

    public static void log(String tag, String str) {
        Log.d(tag, str);
    }


    public static boolean isEmpty(String str) {
        boolean fal = false;
        if (str == null || str.equals("")) {
            fal = true;
        } else {
            fal = false;
        }
        return fal;
    }

}

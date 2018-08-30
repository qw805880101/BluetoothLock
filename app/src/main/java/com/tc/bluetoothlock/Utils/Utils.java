package com.tc.bluetoothlock.Utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.psylife.wrmvplibrary.utils.StringUtil;
import com.psylife.wrmvplibrary.utils.timeutils.DateUtil;
import com.tc.bluetoothlock.BuildConfig;
import com.tc.bluetoothlock.MyApplication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 获取请求头公共参数map
     *
     * @return
     */
    public static Map getHeaderData() {
        String timestamp = "" + (new Date().getTime());
        String certification = getCertification(timestamp);
        Map requestData = new HashMap();
        requestData.put("version", "1.0"); //接口版本号
        requestData.put("appVersion", BuildConfig.VERSION_NAME); //app版本号
        requestData.put("certType", "0"); //认证方式  0：就代表MD5加密  1：RSA加密
        requestData.put("certification", certification); //签名
        requestData.put("timestamp", timestamp); //时间戳
        requestData.put("plat", "android"); //平台
        requestData.put("isTest", "0"); //测试 99 跳过签名  0 需验证签名
        return requestData;
    }

    /**
     * 获取签名
     *
     * @return
     */
    public static String getCertification(String timestamp) {
        String certification = timestamp + MyApplication.apiKey;
        byte[] md5 = Md5Util.MD5(certification);
        certification = StringUtil.byteArrayToHexString(md5);
        return certification;
    }

}

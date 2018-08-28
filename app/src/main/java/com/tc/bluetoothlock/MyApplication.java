package com.tc.bluetoothlock;

import android.os.Build;
import android.os.StrictMode;

import com.psylife.wrmvplibrary.WRCoreApp;
import com.psylife.wrmvplibrary.utils.LogUtil;
import com.tencent.smtt.sdk.QbSdk;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * Created by admin on 2017/8/30.
 */

public class MyApplication extends WRCoreApp {

//    public static String URL = "http://c.99bicycle.com:20001/";
    public static String URL = "http://ehome.kira666.com:20001/EhomePro/";

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

//        //设置调试模式
//        JPushInterface.setDebugMode(true);
//
//        //初始化极光推送
//        JPushInterface.init(this);

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtil.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

        AutoSizeConfig.getInstance();

    }

    @Override
    public String setBaseUrl() {
        return URL;
    }
}

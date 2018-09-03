package com.tc.bluetoothlock.Utils.bluetoothUtils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tc.bluetoothlock.Utils.TestService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 蓝牙链接工具类
 */

public class BLEUtils {
    static String TAG = "BLEUtils";
    public static Activity activity;
    public static String mac;
    public static String key;
    public static String password = "000000";
    static BluetoothDevice device;
    public static final int RECONNECT_CODE = 9978;

    private static SearchBluetoothInterface mSearchBluetoothInterface;

    public static void setSearchBluetoothInterface(SearchBluetoothInterface mSearchBluetoothInterface) {
        BLEUtils.mSearchBluetoothInterface = mSearchBluetoothInterface;
    }

    //通过蓝牙开锁
    public static void openLockByBLE(Activity activity, String mac, String key, byte[] password) {
        BLEUtils.mac = mac;
        BLEUtils.activity = activity;
        BLEUtils.key = key;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        carBean.setSecretKey("23,41,50,64,45,5,78,38,25,29,6,30,15,16,7,77");
//        carBean.setPassword("34,73,75,72,25,6");
        Config.KEY = CMDUtils.hexStr2Key(key);
        Config.password = password;
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {
                if (device != null && mac.equals(device.getAddress())) {
                    //链接蓝牙
//                    Intent mBleService = new Intent(activity, TestService.class);
//                    mBleService.putExtra(TestService.BLUETOOTH_CONNECT_DEVICE, device);
//                    activity.startService(mBleService);
                    mSearchBluetoothInterface.searchSuccess(device);
                } else {
                    //开始搜索蓝牙
                    startLeScan(activity, mLeScanCallback);
                }
            } else {
                // 打开蓝牙
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableIntent, RECONNECT_CODE);
            }
        }
    }


    //蓝牙搜索监听
    private static BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            String address = device.getAddress();
            Log.d(TAG, address + " (device : " + device.getAddress() + ")");
            if (mac.equals(device.getAddress())) {
                timer.cancel(); //取消定时
                BLEUtils.device = device;
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                //链接蓝牙
                Intent mBleService = new Intent(activity, TestService.class);
                mBleService.putExtra(TestService.BLUETOOTH_CONNECT_DEVICE, device);
                activity.startService(mBleService);
                mSearchBluetoothInterface.searchSuccess(device);
            }
        }
    };

    static Timer timer;

    public static void startTime(int time) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mSearchBluetoothInterface.searchFinish(null);
            }
        }, time * 1000);//timer.schedule(执行的方法，延迟多久执行(ms))
    }

    //开始搜索蓝牙
    public static void startLeScan(Activity activity, BluetoothAdapter.LeScanCallback mLeScanCallback) {
        startTime(10); //设置蓝牙搜索时长
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            } else {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        } else {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }

    //开始搜索蓝牙
    public static boolean startLeScan() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.startDiscovery();
    }


    public static void stopLeScan() {

        if (timer != null) {
            timer.cancel(); //取消定时
        }

        if (mLeScanCallback != null) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    public static void stopLeScan(BluetoothAdapter.LeScanCallback mLeScanCallback) {
        if (mLeScanCallback != null) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }


}

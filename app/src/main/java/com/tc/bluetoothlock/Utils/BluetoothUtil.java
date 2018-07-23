package com.tc.bluetoothlock.Utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.psylife.wrmvplibrary.utils.LogUtil;

import java.io.IOException;
import java.util.UUID;

public class BluetoothUtil {

    private static BluetoothUtil mBluetoothUtil;
    private Context mContext;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothUtil(Context mContext) {
        this.mContext = mContext;
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
    }

    public static BluetoothUtil getBluetoothUtil(Context mContext) {
        if (mBluetoothUtil == null) {
            mBluetoothUtil = new BluetoothUtil(mContext);
        }
        return mBluetoothUtil;
    }

    /**
     * 开始搜索蓝牙
     */
    public boolean startSeachBlue() {
        return mBluetoothAdapter.startDiscovery();
    }

    /**
     * 根据蓝牙mac获取蓝牙设备
     *
     * @param bluetoothMac
     * @return
     */
    public BluetoothDevice getBluetoothDevice(String bluetoothMac) {

        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

        if (Utils.isEmpty(bluetoothMac) || bluetoothMac.length() < 6) {
            return null;
        }

        String mac = bluetoothMac.substring(0, 2) + ":";
        mac += bluetoothMac.substring(2, 4) + ":";
        mac += bluetoothMac.substring(4, 6) + ":";
        mac += bluetoothMac.substring(6, 8) + ":";
        mac += bluetoothMac.substring(8, 10) + ":";
        mac += bluetoothMac.substring(10, 12);

        LogUtil.d("mac:" + mac);
//        try {
//            mBluetoothAdapter.listenUsingRfcommWithServiceRecord("FEE7", UUID.fromString("FEE7"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return mBluetoothAdapter.getRemoteDevice(mac);
    }


}

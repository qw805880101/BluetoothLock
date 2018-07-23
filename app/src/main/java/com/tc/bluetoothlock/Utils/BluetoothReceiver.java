package com.tc.bluetoothlock.Utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.psylife.wrmvplibrary.utils.LogUtil;

public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // 获得已经搜索到的蓝牙设备
        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            LogUtil.d("~~~~~:" + device.getAddress());
            LogUtil.d("~~~~~:" + device.getUuids());
        } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
            LogUtil.d("蓝牙搜索完成");
        } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device.getBondState() == BluetoothDevice.BOND_BONDING) {
                LogUtil.d("正在配对" + device.getName());
            } else if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                LogUtil.d("蓝牙搜索完成配对完成" + device.getName());
            } else if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                LogUtil.d("取消配对" + device.getName());
            }
        }
    }
}
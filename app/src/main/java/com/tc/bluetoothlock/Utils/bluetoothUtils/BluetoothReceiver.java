package com.tc.bluetoothlock.Utils.bluetoothUtils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.psylife.wrmvplibrary.utils.LogUtil;
import com.tc.bluetoothlock.Utils.bluetoothUtils.SearchBluetoothInterface;

import java.util.ArrayList;
import java.util.List;

public class BluetoothReceiver extends BroadcastReceiver {

    private SearchBluetoothInterface mSearchBluetoothInterface; //蓝牙搜索回调接口

    private List<BluetoothDevice> mBluetoothDevices = new ArrayList<>();

    public BluetoothReceiver(SearchBluetoothInterface mSearchBluetoothInterface) {
        this.mSearchBluetoothInterface = mSearchBluetoothInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // 获得已经搜索到的蓝牙设备
        if (action.equals(BluetoothDevice.ACTION_FOUND)) { //搜索到蓝牙添加到列表中
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            mBluetoothDevices.add(device);
            mSearchBluetoothInterface.searchSuccess(device);
        } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) { //搜索蓝牙完成，回调接口
            LogUtil.d("蓝牙搜索完成");
            mSearchBluetoothInterface.searchFinish(mBluetoothDevices);
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
package com.tc.bluetoothlock.Utils.bluetoothUtils;

import android.bluetooth.BluetoothDevice;

import java.util.List;

public interface SearchBluetoothInterface {

    /**
     * 蓝牙搜索成功
     *
     * @param mBluetoothDevices
     */
    void searchSuccess(BluetoothDevice mBluetoothDevices);

    /**
     * 蓝牙搜索完成
     *
     * @param mBluetoothDevices 搜索到的蓝牙列表
     */
    void searchFinish(List<BluetoothDevice> mBluetoothDevices);

    /**
     * 获取蓝牙返回参数
     * @param cmdData  指令参数
     * @param data     源参数
     * @param respData 对应的数据
     */
    void getResponseData(String cmdData, byte[] data, String respData);
}

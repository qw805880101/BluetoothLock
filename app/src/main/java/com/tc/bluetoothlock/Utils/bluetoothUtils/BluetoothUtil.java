package com.tc.bluetoothlock.Utils.bluetoothUtils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.psylife.wrmvplibrary.utils.LogUtil;
import com.tc.bluetoothlock.Utils.Utils;

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
     * 开始连接蓝牙
     *
     * @param mBluetoothDevices
     */
    public void startConnectBluetooth(BluetoothDevice mBluetoothDevices) {
        /**
         * autoConnect: 是否直接连接到远程设备（false）或在远程设备可用时自动连接（true）
         */
        mBluetoothDevices.connectGatt(mContext, true, new BluetoothGattCallback() {
            /**
             * 由于远程设备更改PHY 而导致的回调触发。 (PHY 端口物理层)
             * @param gatt
             * @param txPhy
             * @param rxPhy
             * @param status
             */
            @Override
            public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyUpdate(gatt, txPhy, rxPhy, status);
            }

            /**
             * 读取
             * @param gatt
             * @param txPhy
             * @param rxPhy
             * @param status
             */
            @Override
            public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyRead(gatt, txPhy, rxPhy, status);
            }

            /**
             * 回调指示GATT客户端何时与远程GATT服务器连接/断开连接。
             * @param gatt
             * @param status 连接或断开操作的状态。如果操作成功。BluetoothGatt.GATT_SUCCESS
             * @param newState 返回新的连接状态
             */
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) { //已连接
                    LogUtil.d("已连接设备");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) { //已断开

                }

            }

            /**
             * 当远程设备的远程服务，特征和描述符列表已更新时，即已发现新服务时，将调用回调。
             * @param gatt
             * @param status
             */
            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                super.onReliableWriteCompleted(gatt, status);
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                super.onMtuChanged(gatt, mtu, status);
            }
        });
    }

}

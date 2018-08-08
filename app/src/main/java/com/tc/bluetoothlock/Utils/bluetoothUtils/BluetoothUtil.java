package com.tc.bluetoothlock.Utils.bluetoothUtils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.psylife.wrmvplibrary.utils.LogUtil;
import com.tc.bluetoothlock.Utils.AesUtil;
import com.tc.bluetoothlock.Utils.Utils;
import com.tc.bluetoothlock.activity.TestActivity;

import junit.framework.Test;

import java.util.List;
import java.util.UUID;

public class BluetoothUtil {

    UUID RBL_SERVICE = UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb");
    UUID RBL_DEVICE_RX_UUID = UUID.fromString("000036f6-0000-1000-8000-00805f9b34fb");
    UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    UUID RBL_DEVICE_TX_UUID = UUID.fromString("000036f5-0000-1000-8000-00805f9b34fb");
    UUID CCC = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private static BluetoothUtil mBluetoothUtil;
    private Context mContext;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattService mDeviceService;
    private BluetoothGattCharacteristic cmdRespondCharacter;
    private BluetoothGattCharacteristic cmdWriteCharacter;

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
        mBluetoothGatt = mBluetoothDevices.connectGatt(mContext, false, new BluetoothGattCallback() {
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
                    //连接成功后去发现该连接的设备的服务
                    gatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) { //已断开
                    LogUtil.d("已断开设备");
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
                LogUtil.d("===搜到设备服务===");
                if (status == BluetoothGatt.GATT_SUCCESS) {//发现该设备的服务
//                //拿到该服务 1,通过UUID拿到指定的服务  2,可以拿到该设备上所有服务的集合
//                List<BluetoothGattService> serviceList = mBluetoothGatt.getServices();
//
//                //可以遍历获得该设备上的服务集合，通过服务可以拿到该服务的UUID，和该服务里的所有属性Characteristic
//                for (int x = 0; x < serviceList.size(); x++) {
//                    Log.d(TAG, serviceList.get(x).getUuid().toString());
//                }
                    LogUtil.d("===找到设备服务，开始链接===");
                    mDeviceService = gatt.getService(RBL_SERVICE);
                    if (mDeviceService != null) {
                        LogUtil.d("====设备链接成功===");
//                        sendStatusChange(CONNECT_STATE_SERVICE_CONNECTED);
                    } else {
                        LogUtil.d("===设备链接失败，结束操作===");
//                        sendStatusChange(CONNECT_STATE_SERVICE_DISCONNECTED);
                        return;
                    }
                    LogUtil.d("===获取命令响应和写入Character===");
                    //通过UUID拿到设备里的Characteristic

//                // 拿到该服务 1,通过UUID拿到指定的character  2,可以拿到该设备上所有服务的集合
//                List<BluetoothGattCharacteristic> characterList = mDeviceService.getCharacteristics();
//
//                //可以遍历获得该设备上的服务集合，通过服务可以拿到该服务的UUID，和该服务里的所有属性Characteristic
//                for (int x = 0; x < characterList.size(); x++) {
//                    Log.d(TAG, characterList.get(x).getUuid().toString());
//                }
//                    sendStatusChange(CONNECT_STATE_WRITE_CONNECTING);
                    cmdWriteCharacter = mDeviceService.getCharacteristic(RBL_DEVICE_TX_UUID);

                    if (cmdWriteCharacter == null) {
//                        Logger.show(TAG, "===获取写入Character失败，程序结束===");
//                        sendStatusChange(CONNECT_STATE_WRITE_DISCONNECTED);
                        LogUtil.d("===获取写入Character失败===");
                        return;
                    } else {
//                        Logger.show(TAG, "===获取写入Character成功===");
                        LogUtil.d("===获取写入Character成功===");
                    }
//                    sendStatusChange(CONNECT_STATE_RESPOND_CONNECTING);
                    cmdRespondCharacter = mDeviceService.getCharacteristic(RBL_DEVICE_RX_UUID);
                    if (cmdRespondCharacter == null) {
//                        sendStatusChange(CONNECT_STATE_RESPOND_DISCONNECTED);
//                        Logger.show(TAG, "===获取响应Character失败，程序结束===");
                        LogUtil.d("===获取响应Character失败===");
                        return;
                    } else {
//                        Logger.show(TAG, "===获取响应Character成功===");
                        LogUtil.d("===获取响应Character成功===");
                        enableNotification(true, gatt, cmdRespondCharacter);
                    }

                } else {//未发现该设备的服务
//                    Logger.show(TAG, "===未发现设备服务===");
                }
            }

            private void enableNotification(boolean enable, BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                if (gatt == null || characteristic == null)
                    return;

                //这一步必须要有 否则收不到通知
                gatt.setCharacteristicNotification(characteristic, enable);
                BluetoothGattDescriptor clientConfig = characteristic.getDescriptor(CCC);
                if (clientConfig != null) {
                    if (enable) {
                        clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    } else {
                        clientConfig.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                    }
                    gatt.writeDescriptor(clientConfig);
                }
                mContext.startActivity(new Intent(mContext, TestActivity.class));
//            try {
//                Thread.sleep(300);
//                sendStatusChange(CONNECT_STATE_SUCCESS);
//                nextWrite();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            }

            private BluetoothGattCharacteristic findNotifyCharacteristic(BluetoothGattService service, UUID characteristicUUID) {
                //此方法是在没有收到蓝牙的通知回调加的，最后才确定了是硬件那边本来就没给我回调信息，当然加入此方法也不会有问题
                BluetoothGattCharacteristic characteristic = null;
                List<BluetoothGattCharacteristic> characteristics = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    characteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic c : characteristics) {
                        if ((c.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0
                                && characteristicUUID.equals(c.getUuid())) {
                            characteristic = c;
                            break;
                        }
                    }
                    if (characteristic != null)
                        return characteristic;
                    for (BluetoothGattCharacteristic c : characteristics) {
                        if ((c.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0
                                && characteristicUUID.equals(c.getUuid())) {
                            characteristic = c;
                            break;
                        }
                    }
                }
                return characteristic;
            }

            //回调响应特征读操作的结果。
            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
            }

            //回调响应特征写操作的结果。
            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                if (status == BluetoothGatt.GATT_SUCCESS) {//写入成功
                    LogUtil.d("参数写入成功——");
                } else if (status == BluetoothGatt.GATT_FAILURE) {//写入失败
                    LogUtil.d("参数写入失败——");
                } else if (status == BluetoothGatt.GATT_WRITE_NOT_PERMITTED) {//没权限
                    LogUtil.d("参数写入没权限——");
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                //拿到characteristic的值
                byte[] data = characteristic.getValue();
                byte[] x = new byte[16];
                System.arraycopy(data, 0, x, 0, 16);
                byte mingwen[] = AesUtil.Decrypt(x, TestActivity.KEY);
                LogUtil.d("参数返回——" + new String(mingwen));
            }

            //当连接能被被读的操作
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

    public void doWrite(byte[] data) {
//        mWriteQueue.add(data);
//        Logger.show(TAG, "===添加到指令队列===");
//        cmdWriteCharacter.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);//设置为WRITE_TYPE_NO_RESPONSE，这样速度会快
        cmdWriteCharacter.setValue(data);
        if (mBluetoothGatt == null) {
//            Logger.show(TAG, "mBluetoothGatt == null");
            LogUtil.d("mBluetoothGatt未null");
            return;
        }
        mBluetoothGatt.writeCharacteristic(cmdWriteCharacter);

        LogUtil.d("===执行指令===");
    }

}

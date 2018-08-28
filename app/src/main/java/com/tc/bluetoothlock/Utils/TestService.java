package com.tc.bluetoothlock.Utils;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.psylife.wrmvplibrary.utils.LogUtil;
import com.tc.bluetoothlock.Utils.bluetoothUtils.CMDUtils;
import com.tc.bluetoothlock.Utils.bluetoothUtils.Config;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 连接硬件的蓝牙服务
 */
public class TestService extends Service {
    private final static String TAG = "TestService";


    UUID RBL_SERVICE = UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb");
    UUID RBL_DEVICE_RX_UUID = UUID.fromString("000036f6-0000-1000-8000-00805f9b34fb");
    UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    UUID RBL_DEVICE_TX_UUID = UUID.fromString("000036f5-0000-1000-8000-00805f9b34fb");
    UUID CCC = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private static final String BROADCAST_BASE = "com.nokelock.y";
    /**
     * 链接状态--状态改变广播
     */
    public static final String BROADCAST_CONNECT_STATE_CHANGE = BROADCAST_BASE + ".ble_connect_state_change";
    /**
     * 链接状态--广播
     */
    public static final String BROADCAST_CONNECT_STATE_VALUE = BROADCAST_BASE + ".ble_connect_state";
    /**
     * 命令接受广播
     */
    public static final String BROADCAST_CMD = BROADCAST_BASE + ".ble_cmd";
    /**
     * 命令接受广播-命令类型
     */
    public static final String BROADCAST_CMD_TYPE = BROADCAST_BASE + ".ble_cmd_type";
    /**
     * 命令接受广播-命令
     */
    public static final String BROADCAST_CMD_VALUE = BROADCAST_BASE + ".ble_cmd_value";

    /**
     * 响应广播--对应的命令
     */
    public static final String BROADCAST_DATA = BROADCAST_BASE + ".ble_data";
    /**
     * 响应广播--对应的命令
     */
    public static final String BROADCAST_DATA_CMD = BROADCAST_BASE + ".ble_data_cmd";
    /**
     * 响应广播---对应的数据
     */
    public static final String BROADCAST_DATA_VALUE = BROADCAST_BASE + ".ble_data_value";
    /**
     * 响应广播---对应的数据源
     */
    public static final String BROADCAST_DATA_BYTE = BROADCAST_BASE + ".ble_data_byte";

    /**
     * 广播类型 发送命令
     */
    public static final int CMD_TYPE_SEND_CMD = 0;
    /**
     * 广播类型 清空命令队列
     */
    public static final int CMD_TYPE_CLEAR_QUEUE = 1;


    /**
     * 当前的连接状态
     */
    public int CONNECT_STATE = -1;
    /**
     * 链接状态--断开
     */
    public static final int CONNECT_STATE_DISCONNECTED = 0;
    /**
     * 链接状态--GATT通道--连接中
     */
    public static final int CONNECT_STATE_GATT_CONNECTING = 1;
    /**
     * 链接状态--GATT通道--已连接
     */
    public static final int CONNECT_STATE_GATT_CONNECTED = 2;

    /**
     * 链接状态--设备服务--连接中
     */
    public static final int CONNECT_STATE_SERVICE_CONNECTING = 3;
    /**
     * 链接状态--设备服务--已连接
     */
    public static final int CONNECT_STATE_SERVICE_CONNECTED = 4;
    /**
     * 链接状态--设备服务--连接失败
     */
    public static final int CONNECT_STATE_SERVICE_DISCONNECTED = 5;
    /**
     * 链接状态--写入Character--连接中
     */
    public static final int CONNECT_STATE_WRITE_CONNECTING = 6;
    /**
     * 链接状态--写入Character--连接失败
     */
    public static final int CONNECT_STATE_WRITE_DISCONNECTED = 7;
    /**
     * 链接状态--响应Character--连接中
     */
    public static final int CONNECT_STATE_RESPOND_CONNECTING = 8;
    /**
     * 链接状态--响应Character--连接失败
     */
    public static final int CONNECT_STATE_RESPOND_DISCONNECTED = 9;
    /**
     * 链接状态--BLE所有连接--连接成功--等待输入指令
     */
    public static final int CONNECT_STATE_SUCCESS = 10;
    /**
     * 连接的蓝牙设备
     */
    public static final String BLUETOOTH_CONNECT_DEVICE = "CONNECT_DEVICE";

    BluetoothGatt mBluetoothGatt;
    BluetoothGattService mDeviceService;
    BluetoothGattCharacteristic cmdRespondCharacter;
    BluetoothGattCharacteristic cmdWriteCharacter;

    Queue<byte[]> mWriteQueue = new ConcurrentLinkedQueue();//写入指令队列
    BroadcastReceiver broadcast_cmd;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcast_cmd = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    int type = intent.getIntExtra(BROADCAST_CMD_TYPE, 0);
                    switch (type) {
                        case CMD_TYPE_CLEAR_QUEUE:
                            LogUtil.d("=======清空队列");
                            mWriteQueue = new ConcurrentLinkedQueue();
                            break;
                        case CMD_TYPE_SEND_CMD:
                        default:
                            byte[] cmd = intent.getByteArrayExtra(BROADCAST_CMD_VALUE);
                            if (cmd != null && mBluetoothGatt != null) {
                                doWrite(cmd);
                            }
                            break;
                    }
                }
            }
        };
        registerReceiver(broadcast_cmd, new IntentFilter(BROADCAST_CMD));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_STICKY;
        }
        LogUtil.d( "onStartCommand");
        if (mBluetoothGatt == null) {
            LogUtil.d( "mBluetoothGatt == null");
            BluetoothDevice device = intent.getParcelableExtra(BLUETOOTH_CONNECT_DEVICE);
            mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
            sendStatusChange(CONNECT_STATE_GATT_CONNECTING);
        }
        return START_STICKY;
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        /**
         * 蓝牙连接状态改变后调用 此回调 (断开，连接)
         * @param gatt
         * @param status
         * @param newState
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                LogUtil.d( "GATT通信链接成功");
                sendStatusChange(CONNECT_STATE_GATT_CONNECTED);
                //连接成功后去发现该连接的设备的服务
                mBluetoothGatt.discoverServices();
                sendStatusChange(CONNECT_STATE_SERVICE_CONNECTING);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {//连接失败 或者连接断开都会调用此方法
                LogUtil.d( "连接失败或者连接断开");
                sendStatusChange(CONNECT_STATE_DISCONNECTED);
                TestService.this.stopSelf();
            }

        }


        /**
         * 连接成功后发现设备服务后调用此方法
         * @param gatt
         * @param status
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            LogUtil.d( "===搜到设备服务===");
            if (status == BluetoothGatt.GATT_SUCCESS) {//发现该设备的服务
//                //拿到该服务 1,通过UUID拿到指定的服务  2,可以拿到该设备上所有服务的集合
//                List<BluetoothGattService> serviceList = mBluetoothGatt.getServices();
//
//                //可以遍历获得该设备上的服务集合，通过服务可以拿到该服务的UUID，和该服务里的所有属性Characteristic
//                for (int x = 0; x < serviceList.size(); x++) {
//                    Log.d(TAG, serviceList.get(x).getUuid().toString());
//                }
                LogUtil.d( "===找到设备服务，开始链接===");
                mDeviceService = mBluetoothGatt.getService(RBL_SERVICE);
                if (mDeviceService != null) {
                    LogUtil.d( "===设备链接成功===");
                    sendStatusChange(CONNECT_STATE_SERVICE_CONNECTED);
                } else {
                    LogUtil.d( "===设备链接失败，结束操作===");
                    sendStatusChange(CONNECT_STATE_SERVICE_DISCONNECTED);
                    return;
                }
                LogUtil.d( "===获取命令响应和写入Character===");
                //通过UUID拿到设备里的Characteristic

//                // 拿到该服务 1,通过UUID拿到指定的character  2,可以拿到该设备上所有服务的集合
//                List<BluetoothGattCharacteristic> characterList = mDeviceService.getCharacteristics();
//
//                //可以遍历获得该设备上的服务集合，通过服务可以拿到该服务的UUID，和该服务里的所有属性Characteristic
//                for (int x = 0; x < characterList.size(); x++) {
//                    Log.d(TAG, characterList.get(x).getUuid().toString());
//                }
                sendStatusChange(CONNECT_STATE_WRITE_CONNECTING);
                cmdWriteCharacter = mDeviceService.getCharacteristic(RBL_DEVICE_TX_UUID);

                if (cmdWriteCharacter == null) {
                    LogUtil.d( "===获取写入Character失败，程序结束===");
                    sendStatusChange(CONNECT_STATE_WRITE_DISCONNECTED);
                    return;
                } else {
                    LogUtil.d( "===获取写入Character成功===");
                }
                sendStatusChange(CONNECT_STATE_RESPOND_CONNECTING);
                cmdRespondCharacter = mDeviceService.getCharacteristic(RBL_DEVICE_RX_UUID);
                if (cmdRespondCharacter == null) {
                    sendStatusChange(CONNECT_STATE_RESPOND_DISCONNECTED);
                    LogUtil.d( "===获取响应Character失败，程序结束===");
                    return;
                } else {
                    LogUtil.d( "===获取响应Character成功===");
                    enableNotification(true, mBluetoothGatt, cmdRespondCharacter);
                }

            } else {//未发现该设备的服务
                LogUtil.d( "===未发现设备服务===");
            }
            super.onServicesDiscovered(gatt, status);
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
                mBluetoothGatt.writeDescriptor(clientConfig);
            }
            LogUtil.d( "=== enableNotification isWriting = true ===");

            sendStatusChange(CONNECT_STATE_SUCCESS);
//            try {
//                Thread.sleep(300);
//                sendStatusChange(CONNECT_STATE_SUCCESS);
//                nextWrite();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }

        /**
         * 写入完Descriptor之后调用此方法 应在此检查数据是否发送完毕
         *
         * @param gatt
         * @param descriptor
         * @param status
         */
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            LogUtil.d( "===配置指令写入完成===");
            sendStatusChange(CONNECT_STATE_SUCCESS);
//            nextWrite();
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            LogUtil.d( "===指令写入完成=== isWriting = false");
        }

        /**被订阅的Characteristic的值要是改变 调用此方法
         * 执行此方法的前提就是要被订阅
         * @param gatt
         * @param characteristic
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

            //拿到characteristic的值
            byte[] data = characteristic.getValue();
            byte[] x = new byte[16];
            System.arraycopy(data, 0, x, 0, 16);
            byte mingwen[] = CMDUtils.Decrypt(x, Config.KEY);
            sendResultData(mingwen);
        }
    };

    /**
     * 写入数据，方法是同步的
     */
    private synchronized void nextWrite() {


        if (mBluetoothGatt == null) {
            LogUtil.d( "异常 执行指令 mBluetoothGatt == null");
            TestService.this.stopSelf();
            return;
        }
        if (cmdWriteCharacter == null) {
            TestService.this.stopSelf();
            LogUtil.d( "异常 执行指令 cmdWriteCharacter == null");
            return;
        }

//        new Thread() {
//            byte[] data;
//
//            @Override
//            public void run() {
//                while (mBluetoothGatt != null) {
//                    if (!mWriteQueue.isEmpty()) {
//                        LogUtil.d( "===执行下一个指令===");
//                        data = mWriteQueue.poll();//从指令队列里获取下一个指令
//                        cmdWriteCharacter.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);//设置为WRITE_TYPE_NO_RESPONSE，这样速度会快
//                        cmdWriteCharacter.setValue(data);
//                        if (mBluetoothGatt == null) {
//                            TestService.this.stopSelf();
//                            return;
//                        }
//                        mBluetoothGatt.writeCharacteristic(cmdWriteCharacter);
//
//                        LogUtil.d( "===执行指令===");
//
//                    } else {
////                        LogUtil.d( "===指令队列 size 0 ===");
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }.start();

    }

    private void doWrite(byte[] data) {
//        mWriteQueue.add(data);
//        LogUtil.d( "===添加到指令队列===");
//        cmdWriteCharacter.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);//设置为WRITE_TYPE_NO_RESPONSE，这样速度会快
        cmdWriteCharacter.setValue(data);
        if (mBluetoothGatt == null) {
            TestService.this.stopSelf();
            LogUtil.d( "mBluetoothGatt == null");
            return;
        }
        mBluetoothGatt.writeCharacteristic(cmdWriteCharacter);

        LogUtil.d( "===执行指令===");

    }

    @Override
    public void onDestroy() {
        LogUtil.d( "BluetoothService onDestroy");
        super.onDestroy();
        if (broadcast_cmd != null) {
            unregisterReceiver(broadcast_cmd);
        }
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    /**
     * 蓝牙链接状态发生改变广播
     *
     * @param state
     */
    private void sendStatusChange(int state) {
        CONNECT_STATE = state;
        Intent intent = new Intent(TestService.BROADCAST_CONNECT_STATE_CHANGE);
        intent.putExtra(TestService.BROADCAST_CONNECT_STATE_VALUE, CONNECT_STATE);
        sendBroadcast(intent);
    }

    /**
     * 发送命令广播
     *
     * @param cmd
     */
    public static void sendCmd(Context context, byte[] cmd) {
        Intent intent = new Intent(TestService.BROADCAST_CMD);
        intent.putExtra(TestService.BROADCAST_CMD_TYPE, TestService.CMD_TYPE_SEND_CMD);
        intent.putExtra(TestService.BROADCAST_CMD_VALUE, cmd);
        context.sendBroadcast(intent);
    }

    /**
     * 清空命令队列广播
     */
    public static void clearCmdQueue(Context context) {
        Intent intent = new Intent(TestService.BROADCAST_CMD);
        intent.putExtra(TestService.BROADCAST_CMD_TYPE, TestService.CMD_TYPE_CLEAR_QUEUE);
        context.sendBroadcast(intent);
    }

    /**
     * 发送响应数据的广播
     */
    private void sendResultData(byte[] data) {

        String str_data = CMDUtils.toHexString(data);
        LogUtil.d( "===获取到响应数据=== " + str_data);

        if (str_data.substring(0, 4).equals("0602")){
            Config.TOKEN= new byte[]{data[3], data[4], data[5], data[6]};
        }

        String cmd = str_data.substring(0, 4);

        Intent intent = new Intent(BROADCAST_DATA);
        intent.putExtra(BROADCAST_DATA_CMD, cmd);
        intent.putExtra(BROADCAST_DATA_BYTE, data);
        intent.putExtra(BROADCAST_DATA_VALUE, str_data);
        sendBroadcast(intent);
    }


}

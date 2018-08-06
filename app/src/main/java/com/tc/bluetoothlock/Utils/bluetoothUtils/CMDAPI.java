package com.tc.bluetoothlock.Utils.bluetoothUtils;

import android.app.Activity;
import android.os.Handler;

import com.tc.bluetoothlock.mode.BatteryTxOrder;
import com.tc.bluetoothlock.mode.CardReadStatusTxOrder;
import com.tc.bluetoothlock.mode.DeleteFingerprintTxOrder;
import com.tc.bluetoothlock.mode.DeletePasswordTxOrder;
import com.tc.bluetoothlock.mode.GetLockStatusTxOrder;
import com.tc.bluetoothlock.mode.GetTokenTxOrder;
import com.tc.bluetoothlock.mode.OpenLockTxOrder;
import com.tc.bluetoothlock.mode.Order;
import com.tc.bluetoothlock.mode.PasswordTxOrder;
import com.tc.bluetoothlock.mode.QueryCardNumberTxOrder;
import com.tc.bluetoothlock.mode.QueryFingerprintTxOrder;
import com.tc.bluetoothlock.mode.RegisterFingerprintTxOrder;
import com.tc.bluetoothlock.mode.ResetDeviceTxOrder;
import com.tc.bluetoothlock.mode.ResetFingerprintTxOrder;
import com.tc.bluetoothlock.mode.SetKeyPasswordTxOrder;
import com.tc.bluetoothlock.mode.SetPasswordTxOrder;
import com.tc.bluetoothlock.mode.SetTemporaryPasswordTxOrder;
import com.tc.bluetoothlock.mode.SetTimeTxOrder;
import com.tc.bluetoothlock.mode.SetWifiName;
import com.tc.bluetoothlock.mode.SetWifiPassword;
import com.tc.bluetoothlock.mode.TxOrder;
import com.tc.bluetoothlock.mode.WriteCardModeTxOrder;
import com.tc.bluetoothlock.mode.WriteCardTxOrder;

public class CMDAPI {

    /**
     * 相应命令--获取电量成功
     */
    public static final String CMD_GET_POWER = "0202";
    /**
     * 相应命令--开锁成功
     */
    public static final String CMD_OPEN_LOCK = "0502";
    /**
     * 相应命令--修改主密码
     */
    public static final String CMD_CHANGE_PASSWORD = "0505";
    /**
     * 相应命令--关锁成功
     */
    public static final String CMD_CLOSE_LOCK = "0508";
    /**
     * 相应命令--获取锁状态
     */
    public static final String CMD_GET_LOCK_STATUS = "050F";
    /**
     * 相应命令--获取token
     */
    public static final String CMD_TOKEN = "0602";
    /**
     * 相应命令--同步时间
     */
    public static final String CMD_SET_TIME = "0604";
    /**
     * 相应命令--设置wifi名称
     */
    public static final String CMD_SET_WIFI_NAME = "1102";
    /**
     * 相应命令--设置wifi密码
     */
    public static final String CMD_SET_WIFI_PASSWORD = "1202";
    /**
     * 相应命令--WIFI链接状态
     */
    public static final String CMD_WIFI_STATUS = "1301";

    /**
     * 相应命令--— 添加密码
     */
    public static final String CMD_ADD_PASSWORD = "E102";
    /**
     * 相应命令--— 删除密码
     */
    public static final String CMD_DELETE_PASSWORD = "E202";

    /**
     * 相应命令--— 设置按键密码1-4
     */
    public static final String CMD_SET_KEY_PASSWORD = "E402";

    /**
     * 相应命令--— 恢复出厂设置
     */
    public static final String CMD_RESET_DEVICE = "E802";

    /**
     * 相应命令--— 注册指纹
     */
    public static final String CMD_REGISTER_FINGERPRINT = "F002";
    /**
     * 相应命令--— 注册指纹状态
     */
    public static final String CMD_REGISTER_FINGERPRINT_STATUS = "F003";
    /**
     * 相应命令--— 注册指纹成功门锁
     */
    public static final String CMD_REGISTER_FINGERPRINT_SUCCESS = "F004";
    /**
     * 相应命令--— 查询指纹数量
     */
    public static final String CMD_QUERY_FINGERPRINT = "F102";
    /**
     * 相应命令--— 删除指纹
     */
    public static final String CMD_DELETE_FINGERPRINT = "F104";
    /**
     * 相应命令--— 重置指纹
     */
    public static final String CMD_RESET_FINGERPRINT = "F402";
    /**
     * 相应命令--进入录卡模式
     */
    public static final String CMD_WRITE_CARD_MODE = "FC02";
    /**
     * 相应命令--— 卡 写入成功/失败
     */
    public static final String CMD_CARD_WRITE_NUMBER = "FC11";
    /**
     * 相应命令--进入(退出)读卡模式
     */
    public static final String CMD_READ_CARD_STATUS = "FC13";
    /**
     * 相应命令--读卡模式有卡靠近主动反馈卡号
     */
    public static final String CMD_READ_CARD_RESULT = "FC14";
    /**
     * 相应命令--— 查询卡数量
     */
    public static final String CMD_READ_CARD_NUMBER = "FC16";
    /**
     * 相应命令--录卡结果反馈
     */
    public static final String CMD_WRITE_CARD_RESULT = "FC82";


    /**
     * 命令--开锁
     *
     * @return
     */
    public static byte[] OPEN_LOCK() {
        TxOrder order = new OpenLockTxOrder();
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--获取TOKEN
     *
     * @return
     */
    public static byte[] GET_TOKEN() {
        TxOrder order = new GetTokenTxOrder();
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--获取电量
     *
     * @return
     */
    public static byte[] GET_POWER() {
        TxOrder order = new BatteryTxOrder();
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--同步时间
     *
     * @return
     */
    public static byte[] SET_TIME() {
        TxOrder order = new SetTimeTxOrder();
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--获取锁状态
     *
     * @return
     */
    public static byte[] GET_LOCK_STATUS() {
        TxOrder order = new GetLockStatusTxOrder();
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--写入卡
     *
     * @return
     */
    public static byte[] WRITE_CARD_NUMBER() {
        TxOrder order = new WriteCardTxOrder();
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--查询卡数量
     *
     * @return
     */
    public static byte[] READ_ID_CARD_NUMBER() {
        TxOrder order = new QueryCardNumberTxOrder();
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--查询指纹数量
     *
     * @return
     */
    public static byte[] QUERY_FINGERPRINT() {
        TxOrder order = new QueryFingerprintTxOrder();
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--进入注册指纹模式
     *
     * @return
     */
    public static byte[] REGISTER_FINGERPRINT() {
        TxOrder order = new RegisterFingerprintTxOrder();
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--重置指纹
     *
     * @return
     */
    public static byte[] RESET_FINGERPRINT() {
        TxOrder order = new ResetFingerprintTxOrder();
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--删除指纹
     *
     * @return
     */
    public static byte[] DELETE_FINGERPRINT(byte[] bytes) {
        TxOrder order = new DeleteFingerprintTxOrder(bytes);
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--删除指纹
     *
     * @return
     */
    public static byte[] ADD_PASSWORD(byte[] bytes) {
        TxOrder order = new SetPasswordTxOrder(bytes);
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--修改主密码
     *
     * @return
     */
    public static void SET_PASSWORD(final Activity activity) {
        TxOrder order = new PasswordTxOrder(Order.TYPE.RESET_PASSWORD, Config.password);
//        BLEService.sendCmd(activity, CMDUtils.exchangeInfo(order));


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                BLEService.sendCmd(activity, CMDUtils.exchangeInfo(new PasswordTxOrder(Order.TYPE.RESET_PASSWORD2, Config.newPWD)));
            }
        }, 3000);
    }

    /**
     * 命令--修改物理密码1-4
     *
     * @return
     */
    public static byte[] SET_KEY_PASSWORD(byte[] bytes) {
        TxOrder order = new SetKeyPasswordTxOrder(bytes);
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--设置临时密码
     *
     * @param password
     * @param count    次数 0无限次
     * @param time     开始时间
     * @param length   有效时间 0 不限制结束时间
     * @return
     */
    public static byte[] SET_KEY_PASSWORD(byte[] password, byte count, long time, long length) {
        TxOrder order = new SetTemporaryPasswordTxOrder(password, count, time, length);
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--删除密码
     *
     * @return
     */
    public static byte[] DELETE_PASSWORD(byte[] bytes) {
        TxOrder order = new DeletePasswordTxOrder(bytes);
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--设置wifi名称
     *
     * @return
     */
    public static byte[] SET_WIFI_NAME(byte[] bytes) {
        TxOrder order = new SetWifiName(bytes);
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--设置wifi密码
     *
     * @return
     */
    public static byte[] SET_WIFI_PASSWORD(byte[] bytes) {
        TxOrder order = new SetWifiPassword(bytes);
        return CMDUtils.exchangeInfo(order);
    }

    /**
     * 命令--进入写入卡模式
     *
     * @return
     */
    public static byte[] READ_CARD_STATUS() {
        TxOrder order = new CardReadStatusTxOrder();
        return CMDUtils.exchangeInfo(order);
    }
    /**
     * 命令--恢复出厂设置
     *
     * @return
     */
    public static byte[] RESET_DEVICE() {
        TxOrder order = new ResetDeviceTxOrder();
        return CMDUtils.exchangeInfo(order);
    }
    /**
     * 命令--进入写入卡模式
     *
     * @return
     */
    public static byte[] WRITE_CARD_MODE(long time) {
        TxOrder order = new WriteCardModeTxOrder(time);
        return CMDUtils.exchangeInfo(order);
    }

}

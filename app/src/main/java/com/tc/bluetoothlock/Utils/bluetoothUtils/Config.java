package com.tc.bluetoothlock.Utils.bluetoothUtils;

/**
 * 作者：王凯强
 * 时间：2018.7.9
 * 邮箱：317097478@qq.com
 * 备注：
 */
public class Config {

    /**
     * 马蹄锁
     */
    public static byte[] MTX_KEY = {32, 87, 47, 82, 54, 75, 63, 71, 48, 80, 65, 88, 17, 99, 45, 43};
    /**
     * 圆形锁
     */
    public static byte[] KEY_YS = {58, 96, 67, 42, 92, 01, 33, 31, 41, 30, 15, 78, 12, 19, 40, 37};

    public static byte[] password = {0x30, 0x30, 0x30, 0x30, 0x30, 0x30};
    public static byte[] newPWD = {0x30, 0x30, 0x30, 0x30, 0x30, 0x30};

    public static byte[] TOKEN;

    /**
     * 默认密钥
     */
    public static byte[] KEY = {32, 87, 47, 82, 54, 75, 63, 71, 48, 80, 65, 88, 17, 99, 45, 43};

}


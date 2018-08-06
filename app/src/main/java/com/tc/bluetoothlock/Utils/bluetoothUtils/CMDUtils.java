package com.tc.bluetoothlock.Utils.bluetoothUtils;

import com.psylife.wrmvplibrary.utils.LogUtil;
import com.tc.bluetoothlock.mode.TxOrder;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 作者:王凯强 on 2017/3/16.
 * <br> 描述:
 * <br> 邮箱:317097478@qq.com
 */

public class CMDUtils {

    public static byte[] exchangeInfo(TxOrder txOrder) {
        LogUtil.d("===发送指令===" + txOrder.generateString());
        return Encrypt(hexString2Bytes(txOrder.generateString()), Config.KEY);
    }

    /**
     * hexChar转int
     *
     * @param hexChar hex单个字节
     * @return 0..15
     */
    private static int hex2Dec(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * hexString转byteArr
     * <p>例如：</p>
     * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexString2Bytes(String hexString) {
        hexString = hexString.replaceAll(",", "");
        hexString = hexString.replaceAll(":", "");
        int len = hexString.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] res = new byte[len >>> 1];
        for (int i = 0; i < len; i += 2) {
            res[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return res;
    }

    /**
     * bytes字符串转换为key
     *
     * @return byte[]
     */
    public static byte[] hexStr2Key(String src) {
        String[] keys = src.split(",");
        int l = keys.length;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = Byte.decode(keys[i]);
        }
        return ret;
    }

    /**
     * bytes字符串转换为password
     *
     * @return byte[]
     */
    public static byte[] hexStr2Password(String src) {
        String[] passwords = src.split(",");
        int len = passwords.length;
        byte[] res = new byte[len];
        for (int i = 0; i < len; i++) {

            res[i] = Byte.decode(passwords[i]);
        }
        return res;
    }


    // 加密
    public static byte[] Encrypt(byte[] sSrc, byte[] sKey) {

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc);

            return encrypted;//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (Exception ex) {
            return null;
        }
    }


    // 解密
    public static byte[] Decrypt(byte[] sSrc, byte[] sKey) {

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] dncrypted = cipher.doFinal(sSrc);
            return dncrypted;

        } catch (Exception ex) {
            return null;
        }
    }

    public static int byteArrayToInt(byte[] b) {
        return (b[1] & 0xFF) << 0 |
                (b[0] & 0xFF) << 8;
    }

    /**
     * 把一个2位的数组转化位整形
     *
     * @param value
     * @return
     * @throws Exception
     */
    public static int twoBytesToInteger(byte[] value) {
//         if (value.length < 2) {
//            throw new Exception("Byte array too short!");
//         }
        int temp0 = value[0] & 0xFF;
        int temp1 = value[1] & 0xFF;
        return ((temp0 << 8) + temp1);
    }

    public static String toHexString(byte[] bs) {
        return new String(encodeHex(bs));
    }

    private static final char[] DIGITS_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    protected static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_HEX[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_HEX[0x0F & data[i]];
        }
        return out;
    }
}

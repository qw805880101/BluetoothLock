package com.tc.bluetoothlock.mode;


import com.tc.bluetoothlock.Utils.ConvertUtils;

import java.util.Random;

public class SetTemporaryPasswordTxOrder extends TxOrder {
    private Random random = new Random();

    public SetTemporaryPasswordTxOrder(byte[] password, byte count, long time, long length) {
        super(TYPE.SET_KEY_PASSWORD);

        int timeSmall = (int) (time / 1000);
        byte[] bytes_time = ConvertUtils.integerTo4Bytes(timeSmall);
        if (length <= 0) {
            length = 0;
        } else {
            length = length / 60000;
        }
        byte[] bytes_length = ConvertUtils.integerTo3Bytes((int) length);
        add(new byte[]{password[0], password[1], password[2], password[3], password[4], password[5], bytes_time[3], bytes_time[2], bytes_time[1], bytes_time[0], bytes_length[2], bytes_length[1], bytes_length[0], count});
    }

    @Override
    public String generateString() {
        StringBuilder builder = new StringBuilder();
        // 命令类型
        int typeValue = getType().getValue();
        int type = ((typeValue >> 8) & 0x00ff);// 命令类型高8位
        int code = ((typeValue) & 0x00ff);// 命令类型低8位
        // 拼凑命令类型
        builder.append(formatByte2HexStr((byte) type));
        builder.append(formatByte2HexStr((byte) code));

        // 拼凑数据
        for (int i = 0; i < size(); i++) {
            byte value = get(i);//获取数据
            builder.append(formatByte2HexStr(value));//拼凑数据
        }

        // 如果数据总位数不够，在数据后面补0
        for (int i = builder.length() / 2; i < 16; i++) {
            builder.append(formatByte2HexStr((byte) random.nextInt(127)));
        }
        // 生成字符串形式的指令
        String orderStr = builder.toString();
        return orderStr;
    }
}

package com.tc.bluetoothlock.mode;


import com.tc.bluetoothlock.Utils.ConvertUtils;

public class WriteCardModeTxOrder extends TxOrder {

    public WriteCardModeTxOrder(long time) {
        super(TYPE.WRITE_CARD_MODE);

        int timeSmall = (int) (time/ 1000);
        byte[] bytes = ConvertUtils.integerTo4Bytes(timeSmall);
        add(new byte[]{0x06, 0x00, 0x00, bytes[3], bytes[2], bytes[1], bytes[0]});
    }
}

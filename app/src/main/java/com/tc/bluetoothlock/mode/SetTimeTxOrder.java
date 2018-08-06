package com.tc.bluetoothlock.mode;


import com.tc.bluetoothlock.Utils.ConvertUtils;

public class SetTimeTxOrder extends TxOrder {

    public SetTimeTxOrder() {
        super(TYPE.SET_TIME);

        int timeSmall = (int) (System.currentTimeMillis()/1000);
        byte[] bytes = ConvertUtils.integerTo4Bytes(timeSmall);
        add(new byte[]{0x04,bytes[3],bytes[2],bytes[1],bytes[0]});
    }
}

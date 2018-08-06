package com.tc.bluetoothlock.mode;

/**
 * 复位
 * Created by sunshine on 2017/2/24.
 */

public class ResetDeviceTxOrder extends TxOrder {

    public ResetDeviceTxOrder() {
        super(TYPE.RESET_DEVICE);
        add(new byte[]{ 0x01, 0x01});
    }
}

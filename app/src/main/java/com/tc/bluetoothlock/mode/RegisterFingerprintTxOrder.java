package com.tc.bluetoothlock.mode;

/**
 * Created by mac on 2017/12/12.
 */

public class RegisterFingerprintTxOrder extends TxOrder {
    /**
     * @param
     */
    public RegisterFingerprintTxOrder() {
        super (TYPE.REGISTER_FINGERPRINT);
        add(new byte[]{0x01,0x01});
    }
}

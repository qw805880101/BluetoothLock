package com.tc.bluetoothlock.mode;

/**
 * Created by mac on 2017/12/12.
 */

public class ResetFingerprintTxOrder extends TxOrder {
    /**
     * @param
     */
    public ResetFingerprintTxOrder() {
        super (TYPE.RESET_FINGERPRINT);
        add(new byte[]{0x01,0x01});
    }
}

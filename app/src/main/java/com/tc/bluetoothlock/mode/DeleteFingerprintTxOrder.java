package com.tc.bluetoothlock.mode;

/**
 * Created by mac on 2017/12/12.
 */

public class DeleteFingerprintTxOrder extends TxOrder {
    /**
     * @param
     */
    public DeleteFingerprintTxOrder(byte[] data) {
        super (TYPE.DELETE_FINGERPRINT);
        add(data);
    }
}

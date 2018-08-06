package com.tc.bluetoothlock.mode;

/**
 * Created by mac on 2017/12/12.
 */

public class QueryFingerprintTxOrder extends TxOrder {
    /**
     * @param
     */
    public QueryFingerprintTxOrder() {
        super (TYPE.QUERY_FINGERPRINT);
        add(new byte[]{0x01,0x01});
    }
}

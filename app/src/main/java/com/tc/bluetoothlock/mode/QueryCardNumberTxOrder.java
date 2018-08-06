package com.tc.bluetoothlock.mode;

/**
 * 作者: Sunshine
 * 时间: 2018/3/19.
 * 邮箱: 44493547@qq.com
 * 描述:
 */

public class QueryCardNumberTxOrder extends TxOrder {
    /**
     * @param type 指令类型
     */
    public QueryCardNumberTxOrder() {
        super(TYPE.QUERY_ID_CARD_NUMBER);
        add(new byte[]{0x01,0x01});
    }
}

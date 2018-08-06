package com.tc.bluetoothlock.mode;


/**
 * 开锁指令
 * Created by sunshine on 2017/2/24.
 */

public class SetKeyPasswordTxOrder extends TxOrder {
    public SetKeyPasswordTxOrder(byte[] password) {
        super(TYPE.SET_KEY_PASSWORD);
        byte[] bytes = {0x04, password[0],password[1],password[2], password[3]};
        add(bytes);
    }
}

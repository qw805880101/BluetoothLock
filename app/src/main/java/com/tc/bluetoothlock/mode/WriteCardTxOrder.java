package com.tc.bluetoothlock.mode;

import android.text.TextUtils;

import com.tc.bluetoothlock.Utils.ConvertUtils;
import com.tc.bluetoothlock.Utils.ToastUtils;
import com.tc.bluetoothlock.Utils.bluetoothUtils.GlobalParameterUtils;

/**
 * 作者: Sunshine
 * 时间: 2018/3/19.
 * 邮箱: 44493547@qq.com
 * 描述:
 */

public class WriteCardTxOrder extends TxOrder {
    /**
     *
     */
    public WriteCardTxOrder() {
        super(TYPE.WRITE_ID_CARD_NUMBER);

        String card = GlobalParameterUtils.getInstance().getCard();
        byte number = GlobalParameterUtils.getInstance().getNumber();
        number++;
        if (TextUtils.isEmpty(card)||number==-1){
            ToastUtils.show("先刷卡并且查询已写入数量");
            return;
        }
        byte[] bytes = ConvertUtils.hexString2Bytes(card);
        byte[] sendBytes = new byte[bytes.length+2];
        add(new byte[]{0x09, number});
        add(bytes);
    }
}

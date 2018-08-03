package com.tc.bluetoothlock.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.bean.LockInfo;
import com.tc.bluetoothlock.view.BatteryView;

import java.util.List;

public class LockAdapter extends BaseQuickAdapter<LockInfo, BaseViewHolder> {

    public LockAdapter(List<LockInfo> data) {
        super(R.layout.item_lock, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LockInfo item) {

        BatteryView mBatteryView = helper.getView(R.id.battery);

        int battery = (int) (Math.random() * 120);

        mBatteryView.setBattery(130);

        TextView txtBattery = helper.getView(R.id.txt_battery);
        txtBattery.setText(item.getId() + "%");
    }
}

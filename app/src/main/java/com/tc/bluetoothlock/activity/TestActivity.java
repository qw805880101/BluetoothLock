package com.tc.bluetoothlock.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.bluetoothUtils.CMDAPI;
import com.tc.bluetoothlock.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    /**
     * 默认密钥
     */
    public static byte[] KEY = {32, 87, 47, 82, 54, 75, 63, 71, 48, 80, 65, 88, 17, 99, 45, 43};

    @BindView(R.id.bt_scan)
    Button btScan;
    @BindView(R.id.bt_open_lock)
    Button btOpenLock;
    @BindView(R.id.bt_wifi)
    Button btWifi;
    @BindView(R.id.bt_fingerprint)
    Button btFingerprint;
    @BindView(R.id.bt_password)
    Button btPassword;
    @BindView(R.id.list_item)
    RecyclerView listItem;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initdata() {

    }

    @OnClick({R.id.bt_scan, R.id.bt_open_lock, R.id.bt_wifi, R.id.bt_fingerprint, R.id.bt_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_scan:
                break;
            case R.id.bt_open_lock:
                byte[] bytes = CMDAPI.GET_TOKEN();
                mBluetoothUtil.doWrite(bytes);
//                this.sendBroadcast(new Intent());
                break;
            case R.id.bt_wifi:
                break;
            case R.id.bt_fingerprint:
                break;
            case R.id.bt_password:
                break;
        }
    }


    public void startAdvertising() {
        byte[] broadcastData = {0x34, 0x56};
        String bleName = "小郎";
        broadcastData = bleName.getBytes();

        //广播设置参数，广播数据，还有一个是Callback
//        mBluetoothLeAdvertiser.startAdvertising(createAdvSettings(true, 0), createAdvertiseData(broadcastData), mAdvertiseCallback);
    }
}

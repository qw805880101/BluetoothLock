package com.tc.bluetoothlock.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.psylife.wrmvplibrary.utils.LogUtil;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.TestService;
import com.tc.bluetoothlock.Utils.bluetoothUtils.BLEUtils;
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

    private String mac;
    int electricity = -1;//电量
    private Dialog loadingDialog;

    private double lon;
    private double lat;
    private String name;
    private String lockKey;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initdata() {
        mac = getIntent().getStringExtra("mac");
        name = getIntent().getStringExtra("name");
        lockKey = getIntent().getStringExtra("lockKey");
        BLEUtils.openLockByBLE(this, mac.toUpperCase(), lockKey, BLEUtils.password);

    }

    @OnClick({R.id.bt_scan, R.id.bt_open_lock, R.id.bt_wifi, R.id.bt_fingerprint, R.id.bt_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_scan:
                break;
            case R.id.bt_open_lock:
                // 链接状态--BLE所有连接--连接成功--等待输入指令
                TestService.sendCmd(this, CMDAPI.GET_TOKEN());
                LogUtil.d("发送指令--获取TOKEN");
//                this.sendBroadcast(new Intent());
                break;
            case R.id.bt_wifi:
                // 链接状态--BLE所有连接--连接成功--等待输入指令
                TestService.sendCmd(this, CMDAPI.OPEN_LOCK());
                LogUtil.d("发送指令--开锁");
//                this.sendBroadcast(new Intent());
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

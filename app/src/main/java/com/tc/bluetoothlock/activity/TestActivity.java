package com.tc.bluetoothlock.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.AesUtil;
import com.tc.bluetoothlock.Utils.bluetoothUtils.BluetoothUtil;
import com.tc.bluetoothlock.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    /* 蓝牙工具类 */
    private BluetoothUtil mBluetoothUtil;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBluetoothUtil = BluetoothUtil.getBluetoothUtil(this);
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
                byte[] bytes = "06010101000000000000000000000000".getBytes();
                mBluetoothUtil.doWrite(AesUtil.Encrypt(bytes, KEY));
                break;
            case R.id.bt_wifi:
                break;
            case R.id.bt_fingerprint:
                break;
            case R.id.bt_password:
                break;
        }
    }
}

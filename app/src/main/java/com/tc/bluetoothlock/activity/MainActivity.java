package com.tc.bluetoothlock.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.psylife.wrmvplibrary.utils.LogUtil;
import com.psylife.wrmvplibrary.utils.SpUtils;
import com.psylife.wrmvplibrary.utils.helper.RxUtil;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.BluetoothReceiver;
import com.tc.bluetoothlock.Utils.BluetoothUtil;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.bean.BaseBeanClass;
import com.tc.bluetoothlock.bean.BaseBeanInfo;
import com.tc.bluetoothlock.bean.LockInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.xudaojie.qrcodelib.CaptureActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Action1;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends BaseActivity {

    public static final int EXTERNAL_STORAGE_REQ_CAMERA_CODE = 10;
    public static final int REQUEST_QR_CODE = 101;

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

    private BluetoothUtil mBluetoothUtil;

    private BluetoothReceiver mBluetoothReceiver;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        mBluetoothUtil = BluetoothUtil.getBluetoothUtil(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean i = mBluetoothUtil.startSeachBlue();
                if (i){
                    LogUtil.d("开始搜索蓝牙");
                }
            }
        }).start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mBluetoothReceiver = new BluetoothReceiver();
        //需要过滤多个动作，则调用IntentFilter对象的addAction添加新动作
        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        foundFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        foundFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBluetoothReceiver, foundFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBluetoothReceiver);
    }

    @Override
    public void initdata() {
        start("100000000");
    }

    private void requestCameraPerm() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        EXTERNAL_STORAGE_REQ_CAMERA_CODE);
            } else {
                Intent i = new Intent(this, CaptureActivity.class);
                startActivityForResult(i, REQUEST_QR_CODE);
            }
        } else {
            Intent i = new Intent(this, CaptureActivity.class);
            startActivityForResult(i, REQUEST_QR_CODE);
        }
    }

    private void start(String lockNo) {
        startProgressDialog(this);

        Map map = new HashMap();
        map.put("lockNo", lockNo);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        Observable<BaseBeanInfo<BaseBeanClass<LockInfo>>> register = mApi.getLockInfo(requestBody).compose(RxUtil.<BaseBeanInfo<BaseBeanClass<LockInfo>>>rxSchedulerHelper());
        mRxManager.add(register.subscribe(new Action1<BaseBeanInfo<BaseBeanClass<LockInfo>>>() {
            @Override
            public void call(BaseBeanInfo<BaseBeanClass<LockInfo>> info) {
                stopProgressDialog();
                if (info.getCode() == 200 && info.getMsg().equals("SUCCESS")) {
                    LockInfo lockInfo = info.getData().getReturnData();

                    BluetoothDevice mBluetoothDevice = mBluetoothUtil.getBluetoothDevice(lockInfo.getLockBluetoothMac());
                    LogUtil.d("12:"+mBluetoothDevice.getBondState());
                    mBluetoothDevice.createBond();
                    try {
                        mBluetoothDevice.createRfcommSocketToServiceRecord(UUID.randomUUID());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    toastMessage("" + info.getCode(), info.getMsg());
                }
            }
        }, this));
    }

    @OnClick({R.id.bt_scan, R.id.bt_open_lock, R.id.bt_wifi, R.id.bt_fingerprint, R.id.bt_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_scan:
                requestCameraPerm();
                break;
            case R.id.bt_open_lock:
                break;
            case R.id.bt_wifi:
                break;
            case R.id.bt_fingerprint:
                break;
            case R.id.bt_password:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && requestCode == REQUEST_QR_CODE
                && data != null) {
            String result = data.getStringExtra("result");
            start(result);
//            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}

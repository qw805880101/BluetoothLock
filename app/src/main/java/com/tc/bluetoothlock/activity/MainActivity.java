package com.tc.bluetoothlock.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.psylife.wrmvplibrary.utils.LogUtil;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.psylife.wrmvplibrary.utils.helper.RxUtil;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.bluetoothUtils.BluetoothReceiver;
import com.tc.bluetoothlock.Utils.bluetoothUtils.BluetoothUtil;
import com.tc.bluetoothlock.Utils.bluetoothUtils.SearchBluetoothInterface;
import com.tc.bluetoothlock.adapter.MyBluetoothAdapter;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.bean.BaseBeanClass;
import com.tc.bluetoothlock.bean.BaseBeanInfo;
import com.tc.bluetoothlock.bean.LockInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.xudaojie.qrcodelib.CaptureActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Action1;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends BaseActivity implements SearchBluetoothInterface {

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
    @BindView(R.id.list_item)
    RecyclerView mBluetoothList;

    private BluetoothUtil mBluetoothUtil;

    private BluetoothReceiver mBluetoothReceiver;

    private MyBluetoothAdapter mMyBluetoothAdapter;

    List<BluetoothDevice> mBluetoothDevices = new ArrayList<>();

    private LockInfo lockInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        mBluetoothUtil = BluetoothUtil.getBluetoothUtil(this);

        mMyBluetoothAdapter = new MyBluetoothAdapter(this, mBluetoothDevices);

        mBluetoothList.setLayoutManager(new LinearLayoutManager(this));
        mBluetoothList.setAdapter(mMyBluetoothAdapter);

    }

    @Override
    public void initdata() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        /* 注册监听蓝牙广播 */
        mBluetoothReceiver = new BluetoothReceiver(this);
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

    /**
     * 请求二维码拍照
     */
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

    /**
     * 扫描二维码获取锁信息
     *
     * @param lockNo
     */
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
                    lockInfo = info.getData().getReturnData();

                    if (mBluetoothUtil.startSeachBlue()) {
                        Toast.makeText(mContext, "开始搜索蓝牙, 请稍后", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "开始搜索蓝牙失败, 请检查蓝牙是否开启", Toast.LENGTH_SHORT).show();
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
        }
    }

    @Override
    public void searchSuccess(BluetoothDevice mBluetoothDevices) {
        String mac = mBluetoothDevices.getAddress().replace(":", "");
        LogUtil.d("搜索:" + mac);
        if (mac.equals(lockInfo.getLockBluetoothMac())) { //搜索到匹配的锁蓝牙 关闭蓝牙搜索
            unregisterReceiver(mBluetoothReceiver);
            LogUtil.d("搜索到锁了，关闭搜索广播");
            connectBluetooth(mBluetoothDevices);
        }
    }

    /**
     * 搜索蓝牙完成
     *
     * @param mBluetoothDevices 搜索到的蓝牙列表
     */
    @Override
    public void searchFinish(List<BluetoothDevice> mBluetoothDevices) {
        mMyBluetoothAdapter.setNewBluetoothDevices(mBluetoothDevices);
        boolean isSearch = false;
        BluetoothDevice mBluetoothDevice = null;
        for (BluetoothDevice s : mBluetoothDevices) {
            if ((s.getAddress().replace(":", "")).equals(lockInfo.getLockBluetoothMac())) { //搜索到匹配的锁蓝牙 关闭蓝牙搜索
                LogUtil.d("搜索到锁了，关闭搜索广播");
                isSearch = true;
                mBluetoothDevice = s;
                continue;
            }
        }

        if (!isSearch) {
            ToastUtils.showToast(mContext, "未搜索到锁，请确认是否打开锁的蓝牙");
        } else { //找到锁
            connectBluetooth(mBluetoothDevice);
        }
    }

    /**
     * 连接蓝牙
     *
     * @param mBluetoothDevices
     */
    private void connectBluetooth(BluetoothDevice mBluetoothDevices) {
        mBluetoothUtil.startConnectBluetooth(mBluetoothDevices);
    }

}

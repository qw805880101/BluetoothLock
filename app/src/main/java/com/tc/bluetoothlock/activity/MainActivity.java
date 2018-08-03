package com.tc.bluetoothlock.activity;

import android.Manifest;
import android.Manifest.permission;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.psylife.wrmvplibrary.utils.LogUtil;
import com.psylife.wrmvplibrary.utils.TitleBuilder;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.psylife.wrmvplibrary.utils.helper.RxUtil;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.bluetoothUtils.BluetoothReceiver;
import com.tc.bluetoothlock.Utils.bluetoothUtils.BluetoothUtil;
import com.tc.bluetoothlock.Utils.bluetoothUtils.SearchBluetoothInterface;
import com.tc.bluetoothlock.adapter.LockAdapter;
import com.tc.bluetoothlock.adapter.MyBluetoothAdapter;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.bean.BaseBeanClass;
import com.tc.bluetoothlock.bean.BaseBeanInfo;
import com.tc.bluetoothlock.bean.LockInfo;
import com.tc.bluetoothlock.view.InterestSpaceItemDecoration;
import com.tc.bluetoothlock.view.InterestSpaceItemDecorationList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.xudaojie.qrcodelib.CaptureActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Action1;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends BaseActivity implements SearchBluetoothInterface, OnClickListener {

    public static final int EXTERNAL_STORAGE_REQ_CAMERA_CODE = 10;
    public static final int REQUEST_QR_CODE = 101;

    @BindView(R.id.recycler_lock_list)
    RecyclerView mLockList;
    @BindView(R.id.lin_add_lock)
    LinearLayout mLinAddLock;

    /* 蓝牙工具类 */
    private BluetoothUtil mBluetoothUtil;

    /* 蓝牙广播 */
    private BluetoothReceiver mBluetoothReceiver;

    /* 蓝牙列表 */
    private MyBluetoothAdapter mMyBluetoothAdapter;

    List<BluetoothDevice> mBluetoothDevices = new ArrayList<>();

    /* 锁信息 */
    private LockInfo lockInfo;

    private TitleBuilder mTitleBuilder;

    private LockAdapter mLockAdapter;

    @Override
    public View getTitleView() {
        return getTitleBuilder().build();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        mBluetoothUtil = BluetoothUtil.getBluetoothUtil(this);

        mMyBluetoothAdapter = new MyBluetoothAdapter(this, mBluetoothDevices);

        mLinAddLock.setOnClickListener(this);

        List<LockInfo> lockInfos = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            LockInfo lockInfo = new LockInfo();
            lockInfo.setId("" + i * 10);
            lockInfos.add(lockInfo);
        }
        mLockAdapter = new LockAdapter(lockInfos);

        mLinAddLock.setVisibility(View.GONE);
        mLockList.setLayoutManager(new LinearLayoutManager(this));
        mLockList.addItemDecoration(new InterestSpaceItemDecorationList(40));
        mLockList.setAdapter(mLockAdapter);
    }

    @Override
    public void initdata() {

    }

    @Override
    public void onClick(View view) {
        if (view == mLinAddLock) {
            requestCameraPerm();
        }

        if (view == mTitleBuilder.getIvLeft()) {
            ToastUtils.showToast(this, "点我干啥");
        }

        if (view == mTitleBuilder.getIvRight()) {
            ToastUtils.showToast(this, "点我弄啥~");
        }

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
        if (VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this, permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{permission.CAMERA},
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

    /**
     * 初始化标题栏信息
     *
     * @return
     */
    private TitleBuilder getTitleBuilder() {
        mTitleBuilder = new TitleBuilder(this);
        mTitleBuilder.setTitleTextColor(this, R.color.white);
        SpannableString spannableString = new SpannableString(this.getResources().getString(R.string.e_home));
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(this.getResources().getColor(R.color.txt_0577fe));
        spannableString.setSpan(colorSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mTitleBuilder.getTvTitle().setText(spannableString);
        mTitleBuilder.setTitleBgRes(R.color.bg_151519);
        mTitleBuilder.setLeftImage(R.mipmap.nav_icon_personal);
        mTitleBuilder.setLeftOnClickListener(this);
        mTitleBuilder.setRightImage(R.mipmap.nav_icon_add);
        mTitleBuilder.setRightOnClickListener(this);
        return mTitleBuilder;
    }
}

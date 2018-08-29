package com.tc.bluetoothlock.activity;

import android.Manifest;
import android.Manifest.permission;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.psylife.wrmvplibrary.utils.IntentUtils;
import com.psylife.wrmvplibrary.utils.LogUtil;
import com.psylife.wrmvplibrary.utils.StatusBarUtil;
import com.psylife.wrmvplibrary.utils.TitleBuilder;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.psylife.wrmvplibrary.utils.helper.RxUtil;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.bluetoothUtils.BLEUtils;
import com.tc.bluetoothlock.Utils.bluetoothUtils.BluetoothReceiver;
import com.tc.bluetoothlock.Utils.bluetoothUtils.CMDUtils;
import com.tc.bluetoothlock.Utils.bluetoothUtils.SearchBluetoothInterface;
import com.tc.bluetoothlock.adapter.LockAdapter;
import com.tc.bluetoothlock.adapter.MyBluetoothAdapter;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.bean.BaseBeanInfo;
import com.tc.bluetoothlock.bean.LockInfo;
import com.tc.bluetoothlock.helper.BaseViewHelper;
import com.tc.bluetoothlock.view.InterestSpaceItemDecorationList;
import com.tc.bluetoothlock.view.WaveView;
import com.tc.bluetoothlock.view.TitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
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
    @BindView(R.id.txt_hint)
    TextView txt_hint;

    /* 蓝牙列表 */
    private MyBluetoothAdapter mMyBluetoothAdapter;

    List<BluetoothDevice> mBluetoothDevices = new ArrayList<>();

    /* 锁信息 */
    private LockInfo lockInfo;

    private TitleBuilder mTitleBuilder;

    private LockAdapter mLockAdapter;

    //是否搜索到锁
    boolean isSearchLock = false;

    private BaseViewHelper helper;
    private View v;
    private WaveView mWaveView;
    private TextView tvHint;
    private EditText etName;

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        initTitle();

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

        BLEUtils.setSearchBluetoothInterface(this);
    }

    @Override
    public void initdata() {
    }

    @Override
    public void onClick(View view) {
        if (view == mLinAddLock) {
            requestCameraPerm();
        }

        if (view == mTitleView.getIvLeft()) {
            ToastUtils.showToast(this, "点我干啥");
            startActivity(new Intent(this, AccountActivity.class));
        }

        if (view == mTitleView.getIvRight()) {
            ToastUtils.showToast(this, "点我弄啥~");
//            requestCameraPerm();
            start("100000000");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        Observable<BaseBeanInfo<LockInfo>> getLockInfo = mApi.getLockInfo(requestBody).compose(RxUtil.<BaseBeanInfo<LockInfo>>rxSchedulerHelper());
        mRxManager.add(getLockInfo.subscribe(new Action1<BaseBeanInfo<LockInfo>>() {
            @Override
            public void call(BaseBeanInfo<LockInfo> info) {
                stopProgressDialog();
                if (info.getCode() == 200 && info.getMsg().equals("SUCCESS")) {
                    lockInfo = info.getData();
                    openLockAnimation();
                    BLEUtils.openLockByBLE(MainActivity.this, lockInfo.getLockBluetoothMac(), CMDUtils.hexStr(lockInfo.getNewKey()), CMDUtils.hexSixteen(lockInfo.getNewPassword()));

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void searchSuccess(BluetoothDevice mBluetoothDevices) {
        String mac = mBluetoothDevices.getAddress().replace(":", "");
        LogUtil.d("搜索:" + mac);
        if (mac.equals(lockInfo.getLockBluetoothMac())) { //搜索到匹配的锁蓝牙 关闭蓝牙搜索
            LogUtil.d("搜索到锁了");
            if (helper != null && helper.isShowing()) {
                helper.back();
                mWaveView.stop();
            }
            BLEUtils.stopLeScan();
//            connectBluetooth(mBluetoothDevices);
            Bundle bundle = new Bundle();
            bundle.putString("mac", mBluetoothDevices.getAddress());
            bundle.putString("name", mBluetoothDevices.getName());
            bundle.putString("password", lockInfo.getNewPassword());
            bundle.putString("lockKey", lockInfo.getNewKey());

            IntentUtils.startActivity(this, TestActivity.class, bundle);
        }
    }

    /**
     * 搜索蓝牙完成
     *
     * @param mBluetoothDevices 搜索到的蓝牙列表
     */
    @Override
    public void searchFinish(List<BluetoothDevice> mBluetoothDevices) {
        if (mBluetoothDevices != null) {
            mMyBluetoothAdapter.setNewBluetoothDevices(mBluetoothDevices);
            BluetoothDevice mBluetoothDevice = null;
//        for (BluetoothDevice s : mBluetoothDevices) {
//            if ((s.getAddress().replace(":", "")).equals(lockInfo.getLockBluetoothMac())) { //搜索到匹配的锁蓝牙 关闭蓝牙搜索
//                LogUtil.d("搜索到锁了，关闭搜索广播");
//                isSearch = true;
//                mBluetoothDevice = s;
//                continue;
//            }
//        }
        }
        if (!isSearchLock) {
            mHandler.sendEmptyMessage(0);
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                ToastUtils.showToast(mContext, "未搜索到锁，请确认是否打开锁的蓝牙");
                BLEUtils.stopLeScan(); //关闭蓝牙搜索
                if (helper != null && helper.isShowing()) {
                    mWaveView.stop();
                    helper.back();
                }
            }
        }
    };

    /**
     * 初始化标题栏信息
     *
     * @return
     */
    private void initTitle() {
        mTitleView = new TitleView(this);
        mTitleView.setTitleTextColor(this, R.color.white);
        SpannableString spannableString = new SpannableString(this.getResources().getString(R.string.e_home));
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(this.getResources().getColor(R.color.txt_0577fe));
        spannableString.setSpan(colorSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mTitleView.getTvTitle().setText(spannableString);
        mTitleView.setTitleBgRes(R.color.bg_151519);
        mTitleView.setLeftImage(R.mipmap.nav_icon_personal);
        mTitleView.setLeftOnClickListener(this);
        mTitleView.setRightImage(R.mipmap.nav_icon_add);
        mTitleView.setRightOnClickListener(this);
    }

    private void openLockAnimation() {
        v = View.inflate(this, R.layout.layout_ai, null);
        mWaveView = (WaveView) v.findViewById(R.id.wave_view);
        tvHint = (TextView) v.findViewById(R.id.tv_hint);
        tvHint.setText(getString(R.string.searching));
        mWaveView.setDuration(3000);
        mWaveView.setStyle(Paint.Style.FILL);
        mWaveView.setColor(getResources().getColor(R.color.text));
        mWaveView.setInterpolator(new LinearOutSlowInInterpolator());
        //显示在当前页面跳转
        helper = new BaseViewHelper.Builder(this, txt_hint)
                .setEndView(v)
                .setDimAlpha(0)
                .create();
        mWaveView.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BLEUtils.stopLeScan(); //关闭蓝牙搜索
            mWaveView.stop();
            helper.back();
        }
        return true;
    }
}

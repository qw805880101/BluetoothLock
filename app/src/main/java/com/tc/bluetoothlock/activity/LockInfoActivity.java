package com.tc.bluetoothlock.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.psylife.wrmvplibrary.utils.IntentUtils;
import com.psylife.wrmvplibrary.utils.LogUtil;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.bluetoothUtils.BLEUtils;
import com.tc.bluetoothlock.Utils.bluetoothUtils.CMDUtils;
import com.tc.bluetoothlock.Utils.bluetoothUtils.SearchBluetoothInterface;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.bean.LockInfo;
import com.tc.bluetoothlock.helper.BaseViewHelper;
import com.tc.bluetoothlock.view.TitleView;
import com.tc.bluetoothlock.view.WaveView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class LockInfoActivity extends BaseActivity implements SearchBluetoothInterface {

    public static final String LOCK_INFO = "lockInfo";

    @BindView(R.id.txt_state)
    TextView mTxtState;
    @BindView(R.id.txt_click_lock)
    TextView mTxtClickLock;
    @BindView(R.id.txt_password_set)
    TextView mTxtPasswordSet;
    @BindView(R.id.txt_fingerprint_set)
    TextView mTxtFingerprintSet;
    @BindView(R.id.txt_wifi_set)
    TextView mTxtWifiSet;
    @BindView(R.id.rl_click_lock)
    RelativeLayout rlClickLock;

    //是否搜索到锁
    boolean isSearchLock = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lock_info;
    }

    private LockInfo mLockInfo;

    @Override
    public void initView(Bundle savedInstanceState) {
        mTitleView = new TitleView(this);
        mTitleView.setTitleText("我的锁");
        mTitleView.setRightImage(R.mipmap.nav_icon_chage);
        mTitleView.setLeftOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleView.setRightOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        BLEUtils.setSearchBluetoothInterface(this);
    }

    @Override
    public void initdata() {
        Intent intent = this.getIntent();
        mLockInfo = (LockInfo) intent.getSerializableExtra(LOCK_INFO);

        setState(this.getResources().getString(R.string.Connection));
        BLEUtils.openLockByBLE(this, mLockInfo.getLockBluetoothMac(), CMDUtils.hexStr(mLockInfo.getNewKey()), CMDUtils.hexSixteen(mLockInfo.getNewPassword()));
    }

    /**
     * 打开按钮
     */
    private void openButton() {
        mTxtPasswordSet.setEnabled(true);
        mTxtPasswordSet.setTextColor(this.getResources().getColor(R.color.white));
        mTxtFingerprintSet.setEnabled(true);
        mTxtFingerprintSet.setTextColor(this.getResources().getColor(R.color.white));
        mTxtWifiSet.setEnabled(true);
        mTxtWifiSet.setTextColor(this.getResources().getColor(R.color.white));
    }

    @OnClick({R.id.txt_click_lock, R.id.rl_click_lock, R.id.txt_password_set, R.id.txt_fingerprint_set, R.id.txt_wifi_set})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_click_lock:
            case R.id.txt_click_lock:  //点击开锁
                break;
            case R.id.txt_password_set: //密码设置
                intent = new Intent(this, PasswordListActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_fingerprint_set: //指纹设置
                intent = new Intent(this, FingerprintListActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_wifi_set: //wifi设置
                intent = new Intent(this, AllModifyActivity.class);
                intent.putExtra(AllModifyActivity.INTENT_KEY, AllModifyActivity.MODIFY_WIFI);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void searchSuccess(BluetoothDevice mBluetoothDevices) {
        String mac = mBluetoothDevices.getAddress().replace(":", "");
        LogUtil.d("搜索:" + mac);
        if (mac.equals(mLockInfo.getLockBluetoothMac())) { //搜索到匹配的锁蓝牙 关闭蓝牙搜索
            LogUtil.d("搜索到锁了");
            setState(this.getResources().getString(R.string.Connection_success));
            BLEUtils.stopLeScan();
//            connectBluetooth(mBluetoothDevices);
            Bundle bundle = new Bundle();
            bundle.putString("mac", mBluetoothDevices.getAddress());
            bundle.putString("name", mBluetoothDevices.getName());
            bundle.putString("password", mLockInfo.getNewPassword());
            bundle.putString("lockKey", mLockInfo.getNewKey());

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
            }
        }
    };

    /**
     * 状态提示
     *
     * @param state
     */
    private void setState(String state) {
        mTxtState.setText(state);
    }

}

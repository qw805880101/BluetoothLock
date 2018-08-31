package com.tc.bluetoothlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.view.TitleView;

import butterknife.BindView;
import butterknife.OnClick;


public class LockInfoActivity extends BaseActivity {
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_lock_info;
    }

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
    }

    @Override
    public void initdata() {

    }

    @OnClick({R.id.txt_click_lock, R.id.txt_password_set, R.id.txt_fingerprint_set, R.id.txt_wifi_set})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
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
}

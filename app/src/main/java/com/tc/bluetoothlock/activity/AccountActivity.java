package com.tc.bluetoothlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.psylife.wrmvplibrary.utils.StatusBarUtil;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AccountActivity extends BaseActivity {
    @BindView(R.id.iv_head)
    RoundedImageView mIvHead;
    @BindView(R.id.txt_name)
    TextView mTxtName;
    @BindView(R.id.txt_mobile)
    TextView mTxtMobile;
    @BindView(R.id.lin_open_lock_info)
    LinearLayout mLinOpenLockInfo;
    @BindView(R.id.lin_guide_to_use)
    LinearLayout mLinGuideToUse;
    @BindView(R.id.lin_update)
    LinearLayout mLinUpdate;
    @BindView(R.id.lin_set)
    LinearLayout mLinSet;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    public void setStatusBarColor() {
        StatusBarUtil.setColor(this, this.getResources().getColor(R.color.bg_151519));
    }

    @Override
    public int getLayoutId() {
        return R.layout.acitivity_account;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initdata() {

    }

    @OnClick({R.id.iv_back, R.id.iv_head, R.id.lin_open_lock_info, R.id.lin_guide_to_use, R.id.lin_update, R.id.lin_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back: //头像
                finish();
                break;
            case R.id.iv_head: //头像
                startActivity(new Intent(this, AccountInfoActivity.class));
                break;
            case R.id.lin_open_lock_info: //开锁记录
                startActivity(new Intent(this, OpenLockInfoActivity.class));
                break;
            case R.id.lin_guide_to_use: //使用指南
                break;
            case R.id.lin_update: //版本更新
                break;
            case R.id.lin_set: //设置
                startActivity(new Intent(this, SetActivity.class));
                break;
        }
    }
}

package com.tc.bluetoothlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.psylife.wrmvplibrary.utils.StatusBarUtil;
import com.psylife.wrmvplibrary.utils.TitleBuilder;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.lin_feedback)
    LinearLayout mLinFeedback;
    @BindView(R.id.lin_about)
    LinearLayout mLinAbout;

    public void setStatusBarColor() {
        StatusBarUtil.setColor(this, this.getResources().getColor(R.color.bg_151519));
    }

    @Override
    public View getTitleView() {
        mTitleBuilder = new TitleBuilder(this);
//        mTitleBuilder.setTitleText("图片界面");
        mTitleBuilder.setLeftImage(R.mipmap.fanhui);
        mTitleBuilder.setTitleBgRes(R.color.bg_151519);
        mTitleBuilder.setLeftOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mTitleBuilder.setTitleBgRes(R.color.bg_blue);
//        mTitleBuilder.setTitleTextColor(this, R.color.white);
        return mTitleBuilder.build();
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

    @OnClick({R.id.iv_head, R.id.lin_open_lock_info, R.id.lin_guide_to_use, R.id.lin_update, R.id.lin_feedback, R.id.lin_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
            case R.id.lin_feedback: //意见反馈
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.lin_about: //关于我们
                break;
        }
    }
}

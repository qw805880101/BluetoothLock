package com.tc.bluetoothlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.psylife.wrmvplibrary.utils.StatusBarUtil;
import com.psylife.wrmvplibrary.utils.TitleBuilder;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SetActivity extends BaseActivity {

    @BindView(R.id.lin_feedback)
    LinearLayout mLinFeedback;
    @BindView(R.id.lin_about)
    LinearLayout mLinAbout;
    @BindView(R.id.bt_exit)
    Button mBtExit;

    public void setStatusBarColor() {
        StatusBarUtil.setColor(this, this.getResources().getColor(R.color.bg_151519));
    }

    @Override
    public View getTitleView() {
        mTitleBuilder = new TitleBuilder(this);
        mTitleBuilder.setTitleText(getResources().getString(R.string.set));
        mTitleBuilder.setLeftImage(R.mipmap.nav_icon_back);
        mTitleBuilder.setTitleBgRes(R.color.bg_151519);
        mTitleBuilder.setLeftOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mTitleBuilder.setTitleBgRes(R.color.bg_blue);
        mTitleBuilder.setTitleTextColor(this, R.color.white);
        return mTitleBuilder.build();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initdata() {

    }

    @OnClick({R.id.bt_exit, R.id.lin_feedback, R.id.lin_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_exit: //退出
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.lin_feedback: //意见反馈
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.lin_about: //关于我们
                break;
        }
    }
}

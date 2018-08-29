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
import com.tc.bluetoothlock.view.TitleView;

import butterknife.BindView;
import butterknife.OnClick;

public class SetActivity extends BaseActivity {

    @BindView(R.id.lin_feedback)
    LinearLayout mLinFeedback;
    @BindView(R.id.lin_about)
    LinearLayout mLinAbout;
    @BindView(R.id.bt_exit)
    Button mBtExit;

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTitleView = new TitleView(this);
        mTitleView.setTitleText(this.getResources().getString(R.string.set));
        mTitleView.setLeftOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

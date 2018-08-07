package com.tc.bluetoothlock.activity;

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

public class AccountInfoActivity extends BaseActivity {
    @BindView(R.id.iv_head)
    RoundedImageView mIvHead;
    @BindView(R.id.lin_head)
    LinearLayout mLinHead;
    @BindView(R.id.txt_user_name)
    TextView mTxtUserName;
    @BindView(R.id.txt_mobile)
    TextView mTxtMobile;

    public void setStatusBarColor() {
        StatusBarUtil.setColor(this, this.getResources().getColor(R.color.bg_151519));
    }

    @Override
    public View getTitleView() {
        mTitleBuilder = new TitleBuilder(this);
        mTitleBuilder.setTitleText(getResources().getString(R.string.Personal_nformation));
        mTitleBuilder.setLeftImage(R.mipmap.fanhui);
        mTitleBuilder.setTitleBgRes(R.color.bg_151519);
        mTitleBuilder.setTitleTextColor(this, R.color.white);
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
        return R.layout.activity_account_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initdata() {

    }

    @OnClick({R.id.iv_head, R.id.txt_user_name, R.id.txt_mobile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_head:
                break;
            case R.id.txt_user_name:
                break;
            case R.id.txt_mobile:
                break;
        }
    }
}

package com.tc.bluetoothlock.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.psylife.wrmvplibrary.utils.StatusBarUtil;
import com.psylife.wrmvplibrary.utils.TitleBuilder;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.base.BaseActivity;

import butterknife.BindView;

public class OpenLockInfoActivity extends BaseActivity {

    @BindView(R.id.recycler_open_lock_info)
    RecyclerView mRecyclerView;

    public void setStatusBarColor() {
        StatusBarUtil.setColor(this, this.getResources().getColor(R.color.bg_151519));
    }

    @Override
    public View getTitleView() {
        mTitleBuilder = new TitleBuilder(this);
        mTitleBuilder.setTitleText(getResources().getString(R.string.open_lock_info));
        mTitleBuilder.setLeftImage(R.mipmap.nav_icon_back);
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
        return R.layout.activity_open_lock_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initdata() {

    }
}

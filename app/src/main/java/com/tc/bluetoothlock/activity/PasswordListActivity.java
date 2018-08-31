package com.tc.bluetoothlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.adapter.PasswordAdapter;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.bean.LockInfo;
import com.tc.bluetoothlock.view.InterestSpaceItemDecorationList;
import com.tc.bluetoothlock.view.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PasswordListActivity extends BaseActivity {
    @BindView(R.id.recycler_list)
    RecyclerView mRecyclerList;

    private PasswordAdapter mPasswordAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTitleView = new TitleView(this);
        mTitleView.setTitleText(this.getResources().getString(R.string.password_setting));
        mTitleView.setLeftOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleView.setRightImage(R.mipmap.nav_icon_add_right);
        mTitleView.setRightOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AllModifyActivity.class);
                intent.putExtra(AllModifyActivity.INTENT_KEY, AllModifyActivity.ADD_PASSWORD);
                startActivity(intent);
            }
        });

        List<LockInfo> lockInfos = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            LockInfo lockInfo = new LockInfo();
            lockInfo.setId("" + i * 10);
            lockInfos.add(lockInfo);
        }
        mPasswordAdapter = new PasswordAdapter(lockInfos);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerList.addItemDecoration(new InterestSpaceItemDecorationList(40));
        mRecyclerList.setMotionEventSplittingEnabled(false);
        mRecyclerList.setAdapter(mPasswordAdapter);
    }

    @Override
    public void initdata() {

    }

}

package com.tc.bluetoothlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.view.TitleView;

import butterknife.BindView;
import butterknife.OnClick;

public class LockSettingActivity extends BaseActivity {
    @BindView(R.id.lin_modify_name)
    LinearLayout mLinModifyName;
    @BindView(R.id.bt_un_binding)
    Button mBtUnBinding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lock_set;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTitleView = new TitleView(this);
        mTitleView.setTitleText(getResources().getString(R.string.Lock_set));
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


    @OnClick({R.id.lin_modify_name, R.id.bt_un_binding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_modify_name:
                Intent intent = new Intent(this, AllModifyActivity.class);
                intent.putExtra(AllModifyActivity.INTENT_KEY, AllModifyActivity.MODIFY_LOCK_NAME);
                startActivity(intent);
                break;
            case R.id.bt_un_binding:
                break;
        }
    }
}

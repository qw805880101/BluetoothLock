package com.tc.bluetoothlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.view.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFingerprintActivity extends BaseActivity {
    @BindView(R.id.txt_state)
    TextView mTxtState;
    @BindView(R.id.txt_click_add)
    TextView mTxtClickAdd;
    @BindView(R.id.rl_add_fingerprint)
    RelativeLayout mRlAddFingerprint;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_fingerprint;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTitleView = new TitleView(this);
        mTitleView.setTitleText(this.getResources().getString(R.string.Add_fingerprints));
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

    @OnClick({R.id.rl_add_fingerprint, R.id.txt_click_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_add_fingerprint:
            case R.id.txt_click_add:
                Intent intent = new Intent(this, AllModifyActivity.class);
                intent.putExtra(AllModifyActivity.INTENT_KEY, AllModifyActivity.MODIFY_FINGERPRINT);
                startActivity(intent);
                break;
        }
    }
}

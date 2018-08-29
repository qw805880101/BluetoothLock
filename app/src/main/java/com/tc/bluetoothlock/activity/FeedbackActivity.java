package com.tc.bluetoothlock.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.psylife.wrmvplibrary.utils.StatusBarUtil;
import com.psylife.wrmvplibrary.utils.TitleBuilder;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.view.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.et_feedback_msg)
    EditText mEtFeedbackMsg;
    @BindView(R.id.txt_count)
    TextView mTxtCount;

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        mTitleView = new TitleView(this);
        mTitleView.setTitleText(this.getResources().getString(R.string.feedback));
        mTitleView.setRightText(this.getResources().getString(R.string.confirm));
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

        mEtFeedbackMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTxtCount.setText(editable.length() + "/140");
            }
        });
    }

    @Override
    public void initdata() {

    }
}

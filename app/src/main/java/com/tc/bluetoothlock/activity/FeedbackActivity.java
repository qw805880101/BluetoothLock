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

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.et_feedback_msg)
    EditText mEtFeedbackMsg;
    @BindView(R.id.txt_count)
    TextView mTxtCount;

    public void setStatusBarColor() {
        StatusBarUtil.setColor(this, this.getResources().getColor(R.color.bg_151519));
    }

    @Override
    public View getTitleView() {
        mTitleBuilder = new TitleBuilder(this);
        mTitleBuilder.setTitleText(getResources().getString(R.string.feedback));
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
        return R.layout.activity_feedback;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
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

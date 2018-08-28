package com.tc.bluetoothlock.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.psylife.wrmvplibrary.utils.StringUtils;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.psylife.wrmvplibrary.utils.helper.RxUtil;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.bean.BaseBeanInfo;
import com.tc.bluetoothlock.view.TitleView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Action1;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_verifying)
    EditText etVerifying;
    @BindView(R.id.bt_get_verifying)
    Button btGetVerifying;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_sure_password)
    EditText etSurePassword;
    @BindView(R.id.txt_agreement)
    TextView txtAgreement;
    @BindView(R.id.bt_register)
    Button btRegister;

    private int count = 60;

    private Runnable countDown;

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTitleView = new TitleView(this);
        mTitleView.setTitleText(this.getResources().getString(R.string.bt_register));
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

    @OnClick({R.id.bt_get_verifying, R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_get_verifying:
                getVerifyingCode();
                break;
            case R.id.bt_register:
                register();
                break;
        }
    }

    /**
     * 调用获取验证码接口
     */
    private void getVerifyingCode() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtils.showToast(this, getResources().getString(R.string.phone_number));
            return;
        }
        if (!StringUtils.isPhoneNum(phoneNumber)) {
            ToastUtils.showToast(this, getResources().getString(R.string.Please_enter_the_correct_phone_number));
            return;
        }

        startProgressDialog(this);
        Map map = new HashMap();
        map.put("phone", phoneNumber);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        Observable<BaseBeanInfo> register = mApi.getVerifyingCode(requestBody).compose(RxUtil.<BaseBeanInfo>rxSchedulerHelper());
        mRxManager.add(register.subscribe(new Action1<BaseBeanInfo>() {
            @Override
            public void call(BaseBeanInfo info) {
                stopProgressDialog();
                if (info.getCode() == 200) {
                    ToastUtils.showToast(mContext, getResources().getString(R.string.Send_the_authentication_code_successfully));
                    startCountDown();
                } else {
                    toastMessage("" + info.getCode(), info.getMsg());
                }
            }
        }, this));
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (count > 0) {
                btGetVerifying.setText(count + getResources().getString(R.string.Re_cquisition_after));
            } else {
                count = 60;
                btGetVerifying.setText(getResources().getString(R.string.get_the_verifying_code));
                btGetVerifying.setEnabled(true);
                mHandler.removeCallbacks(countDown);
            }
        }
    };

    /**
     * 开始验证码倒计时
     */
    private void startCountDown() {
        btGetVerifying.setText(count + getResources().getString(R.string.Re_cquisition_after));
        btGetVerifying.setEnabled(false);
        countDown = new Runnable() {
            @Override
            public void run() {
                count--;
                mHandler.sendEmptyMessage(0);
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.postDelayed(countDown, 1000);
    }

    /**
     * 验证注册参数
     */
    private void register() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtils.showToast(this, getResources().getString(R.string.phone_number));
            return;
        }
        if (!StringUtils.isPhoneNum(phoneNumber)) {
            ToastUtils.showToast(this, getResources().getString(R.string.Please_enter_the_correct_phone_number));
            return;
        }

        String verCode = etVerifying.getText().toString().trim();
        if (StringUtils.isEmpty(verCode)) {
            ToastUtils.showToast(this, getResources().getString(R.string.verifying_code));
            return;
        }
        if (verCode.length() < 4) {
            ToastUtils.showToast(this, getResources().getString(R.string.Please_enter_the_correct_verifying_code));
            return;
        }

        String password = etPassword.getText().toString().trim();
        if (StringUtils.isEmpty(password)) {
            ToastUtils.showToast(this, getResources().getString(R.string.password));
            return;
        }

        String surePassword = etSurePassword.getText().toString().trim();
        if (StringUtils.isEmpty(surePassword)) {
            ToastUtils.showToast(this, getResources().getString(R.string.Please_enter_the_password_again));
            return;
        }
        if (!password.equals(surePassword)) {
            ToastUtils.showToast(this, getResources().getString(R.string.The_two_password_does_not_agree_please_reenter_it));
            return;
        }
        startRegister(phoneNumber, password, verCode);
    }

    /**
     * 调用注册接口
     */
    private void startRegister(String phoneNum, String password, String verifyingCode) {
        startProgressDialog(this);
        Map map = new HashMap();
        map.put("phone", phoneNum);
        map.put("code", verifyingCode);
        map.put("password", password);
//        map.put("deviceId", deviceId);
//        map.put("mac", mac);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        Observable<BaseBeanInfo> register = mApi.register(requestBody).compose(RxUtil.<BaseBeanInfo>rxSchedulerHelper());
        mRxManager.add(register.subscribe(new Action1<BaseBeanInfo>() {
            @Override
            public void call(BaseBeanInfo info) {
                stopProgressDialog();
                if (info.getCode() == 200) {
                    ToastUtils.showToast(mContext, getResources().getString(R.string.register_successfully));
                    finish();
                } else {
                    toastMessage("" + info.getCode(), info.getMsg());
                }
            }
        }, this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(countDown);
    }

}

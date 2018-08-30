package com.tc.bluetoothlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.psylife.wrmvplibrary.utils.StatusBarUtil;
import com.psylife.wrmvplibrary.utils.StringUtils;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.psylife.wrmvplibrary.utils.helper.RxUtil;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.Utils;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.bean.BaseBeanInfo;
import com.tc.bluetoothlock.bean.LoginInfo;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Action1;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.txt_forget_password)
    TextView txtForgetPassword;
    @BindView(R.id.txt_register)
    TextView txtRegister;

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initdata() {

    }

    @OnClick({R.id.bt_login, R.id.txt_forget_password, R.id.txt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.txt_forget_password:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.txt_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    /**
     * 验证参数
     */
    private void login() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtils.showToast(this, getResources().getString(R.string.phone_number));
            return;
        }
        if (!StringUtils.isPhoneNum(phoneNumber)) {
            ToastUtils.showToast(this, getResources().getString(R.string.Please_enter_the_correct_phone_number));
            return;
        }

        String password = etPassword.getText().toString().trim();
        if (StringUtils.isEmpty(password)) {
            ToastUtils.showToast(this, getResources().getString(R.string.password));
            return;
        }

        startLogin(phoneNumber, password);
    }

    /**
     * 调用登录接口
     */
    private void startLogin(String phoneNum, String password) {
        startProgressDialog(this);
        Map map = new HashMap();
        map.put("phone", phoneNum);
        map.put("password", password);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        Observable<BaseBeanInfo<LoginInfo>> register = mApi.login(Utils.getHeaderData(), requestBody).compose(RxUtil.<BaseBeanInfo<LoginInfo>>rxSchedulerHelper());
        mRxManager.add(register.subscribe(new Action1<BaseBeanInfo<LoginInfo>>() {
            @Override
            public void call(BaseBeanInfo<LoginInfo> info) {
                stopProgressDialog();
                if (info.getCode() == 200) {
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                } else {
                    toastMessage("" + info.getCode(), info.getMsg());
                }
            }
        }, this));
    }

}

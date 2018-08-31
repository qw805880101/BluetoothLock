package com.tc.bluetoothlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.psylife.wrmvplibrary.utils.LogUtil;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.psylife.wrmvplibrary.utils.helper.RxUtil;
import com.tc.bluetoothlock.MyApplication;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.StringUtils;
import com.tc.bluetoothlock.Utils.Utils;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.bean.BaseBeanInfo;
import com.tc.bluetoothlock.bean.LoginInfo;
import com.tc.bluetoothlock.view.TitleView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Action1;

public class AllModifyActivity extends BaseActivity {

    public static final String INTENT_KEY = "intentType";
    public static final String PASSWORD_NAME = "passwordName";
    public static final String PASSWORD = "password";
    public static final String PASSWORD_STEP = "passwordStep"; //添加密码步骤

    public static final int MODIFY_NAME = 0; //修改昵称
    public static final int MODIFY_PASSWORD = 1; //修改密码
    public static final int MODIFY_FINGERPRINT = 2; //指纹设置
    public static final int MODIFY_WIFI = 3; //WiFi配置
    public static final int ADD_PASSWORD = 4; //添加密码

    public static final int PASSWORD_STEP_01 = 5; //添加密码步骤 5：蓝牙添加密码，  6：上传密码名称
    public static final int PASSWORD_STEP_02 = 6; //添加密码步骤 5：蓝牙添加密码，  6：上传密码名称

    @BindView(R.id.txt_hint)
    TextView mTxtHint;
    @BindView(R.id.et_text)
    EditText mEtText;
    @BindView(R.id.image_del)
    ImageView mImageDel;
    @BindView(R.id.txt_hint_02)
    TextView mTxtHint02;
    @BindView(R.id.et_text_02)
    EditText mEtText02;
    @BindView(R.id.image_del_02)
    ImageView mImageDel02;
    @BindView(R.id.lin_mod_02)
    LinearLayout mLinMod02;

    private int modifyType;

    private int passwordStep; // 密码步骤

    @Override
    public int getLayoutId() {
        return R.layout.activity_all_modify;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTitleView = new TitleView(this);
        mTitleView.setRightText(this.getResources().getString(R.string.complete));
        mTitleView.setLeftOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modifyType == ADD_PASSWORD && passwordStep == PASSWORD_STEP_02) {
                    LogUtil.d("添加密码名称");
                } else
                    finish();
            }
        });
        mTitleView.setRightOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
    }

    @Override
    public void initdata() {
        Intent intent = this.getIntent();
        modifyType = intent.getIntExtra(INTENT_KEY, -1);

        passwordStep = intent.getIntExtra(PASSWORD_STEP, 5);

        if (modifyType == MODIFY_NAME) {
            mTitleView.setTitleText(this.getResources().getString(R.string.Modify_the_nickname));
            mTitleView.setRightText(this.getResources().getString(R.string.confirm));
            mEtText.setHint(this.getResources().getString(R.string.Please_enter_a_new_nickname));
            mEtText.setText(MyApplication.mLoginInfo.getRealName());
        }

        if (modifyType == MODIFY_PASSWORD) {
            mTitleView.setTitleText(this.getResources().getString(R.string.modify_password));
            mTitleView.setRightText(this.getResources().getString(R.string.complete));
            mTxtHint.setText(this.getResources().getString(R.string.password_name));
            mEtText.setHint(this.getResources().getString(R.string.Please_input_password_name));
            if (passwordStep == PASSWORD_STEP_01)
                mEtText.setText(intent.getStringExtra(PASSWORD));
            if (passwordStep == PASSWORD_STEP_02)
                mEtText.setText(intent.getStringExtra(PASSWORD));
        }

        if (modifyType == ADD_PASSWORD) {
            mTitleView.setTitleText(this.getResources().getString(R.string.Add_password));
            mTitleView.setRightText(this.getResources().getString(R.string.complete));
            if (passwordStep == PASSWORD_STEP_01) {
                mTxtHint.setText(this.getResources().getString(R.string.password));
                mEtText.setHint(this.getResources().getString(R.string.Please_input_password));
            }
            if (passwordStep == PASSWORD_STEP_02) {
                mTxtHint.setText(this.getResources().getString(R.string.password_name));
                mEtText.setHint(this.getResources().getString(R.string.Please_input_password_name));
            }
        }

        if (modifyType == MODIFY_FINGERPRINT) {
            mTitleView.setTitleText(this.getResources().getString(R.string.Add_fingerprints));
            mTitleView.setRightText(this.getResources().getString(R.string.complete));
            mTxtHint.setText(this.getResources().getString(R.string.Fingerprints_name));
        }

        if (modifyType == MODIFY_WIFI) {
            mTitleView.setTitleText(this.getResources().getString(R.string.WIFI_configuration));
            mTitleView.setRightText(this.getResources().getString(R.string.complete));
            mTxtHint.setText(this.getResources().getString(R.string.wifi_name));
            mTxtHint02.setText(this.getResources().getString(R.string.wifi_password));
            mEtText.setText(intent.getStringExtra(PASSWORD_NAME) != null && !intent.getStringExtra(PASSWORD_NAME).equals("") ? intent.getStringExtra(PASSWORD_NAME) : "");
            mEtText.setHint(this.getResources().getString(R.string.Please_input_wifi_name));
            mEtText02.setText(intent.getStringExtra(PASSWORD) != null && !intent.getStringExtra(PASSWORD).equals("") ? intent.getStringExtra(PASSWORD) : "");
            mEtText02.setHint(this.getResources().getString(R.string.Please_input_wifi_password));
            mLinMod02.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.image_del, R.id.image_del_02})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_del:
                mEtText.setText("");
                break;
            case R.id.image_del_02:
                break;
        }
    }

    /**
     * 标题栏右侧按钮
     */
    private void confirm() {
        String str = mEtText.getText().toString().trim();

        if (StringUtils.isEmpty(str)) {
            if (modifyType == MODIFY_NAME) { //修改昵称
                ToastUtils.showToast(this, this.getResources().getString(R.string.Please_enter_a_new_nickname));
            }
            if (modifyType == ADD_PASSWORD) { //添加密码
                if (passwordStep == PASSWORD_STEP_01) {
                    ToastUtils.showToast(this, this.getResources().getString(R.string.Please_input_password));
                }
                if (passwordStep == PASSWORD_STEP_02) {
                    ToastUtils.showToast(this, this.getResources().getString(R.string.Please_input_password_name));
                }
            }
            if (modifyType == MODIFY_FINGERPRINT) { //添加指纹
                ToastUtils.showToast(this, this.getResources().getString(R.string.Please_input_fingerprints_name));
            }
            return;
        }

        if (modifyType == MODIFY_NAME) {
            modifyName(str);
        }
        if (modifyType == ADD_PASSWORD) {
            if (passwordStep == PASSWORD_STEP_01) {
                Intent intent = new Intent(this, AllModifyActivity.class);
                intent.putExtra(AllModifyActivity.INTENT_KEY, AllModifyActivity.ADD_PASSWORD);
                intent.putExtra(AllModifyActivity.PASSWORD_STEP, AllModifyActivity.PASSWORD_STEP_02);
                startActivity(intent);
                finish();
            }
            if (passwordStep == PASSWORD_STEP_02) {
                LogUtil.d("添加密码名称");
            }
        }
        if (modifyType == MODIFY_FINGERPRINT) {
//            modifyName(str);
            LogUtil.d("添加指纹");
        }
    }

    /**
     * 调用修改昵称接口
     */
    private void modifyName(String nickName) {
        startProgressDialog(this);
        Map map = new HashMap();
        map.put("nickName", nickName);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        Observable<BaseBeanInfo<LoginInfo>> register = mApi.modifyNickName(Utils.getHeaderData(), requestBody).compose(RxUtil.<BaseBeanInfo<LoginInfo>>rxSchedulerHelper());
        mRxManager.add(register.subscribe(new Action1<BaseBeanInfo<LoginInfo>>() {
            @Override
            public void call(BaseBeanInfo<LoginInfo> info) {
                stopProgressDialog();
                if (info.getCode() == 200) {
                    MyApplication.mLoginInfo = info.getData();
//                        startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                } else {
                    toastMessage("" + info.getCode(), info.getMsg());
                }
            }
        }, this));
    }


}

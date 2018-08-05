package com.tc.bluetoothlock.base;

import android.view.View;

import com.psylife.wrmvplibrary.RxManager;
import com.psylife.wrmvplibrary.base.WRBaseActivity;
import com.psylife.wrmvplibrary.data.net.RxService;
import com.psylife.wrmvplibrary.utils.LogUtil;
import com.psylife.wrmvplibrary.utils.StatusBarUtil;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.api.Api;

import rx.functions.Action1;

/**
 * Created by admin on 2017/8/23.
 */

public abstract class BaseActivity extends WRBaseActivity implements Action1<Throwable> {

    public Api mApi = RxService.createApi(Api.class);

    public RxManager mRxManager = new RxManager();

    public void setStatusBarColor() {
        StatusBarUtil.setTransparent(this);
    }

    @Override
    public View getTitleView() {
        return null;
    }


    /**
     * 显示错误日志
     *
     * @param code
     * @param msg
     */
    public void toastMessage(String code, String msg) {
        if (code.equals("1006")) {

        }
        ToastUtils.showToast(this, msg);
    }

    @Override
    public void call(Throwable throwable) {
        LogUtil.d(throwable.getMessage());
        stopProgressDialog();
        ToastUtils.showToast(this, "网络错误");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxManager.clear();
    }
}

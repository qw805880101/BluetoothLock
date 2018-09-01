package com.tc.bluetoothlock.activity;

import android.Manifest.permission;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.psylife.wrmvplibrary.utils.helper.RxUtil;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.Utils;
import com.tc.bluetoothlock.adapter.LockAdapter;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.bean.BaseBeanInfo;
import com.tc.bluetoothlock.bean.LockInfo;
import com.tc.bluetoothlock.helper.BaseViewHelper;
import com.tc.bluetoothlock.view.InterestSpaceItemDecorationList;
import com.tc.bluetoothlock.view.WaveView;
import com.tc.bluetoothlock.view.TitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.github.xudaojie.qrcodelib.CaptureActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Action1;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends BaseActivity implements OnClickListener {

    public static final int EXTERNAL_STORAGE_REQ_CAMERA_CODE = 10;
    public static final int REQUEST_QR_CODE = 101;

    @BindView(R.id.recycler_lock_list)
    RecyclerView mLockList;
    @BindView(R.id.lin_add_lock)
    LinearLayout mLinAddLock;
    @BindView(R.id.txt_hint)
    TextView txt_hint;

    private LockAdapter mLockAdapter;

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        initTitle();

        mLinAddLock.setOnClickListener(this);

        List<LockInfo> lockInfos = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            LockInfo lockInfo = new LockInfo();
            lockInfo.setId("" + i * 10);
            lockInfos.add(lockInfo);
        }
        mLockAdapter = new LockAdapter(lockInfos);

        mLinAddLock.setVisibility(View.GONE);
        mLockList.setLayoutManager(new LinearLayoutManager(this));
        mLockList.addItemDecoration(new InterestSpaceItemDecorationList(40));
        mLockList.setAdapter(mLockAdapter);
    }

    @Override
    public void initdata() {
    }

    @Override
    public void onClick(View view) {
        if (view == mLinAddLock) {
            requestCameraPerm();
        }

        if (view == mTitleView.getIvLeft()) {
            ToastUtils.showToast(this, "点我干啥");
            startActivity(new Intent(this, AccountActivity.class));
        }

        if (view == mTitleView.getIvRight()) {
            ToastUtils.showToast(this, "点我弄啥~");
//            requestCameraPerm();
            start("100000000");
        }
    }

    /**
     * 请求二维码拍照
     */
    private void requestCameraPerm() {
        if (VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this, permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{permission.CAMERA},
                        EXTERNAL_STORAGE_REQ_CAMERA_CODE);
            } else {
                Intent i = new Intent(this, CaptureActivity.class);
                startActivityForResult(i, REQUEST_QR_CODE);
            }
        } else {
            Intent i = new Intent(this, CaptureActivity.class);
            startActivityForResult(i, REQUEST_QR_CODE);
        }
    }

    /**
     * 扫描二维码获取锁信息
     *
     * @param lockNo
     */
    private void start(String lockNo) {
        startProgressDialog(this);
        Map map = new HashMap();
        map.put("lockNo", lockNo);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        Observable<BaseBeanInfo<LockInfo>> getLockInfo = mApi.getLockInfo(Utils.getHeaderData(), requestBody).compose(RxUtil.<BaseBeanInfo<LockInfo>>rxSchedulerHelper());
        mRxManager.add(getLockInfo.subscribe(new Action1<BaseBeanInfo<LockInfo>>() {
            @Override
            public void call(BaseBeanInfo<LockInfo> info) {
                stopProgressDialog();
                if (info.getCode() == 200 && info.getMsg().equals("SUCCESS")) {
                    Intent intent = new Intent(mContext, LockInfoActivity.class);
                    intent.putExtra(LockInfoActivity.LOCK_INFO, info.getData());
                    startActivity(intent);

                } else {
                    toastMessage("" + info.getCode(), info.getMsg());
                }
            }
        }, this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && requestCode == REQUEST_QR_CODE
                && data != null) {
            String result = data.getStringExtra("result");
            start(result);
        }
    }

    /**
     * 初始化标题栏信息
     *
     * @return
     */
    private void initTitle() {
        mTitleView = new TitleView(this);
        mTitleView.setTitleTextColor(this, R.color.white);
        SpannableString spannableString = new SpannableString(this.getResources().getString(R.string.e_home));
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(this.getResources().getColor(R.color.txt_0577fe));
        spannableString.setSpan(colorSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mTitleView.getTvTitle().setText(spannableString);
        mTitleView.setTitleBgRes(R.color.bg_151519);
        mTitleView.setLeftImage(R.mipmap.nav_icon_personal);
        mTitleView.setLeftOnClickListener(this);
        mTitleView.setRightImage(R.mipmap.nav_icon_add);
        mTitleView.setRightOnClickListener(this);
    }
}

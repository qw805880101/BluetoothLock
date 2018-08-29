package com.tc.bluetoothlock.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.psylife.wrmvplibrary.utils.StatusBarUtil;
import com.psylife.wrmvplibrary.utils.TakePhotosDispose;
import com.psylife.wrmvplibrary.utils.TitleBuilder;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.psylife.wrmvplibrary.utils.helper.RxUtil;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.Utils.DialogUtil;
import com.tc.bluetoothlock.base.BaseActivity;
import com.tc.bluetoothlock.view.TitleView;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Action1;

import static android.os.Build.VERSION_CODES.M;
import static com.tc.bluetoothlock.activity.MainActivity.EXTERNAL_STORAGE_REQ_CAMERA_CODE;

public class AccountInfoActivity extends BaseActivity {
    @BindView(R.id.iv_head)
    RoundedImageView mIvHead;
    @BindView(R.id.lin_head)
    LinearLayout mLinHead;
    @BindView(R.id.txt_user_name)
    TextView mTxtUserName;
    @BindView(R.id.txt_mobile)
    TextView mTxtMobile;

    private String path;

    @Override
    public View getTitleView() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_account_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTitleView = new TitleView(this);
        mTitleView.setTitleText(this.getResources().getString(R.string.Personal_nformation));
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

    @OnClick({R.id.iv_head, R.id.txt_user_name, R.id.txt_mobile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_head:
                startTakePhoto();
                break;
            case R.id.txt_user_name:
                break;
            case R.id.txt_mobile:
                break;
        }
    }

    private void startTakePhoto() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_REQ_CAMERA_CODE);
            } else {
                DialogUtil.showTakePhotoDialog(this);
            }
        } else {
            DialogUtil.showTakePhotoDialog(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TakePhotosDispose.CROPIMAGE:
                case TakePhotosDispose.TAKEPHOTO:
                    path = DialogUtil.currentFileName.getAbsolutePath();
                    break;
                case TakePhotosDispose.PICKPHOTO:
                    // 相册选图
                    Uri selectedImage = data.getData();
                    if (!selectedImage.toString().substring(0, 7).equals("content")) {
                        // 如果路径错误
                        String picturePath = selectedImage.getPath();
                        path = picturePath;
                    } else {
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        path = picturePath;
                    }
                    break;
                default:
                    break;
            }
            System.out.println("path = " + path);
            uploadHead();
//            Glide.with(this).load(new File(path))
//                    .placeholder(R.mipmap.wellcom) //设置占位图
//                    .error(R.mipmap.wellcom) //设置错误图片
//                    .crossFade() //设置淡入淡出效果，默认300ms，可以传参
//                    .transform(new GlideCircleTransform(context)).into(mIvHead);
        }
    }

    /**
     * 上传头像
     */
    private void uploadHead() {
        startProgressDialog(this);
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/from-data"), file);
//        Observable<BaseBeanClass<HeadInfo>> uploadHead = mXuanXingApi.uploadHead(MyApplication.loginInfo.getMemberInfo().getMemberId(),
//                MyApplication.loginInfo.getP_token(), requestBody).compose(RxUtil.<BaseBeanClass<HeadInfo>>rxSchedulerHelper());
//        mRxManager.add(uploadHead.subscribe(new Action1<BaseBeanClass<HeadInfo>>() {
//            @Override
//            public void call(BaseBeanClass<HeadInfo> baseBean) {
//                stopProgressDialog();
//                if (baseBean.getCode().equals("0000")) {
//                    final Map<String, String> map = new LinkedHashMap();
//                    map.put("headicon", baseBean.getData().getHeadIcon());
//                    //事件发送 通知我的界面更新头像
//                    EventBus.getDefault().post(new SendEvent("headicon", baseBean.getData().getHeadIcon()));
//                    XUtils.modUserInfo(PersonalInfoActivity.this, map);
//                    XUtils.loadHeadIcon(mContext, new File(path), mIvHead);
//                } else {
//                    ToastUtils.showToast(PersonalInfoActivity.this, baseBean.getMsg());
//                }
//            }
//        }, this));
    }

//    Action1<BaseBean> mAction1 = new Action1<BaseBean>() {
//        @Override
//        public void call(BaseBean baseBean) {
//            if (baseBean.getCode().equals("0000")) {
//                final Map<String, String> map = new LinkedHashMap();
//                map.put("birthday", birthday);
//                //事件发送
//                XUtils.modUserInfo(PersonalInfoActivity.this, map);
//                ToastUtils.showToast(PersonalInfoActivity.this, baseBean.getMsg());
//                finish();
//            } else {
//                toastMessage(baseBean.getCode(), baseBean.getMsg());
//            }
//        }
//    };
}

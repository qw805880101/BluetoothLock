package com.tc.bluetoothlock.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tc.bluetoothlock.R;

public class TitleView {

    private View rootView;
    private TextView tvTitle;
    private ImageButton ivLeft;
    private ImageButton ivRight;
    private TextView tvLeft;
    private TextView tvRight;
    private RelativeLayout rlTitleView;

    public View getRootView() {
        return rootView;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageView getIvLeft() {
        return ivLeft;
    }

    public ImageView getIvRight() {
        return ivRight;
    }

    public TextView getTvLeft() {
        return tvLeft;
    }

    public TextView getTvRight() {
        return tvRight;
    }

    /**
     * Activity中使用这个构造方法
     */
    public TitleView(Activity mActivity) {
        rootView = mActivity.getWindow().getDecorView();
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelOffset(R.dimen.title_height));
//        rootView.setLayoutParams(params);
//        rootView = context.findViewById(R.id.rl_titlebar);
        if (rootView == null) {
            return;
        }
        tvTitle = (TextView) rootView.findViewById(R.id.txt_title);
        ivLeft = (ImageButton) rootView.findViewById(R.id.ibt_left);
        ivRight = (ImageButton) rootView.findViewById(R.id.ibt_right);
        tvLeft = (TextView) rootView.findViewById(R.id.txt_left);
        tvRight = (TextView) rootView.findViewById(R.id.txt_right);
        rlTitleView = (RelativeLayout) rootView.findViewById(R.id.rl_title);
        //取消状态栏修改颜色
        if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT_WATCH) {
            rlTitleView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mActivity.getResources().getDimensionPixelOffset(R.dimen.title_height)));
            tvTitle.setPadding(0, mActivity.getResources().getDimensionPixelOffset(R.dimen.title_pd), 0, 0);
            ivLeft.setPadding(0, mActivity.getResources().getDimensionPixelOffset(R.dimen.title_pd), 0, 0);
            ivRight.setPadding(0, mActivity.getResources().getDimensionPixelOffset(R.dimen.title_pd), 0, 0);
            tvLeft.setPadding(mActivity.getResources().getDimensionPixelOffset(R.dimen.title_pd), mActivity.getResources().getDimensionPixelOffset(R.dimen.title_pd), 0, 0);
            tvRight.setPadding(0, mActivity.getResources().getDimensionPixelOffset(R.dimen.title_pd), mActivity.getResources().getDimensionPixelOffset(R.dimen.title_pd), 0);
        } else {
            rlTitleView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mActivity.getResources().getDimensionPixelOffset(R.dimen.title_height_60)));
        }
    }

    /**
     * Fragment中使用这个构造方法
     */
    public TitleView(View context) {
        rootView = context.findViewById(R.id.rl_titlebar);
        if (rootView == null) {
            return;
        }
        tvTitle = (TextView) rootView.findViewById(R.id.txt_title);
        ivLeft = (ImageButton) rootView.findViewById(R.id.ibt_left);
        ivRight = (ImageButton) rootView.findViewById(R.id.ibt_right);
        tvLeft = (TextView) rootView.findViewById(R.id.txt_left);
        tvRight = (TextView) rootView.findViewById(R.id.txt_right);
    }

    // title
    public TitleView setTitleBgRes(int resid) {
        rootView.setBackgroundResource(resid);
        return this;
    }

    public TitleView setTitleText(String text) {
        tvTitle.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        tvTitle.setText(text);
        return this;
    }

    // left
    public TitleView setLeftImage(int resId) {
        ivLeft.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        ivLeft.setImageResource(resId);
        return this;
    }

    public TitleView setLeftText(String text) {
        tvLeft.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        tvLeft.setText(text);
        return this;
    }

    public TitleView setLeftOnClickListener(View.OnClickListener listener) {
        if (ivLeft.getVisibility() == View.VISIBLE) {
            ivLeft.setOnClickListener(listener);
        } else if (tvLeft.getVisibility() == View.VISIBLE) {
            tvLeft.setOnClickListener(listener);
        }
        return this;
    }

    // right
    public TitleView setRightImage(int resId) {
        ivRight.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        ivRight.setImageResource(resId);
        return this;
    }

    public TitleView setRightText(String text) {
        tvRight.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        tvRight.setText(text);
        return this;
    }

    public TitleView setRightTextColor(Context context, int resId) {
        tvRight.setTextColor(context.getResources().getColor(resId));
        return this;
    }

    public TitleView setTitleTextColor(Context context, int resId) {
        tvTitle.setTextColor(context.getResources().getColor(resId));
        return this;
    }


    public TitleView setRightOnClickListener(View.OnClickListener listener) {
        if (ivRight.getVisibility() == View.VISIBLE) {
            ivRight.setOnClickListener(listener);
        } else if (tvRight.getVisibility() == View.VISIBLE) {
            tvRight.setOnClickListener(listener);
        }
        return this;
    }

    public View build() {
        return rootView;
    }

}

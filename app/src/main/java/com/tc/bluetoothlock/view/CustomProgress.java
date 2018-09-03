package com.tc.bluetoothlock.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.tc.bluetoothlock.R;

public class CustomProgress extends View {

    private Paint mPaint;

    private ValueAnimator animator; //动画

    private int startAngle = 270;
    private int endAngle = 270;

    // view 的宽高
    private int mTotalWidth, mTotalHeight;

    public CustomProgress(Context context) {
        super(context, null);
    }

    public CustomProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(getResources().getColor(R.color.bg_002652));
        //设置画笔粗细
        mPaint.setStrokeWidth(4);
        //设置是否为空心
        mPaint.setStyle(Style.STROKE);
        canvas.drawCircle(mTotalWidth / 2, mTotalHeight / 2, mTotalWidth / 2 - 4, mPaint);

        mPaint.setColor(getResources().getColor(R.color.txt_0577fe));
//        LinearGradient lg = new LinearGradient(0, 0, mTotalWidth, mTotalHeight, getResources().getColor(R.color.bg_002652), getResources().getColor(R.color.txt_0577fe), Shader.TileMode.MIRROR); //参数一为渐变
//        mPaint.setShader(lg);
        //设置画笔粗细
        mPaint.setStrokeWidth(4);
        canvas.drawArc(new RectF(0 + 4, 0 + 4, mTotalWidth - 4, mTotalHeight - 4), startAngle, endAngle, false, mPaint);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(300);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (endAngle < 360) {
//                    startAngle += 10;
//                } else {
//                    endAngle = 36;
//                }
//                postInvalidate();
//
//            }
//        }).start();
    }

    private void startAnim(int startProgress) {
        if (animator == null) {
            animator = ObjectAnimator.ofInt(0, startProgress);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int i = (int) animation.getAnimatedValue();
//                CustomProgress.this.startAngle = i * 360 / 100 + startAngle;
                    CustomProgress.this.startAngle += 5;
                    postInvalidate();
                }
            });
//        animator.setStartDelay(500);   //设置延迟开始
            animator.setDuration(2000);
            animator.setInterpolator(new LinearInterpolator());   //动画匀速
            animator.setRepeatCount(ValueAnimator.INFINITE); //设置动画循环播放
        }
        animator.start();
    }

    /**
     * 设置角度
     */
    public void start() {
//        postInvalidate();
        startAnim(100);
    }

    public void stop() {
        animator.cancel();
    }

}

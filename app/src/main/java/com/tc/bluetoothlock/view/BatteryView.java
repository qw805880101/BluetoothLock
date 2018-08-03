package com.tc.bluetoothlock.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tc.bluetoothlock.R;

public class BatteryView extends View {

    private double battery; //电量

    private Paint mPaint;

    // view 的宽高
    private int mTotalWidth, mTotalHeight;

    private Rect mSrcRect;

    private Rect mDestRect;

    public BatteryView(Context context) {
        super(context, null);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
    }

    /**
     * 设置电量
     *
     * @param battery
     */
    public void setBattery(int battery) {
        this.battery = battery;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        Bitmap bitmap = setImgSize(mTotalWidth, mTotalHeight);
        mSrcRect = new Rect(0, 0, bitmap.getWidth(), mTotalHeight);
        mDestRect = new Rect(0, 0, bitmap.getWidth(), mTotalHeight);
        canvas.drawBitmap(bitmap, mSrcRect, mDestRect, mPaint);
        int a = (int) (battery / bitmap.getWidth() * bitmap.getWidth());
        mPaint.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.txt_1d79ea), PorterDuff.Mode.SRC_IN));
        mSrcRect = new Rect(0, 0, a, mTotalHeight);
        mDestRect = new Rect(0, 0, a, mTotalHeight);
        canvas.drawBitmap(bitmap, mSrcRect, mDestRect, mPaint);
    }

    public int getW(){
        return mTotalWidth;
    }

    private Bitmap getBitmap() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.icon_battery);
    }

    public Bitmap setImgSize(int newWidth, int newHeight) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_battery);
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

}

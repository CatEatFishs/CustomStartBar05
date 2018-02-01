package com.lm.customstart05;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Lm on 2018/1/31.
 * Email:1002464056@qq.com
 */

public class CustomStartView extends View {
    private Bitmap mNormalBitmap, mSelectBitmap;
    private int mGradeNumber = 5;
    private int mCurrentGrade = 0;
    //间隔
    private int mInterval =2;
    public CustomStartView(Context context) {
        this(context,null);
    }

    public CustomStartView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomStartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomStartView);
        int normalId = typedArray.getResourceId(R.styleable.CustomStartView_normalstart, 0);
        if (normalId==0) {
            throw new RuntimeException("请设置属性 normalstart");
        }

        mNormalBitmap = BitmapFactory.decodeResource(getResources(), normalId);
        int selectId = typedArray.getResourceId(R.styleable.CustomStartView_selectstart, 0);
        if (selectId==0) {
            throw new RuntimeException("请设置属性 selectstart");
        }
        mSelectBitmap = BitmapFactory.decodeResource(getResources(), selectId);
        mGradeNumber = typedArray.getInt(R.styleable.CustomStartView_startNumber, mGradeNumber);
        mInterval= (int) typedArray.getDimension(R.styleable.CustomStartView_interval,mInterval);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = mSelectBitmap.getWidth() * mGradeNumber + getPaddingLeft() + getPaddingRight()+(mGradeNumber-1) * mInterval;
        int height = mSelectBitmap.getHeight() + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(width,height);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mGradeNumber; i++) {
            // i*星星的宽度
            int x = getPaddingLeft()+i * mSelectBitmap.getWidth()+ i * mInterval;
            if (mCurrentGrade>i) {
                canvas.drawBitmap(mSelectBitmap, x, 0, null);
            }else {
                canvas.drawBitmap(mNormalBitmap, x, 0, null);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                //当前触摸的X
                float currentX = event.getX() - getPaddingLeft();
                int startX = mSelectBitmap.getWidth() + mInterval ;

                int currentGradeNumber=((int)(currentX/startX)+1);
                // 范围问题
                if (currentX<0)
                    currentGradeNumber=0;

                if (currentGradeNumber<0)
                    currentGradeNumber=0;
                if (currentGradeNumber>mGradeNumber)
                    currentGradeNumber=mGradeNumber;

                // 分数相同的情况下不要绘制了 , 尽量减少onDraw()的调用
                if(currentGradeNumber == mCurrentGrade){
                    return true;
                }
                // 再去刷新显示
                mCurrentGrade = currentGradeNumber;
                if (mTouchNumberListener != null) {
                    mTouchNumberListener.touchNumber(mCurrentGrade);
                }
                invalidate();
                break;
        }
        return true;
    }

    private TouchNumberListener mTouchNumberListener;

    public interface TouchNumberListener {
        void touchNumber(int touchNumber);
    }

    public void setTouchNumberListener(TouchNumberListener touchNumberListener) {
        this.mTouchNumberListener = touchNumberListener;
    }
}

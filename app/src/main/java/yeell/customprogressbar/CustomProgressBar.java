package yeell.customprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by YeeLL on 17/2/24.
 */

public class CustomProgressBar extends View {

    private Paint mPaint;
    private Rect mLeftRect;
    private Rect mRightRect;

    //view的宽
    private int mWidth = 0;
    //View的高
    private int mHeight = 0;

    //View的左边坐标
    private int mLeft = 0;
    //View的右边坐标
    private int mRight = 0;
    //View的上边坐标
    private int mTop = 0;
    //View的下边坐标
    private int mBottom = 0;

    //progressbar一份的长度
    private int mLength = 0;

    //左边长方形长度
    private int mLeftLength = 0;

    private int mProgressSize = 0;

    private boolean startProgress = false;

    //速度
    private int mSpeed = 1;

    public CustomProgressBar(Context context) {
        super(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }


    public void initPaint(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(ScreenUtils.getSP(context, 18));
        mLeftRect = new Rect();
        mRightRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            //warpcontent
            width = ScreenUtils.getHeight(getContext()) - getPaddingRight() - getPaddingLeft();
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            height = ScreenUtils.getDP(getContext(), 40) - getPaddingTop() - getPaddingBottom();
        }

        Log.e("TAG", "width:" + width + "height:" + height);
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mLeft = left;
        this.mTop = top;
        this.mRight = ScreenUtils.getWith(getContext()) - getPaddingRight();
        this.mBottom = bottom;

        Log.e("TAG", "left:" + left + "top:" + top + "right" + right + "bottom" + bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLeftRect(canvas);

        drawRightRect(canvas);

        drawText(canvas);
    }

    /**
     * 画右边的长方形
     *
     * @param canvas
     */
    private void drawRightRect(Canvas canvas) {
        mPaint.setColor(Color.BLUE);
        mLeftRect.set(mLeft, mTop + ScreenUtils.getDP(getContext(), 5), mLeft + mLeftLength, mBottom - ScreenUtils.getDP(getContext(), 5));
        canvas.drawRect(mLeftRect, mPaint);
    }

    /**
     * 画数字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        mPaint.setColor(Color.RED);
        if (mLeftLength < mRight - 100) {
            if (mLeftLength < 60) {
                canvas.drawText(mProgressSize + "%", 20, mTop + (mBottom - mTop) / 2, mPaint);
            } else {
                canvas.drawText(mProgressSize + "%", mLeft + mLeftLength - 40, mTop + (mBottom - mTop) / 2, mPaint);
            }
        } else {
            canvas.drawText(mProgressSize + "%", mRight - 100, mTop + (mBottom - mTop) / 2, mPaint);
        }
    }

    /**
     * 画左边的长方形
     *
     * @param canvas
     */
    private void drawLeftRect(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mRightRect.set(mLeft + mLeftLength, mTop + ScreenUtils.getDP(getContext(), 5), mRight, mBottom - ScreenUtils.getDP(getContext(), 5));
        canvas.drawRect(mRightRect, mPaint);
    }

    public void start() {
        mLeftLength = 0;
        mProgressSize = 0;
        startProgress = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!startProgress) {
                    if (mLeftLength < mRight) {
                        mLeftLength = mLeftLength + mSpeed;
                        mProgressSize = (int) (((double) mLeftLength / (double) mRight) * 100);
                    } else {
                        mLeftLength = mRight;
                        mProgressSize = 100;
                        startProgress = true;
                    }
                    try {
                        new Thread().sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }

            }
        }).start();
    }

    /**
     * 设置速度
     *
     * @param speed
     */
    public void setSpeed(int speed) {
        this.mSpeed = speed;
    }
}

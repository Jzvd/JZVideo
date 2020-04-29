package cn.jzvd.demo.api.BigUIChangeAG.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.DipAndPx;


/**
 * @author HRR
 * @date 2019/12/19
 */
public class LoadingView extends View {
    //圆形的矩形轮廓
    RectF rectF;
    //圆弧扫过的角度
    float sweepAngle;
    float rotateAngle;
    ObjectAnimator objectAnimator;
    ObjectAnimator rotateAnimator;
    AnimatorSet animatorSet;
    //画笔
    private Paint mPaint;
    //圆圈边框宽度
    private int borderWidth = DipAndPx.dip2px(getContext(), 2);

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getContext().getResources().getColor(R.color.ThemeColor));
        rectF = new RectF();
        objectAnimator = ObjectAnimator.ofFloat(
                this,
                "sweepAngle",
                0, 180);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setInterpolator(new CycleInterpolator(0.5f));
        rotateAnimator = ObjectAnimator.ofFloat(this, "rotateAngle", 0, 720);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, rotateAnimator);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //绘制圆弧
        canvas.rotate(rotateAngle, getHeight() / 2, getWidth() / 2);
        canvas.drawArc(rectF, 0, sweepAngle, false, mPaint);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //圆形的矩形轮廓
        rectF.set(borderWidth, borderWidth, getMeasuredWidth() - borderWidth, getMeasuredHeight() - borderWidth);
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        postInvalidate();
    }

    public float getRotateAngle() {
        return rotateAngle;
    }

    public void setRotateAngle(float rotateAngle) {
        this.rotateAngle = rotateAngle;
        postInvalidate();
    }

}
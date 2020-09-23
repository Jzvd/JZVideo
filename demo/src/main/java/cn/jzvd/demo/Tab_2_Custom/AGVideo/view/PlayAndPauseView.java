package cn.jzvd.demo.Tab_2_Custom.AGVideo.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

import cn.jzvd.demo.Tab_2_Custom.AGVideo.Utils;
import cn.jzvd.demo.R;


/**
 * 仿爱奇艺客户端播放暂停按钮动画
 */
public class PlayAndPauseView extends View {
    private int animationStep = 1;
    private int animationType = 2;
    //左边动画时间
    private long leftDuration = 300;
    //右边动画时间
    private long rightDuration = 2 * leftDuration / 3;

    private Path leftPath;

    private PathMeasure leftPathMeasure;

    private float leftZoomValue;

    //线的间隔
    private float mLineInterval = Utils.dp2px(15);
    //线的长度
    private float mLineLength = Utils.dp2px(18);
    private Paint linePaint;
    //线的粗度
    private float lineThickness = Utils.dp2px(4);

    private Float mLeftCurAnimValue;

    private Path mLeftDstPath;

    private Float mRightCurAnimValue;

    private Path mRightDstPath;

    private AnimatorSet orderAnimatorSet;

    private AnimatorSet reverseAnimatorSet;


    private Path rightPath;

    private PathMeasure rightPathMeasure;

    private float rightZoomValue;

    public PlayAndPauseView(Context paramContext) {
        super(paramContext);
    }

    public PlayAndPauseView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init() {
        leftPath = new Path();
        rightPath = new Path();
        mLeftDstPath = new Path();
        mRightDstPath = new Path();
        leftPathMeasure = new PathMeasure();
        rightPathMeasure = new PathMeasure();
        linePaint = new Paint(1);
        linePaint.setColor(getResources().getColor(R.color.colorWhite));
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(lineThickness);
        mLeftCurAnimValue = 1.0f;
        mRightCurAnimValue = 1.0f;
        initOrderAnimation();
        initReverseAnimation();
    }

    private void initOrderAnimation() {
        //左边线条缩放隐藏动画
        final ObjectAnimator leftZoomHideAnimation = ObjectAnimator.ofFloat(this, "leftZoomValue", 0f, 1.0f);
        //左边线条缩放动画
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this, "leftZoomValue", 0f, 0.2f);
        objectAnimator1.setDuration(200);
        objectAnimator1.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator param1Animator) {
            }

            public void onAnimationEnd(Animator param1Animator) {
                //左边线条缩放结束后开始左边线条的缩放隐藏动画
                animationStep = 2;
                leftZoomHideAnimation.start();
            }

            public void onAnimationRepeat(Animator param1Animator) {
            }

            public void onAnimationStart(Animator param1Animator) {
            }
        });
        //左边线条动画
        ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(0f, 1.0f);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
                mLeftCurAnimValue = (Float) param1ValueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator1.setDuration(leftDuration);
        //右边线条动画
        ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(0f, 1.0f);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
                mRightCurAnimValue = (Float) param1ValueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator2.setDuration(rightDuration);

        AnimatorSet animatorSet = new AnimatorSet();
        //左边动画和右边动画一起执行
        animatorSet.playTogether(valueAnimator1, valueAnimator2);
        animatorSet.start();
        orderAnimatorSet = new AnimatorSet();
        //先执行左边缩放动画，再执行animatorSet
        orderAnimatorSet.playSequentially(objectAnimator1, animatorSet);
    }

    private void initReverseAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "leftZoomValue", 0.2f, 0f);
        objectAnimator.setDuration(200);
        ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(0f, 1.0f);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
                mLeftCurAnimValue = (Float) param1ValueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator1.setDuration(leftDuration);
        ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(0f, 1.0f);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
                mRightCurAnimValue = (Float) param1ValueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator2.setDuration(leftDuration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimator1, valueAnimator2);
        animatorSet.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator param1Animator) {
            }

            public void onAnimationEnd(Animator param1Animator) {
                animationStep = 1;
            }

            public void onAnimationRepeat(Animator param1Animator) {
            }

            public void onAnimationStart(Animator param1Animator) {
            }
        });
        animatorSet.start();
        reverseAnimatorSet = new AnimatorSet();
        reverseAnimatorSet.playSequentially(animatorSet, objectAnimator);
    }

    private void pause() {
        animationType = 2;
        reverseAnimatorSet.start();
    }

    private void play() {
        animationType = 1;
        orderAnimatorSet.start();
    }

    public void playOrPause() {
        if (animationType == 1) {
            pause();
        } else if (animationType == 2) {
            play();
        }
    }

    public float getLeftZoomValue() {
        return leftZoomValue;
    }

    public void setLeftZoomValue(float paramFloat) {
        leftZoomValue = paramFloat;
        postInvalidate();
    }

    public float getRightZoomValue() {
        return rightZoomValue;
    }

    public void setRightZoomValue(float paramFloat) {
        rightZoomValue = paramFloat;
        postInvalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onDraw(Canvas canvas) {
        float leftWidth = (getWidth() - mLineInterval + 2 * lineThickness) / 2;
        float height = getHeight();
        float lineLength = mLineLength;
        float startHeight = (height - lineLength) / 2;
        if (animationStep == 1) {
            //画左边线条
            canvas.drawLine(leftWidth, startHeight + lineLength * leftZoomValue, leftWidth, startHeight + lineLength, linePaint);
            //画右边线条
            canvas.drawLine(leftWidth + mLineInterval, startHeight - mLineLength * leftZoomValue,
                    leftWidth + mLineInterval, startHeight + mLineLength * (1 - leftZoomValue), linePaint);
        }
        if (animationStep == 2) {
            //绘制左边线条动画轨迹
            leftPath.moveTo(leftWidth, startHeight + mLineLength);
            leftPath.lineTo(leftWidth, startHeight + (1 - leftZoomValue) * mLineLength);
            leftPath.lineTo(leftWidth, startHeight);
            leftPath.cubicTo(leftWidth, startHeight, leftWidth + Utils.dp2px(1), startHeight - Utils.dp2px(2), leftWidth + Utils.dp2px(4), startHeight - Utils.dp2px(1));
            leftPath.lineTo(leftWidth + mLineInterval, startHeight + mLineLength / 2 - Utils.dp2px(1));
            leftPath.cubicTo(leftWidth + mLineInterval, startHeight + mLineLength / 2.0F - Utils.dp2px(1), leftWidth + mLineInterval + Utils.dp2px(2), startHeight + mLineLength / 2 - Utils.dp2px(1) + Utils.dp2px(2), leftWidth + mLineInterval, startHeight + mLineLength / 2.0F - Utils.dp2px(1.0F) + Utils.dp2px(4.0F));
            leftPath.lineTo(leftWidth + Utils.dp2px(4), startHeight + mLineLength + Utils.dp2px(1));
            leftPath.cubicTo(leftWidth + Utils.dp2px(4), startHeight + mLineLength + Utils.dp2px(1), leftWidth + Utils.dp2px(1), startHeight + mLineLength + Utils.dp2px(2), leftWidth, startHeight + mLineLength);
            leftPath.lineTo(leftWidth, startHeight + mLineLength / 2);
            leftPathMeasure.setPath(leftPath, false);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeCap(Paint.Cap.ROUND);
            mLeftDstPath.reset();
            float leftPathMeasureLength = leftPathMeasure.getLength();
            if (animationType == 1) {
                leftPathMeasure.getSegment(mLineLength / 2 * mLeftCurAnimValue, leftPathMeasureLength * mLeftCurAnimValue + mLineLength, mLeftDstPath, true);
            } else {
                leftPathMeasure.getSegment(mLineLength / 2.0F * (1 - mLeftCurAnimValue),
                        leftPathMeasureLength - leftPathMeasureLength * mLeftCurAnimValue + mLineLength, mLeftDstPath, true);
            }
            canvas.drawPath(mLeftDstPath, linePaint);
            //绘制右边线条动画轨迹
            rightPath.moveTo(leftWidth + mLineInterval, (float) (startHeight - mLineLength * 0.2));
            rightPath.lineTo(leftWidth + mLineInterval, startHeight + mLineLength);
            rightPath.arcTo(leftWidth, startHeight + mLineLength - Utils.dp2px(8)
                    , leftWidth + mLineInterval, startHeight + mLineLength + Utils.dp2px(8)
                    , 0, 180, false);
            rightPath.lineTo(leftWidth, startHeight);
            rightPathMeasure.setPath(rightPath, false);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeJoin(Paint.Join.ROUND);
            float rightPathMeasureLength = rightPathMeasure.getLength();
            mRightDstPath.reset();
            if (animationType == 1) {
                rightPathMeasure.getSegment(rightPathMeasureLength * mRightCurAnimValue, rightPathMeasureLength * mRightCurAnimValue + mLineLength
                        , mRightDstPath, true);
            } else {
                rightPathMeasure.getSegment(rightPathMeasureLength - rightPathMeasureLength * mRightCurAnimValue
                        , rightPathMeasureLength - rightPathMeasureLength * mRightCurAnimValue + mLineLength * mRightCurAnimValue
                        , mRightDstPath, true);
            }
            canvas.drawPath(mRightDstPath, linePaint);
        }
    }
}


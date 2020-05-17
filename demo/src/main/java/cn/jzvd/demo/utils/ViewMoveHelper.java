package cn.jzvd.demo.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import cn.jzvd.demo.utils.ViewAttr;

/**
 * 视图平移动画工具类
 *
 * @author Liberations
 */
public class ViewMoveHelper {
    private ViewGroup targetView;
    private ViewAttr fromViewInfo;
    private ViewAttr toViewInfo;
    private long duration;

    /**
     * @param targetView   目标布局
     * @param fromViewInfo 起始view坐标信息
     * @param toViewInfo   目标view坐标信息
     * @param duration     动画时长
     */
    public ViewMoveHelper(ViewGroup targetView, ViewAttr fromViewInfo, ViewAttr toViewInfo, long duration) {
        this.targetView = targetView;
        this.fromViewInfo = fromViewInfo;
        this.toViewInfo = toViewInfo;
        this.duration = duration;
    }

    public void startAnim() {
        ObjectAnimator xAnim = ObjectAnimator.ofFloat(targetView, "x", fromViewInfo.getX(), toViewInfo.getX());
        ObjectAnimator yAnim = ObjectAnimator.ofFloat(targetView, "y", fromViewInfo.getY(), toViewInfo.getY());
        ValueAnimator widthAnim = ValueAnimator.ofInt(fromViewInfo.getWidth(), toViewInfo.getWidth());
        ValueAnimator heightAnim = ValueAnimator.ofInt(fromViewInfo.getHeight(), toViewInfo.getHeight());
        widthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams param = targetView.getLayoutParams();
                param.width = (int) valueAnimator.getAnimatedValue();
                targetView.setLayoutParams(param);
            }
        });
        heightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams param = targetView.getLayoutParams();
                param.height = (int) valueAnimator.getAnimatedValue();
                targetView.setLayoutParams(param);
            }
        });

        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(xAnim, yAnim, widthAnim, heightAnim);
        animation.setDuration(duration);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }
}

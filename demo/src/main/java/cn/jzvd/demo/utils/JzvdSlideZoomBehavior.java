package cn.jzvd.demo.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import cn.jzvd.JZUtils;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;

///**
// * eg: 皮皮虾 视频列表高是不固定的  demo是固定的
// * 要想实现皮皮虾一模一样的效果  请自行修改
//  皮皮虾效果后面在搞 这里只是简单列表缩放
//   参考列子：皮皮虾
// */
public class JzvdSlideZoomBehavior extends CoordinatorLayout.Behavior<JzvdStd> {

    private int minHeight;
    private int scrollingId;
    private int childHeight;
    private Context context;
    private View scrollingView;
    private JzvdStd jzvdStd;
    private FlingRunnable runnable;
    private ViewDragHelper viewDragHelper;
    private OverScroller overScroller;

    public JzvdSlideZoomBehavior() {

    }

    public JzvdSlideZoomBehavior(Context context, AttributeSet attributeSet) {
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.JzvdSlideZoomBehavior, 0, 0);
        scrollingId = array.getResourceId(R.styleable.JzvdSlideZoomBehavior_scrolling_id, 0);
        minHeight = array.getDimensionPixelSize(R.styleable.JzvdSlideZoomBehavior_min_height, 200);
        array.recycle();
        this.context = context;
        overScroller = new OverScroller(context);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent,
                                 @NonNull JzvdStd child,
                                 int layoutDirection) {
        if (viewDragHelper == null) {
            viewDragHelper = ViewDragHelper.create(parent, 1.0f, mCallback);
            this.scrollingView = parent.findViewById(scrollingId);
            this.jzvdStd = child;
            this.childHeight = JZUtils.getScreenHeight(context);
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }


    private final ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return jzvdStd.getBottom() > minHeight && jzvdStd.getBottom() < childHeight;
        }


        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            int isMove = 0;
            if (!scrollingView.canScrollVertically(-1))
                isMove = viewDragHelper.getTouchSlop();
            return isMove;
        }


        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if (jzvdStd == null || dy == 0)
                return 0;
            if (dy > 0 && jzvdStd.getBottom() < minHeight || (dy < 0 && jzvdStd.getBottom() > childHeight) || (dy < 0 && (scrollingView != null && scrollingView.canScrollVertically(-1)))) {
                return 0;
            }

            int maxConsumed = 0;
            if (dy > 0) {
                if (jzvdStd.getBottom() + dy > childHeight) {
                    maxConsumed = childHeight - jzvdStd.getBottom();
                } else {
                    maxConsumed = dy;
                }
            } else {
                if (jzvdStd.getBottom() + dy < minHeight) {
                    maxConsumed = minHeight - jzvdStd.getBottom();
                } else {
                    maxConsumed = dy;
                }
            }
            ViewGroup.LayoutParams layoutParams = jzvdStd.getLayoutParams();
            layoutParams.height = layoutParams.height + maxConsumed;
            jzvdStd.setLayoutParams(layoutParams);
            return maxConsumed;
        }


        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (jzvdStd.getBottom() > minHeight && jzvdStd.getBottom() < childHeight && yvel != 0) {
                jzvdStd.removeCallbacks(runnable);
                runnable = new FlingRunnable(jzvdStd);
                runnable.fling((int) xvel, (int) yvel);
            }
        }
    };

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull JzvdStd child, @NonNull MotionEvent ev) {
        if (viewDragHelper == null)
            return super.onTouchEvent(parent, child, ev);
        viewDragHelper.processTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull JzvdStd child, @NonNull MotionEvent ev) {
        if (viewDragHelper == null)
            return super.onInterceptTouchEvent(parent, child, ev);
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }


    private class FlingRunnable implements Runnable {
        private final View mFlingView;

        public FlingRunnable(View flingView) {
            mFlingView = flingView;
        }

        public void fling(int xvel, int yvel) {
            /*
             * startX:开始的X值，由于我们不需要再水平方向滑动 所以为0
             * startY:开始滑动时Y的起始值，那就是flingview的bottom值
             * xvel:水平方向上的速度，实际上为0的
             * yvel:垂直方向上的速度。即松手时的速度
             * minX:水平方向上 滚动回弹的越界最小值，给0即可
             * maxX:水平方向上 滚动回弹越界的最大值，实际上给0也是一样的
             * minY：垂直方向上 滚动回弹的越界最小值，给0即可
             * maxY:垂直方向上，滚动回弹越界的最大值，实际上给0 也一样
             */
            overScroller.fling(0, mFlingView.getBottom(), xvel, yvel, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            run();
        }

        @Override
        public void run() {
            ViewGroup.LayoutParams params = mFlingView.getLayoutParams();
            int height = params.height;
            if (overScroller.computeScrollOffset() && height >= minHeight && height <= childHeight) {
                int newHeight = Math.min(overScroller.getCurrY(), childHeight);
                if (newHeight != height) {
                   if (newHeight < minHeight) newHeight = minHeight;
                    params.height = newHeight;
                    mFlingView.setLayoutParams(params);
                }
                ViewCompat.postOnAnimation(mFlingView, this);
            } else {
                mFlingView.removeCallbacks(this);
            }
        }
    }
}

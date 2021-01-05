package cn.jzvd.demo.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import cn.jzvd.demo.R;


public class JzvdDependBehavior extends CoordinatorLayout.Behavior<View> {

    private int dependId;

    public JzvdDependBehavior() {

    }

    public JzvdDependBehavior(Context context, AttributeSet attributeSet) {
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.JzvdDependBehavior, 0, 0);
        dependId = array.getResourceId(R.styleable.JzvdDependBehavior_depend, 0);
        array.recycle();
    }


    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependId == dependency.getId();
    }


    @Override
    public boolean onMeasureChild(@NonNull CoordinatorLayout parent,
                                  @NonNull View child,
                                  int parentWidthMeasureSpec,
                                  int widthUsed,
                                  int parentHeightMeasureSpec,
                                  int heightUsed) {


        View dependView = parent.findViewById(dependId);
        if (dependView == null) {
            return false;
        }
        parent.onMeasureChild(child, parentWidthMeasureSpec, 0, parentHeightMeasureSpec,  dependView.getBottom());
        return true;
    }


    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent,
                                 @NonNull View child,
                                 int layoutDirection) {
        View anchorView = parent.findViewById(dependId);
        if (anchorView == null) {
            return false;
        }
        parent.onLayoutChild(child, layoutDirection);
        child.offsetTopAndBottom(anchorView.getBottom());
        return true;
    }
}

package cn.jzvd.demo;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.JzvdStdRv;
import cn.jzvd.demo.CustomJzvd.ViewAttr;

/**
 * 列表平滑进入详情页
 */
public class ActivityListViewDetail extends AppCompatActivity {
    private LinearLayout llContent;
    private FrameLayout container;
    private TextView tvTitle;
    private static final String TAG = "ActivityListViewDetail";
    private ViewAttr attr;
    public final long DURATION = 400;
    private ObjectAnimator bgAnimator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("ActivityListViewDetail");
        setContentView(R.layout.activity_listview_detail);
        attr = getIntent().getParcelableExtra("attr");
        llContent = findViewById(R.id.ll_content);
        tvTitle = findViewById(R.id.tv_title);
        container = findViewById(R.id.surface_container);

        llContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                llContent.getViewTreeObserver().removeOnPreDrawListener(this);
                int[] l = new int[2];
                llContent.getLocationOnScreen(l);
                llContent.setTranslationY(attr.getY() - l[1] - (container.getMeasuredHeight() - attr.getHeight()) / 2 - tvTitle.getMeasuredHeight());
                container.setScaleX(attr.getWidth() / (float) container.getMeasuredWidth());
                container.setScaleY(attr.getHeight() / (float) container.getMeasuredHeight());
                tvTitle.setAlpha(0);
                llContent.animate().translationY(0).setDuration(DURATION);
                tvTitle.animate().alpha(1f).setDuration(DURATION);
                container.animate().scaleX(1f).scaleY(1f).setDuration(DURATION);
                ViewParent parent = JzvdStdRv.CURRENT_JZVD.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(JzvdStdRv.CURRENT_JZVD);
                }
                container.addView(JzvdStdRv.CURRENT_JZVD, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return true;
            }
        });
        bgAnimator = ObjectAnimator.ofInt(llContent, "backgroundColor", 0x00000000, 0xff000000);
        bgAnimator.setEvaluator(new ArgbEvaluator());
        bgAnimator.setDuration(DURATION);
        bgAnimator.start();

    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        backAnimation();
        //super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backAnimation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backAnimation() {
        final int[] location = new int[2];
        llContent.getLocationOnScreen(location);
        tvTitle.animate().alpha(0f).setDuration(DURATION);
        container.animate().translationY(attr.getY() - location[1] - (container.getHeight() - attr.getHeight()) / 2 - attr.getHeight()).setDuration(DURATION);
        bgAnimator.reverse();
        tvTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                //should better try other method
                ActivityListViewToDetail.activityListViewToDetail.animateFinish();
                finish();
            }
        }, DURATION);
    }


}

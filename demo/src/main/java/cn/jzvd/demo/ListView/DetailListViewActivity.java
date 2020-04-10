package cn.jzvd.demo.ListView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.JzvdStdRv;
import cn.jzvd.demo.ListView.adapter.CommentAdapter;
import cn.jzvd.demo.utils.ViewAttr;
import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.ViewMoveHelper;

/**
 * @author Liberations
 */
public class DetailListViewActivity extends AppCompatActivity {
    public final long DURATION = 300;
    private LinearLayout llContent;
    private FrameLayout container;
    private ViewAttr attr;
    private ViewAttr currentAttr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JZUtils.hideSystemUI(this);
        JZUtils.hideStatusBar(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_listview_detail);
        attr = getIntent().getParcelableExtra("attr");
        llContent = findViewById(R.id.ll_content);
        container = findViewById(R.id.surface_container);

        container.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                container.getViewTreeObserver().removeOnPreDrawListener(this);
                ViewParent parent = JzvdStdRv.CURRENT_JZVD.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(JzvdStdRv.CURRENT_JZVD);
                }
                container.addView(JzvdStdRv.CURRENT_JZVD, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                currentAttr = new ViewAttr();
                int[] location = new int[2];
                container.getLocationInWindow(location);
                currentAttr.setX(location[0]);
                currentAttr.setY(location[1]);
                currentAttr.setWidth(container.getMeasuredWidth());
                currentAttr.setHeight(container.getMeasuredHeight());
                new ViewMoveHelper(container, attr, currentAttr, DURATION).startAnim();

                AlphaAnimation animation = new AlphaAnimation(0, 1);
                animation.setDuration(DURATION);
                llContent.setAnimation(animation);
                animation.start();
                return true;
            }
        });

        CommentAdapter commentAdapter = new CommentAdapter();
        RecyclerView recyclerView = findViewById(R.id.rv_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        backAnimation();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Jzvd.releaseAllVideos();
//    }

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
        new ViewMoveHelper(container, currentAttr, attr, DURATION).startAnim();
        llContent.setVisibility(View.GONE);
        container.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListViewToDetailActivity.listViewToDetailActivity.animateFinish();
                finish();
                overridePendingTransition(0, 0);
            }
        }, DURATION);

    }


}

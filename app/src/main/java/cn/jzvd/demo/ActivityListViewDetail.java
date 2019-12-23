package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.JzvdStdRv;

/**
 * 列表平滑进入详情页
 */
public class ActivityListViewDetail extends AppCompatActivity {
    private FrameLayout container;
    private static final String TAG = "ActivityListViewDetail";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("ActivityListViewDetail");
        setContentView(R.layout.activity_listview_detail);
        container = findViewById(R.id.surface_container);
        ViewParent parent = JzvdStdRv.CURRENT_JZVD.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(JzvdStdRv.CURRENT_JZVD);
        }
        container.addView(JzvdStdRv.CURRENT_JZVD, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}

package cn.jzvd.demo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.MenuItem;
import android.view.Window;

import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.AutoPlayUtils;
import cn.jzvd.demo.CustomJzvd.ViewAttr;

/**
 * 列表平滑进入详情页
 */
public class ActivityListViewToDetail extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterSmoothRecyclerView adapterVideoList;
    private static final String TAG = "ActivityListViewToDetai";
    public static ActivityListViewToDetail activityListViewToDetail;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_recyclerview_content);
        activityListViewToDetail = this;
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapterVideoList = new AdapterSmoothRecyclerView(this);
        recyclerView.setAdapter(adapterVideoList);
        adapterVideoList.setOnVideoClick(new AdapterSmoothRecyclerView.OnVideoClick() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void videoClick(ViewAttr viewAttr, int position) {
                Intent intent = new Intent(ActivityListViewToDetail.this, ActivityListViewDetail.class);
                intent.putExtra("attr", viewAttr);
                startActivity(intent);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy != 0) {
                    AutoPlayUtils.onScrollReleaseAllVideos(mLayoutManager.findFirstVisibleItemPosition(), mLayoutManager.findLastVisibleItemPosition(), 0.2f);
                }
            }
        });
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }


    public void animateFinish(){
        adapterVideoList.notifyDataSetChanged();
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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityListViewToDetail = null;
        AutoPlayUtils.positionInList = -1;
        Jzvd.releaseAllVideos();
    }
}

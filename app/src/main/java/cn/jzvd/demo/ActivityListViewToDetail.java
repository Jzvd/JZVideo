package cn.jzvd.demo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.AutoPlayUtils;

/**
 * 列表平滑进入详情页
 */
public class ActivityListViewToDetail extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterSmoothRecyclerView adapterVideoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("ActivityListViewToDetail");
        setContentView(R.layout.activity_recyclerview_content);
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapterVideoList = new AdapterSmoothRecyclerView(this);
        recyclerView.setAdapter(adapterVideoList);
        adapterVideoList.setOnVideoClick(new AdapterSmoothRecyclerView.OnVideoClick() {
            @Override
            public void videoClick(View container, int position) {
                ViewCompat.setTransitionName(container, "videoView");
                Intent intent = new Intent(ActivityListViewToDetail.this, ActivityListViewDetail.class);
                // 这里指定了共享的视图元素
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ActivityListViewToDetail.this, container, "videoView");
                ActivityCompat.startActivity(ActivityListViewToDetail.this, intent, options.toBundle());
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    AutoPlayUtils.onScrollPlayVideo(recyclerView, mLayoutManager.findFirstVisibleItemPosition(), mLayoutManager.findLastVisibleItemPosition());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy != 0) {
                    AutoPlayUtils.onScrollReleaseAllVideos(mLayoutManager.findFirstVisibleItemPosition(), mLayoutManager.findLastVisibleItemPosition(), 0.2f);
                }
            }
        });
        setExitSharedElementCallback(new TransitionCallBack());

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public class TransitionCallBack extends SharedElementCallback {
        @Override
        public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
            //保证动画执行完毕再去添加视频到列表，解决闪屏问题
            adapterVideoList.notifyDataSetChanged();
        }
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
        AutoPlayUtils.positionInList = -1;
        Jzvd.releaseAllVideos();
    }
}

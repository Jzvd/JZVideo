package cn.jzvd.demo.Tab_3_List.ListView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;

import cn.jzvd.Jzvd;
import cn.jzvd.demo.Tab_3_List.ListView.adapter.RecyclerViewLoadMoreAdapter;
import cn.jzvd.demo.R;
import cn.jzvd.demo.Tab_3_List.ListView.entity.VideoEntity;
import cn.jzvd.demo.Urls;

/**
 * Created by yujunkui on 16/8/29.
 */
public class RecyclerViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewLoadMoreAdapter adapterVideoList;
    SmartRefreshLayout refreshLayout;
    ArrayList<VideoEntity> videos;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.recyclerview));
        setContentView(R.layout.activity_recyclerview_content);

        initView();
        initData();
        initListener();
    }

    private void initListener() {
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Jzvd jzvd = view.findViewById(R.id.videoplayer);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastPositon = linearLayoutManager.findLastVisibleItemPosition();
                int count = recyclerView.getAdapter().getItemCount();
                if(newState == RecyclerView.SCROLL_STATE_IDLE && count > 3 && lastPositon >= count - 3){
                    loadMore();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initView() {
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerview);
    }

    private void initData() {
        videos = new ArrayList<>();
        addData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterVideoList = new RecyclerViewLoadMoreAdapter(this,videos);
        recyclerView.setAdapter(adapterVideoList);
    }

    private void addData() {
        for (int i = 0; i < Urls.videoUrls[0].length; i++) {
            VideoEntity videoEntity = new VideoEntity();
            videoEntity.setUrl(Urls.videoUrls[0][i]);
            videoEntity.setTitle(Urls.videoTitles[0][i]);
            videoEntity.setThumb(Urls.videoPosters[0][i]);
            videos.add(videoEntity);
        }
    }

    private void loadData() {
        //模拟网络加载
        refreshLayout.finishRefresh(1500);
        videos.clear();
        addData();
        //随机打乱
        Collections.shuffle(videos);
        adapterVideoList.notifyDataSetChanged();
    }

    /**
     * 重复添加数据，模拟加载更多
     */
    private void loadMore() {
        refreshLayout.finishLoadMore(1500);
        addData();
        //随机打乱
        Collections.shuffle(videos);
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
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
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

}

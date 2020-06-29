package cn.jzvd.demo.Tab_3_List.ListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.demo.Tab_3_List.ListView.adapter.SmoothRecyclerViewAdapter;
import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.AutoPlayUtils;
import cn.jzvd.demo.utils.ViewAttr;
import cn.jzvd.demo.utils.ViewMoveHelper;

/**
 * 列表平滑进入详情页
 * <p>1.获取当前播放的JZVD添加到详情页中
 * <p>2.获取列表中JZVD的坐标，宽高，获取详情页JZVD坐标，宽高，借助ViewMoveHelper实现平移{@link ViewMoveHelper}
 *
 * @author Liberations
 */
public class ListViewToDetailActivity extends AppCompatActivity {
    public static ListViewToDetailActivity listViewToDetailActivity;
    RecyclerView recyclerView;
    SmoothRecyclerViewAdapter adapterVideoList;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        JZUtils.hideSystemUI(this);
        JZUtils.hideStatusBar(this);
        Jzvd.TOOL_BAR_EXIST = false;
        setContentView(R.layout.activity_recyclerview_content);
        listViewToDetailActivity = this;
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapterVideoList = new SmoothRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapterVideoList);
        adapterVideoList.setOnVideoClick(new SmoothRecyclerViewAdapter.OnVideoClick() {
            @Override
            public void videoClick(ViewGroup focusView, ViewAttr viewAttr, int position) {
                Intent intent = new Intent(ListViewToDetailActivity.this,
                        DetailListViewActivity.class);
                intent.putExtra("attr", viewAttr);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy != 0) {
                    AutoPlayUtils.onScrollReleaseAllVideos(mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition(), 0.2f);
                }
            }
        });
    }


    public void animateFinish() {
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
        Jzvd.TOOL_BAR_EXIST = true;
        listViewToDetailActivity = null;
        AutoPlayUtils.positionInList = -1;
        Jzvd.releaseAllVideos();
    }
}

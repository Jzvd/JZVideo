package cn.jzvd.demo.ComplexFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.jzvd.demo.ComplexFragment.ListView.AutoPlayListViewActivity;
import cn.jzvd.demo.ComplexFragment.ListView.ListViewFragmentViewPagerActivity;
import cn.jzvd.demo.ComplexFragment.ListView.ListViewMultiHolderActivity;
import cn.jzvd.demo.ComplexFragment.ListView.NormalListViewActivity;
import cn.jzvd.demo.ComplexFragment.ListView.RecyclerViewActivity;
import cn.jzvd.demo.ComplexFragment.ListView.ListViewToDetailActivity;
import cn.jzvd.demo.ComplexFragment.ListView.tiktok.ActivityTikTok;
import cn.jzvd.demo.R;

/**
 * Created by Nathen on 16/7/31.
 */
public class ListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.listview));
        setContentView(R.layout.activity_listview);
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

    public void clickNormal(View view) {
        startActivity(new Intent(ListViewActivity.this, NormalListViewActivity.class));

    }

    public void clickListViewFragmentViewpager(View view) {
        startActivity(new Intent(ListViewActivity.this, ListViewFragmentViewPagerActivity.class));

    }

    public void clickMultiHolder(View view) {
        startActivity(new Intent(ListViewActivity.this, ListViewMultiHolderActivity.class));

    }

    public void clickRecyclerView(View view) {
        startActivity(new Intent(ListViewActivity.this, RecyclerViewActivity.class));
    }

    public void clickListSmoothToDetail(View view) {
        startActivity(new Intent(ListViewActivity.this, ListViewToDetailActivity.class));
    }

    public void clickListAutoPlay(View view) {
        startActivity(new Intent(ListViewActivity.this, AutoPlayListViewActivity.class));
    }

    public void clickTikTok(View view) {
        startActivity(new Intent(ListViewActivity.this, ActivityTikTok.class));
    }

}

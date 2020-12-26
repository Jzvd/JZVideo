package cn.jzvd.demo.Tab_4_More;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jzvd.jzvideo.UrlsKt;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.JzvdDanmu;
import cn.jzvd.demo.R;

/**
 * @author Liberations
 * @time 2020-12-01
 * @des 弹幕库 https://github.com/bilibili/DanmakuFlameMaster
 */
public class DanmuActivity extends AppCompatActivity implements View.OnClickListener {
    private JzvdDanmu jzvdDanmu;
    private Button btnDanmu, btnDiyDanmu,showDanmu,hideDanmu;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.danmu_show));
        setContentView(R.layout.activity_danmu);
        btnDanmu = findViewById(R.id.btn_add_danmu);
        btnDiyDanmu = findViewById(R.id.btn_diy_danmu);
        showDanmu = findViewById(R.id.show_danmu);
        hideDanmu = findViewById(R.id.hide_danmu);
        jzvdDanmu = findViewById(R.id.jz_danmu_player);
        btnDanmu.setOnClickListener(this);
        btnDiyDanmu.setOnClickListener(this);
        showDanmu.setOnClickListener(this);
        hideDanmu.setOnClickListener(this);


        jzvdDanmu.setUp(UrlsKt.getVideos()[14], UrlsKt.getTitles()[14], Jzvd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[14]).into(jzvdDanmu.posterImageView);
        testDanmu();
        jzvdDanmu.startButton.performClick();

    }

    /**
     * 模拟弹幕
     */
    private void testDanmu() {
        handler.removeCallbacksAndMessages(null);
        handler.post(new Runnable() {
            @Override
            public void run() {
                jzvdDanmu.addDanmaku("嫂子666", false);
                handler.postDelayed(this, 100);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZUtils.clearSavedProgress(this, null);
        Jzvd.releaseAllVideos();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_danmu:
                jzvdDanmu.showDanmmu();
                break;
            case R.id.hide_danmu:
                jzvdDanmu.hideDanmmu();
                break;
            case R.id.btn_add_danmu:
                jzvdDanmu.addDanmaku("骚还是各位骚呀", true);
                break;
            case R.id.btn_diy_danmu:
                jzvdDanmu.addDanmakuWithDrawable();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        jzvdDanmu.releaseDanMu();
    }
}

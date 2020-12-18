package cn.jzvd.demo.Tab_1_Basic;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jzvd.jzvideo.UrlsKt;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.CustomMedia.JZMediaIjk;
import cn.jzvd.demo.R;

public class PreloadingActivity extends AppCompatActivity {

    JzvdStd jzvdStd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.preloading));
        setContentView(R.layout.activity_preloading);
        jzvdStd = findViewById(R.id.jz_video);

        jzvdStd.setUp(UrlsKt.getTitles()[7]
                , UrlsKt.getTitles()[7], Jzvd.SCREEN_NORMAL, JZMediaIjk.class);
        
        Glide.with(this).load(UrlsKt.getTitles()[7]).into(jzvdStd.posterImageView);

    }

    public void clickStartPreloading(View view) {
        jzvdStd.startPreloading();
    }

    public void clickStartVideoAfterPreloading(View view) {
        jzvdStd.startVideoAfterPreloading();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }


}

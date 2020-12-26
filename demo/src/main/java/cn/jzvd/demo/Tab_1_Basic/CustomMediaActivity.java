package cn.jzvd.demo.Tab_1_Basic;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jzvd.jzvideo.UrlsKt;

import cn.jzvd.JZMediaSystem;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.CustomMedia.JZMediaAliyun;
import cn.jzvd.demo.CustomMedia.JZMediaExo;
import cn.jzvd.demo.CustomMedia.JZMediaIjk;
import cn.jzvd.demo.R;

/**
 * Created by Nathen on 2017/11/23.
 */

public class CustomMediaActivity extends AppCompatActivity {
    JzvdStd jzvdStd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.custom_mediaplayer));
        setContentView(R.layout.activity_api_custom_mediaplayer);

        jzvdStd = findViewById(R.id.videoplayer);

        jzvdStd.setUp(UrlsKt.getVideos()[9]
                , UrlsKt.getTitles()[9], JzvdStd.SCREEN_NORMAL);

        Glide.with(this)
                .load(UrlsKt.getThumbnails()[9])
                .into(jzvdStd.posterImageView);

    }


    public void clickChangeToIjkplayer(View view) {
        Jzvd.releaseAllVideos();
        jzvdStd.setUp(UrlsKt.getVideos()[1]
                , UrlsKt.getTitles()[1], JzvdStd.SCREEN_NORMAL, JZMediaIjk.class);
        jzvdStd.startVideo();
        Toast.makeText(this, "Change to Ijkplayer", Toast.LENGTH_SHORT).show();
    }

    public void clickChangeToSystem(View view) {
        Jzvd.releaseAllVideos();
        jzvdStd.setUp(UrlsKt.getVideos()[1]
                , UrlsKt.getTitles()[1], JzvdStd.SCREEN_NORMAL, JZMediaSystem.class);
        jzvdStd.startVideo();
        Toast.makeText(this, "Change to MediaPlayer", Toast.LENGTH_SHORT).show();
    }

    public void clickChangeToExo(View view) {
        Jzvd.releaseAllVideos();
        jzvdStd.setUp(UrlsKt.getVideos()[1]
                , UrlsKt.getTitles()[1], JzvdStd.SCREEN_NORMAL, JZMediaExo.class);
        jzvdStd.startVideo();
        Toast.makeText(this, "Change to ExoPlayer", Toast.LENGTH_SHORT).show();
    }

    public void clickChangeToAliyun(View view) {
        Jzvd.releaseAllVideos();
        jzvdStd.setMediaInterface(JZMediaAliyun.class);
        jzvdStd.startVideo();
        Toast.makeText(this, "Change to AliyunPlayer", Toast.LENGTH_SHORT).show();
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

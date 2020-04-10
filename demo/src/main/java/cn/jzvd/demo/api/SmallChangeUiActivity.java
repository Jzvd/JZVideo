package cn.jzvd.demo.api;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.CustomJzvd.JzvdStdAutoCompleteAfterFullscreen;
import cn.jzvd.demo.CustomJzvd.JzvdStdLockScreen;
import cn.jzvd.demo.CustomJzvd.JzvdStdMp3;
import cn.jzvd.demo.CustomJzvd.JzvdStdShowShareButtonAfterFullscreen;
import cn.jzvd.demo.CustomJzvd.JzvdStdShowTextureViewAfterAutoComplete;
import cn.jzvd.demo.CustomJzvd.JzvdStdShowTitleAfterFullscreen;
import cn.jzvd.demo.CustomJzvd.JzvdStdSpeed;
import cn.jzvd.demo.CustomJzvd.JzvdStdVolume;
import cn.jzvd.demo.CustomJzvd.JzvdStdVolumeAfterFullscreen;
import cn.jzvd.demo.R;
import cn.jzvd.demo.Urls;

/**
 * Created by Nathen on 16/7/31.
 */
public class SmallChangeUiActivity extends AppCompatActivity {
    JzvdStdShowShareButtonAfterFullscreen jzvdStdWithShareButton;
    JzvdStdShowTitleAfterFullscreen jzvdStdShowTitleAfterFullscreen;
    JzvdStdShowTextureViewAfterAutoComplete jzvdStdShowTextureViewAfterAutoComplete;
    JzvdStdAutoCompleteAfterFullscreen jzvdStdAutoCompleteAfterFullscreen;
    JzvdStdVolumeAfterFullscreen jzvdStdVolumeAfterFullscreen;
    JzvdStdMp3 jzvdStdMp3;
    JzvdStdSpeed jzvdStdSpeed;
    JzvdStdLockScreen lockScreen;
    JzvdStdVolume jzvdStdVolume;

    JzvdStd jzvdStd_1_1, jzvdStd_16_9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("SmallChangeUI");
        setContentView(R.layout.activity_ui_small_change);


        lockScreen = findViewById(R.id.lock_screen);
        lockScreen.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子定身");
        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(lockScreen.posterImageView);


        jzvdStdWithShareButton = findViewById(R.id.custom_videoplayer_standard_with_share_button);
        jzvdStdWithShareButton.setUp(Urls.videoUrlList[3], "饺子想呼吸", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosterList[3])
                .into(jzvdStdWithShareButton.posterImageView);


        jzvdStdShowTitleAfterFullscreen = findViewById(R.id.custom_videoplayer_standard_show_title_after_fullscreen);
        jzvdStdShowTitleAfterFullscreen.setUp(Urls.videoUrlList[4], "饺子想摇头", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosterList[4])
                .into(jzvdStdShowTitleAfterFullscreen.posterImageView);

        jzvdStdShowTextureViewAfterAutoComplete = findViewById(R.id.custom_videoplayer_standard_show_textureview_aoto_complete);
        jzvdStdShowTextureViewAfterAutoComplete.setUp(Urls.videoUrlList[5], "饺子想旅行", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosterList[5])
                .into(jzvdStdShowTextureViewAfterAutoComplete.posterImageView);

        jzvdStdAutoCompleteAfterFullscreen = findViewById(R.id.custom_videoplayer_standard_aoto_complete);
        jzvdStdAutoCompleteAfterFullscreen.setUp(Urls.videoUrls[0][1], "饺子没来", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStdAutoCompleteAfterFullscreen.posterImageView);

        jzvdStd_1_1 = findViewById(R.id.jz_videoplayer_1_1);
        jzvdStd_1_1.setUp(Urls.videoUrls[0][1], "饺子有事吗", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStd_1_1.posterImageView);
        jzvdStd_1_1.widthRatio = 1;
        jzvdStd_1_1.heightRatio = 1;

        jzvdStd_16_9 = findViewById(R.id.jz_videoplayer_16_9);
        jzvdStd_16_9.setUp(Urls.videoUrls[0][1], "饺子来不了", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStd_16_9.posterImageView);
        jzvdStd_16_9.widthRatio = 16;
        jzvdStd_16_9.heightRatio = 9;

        jzvdStdVolumeAfterFullscreen = findViewById(R.id.jz_videoplayer_volume);
        jzvdStdVolumeAfterFullscreen.setUp(Urls.videoUrls[0][1], "饺子摇摆", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStdVolumeAfterFullscreen.posterImageView);

        jzvdStdMp3 = findViewById(R.id.jz_videoplayer_mp3);
        jzvdStdMp3.setUp(Urls.videoUrls[0][1],
                "饺子你听", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStdMp3.posterImageView);

        jzvdStdSpeed = findViewById(R.id.jz_videoplayer_speed);
        jzvdStdSpeed.setUp(Urls.videoUrls[0][1],
                "饺子快点", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStdSpeed.posterImageView);


        jzvdStdVolume = findViewById(R.id.custom_videoplayer_standard_with_volume_button);
        jzvdStdVolume.setUp(Urls.videoUrls[0][1],
                "饺子吃莽莽", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStdVolume.posterImageView);
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

package cn.jzvd.demo.Tab_1_Basic;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jzvd.jzvideo.UrlsKt;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;

/**
 * Created by Nathen on 2017/11/2.
 */

public class RotationVideoSizeActivity extends AppCompatActivity {

    JzvdStd myJzvdStd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.video_cover_and_orientation));
        setContentView(R.layout.activity_api_rotation_videosize);

        myJzvdStd = findViewById(R.id.jz_video);
        myJzvdStd.setUp(UrlsKt.getVideos()[26], UrlsKt.getTitles()[26]
                , JzvdStd.SCREEN_NORMAL);
        Glide.with(this)
                .load(UrlsKt.getThumbnails()[26])
                .into(myJzvdStd.posterImageView);
        // The Point IS 或者这样写也可以
//        myJzvdStd.videoRotation = 180;
    }


    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
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

    public void clickRotationTo0(View view) {
        Jzvd.setTextureViewRotation(0);
    }

    public void clickRotationTo90(View view) {
        Jzvd.setTextureViewRotation(90);
    }

    public void clickRotationTo180(View view) {
        Jzvd.setTextureViewRotation(180);
    }

    public void clickRotationTo270(View view) {
        Jzvd.setTextureViewRotation(270);
    }

    public void clickVideoImageDiaplayAdapter(View view) {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
    }

    public void clickVideoImageDisplayFillParent(View view) {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
    }

    public void clickVideoImageDisplayFillCrop(View view) {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
    }

    public void clickVideoImageDiaplayOriginal(View view) {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL);
    }
}

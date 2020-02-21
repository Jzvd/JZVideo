package cn.jzvd.demo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by HRR on 2020/02/22.
 */
public class ActivityScreenRotate extends AppCompatActivity implements ScreenRotateUtils.OrientationChangeListener {
    JzvdStd mJzvdStd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("ScreenRotate");
        setContentView(R.layout.activity_screen_rotate);
        mJzvdStd = findViewById(R.id.jz_video);
        mJzvdStd.setUp(VideoConstant.videoUrlList[0], "饺子不信"
                , JzvdStd.SCREEN_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbList[0])
                .into(mJzvdStd.thumbImageView);
        ScreenRotateUtils.getInstance(this.getApplicationContext()).setOrientationChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenRotateUtils.getInstance(this).start(this);
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
        ScreenRotateUtils.getInstance(this).stop();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenRotateUtils.getInstance(this.getApplicationContext()).setOrientationChangeListener(null);
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
    public void orientationChange(int orientation) {
        if (Jzvd.CURRENT_JZVD != null
                && (mJzvdStd.state == Jzvd.STATE_PLAYING || mJzvdStd.state == Jzvd.STATE_PAUSE)
                && mJzvdStd.screen != Jzvd.SCREEN_TINY) {
            //顺时针旋转
            if (ScreenRotateUtils.isClockwise) {
                if (orientation >= 75 && orientation <= 264 && mJzvdStd.screen != Jzvd.SCREEN_FULLSCREEN) {
                    changeScreenFullReverseLandscape();
                } else if (orientation >= 265 && orientation <= 344 && mJzvdStd.screen == Jzvd.SCREEN_FULLSCREEN
                        && getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                    changeScreenFullLandscape();
                } else if ((orientation >= 345 && orientation <= 359) && mJzvdStd.screen == Jzvd.SCREEN_FULLSCREEN) {
                    changeScrenNormal();
                }
            } else {
                //逆时针旋转
                if (orientation >= 115 && orientation <= 285 && mJzvdStd.screen != Jzvd.SCREEN_FULLSCREEN) {
                    changeScreenFullLandscape();
                } else if (orientation >= 16 && orientation <= 114 && mJzvdStd.screen == Jzvd.SCREEN_FULLSCREEN
                        && getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    changeScreenFullReverseLandscape();
                } else if ((orientation >= 0 && orientation <= 15) && mJzvdStd.screen == Jzvd.SCREEN_FULLSCREEN) {
                    changeScrenNormal();
                }
            }
        }
    }


    /**
     * 竖屏并退出全屏
     */
    private void changeScrenNormal() {
        if (mJzvdStd != null && mJzvdStd.screen == Jzvd.SCREEN_FULLSCREEN) {
            mJzvdStd.backPress();
        }
    }

    /**
     * 横屏
     */
    private void changeScreenFullLandscape() {
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        mJzvdStd.gotoScreenFullscreen();
    }


    /**
     * 反向横屏
     */
    private void changeScreenFullReverseLandscape() {
        Jzvd.FULLSCREEN_ORIENTATION=ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
        mJzvdStd.gotoScreenFullscreen();
    }
}

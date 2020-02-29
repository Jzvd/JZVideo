package cn.jzvd.demo;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by HRR on 2020/02/22.
 */
public class ActivityScreenRotate extends AppCompatActivity implements ScreenRotateUtils.OrientationChangeListener {
    JzvdStd mJzvdStd;
    SensorManager sensorManager;
    private OrientationSensorListener listener;
    private static int DATA_X = 0;
    private static int DATA_Y = 1;
    private static int DATA_Z = 2;
    private static int ORIENTATION_UNKNOWN = -1;
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
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        listener = new OrientationSensorListener();
//        ScreenRotateUtils.getInstance(this.getApplicationContext()).setOrientationChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        ScreenRotateUtils.getInstance(this).start(this);
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
        sensorManager.unregisterListener(listener);
//        ScreenRotateUtils.getInstance(this).stop();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ScreenRotateUtils.getInstance(this.getApplicationContext()).setOrientationChangeListener(null);
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
//        if (Jzvd.CURRENT_JZVD != null
//                && (mJzvdStd.state == Jzvd.STATE_PLAYING || mJzvdStd.state == Jzvd.STATE_PAUSE)
//                && mJzvdStd.screen != Jzvd.SCREEN_TINY) {
//            if (orientation >= 45 && orientation <= 315 && mJzvdStd.screen == Jzvd.SCREEN_NORMAL) {
//                changeScreenFullLandscape(ScreenRotateUtils.orientationDirection);
//            } else if (((orientation >= 0 &&orientation <45) || orientation > 315) && mJzvdStd.screen == Jzvd.SCREEN_FULLSCREEN) {
//                changeScrenNormal();
//            }
//        }
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
    private void changeScreenFullLandscape(float x) {
        //从竖屏状态进入横屏
        if (mJzvdStd != null && mJzvdStd.screen != Jzvd.SCREEN_FULLSCREEN) {
            mJzvdStd.autoFullscreen(x);
        }
    }

    class OrientationSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int orientation = ORIENTATION_UNKNOWN;
            float x = -values[DATA_X];
            float y = -values[DATA_Y];
            float z = -values[DATA_Z];
            float magnitude = x * x + y * y;
            if (magnitude * 4 >= z * z) {
                float oneEightyOverPi = 57.29577957855f;
                float angle = (float) (Math.atan2(-y,x) * oneEightyOverPi);
                orientation = 90 - Math.round(angle);
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }

                if (Jzvd.CURRENT_JZVD != null
                        && (mJzvdStd.state == Jzvd.STATE_PLAYING || mJzvdStd.state == Jzvd.STATE_PAUSE)
                        && mJzvdStd.screen != Jzvd.SCREEN_TINY) {
                    if (orientation >= 45 && orientation <= 315 && mJzvdStd.screen == Jzvd.SCREEN_NORMAL) {
                        changeScreenFullLandscape(-x);
                    } else if (((orientation >= 0 &&orientation <45) || orientation > 315) && mJzvdStd.screen == Jzvd.SCREEN_FULLSCREEN) {
                        changeScrenNormal();
                    }
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

}

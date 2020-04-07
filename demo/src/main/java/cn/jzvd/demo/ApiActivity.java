package cn.jzvd.demo;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.LinkedHashMap;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.api.CustomMediaActivity;
import cn.jzvd.demo.api.ExtendsNormalActivity;
import cn.jzvd.demo.api.GetGifActivity;
import cn.jzvd.demo.api.OrientationActivity;
import cn.jzvd.demo.api.PreloadingActivity;
import cn.jzvd.demo.api.RotationVideoSizeActivity;
import cn.jzvd.demo.api.ScreenRotateActivity;
import cn.jzvd.demo.api.BigChangeUiActivity;
import cn.jzvd.demo.api.SmallChangeUiActivity;

/**
 * Created by Nathen on 16/7/31.
 */
public class ApiActivity extends AppCompatActivity {
    JzvdStd mJzvdStd;
    Jzvd.JZAutoFullscreenListener mSensorEventListener;
    SensorManager mSensorManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("Api");
        setContentView(R.layout.activity_api);

        mJzvdStd = findViewById(R.id.jz_video);
        LinkedHashMap map = new LinkedHashMap();
        String proxyUrl = ApplicationDemo.getProxy(this).getProxyUrl(Urls.videoUrls[0][9]);
        map.put("高清", proxyUrl);
        map.put("标清", Urls.videoUrls[0][6]);
        map.put("普清", Urls.videoUrlList[0]);
        JZDataSource jzDataSource = new JZDataSource(map, "饺子不信");
        jzDataSource.looping = true;
        jzDataSource.currentUrlIndex = 2;
        jzDataSource.headerMap.put("key", "value");//header
        mJzvdStd.setUp(jzDataSource
                , JzvdStd.SCREEN_NORMAL);
        Glide.with(this).load(Urls.videoPosterList[0]).into(mJzvdStd.posterImageView);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new Jzvd.JZAutoFullscreenListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //home back
        Jzvd.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JZUtils.clearSavedProgress(this, null);
        //home back
        Jzvd.goOnPlayOnPause();
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

    public void clickSmallChange(View view) {
        startActivity(new Intent(ApiActivity.this, SmallChangeUiActivity.class));
    }

    public void clickBigChange(View view) {
        startActivity(new Intent(ApiActivity.this, BigChangeUiActivity.class));
    }

    public void clickOrientation(View view) {
        startActivity(new Intent(ApiActivity.this, OrientationActivity.class));

    }

    public void clickExtendsNormalActivity(View view) {
        startActivity(new Intent(ApiActivity.this, ExtendsNormalActivity.class));
    }

    public void clickRotationAndVideoSize(View view) {
        startActivity(new Intent(ApiActivity.this, RotationVideoSizeActivity.class));
    }

    public void clickCustomMediaPlayer(View view) {
        startActivity(new Intent(ApiActivity.this, CustomMediaActivity.class));
    }

    public void clickPreloading(View view) {
        startActivity(new Intent(ApiActivity.this, PreloadingActivity.class));
    }

    public void clickScreenRotate(View view) {
        startActivity(new Intent(ApiActivity.this, ScreenRotateActivity.class));
    }

    public void clickStartGif(View view) {
        startActivity(new Intent(ApiActivity.this, GetGifActivity.class));
    }
}

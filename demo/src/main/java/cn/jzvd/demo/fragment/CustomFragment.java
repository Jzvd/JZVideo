package cn.jzvd.demo.fragment;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;

import java.util.LinkedHashMap;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.ApplicationDemo;
import cn.jzvd.demo.R;
import cn.jzvd.demo.Urls;
import cn.jzvd.demo.api.BigChangeUiActivity;
import cn.jzvd.demo.api.CustomMediaActivity;
import cn.jzvd.demo.api.ExtendsNormalActivity;
import cn.jzvd.demo.api.GetGifActivity;
import cn.jzvd.demo.api.OrientationActivity;
import cn.jzvd.demo.api.PreloadingActivity;
import cn.jzvd.demo.api.RotationVideoSizeActivity;
import cn.jzvd.demo.api.ScreenRotateActivity;
import cn.jzvd.demo.api.SmallChangeUiActivity;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by pengan.li on 2020/5/8.
 */
public class CustomFragment extends BaseFragment implements View.OnClickListener {

    private JzvdStd mJzvdStd;
    private Button mSmallChange, mBigChange, mOrientation, mExtendsNormalActivity,
            mRotationAndVideoSize, mCustomMediaPlayer, mPreLoading, mScreenRotate, mGetGif;
    private Jzvd.JZAutoFullscreenListener mSensorEventListener;
    private SensorManager mSensorManager;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_api, null);
        mJzvdStd = view.findViewById(R.id.jz_video);
        mSmallChange = view.findViewById(R.id.small_change);
        mBigChange = view.findViewById(R.id.big_change);
        mOrientation = view.findViewById(R.id.orientation);
        mExtendsNormalActivity = view.findViewById(R.id.extends_normal_activity);
        mRotationAndVideoSize = view.findViewById(R.id.rotation_and_videosize);
        mCustomMediaPlayer = view.findViewById(R.id.custom_mediaplayer);
        mPreLoading = view.findViewById(R.id.preloading);
        mScreenRotate = view.findViewById(R.id.screen_rotate);
        mGetGif = view.findViewById(R.id.get_gif);

        mSmallChange.setOnClickListener(this);
        mBigChange.setOnClickListener(this);
        mOrientation.setOnClickListener(this);
        mExtendsNormalActivity.setOnClickListener(this);
        mRotationAndVideoSize.setOnClickListener(this);
        mCustomMediaPlayer.setOnClickListener(this);
        mPreLoading.setOnClickListener(this);
        mScreenRotate.setOnClickListener(this);
        mGetGif.setOnClickListener(this);

        mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new Jzvd.JZAutoFullscreenListener();

        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        LinkedHashMap map = new LinkedHashMap();
        String proxyUrl = ApplicationDemo.getProxy(mContext).getProxyUrl(Urls.clarities[0]);
        map.put("高清", proxyUrl);
        map.put("标清", Urls.clarities[1]);
        map.put("普清", Urls.clarities[2]);
        JZDataSource jzDataSource = new JZDataSource(map, "饺子不信");
        jzDataSource.looping = true;
        jzDataSource.currentUrlIndex = 2;
        jzDataSource.headerMap.put("key", "value");//header
        mJzvdStd.setUp(jzDataSource
                , JzvdStd.SCREEN_NORMAL);
        Glide.with(this).load(Urls.videoPosterList[0]).into(mJzvdStd.posterImageView);
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //home back
        Jzvd.goOnPlayOnResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JZUtils.clearSavedProgress(mContext, null);
        //home back
        Jzvd.goOnPlayOnPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.small_change:
                startActivity(new Intent(mContext, SmallChangeUiActivity.class));
                break;
            case R.id.big_change:
                startActivity(new Intent(mContext, BigChangeUiActivity.class));
                break;
            case R.id.orientation:
                startActivity(new Intent(mContext, OrientationActivity.class));
                break;
            case R.id.extends_normal_activity:
                startActivity(new Intent(mContext, ExtendsNormalActivity.class));
                break;
            case R.id.rotation_and_videosize:
                startActivity(new Intent(mContext, RotationVideoSizeActivity.class));
                break;
            case R.id.custom_mediaplayer:
                startActivity(new Intent(mContext, CustomMediaActivity.class));
                break;
            case R.id.preloading:
                startActivity(new Intent(mContext, PreloadingActivity.class));
                break;
            case R.id.screen_rotate:
                startActivity(new Intent(mContext, ScreenRotateActivity.class));
                break;
            case R.id.get_gif:
                startActivity(new Intent(mContext, GetGifActivity.class));
                break;
        }
    }
}

package cn.jzvd.demo;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.jzvd.jzvideo.UrlsKt;

import java.util.LinkedHashMap;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.Tab_1_Basic.CustomMediaActivity;
import cn.jzvd.demo.Tab_1_Basic.OrientationActivity;
import cn.jzvd.demo.Tab_1_Basic.PreloadingActivity;
import cn.jzvd.demo.Tab_1_Basic.RotationVideoSizeActivity;
import cn.jzvd.demo.Tab_1_Basic.ScreenRotateActivity;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by pengan.li on 2020/5/8.
 * 展示饺子的一些自定义用法，修改jzvdStd暴露的函数和变量，不需要继承jzvdStd的用法
 */
public class Fragment_1_Base extends Fragment implements View.OnClickListener {

    private JzvdStd mJzvdStd;
    private Button mOrientation,
            mRotationAndVideoSize, mCustomMediaPlayer, mPreLoading, mScreenRotate;
    private Button serverCn, serverUs;
    private Jzvd.JZAutoFullscreenListener mSensorEventListener;
    private SensorManager mSensorManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_base, null);
        mJzvdStd = view.findViewById(R.id.jz_video);
        mOrientation = view.findViewById(R.id.orientation);
        mRotationAndVideoSize = view.findViewById(R.id.rotation_and_videosize);
        mCustomMediaPlayer = view.findViewById(R.id.custom_mediaplayer);
        mPreLoading = view.findViewById(R.id.preloading);
        mScreenRotate = view.findViewById(R.id.screen_rotate);
        serverCn = view.findViewById(R.id.server_cn);
        serverUs = view.findViewById(R.id.server_us);

        mOrientation.setOnClickListener(this);
        mRotationAndVideoSize.setOnClickListener(this);
        mCustomMediaPlayer.setOnClickListener(this);
        mPreLoading.setOnClickListener(this);
        mScreenRotate.setOnClickListener(this);
        serverCn.setOnClickListener(this);
        serverUs.setOnClickListener(this);

        mSensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new Jzvd.JZAutoFullscreenListener();

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinkedHashMap map = new LinkedHashMap();
        String proxyUrl = ApplicationDemo.getProxy(getContext()).getProxyUrl(UrlsKt.getCndVideos()[0]);
        map.put("高清", proxyUrl);
        map.put("标清", UrlsKt.getCndVideos()[1]);
        map.put("普清", UrlsKt.getCndVideos()[2]);
        JZDataSource jzDataSource = new JZDataSource(map, "饺子起立");
        jzDataSource.looping = true;
        jzDataSource.currentUrlIndex = 2;
        jzDataSource.headerMap.put("key", "value");//header
        mJzvdStd.setUp(jzDataSource
                , JzvdStd.SCREEN_NORMAL);
        Jzvd.PROGRESS_DRAG_RATE = 2f;//设置播放进度条手势滑动阻尼系数
        Glide.with(this).load(UrlsKt.getCndThumbnail()[0]).into(mJzvdStd.posterImageView);
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
        JZUtils.clearSavedProgress(getContext(), null);
        //home back
        Jzvd.goOnPlayOnPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.orientation:
                startActivity(new Intent(getContext(), OrientationActivity.class));
                break;
            case R.id.rotation_and_videosize:
                startActivity(new Intent(getContext(), RotationVideoSizeActivity.class));
                break;
            case R.id.custom_mediaplayer:
                startActivity(new Intent(getContext(), CustomMediaActivity.class));
                break;
            case R.id.preloading:
                startActivity(new Intent(getContext(), PreloadingActivity.class));
                break;
            case R.id.screen_rotate:
                startActivity(new Intent(getContext(), ScreenRotateActivity.class));
                break;
            case R.id.server_cn:
                UrlsKt.setServer_name(UrlsKt.getCn());
                Toast.makeText(getContext(), "change server to: " + UrlsKt.getCn(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.server_us:
                UrlsKt.setServer_name(UrlsKt.getUs());
                Toast.makeText(getContext(), "change server to: " + UrlsKt.getUs(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

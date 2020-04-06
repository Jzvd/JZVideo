package cn.jzvd.demo;

import java.io.File;

import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.JzvdStdGif;
import cn.jzvd.demo.utils.GifCreateHelper;

/**
 * @author dl
 * @time 2020-03-30
 * @des
 */
public class ActivityGifVideo extends AppCompatActivity {

    JzvdStdGif myJzvdStd;
    HttpProxyCacheServer proxy;
    String urlCrude="http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4";

    TextView tv_hint;
    FrameLayout fl_hint_region;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        myJzvdStd = findViewById(R.id.jz_video);
        tv_hint = findViewById(R.id.tv_hint);
        fl_hint_region = findViewById(R.id.fl_hint_region);
        proxy = ApplicationDemo.getProxy(this);

        String url=proxy.getProxyUrl(urlCrude);
        myJzvdStd.setUp(url, "饺子快长大");
        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(myJzvdStd.posterImageView);

        //gif相关********
        initGifHelper();
        findViewById(R.id.convert_to_gif).setOnClickListener((v -> {
            clickStartGif(v);
        }));
        //gif相关**********
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

    long current;
    private void initGifHelper() {
        //详细使用方式
//        myJzvdStd.initGifHelper(jaoziVideoGifSaveListener,delay,inSampleSize,smallScale,gifPeriod, gifPath)

        //快速使用方式
        myJzvdStd.initGifHelper(new GifCreateHelper.JaoziVideoGifSaveListener() {
            @Override
            public void result(boolean success, File file) {
                myJzvdStd.post(new Runnable() {
                    @Override
                    public void run() {
                        fl_hint_region.setVisibility(View.GONE);
                        Toast.makeText(myJzvdStd.getContext(), "创建成功", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void process(int curPosition, int total,String status) {
                Log.e("fffs",status+"  "+curPosition+"/"+total+"  curruentTime: "+(System.currentTimeMillis()-current));
                myJzvdStd.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_hint.setText(status+"  "+curPosition+"/"+total);
                    }
                });
            }
        });
    }

    public void clickStartGif(View view) {
        tv_hint.setText("正在创建Gif...");
        fl_hint_region.setVisibility(View.VISIBLE);
        current=System.currentTimeMillis();

        //startGif的两种使用方式
        if(proxy.isCached(urlCrude)){
            //被缓存了直接读取缓存
            myJzvdStd.startGif();
        }else{
            //没被缓存读取原始链接（速度较慢）
            myJzvdStd.startGif(myJzvdStd.getCurrentPositionWhenPlaying(),urlCrude);
        }

    }

}

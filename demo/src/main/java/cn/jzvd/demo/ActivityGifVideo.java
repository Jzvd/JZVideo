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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        myJzvdStd = findViewById(R.id.jz_video);
        String url="http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4";;
        myJzvdStd.setUp(url, "饺子快长大");
        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(myJzvdStd.posterImageView);
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

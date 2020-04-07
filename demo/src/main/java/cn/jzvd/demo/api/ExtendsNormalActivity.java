package cn.jzvd.demo.api;

import android.app.Activity;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;
import cn.jzvd.demo.Urls;

/**
 * 适配了普通的Activity，如果不适配并且不继承AppCompatActivity的话会出现Context空指针的情况
 * Created by Nathen on 2017/9/19.
 */
public class ExtendsNormalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extends_normal);
        JzvdStd jzvdStd = findViewById(R.id.videoplayer);
        jzvdStd.setUp(Urls.videoUrlList[0], "饺子不信"
                , JzvdStd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosterList[0])
                .into(jzvdStd.posterImageView);
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
}

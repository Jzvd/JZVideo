package cn.jzvd.demo.Tab_3_List;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;

import org.jzvd.jzvideo.UrlsKt;

import java.io.File;
import java.util.LinkedHashMap;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.ApplicationDemo;
import cn.jzvd.demo.CustomJzvd.JzvdStdGetGif;
import cn.jzvd.demo.R;

/**
 * @author dl
 * @time 2020-03-30
 * @des
 */
public class GetGifActivity extends AppCompatActivity {

    JzvdStdGetGif jzvdStdGetGif;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.get_gif));
        setContentView(R.layout.activity_get_gif);

        //checkPermission
        int permission = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }

        jzvdStdGetGif = findViewById(R.id.jz_video);

        LinkedHashMap map = new LinkedHashMap();
        String proxyUrl = ApplicationDemo.getProxy(getBaseContext()).getProxyUrl("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
        map.put("高清", proxyUrl);
        map.put("标清", "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
        map.put("普清", "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
        JZDataSource jzDataSource = new JZDataSource(map, "饺子Gif");
        jzDataSource.looping = true;
        jzDataSource.currentUrlIndex = 2;
        jzDataSource.headerMap.put("key", "value");//header
        jzvdStdGetGif.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[0]).into(jzvdStdGetGif.posterImageView);

        //设置保存路径和文件名(可选)
//        jzvdStdGetGif.setSaveGifPathName(Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/jiaozi-" + System.currentTimeMillis() + ".gif");
        //设置保存监听(可选)
        jzvdStdGetGif.setGifListener(new JzvdStdGetGif.GifListener() {
            @Override
            public void result(boolean success, File file) {
                if (success) {
                    //保存成功
                } else {
                    //保存失败
                }
            }
        });
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

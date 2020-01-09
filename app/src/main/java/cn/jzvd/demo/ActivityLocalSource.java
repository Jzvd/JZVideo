package cn.jzvd.demo;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.CustomMedia.JZMediaSystemAssertFolder;

/**
 * @author duguodong
 * @time 2019-12-30
 * @des
 */
public class ActivityLocalSource extends AppCompatActivity {

    private JzvdStd mJzvdStd;
    private JzvdStd mJzvdStd2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("Local");
        setContentView(cn.jzvd.R.layout.local_source);
        mJzvdStd = findViewById(cn.jzvd.demo.R.id.jz_video);
        mJzvdStd2 = findViewById(cn.jzvd.demo.R.id.jz_video2);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            verifyStoragePermissions();
        }else{
            cpAssertVideoToLocalPath();
            initAssetVideo();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        //home back
        Jzvd.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.clearSavedProgress(this, null);
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

    public void cpAssertVideoToLocalPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/local_video.mp4";
        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(path);
            myInput = this.getAssets().open("local_video.mp4");
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }
            myOutput.flush();
            myInput.close();
            myOutput.close();

            mJzvdStd.setUp(path,"Local");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initAssetVideo() {
        JZDataSource jzDataSource = null;
        try {
            jzDataSource = new JZDataSource(getAssets().openFd("local_video.mp4"));
            jzDataSource.title = "Asset";
        } catch (IOException e) {
            e.printStackTrace();
        }
        mJzvdStd2.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL, JZMediaSystemAssertFolder.class);
    }



    public void verifyStoragePermissions() {
        try {
            int permission = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        "android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            }else{
                cpAssertVideoToLocalPath();
                initAssetVideo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                cpAssertVideoToLocalPath();
                initAssetVideo();
            }
        }
    }
}

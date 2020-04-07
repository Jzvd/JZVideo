package cn.jzvd.demo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.CustomJzvd.JzvdStdAssert;
import cn.jzvd.demo.CustomMedia.JZMediaSystemAssertFolder;
import cn.jzvd.demo.R;

/**
 * @author duguodong
 * @time 2019-12-30
 * @des
 */
public class LocalVideoActivity extends AppCompatActivity {

    public String localVideoPath;


    private JzvdStd jzvdLocalPath;
    private JzvdStdAssert jzvdAssertPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("PlayLocalVideo");
        setContentView(R.layout.activity_local_video);
        jzvdLocalPath = findViewById(cn.jzvd.demo.R.id.lcoal_path);
        jzvdAssertPath = findViewById(cn.jzvd.demo.R.id.assert_path);
        localVideoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/activity_local_video.mp4";

        //checkPermission
        int permission = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }


        //cp video 防止视频被意外删除
        cpAssertVideoToLocalPath();

        //setUp jzvd
        jzvdLocalPath.setUp(localVideoPath, "Play Local Video");
        jzvdAssertPath.setUp("local_video.mp4", "Play Assert Video", JzvdStd.SCREEN_NORMAL, JZMediaSystemAssertFolder.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZUtils.clearSavedProgress(this, null);
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


    public void cpAssertVideoToLocalPath() {
        if (new File(localVideoPath).exists()) return;

        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(localVideoPath);
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
            Toast.makeText(this, "cp from assert to local path succ", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cpAssertVideoToLocalPath();
            } else {
                finish();
            }
        }
    }
}

package cn.jzvd.demo.BigUIChangeAG;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.CustomMedia.JZMediaExo;
import cn.jzvd.demo.R;
import cn.jzvd.demo.VideoConstant;

public class ActivityApiUIBigChangeAG extends AppCompatActivity implements AGVideo.JzVideoListener{
    private AGVideo mPlayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setDisplayUseLogoEnabled(false);
//        getSupportActionBar().setTitle("BigChangeUI-AG");
        setContentView(R.layout.activity_ui_big_change_ag);
        initView();
    }

    private void initView(){
        mPlayer=findViewById(R.id.ag_player);
        JZDataSource jzDataSource = new JZDataSource(VideoConstant.videoUrlList[0], "饺子不信");
        mPlayer.setUp(jzDataSource
                , JzvdStd.SCREEN_NORMAL, JZMediaExo.class);
        mPlayer.startVideo();
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
    public void nextClick(int type) {

    }

    @Override
    public void backClick() {

    }

    @Override
    public void throwingScreenClick() {

    }

    @Override
    public void fullscreenClick() {

    }

    @Override
    public void selectPartsClick() {

    }

    @Override
    public void speedClick() {

    }

    @Override
    public void lockClick(boolean isLock) {

    }

    @Override
    public void notNetWorkRetry() {

    }

}

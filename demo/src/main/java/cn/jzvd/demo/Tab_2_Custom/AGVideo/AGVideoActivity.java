package cn.jzvd.demo.Tab_2_Custom.AGVideo;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.jzvd.jzvideo.UrlsKt;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.CustomMedia.JZMediaExo;
import cn.jzvd.demo.R;
import cn.jzvd.demo.Tab_2_Custom.AGVideo.popup.VideoEpisodePopup;
import cn.jzvd.demo.Tab_2_Custom.AGVideo.popup.VideoSpeedPopup;
import cn.jzvd.demo.utils.ScreenRotateUtils;

public class AGVideoActivity extends AppCompatActivity implements AGVideo.JzVideoListener, ScreenRotateUtils.OrientationChangeListener
        , VideoSpeedPopup.SpeedChangeListener, VideoEpisodePopup.EpisodeClickListener {
    private AGVideo mPlayer;
    private JZDataSource mJzDataSource;
    private List<AGEpsodeEntity> episodeList;
    private TabLayout episodes;
    private int playingNum = 0;
    //倍数弹窗
    private VideoSpeedPopup videoSpeedPopup;
    private VideoEpisodePopup videoEpisodePopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_agvideo);
        initVideoData();
        initView();
        ScreenRotateUtils.getInstance(this.getApplicationContext()).setOrientationChangeListener(this);
    }

    private void initView() {
        episodes = findViewById(R.id.video_episodes);
        mPlayer = findViewById(R.id.ag_player);
        initEpisodesTablayout();
        mPlayer.setJzVideoListener(this);
        mJzDataSource = new JZDataSource(episodeList.get(0).getVideoUrl(), episodeList.get(0).getVideoName());
        mPlayer.setUp(mJzDataSource
                , JzvdStd.SCREEN_NORMAL, JZMediaExo.class);
        mPlayer.startVideo();
    }

    private void initEpisodesTablayout() {
        episodes.clearOnTabSelectedListeners();
        episodes.removeAllTabs();
        for (int i = 0; i < episodeList.size(); i++) {
            episodes.addTab(episodes.newTab().setText(String.valueOf((i + 1))));
        }
        //用来循环适配器中的视图总数
        for (int i = 0; i < episodes.getTabCount(); i++) {
            //获取每一个tab对象
            TabLayout.Tab tabAt = episodes.getTabAt(i);
            //将每一个条目设置我们自定义的视图
            tabAt.setCustomView(R.layout.tab_video_episodes);
            //通过tab对象找到自定义视图的ID
            TextView textView = tabAt.getCustomView().findViewById(R.id.tab_video_episodes_tv);
            //设置tab上的文字
            textView.setText(episodes.getTabAt(i).getText());
            int current = playingNum;
            if (i == current) {
                //选中后字体
                textView.setTextColor(getResources().getColor(R.color.ThemeColor));
            } else {
                textView.setTextColor(getResources().getColor(R.color.font_color));

            }
        }

        episodes.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //定义方法，判断是否选中
                int tag = Integer.parseInt(tab.getText().toString());
                AGEpsodeEntity entity = episodeList.get(tag - 1);
                mJzDataSource = new JZDataSource(entity.getVideoUrl(), entity.getVideoName());
                updateTabView(tab, true);
                playChangeUrl();
                isNext(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //定义方法，判断是否选中
                updateTabView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        isNext(episodes.getSelectedTabPosition());
    }


    @Override
    protected void onResume() {
        Jzvd.goOnPlayOnResume();
        super.onResume();
        ScreenRotateUtils.getInstance(this).start(this);
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
        ScreenRotateUtils.getInstance(this).stop();
        Jzvd.goOnPlayOnPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Jzvd.releaseAllVideos();
        ScreenRotateUtils.getInstance(this.getApplicationContext()).setOrientationChangeListener(null);
    }

    /**
     * 用来改变tabLayout选中后的字体大小及颜色
     *
     * @param tab
     * @param isSelect
     */
    private void updateTabView(TabLayout.Tab tab, boolean isSelect) {
        //找到自定义视图的控件ID
        TextView tv_tab = tab.getCustomView().findViewById(R.id.tab_video_episodes_tv);
        if (isSelect) {
            //设置标签选中
            tv_tab.setSelected(true);
            //选中后字体
            tv_tab.setTextColor(getResources().getColor(R.color.ThemeColor));
        } else {
            //设置标签取消选中
            tv_tab.setSelected(false);
            //恢复为默认字体
            tv_tab.setTextColor(getResources().getColor(R.color.font_color));
        }
    }

    /**
     * 判断是否有下一集
     */
    private void isNext(int position) {
        //判断是否还有下一集
        if (position == (episodeList.size() - 1)) {
            mPlayer.changeNextBottonUi(false);
        } else {
            mPlayer.changeNextBottonUi(true);
        }
    }

    /**
     * 更换播放地址
     */
    private void playChangeUrl() {
        long progress = 0;
        mPlayer.changeUrl(mJzDataSource, progress);
    }

    @Override
    public void orientationChange(int orientation) {
        if (Jzvd.CURRENT_JZVD != null
                && (mPlayer.state == Jzvd.STATE_PLAYING || mPlayer.state == Jzvd.STATE_PAUSE)
                && mPlayer.screen != Jzvd.SCREEN_TINY) {
            if (orientation >= 45 && orientation <= 315 && mPlayer.screen == Jzvd.SCREEN_NORMAL) {
                changeScreenFullLandscape(ScreenRotateUtils.orientationDirection);
            } else if (((orientation >= 0 && orientation < 45) || orientation > 315) && mPlayer.screen == Jzvd.SCREEN_FULLSCREEN) {
                changeScrenNormal();
            }
        }
    }


    /**
     * 竖屏并退出全屏
     */
    private void changeScrenNormal() {
        if (mPlayer != null && mPlayer.screen == Jzvd.SCREEN_FULLSCREEN) {
            mPlayer.autoQuitFullscreen();
        }
    }

    /**
     * 横屏
     */
    private void changeScreenFullLandscape(float x) {
        //从竖屏状态进入横屏
        if (mPlayer != null && mPlayer.screen != Jzvd.SCREEN_FULLSCREEN) {
            if ((System.currentTimeMillis() - Jzvd.lastAutoFullscreenTime) > 2000) {
                mPlayer.autoFullscreen(x);
                Jzvd.lastAutoFullscreenTime = System.currentTimeMillis();
            }
        }
    }


    /**
     * 关闭倍速播放弹窗和选集弹窗
     */
    private void dismissSpeedPopAndEpisodePop() {
        if (videoSpeedPopup != null) {
            videoSpeedPopup.dismiss();
        }
        if (videoEpisodePopup != null) {
            videoEpisodePopup.dismiss();
        }
    }

    /**
     * 改变播放倍速
     *
     * @param speed
     */
    private void changeSpeed(float speed) {
        Object[] object = { speed };
        mPlayer.mediaInterface.setSpeed(speed);
        mJzDataSource.objects[0] = object;
        Toast.makeText(this, "正在以" + speed + "X倍速播放", Toast.LENGTH_SHORT).show();
        mPlayer.speedChange(speed);
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
    public void nextClick() {
        int position = episodes.getSelectedTabPosition() + 1;
        AGEpsodeEntity entity = episodeList.get(position);
        mJzDataSource = new JZDataSource(entity.getVideoUrl(), entity.getVideoName());
        TabLayout.Tab tab = episodes.getTabAt(position);
        if (tab != null) {
            tab.select();
        }
    }

    @Override
    public void backClick() {
        if (mPlayer.screen == mPlayer.SCREEN_FULLSCREEN) {
            dismissSpeedPopAndEpisodePop();
            AGVideo.backPress();
        } else {
            finish();
        }
    }

    @Override
    public void throwingScreenClick() {
        Toast.makeText(this, "投屏", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void selectPartsClick() {
        if (videoEpisodePopup == null) {
            videoEpisodePopup = new VideoEpisodePopup(this, episodeList);
            videoEpisodePopup.setEpisondeClickListener(this);
        }
        videoEpisodePopup.setPlayNum(episodes.getSelectedTabPosition() + 1);
        videoEpisodePopup.showAtLocation(getWindow().getDecorView(), Gravity.RIGHT, 0, 0);
    }

    @Override
    public void speedClick() {
        if (videoSpeedPopup == null) {
            videoSpeedPopup = new VideoSpeedPopup(this);
            videoSpeedPopup.setSpeedChangeListener(this);
        }
        videoSpeedPopup.showAtLocation(getWindow().getDecorView(), Gravity.RIGHT, 0, 0);
    }


    @Override
    public void speedChange(float speed) {
        changeSpeed(speed);
    }

    @Override
    public void onEpisodeClickListener(AGEpsodeEntity entity, int position) {
        TabLayout.Tab tab = episodes.getTabAt(position);
        if (tab != null) {
            tab.select();
        }
    }

    private void initVideoData() {
        episodeList = new ArrayList<>();
        for (int i = 0; i < UrlsKt.getLdjVideos().length; i++) {
            episodeList.add(new AGEpsodeEntity(UrlsKt.getLdjVideos()[i], "鹿鼎记 第" + (i + 1) + "集"));
        }
    }
}

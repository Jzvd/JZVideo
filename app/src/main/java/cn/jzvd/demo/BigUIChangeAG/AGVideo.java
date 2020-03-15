package cn.jzvd.demo.BigUIChangeAG;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.NetworkUtils;
import cn.jzvd.demo.widget.LoadingView;

public class AGVideo  extends JzvdStd {
    private JzVideoListener jzVideoListener;
    //视频控制布局
    private RelativeLayout videoPlayControlLayout;
    private ImageView screenIV, quickRetreat, fastForward, start_bottom, next_bottom;
    private CheckBox lock;
    private TextView tvSpeed, tvSelectPart;
    private LinearLayout startLayout;
    //无网络布局
    private ConstraintLayout notNetWorkLayout;
    private TextView retryButton;
    private LoadingView loadingView;

    //是否锁屏状态
    private boolean isLock = false;
    public AGVideo(Context context) {
        super(context);
    }

    public AGVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public int getLayoutId() {
        return R.layout.layout_ag_video;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        //无网络布局
        notNetWorkLayout=findViewById(R.id.player_notNetWork_layout);
        retryButton=findViewById(R.id.player_notNetWork_retry);
        loadingView=findViewById(R.id.player_newLoading);

        videoPlayControlLayout = findViewById(R.id.video_control_layout);
        tvSpeed = findViewById(R.id.tv_speed);
        tvSelectPart = findViewById(R.id.tv_select_parts);
        screenIV = findViewById(R.id.screen);
        startLayout = findViewById(R.id.start_layout);
        quickRetreat = findViewById(R.id.quick_retreat);
        fastForward = findViewById(R.id.fast_forward);
        start_bottom = findViewById(R.id.start_bottom);
        next_bottom = findViewById(R.id.next_bottom);
        lock = findViewById(R.id.lock);

        retryButton.setOnClickListener(this);
        replayTextView.setOnClickListener(this);
        tvSpeed.setOnClickListener(this);
        tvSelectPart.setOnClickListener(this);
        start_bottom.setOnClickListener(this);
        next_bottom.setOnClickListener(this);
        quickRetreat.setOnClickListener(this);
        fastForward.setOnClickListener(this);
        screenIV.setOnClickListener(this);
        lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLock = isChecked;
                if (jzVideoListener != null) {
                    jzVideoListener.lockClick(isChecked);
                }
                if (isChecked) {
                    //锁屏按钮单独延迟隐藏
                    goneLock();
                    //隐藏其他功能
                    dissmissControlView();
                } else {
                    cancelDismissControlViewTimer();
                    startDismissControlViewTimer();
                    //取消锁屏按钮的单独延迟隐藏，使锁屏按钮的延迟隐藏和其他功能按钮相同
                    cancelGoneLock();
                    onClickUiToggle();
                }
            }
        });
        if (!isHaveNetWork()){
            showNotNetWorkLayout();
        }
    }

    private void cancelGoneLock() {
    }

    private void goneLock() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back:
                backPress();
                break;
            case R.id.tv_speed:
                if (jzVideoListener != null) {
                    jzVideoListener.speedClick();
                }
                break;
            case R.id.tv_select_parts:
                if (jzVideoListener != null) {
                    jzVideoListener.selectPartsClick();
                }
                break;
            case R.id.next_bottom:
                if (jzVideoListener != null) {
                    jzVideoListener.nextClick(1);
                }
                break;
            case R.id.start_bottom:
                if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                    Toast.makeText(getContext(), getResources().getString(cn.jzvd.R.string.no_url), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (state == STATE_NORMAL) {
                    if (!jzDataSource.getCurrentUrl().toString().startsWith("file") && !
                            jzDataSource.getCurrentUrl().toString().startsWith("/") &&
                            !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {//这个可以放到std中
                        showWifiDialog();
                        return;
                    }
                    startVideo();
                } else if (state == STATE_PLAYING) {
                    Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
                    mediaInterface.pause();
                    onStatePause();
                } else if (state == STATE_PAUSE) {
                    mediaInterface.start();
                    onStatePlaying();
                } else if (state == STATE_AUTO_COMPLETE) {
                    startVideo();
                }
                break;
            case R.id.fast_forward:
                //总时间长度
                long duration = getDuration();
                //当前时间
                long currentPositionWhenPlaying = getCurrentPositionWhenPlaying();
                //快进（15S）
                long fastForwardProgress = currentPositionWhenPlaying + 15 * 1000;
                if (duration > fastForwardProgress) {
                    mediaInterface.seekTo(fastForwardProgress);
                } else {
                    mediaInterface.seekTo(duration);
                }
                break;
            case R.id.quick_retreat:
                //当前时间
                long quickRetreatCurrentPositionWhenPlaying = getCurrentPositionWhenPlaying();
                //快退（15S）
                long quickRetreatProgress = quickRetreatCurrentPositionWhenPlaying - 15 * 1000;
                if (quickRetreatProgress > 0) {
                    mediaInterface.seekTo(quickRetreatProgress);
                } else {
                    mediaInterface.seekTo(0);
                }
                break;
            case R.id.fullscreen:
                Log.i(TAG, "onClick fullscreen [" + this.hashCode() + "] ");
                if (state == STATE_AUTO_COMPLETE) return;
                if (screen == SCREEN_FULLSCREEN) {
                    //quit fullscreen

                    backPress();
                } else {
                    Log.d(TAG, "toFullscreenActivity [" + this.hashCode() + "] ");
                    Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    gotoScreenFullscreen();
                }
                if (jzVideoListener != null) {
                    jzVideoListener.fullscreenClick();
                }
                break;
        }
    }

    @Override
    public void dissmissControlView() {
        if (state != STATE_NORMAL
                && state != STATE_ERROR
                && state != STATE_AUTO_COMPLETE) {
            post(() -> {
                bottomContainer.setVisibility(View.INVISIBLE);
                topContainer.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.INVISIBLE);
                fastForward.setVisibility(View.INVISIBLE);
                quickRetreat.setVisibility(View.INVISIBLE);
                if (!isLock) {
                    lock.setVisibility(View.INVISIBLE);
                }
                if (clarityPopWindow != null) {
                    clarityPopWindow.dismiss();
                }
                if (screen != SCREEN_TINY && screen != SCREEN_FULLSCREEN) {
                    bottomProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void startVideo() {
        if (isHaveNetWork()){
            super.startVideo();
        }else {
            showNotNetWorkLayout();
        }
    }

    @Override
    public void updateStartImage() {
        super.updateStartImage();
        if (state == STATE_PLAYING) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(R.mipmap.btn_movie_suspend);
            start_bottom.setImageResource(R.mipmap.btn_movie_stop_bottom);
            fastForward.setVisibility(VISIBLE);
            quickRetreat.setVisibility(VISIBLE);
            replayTextView.setVisibility(GONE);
        } else if (state == STATE_PREPARING) {
            backButton.setVisibility(VISIBLE);
        } else if (state == STATE_ERROR) {
            startButton.setVisibility(INVISIBLE);
            replayTextView.setVisibility(GONE);
            fastForward.setVisibility(GONE);
            quickRetreat.setVisibility(GONE);
        } else if (state == STATE_AUTO_COMPLETE) {
            //视频播放完成状态
            startButton.setVisibility(View.GONE);
//            startButton.setImageResource(R.drawable.jz_click_replay_selector);
            replayTextView.setVisibility(VISIBLE);
            fastForward.setVisibility(GONE);
            quickRetreat.setVisibility(GONE);
        } else {
            startButton.setImageResource(R.mipmap.btn_movie_play);
            start_bottom.setImageResource(R.mipmap.btn_movie_play_bottom);
            replayTextView.setVisibility(GONE);
            fastForward.setVisibility(GONE);
            quickRetreat.setVisibility(GONE);
        }
    }


    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
//        super.setAllControlsVisiblity(topCon, bottomCon, startBtn, loadingPro, thumbImg, bottomPro, retryLayout);
        topContainer.setVisibility(topCon);
        bottomContainer.setVisibility(bottomCon);
        startButton.setVisibility(startBtn);
        loadingView.setVisibility(loadingPro);
        thumbImageView.setVisibility(thumbImg);
        bottomProgressBar.setVisibility(bottomPro);
        mRetryLayout.setVisibility(retryLayout);
        fastForward.setVisibility(startBtn);
        quickRetreat.setVisibility(startBtn);
    }

    /**
     * 是否有网络并显示布局
     */
    public boolean isHaveNetWork(){
        if (!NetworkUtils.isConnected(this.getApplicationContext()) || !NetworkUtils.isAvailable(this.getApplicationContext())) {
            return false;
        }
        return true;
    }

    /**
     * 展示无网络布局
     */
    private void showNotNetWorkLayout() {
        if (notNetWorkLayout!=null){
            notNetWorkLayout.setVisibility(VISIBLE);
        }
        if (loadingView!=null){
            loadingView.setVisibility(GONE);
        }
    }


    /**
     * 隐藏无网络布局
     */
    private void hideNotWorkLayout(){
        if (notNetWorkLayout==null){
            return;
        }
        notNetWorkLayout.setVisibility(GONE);
    }


    public interface JzVideoListener {

        void nextClick(int type);

        void backClick();

        void throwingScreenClick();

        void fullscreenClick();

        void selectPartsClick();

        void speedClick();

        void lockClick(boolean isLock);


        /**
         * 无网络时重试
         */
        void notNetWorkRetry();

    }
}

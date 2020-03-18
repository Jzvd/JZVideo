package cn.jzvd.demo.BigUIChangeAG;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.NetworkUtils;
import cn.jzvd.demo.widget.LoadingView;

public class AGVideo extends JzvdStd {
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
    //是否有下一集
    private boolean isNext;
    private Timer mDismissLockViewTimer;
    protected DismissLockViewTimerTask mDismissLockViewTimerTask;

    public AGVideo(Context context) {
        super(context);
    }

    public AGVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JzVideoListener getJzVideoListener() {
        return jzVideoListener;
    }

    public void setJzVideoListener(JzVideoListener jzVideoListener) {
        this.jzVideoListener = jzVideoListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_ag_video;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        //无网络布局
        notNetWorkLayout = findViewById(R.id.player_notNetWork_layout);
        retryButton = findViewById(R.id.player_notNetWork_retry);
        loadingView = findViewById(R.id.player_newLoading);

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
        if (!isHaveNetWork()) {
            showNotNetWorkLayout();
        }
    }

    private void cancelGoneLock() {
        cancelDismissLockViewTimer();
    }

    private void goneLock() {
        startDismissLockViewTimer();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.start:
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
            case R.id.poster:
                if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                    Toast.makeText(getContext(), getResources().getString(cn.jzvd.R.string.no_url), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (state == STATE_NORMAL) {
                    if (!jzDataSource.getCurrentUrl().toString().startsWith("file") &&
                            !jzDataSource.getCurrentUrl().toString().startsWith("/") &&
                            !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                        showWifiDialog();
                        return;
                    }
                    startVideo();
                } else if (state == STATE_AUTO_COMPLETE) {
                    onClickUiToggle();
                }
                break;
            case R.id.surface_container:
                startDismissControlViewTimer();
                break;
            case R.id.back_tiny:
                clearFloatScreen();
                break;
            case R.id.clarity:
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout layout = (LinearLayout) inflater.inflate(cn.jzvd.R.layout.jz_layout_clarity, null);

                OnClickListener mQualityListener = v1 -> {
                    int index = (int) v1.getTag();

//                    this.seekToInAdvance = getCurrentPositionWhenPlaying();
                    jzDataSource.currentUrlIndex = index;
//                    onStatePreparingChangeUrl();

                    changeUrl(jzDataSource, getCurrentPositionWhenPlaying());


                    clarity.setText(jzDataSource.getCurrentKey().toString());
                    for (int j = 0; j < layout.getChildCount(); j++) {//设置点击之后的颜色
                        if (j == jzDataSource.currentUrlIndex) {
                            ((TextView) layout.getChildAt(j)).setTextColor(Color.parseColor("#fff85959"));
                        } else {
                            ((TextView) layout.getChildAt(j)).setTextColor(Color.parseColor("#ffffff"));
                        }
                    }
                    if (clarityPopWindow != null) {
                        clarityPopWindow.dismiss();
                    }
                };

                for (int j = 0; j < jzDataSource.urlsMap.size(); j++) {
                    String key = jzDataSource.getKeyFromDataSource(j);
                    TextView clarityItem = (TextView) View.inflate(getContext(), cn.jzvd.R.layout.jz_layout_clarity_item, null);
                    clarityItem.setText(key);
                    clarityItem.setTag(j);
                    layout.addView(clarityItem, j);
                    clarityItem.setOnClickListener(mQualityListener);
                    if (j == jzDataSource.currentUrlIndex) {
                        clarityItem.setTextColor(Color.parseColor("#fff85959"));
                    }
                }

                clarityPopWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
                clarityPopWindow.setContentView(layout);
                clarityPopWindow.showAsDropDown(clarity);
                layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int offsetX = clarity.getMeasuredWidth() / 3;
                int offsetY = clarity.getMeasuredHeight() / 3;
                clarityPopWindow.update(clarity, -offsetX, -offsetY, Math.round(layout.getMeasuredWidth() * 2), layout.getMeasuredHeight());
                break;
            case R.id.retry_btn:
                if (jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                    Toast.makeText(getContext(), getResources().getString(cn.jzvd.R.string.no_url), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!jzDataSource.getCurrentUrl().toString().startsWith("file") && !
                        jzDataSource.getCurrentUrl().toString().startsWith("/") &&
                        !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                addTextureView();
                onStatePreparing();
                break;
            case R.id.back:
            case R.id.top_back:
                if (jzVideoListener != null) {
                    jzVideoListener.backClick();
                }
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
                if (state == STATE_AUTO_COMPLETE) return;
                if (screen == SCREEN_FULLSCREEN) {
                    //quit fullscreen
                    backPress();
                } else {
                    gotoScreenFullscreen();
                }
                if (jzVideoListener != null) {
                    jzVideoListener.fullscreenClick();
                }
                break;
            case R.id.screen:
                if (jzVideoListener != null) {
                    jzVideoListener.throwingScreenClick();
                }
                break;
        }
    }


    @Override
    public void changeUrl(JZDataSource jzDataSource, long seekToInAdvance) {
        showProgress();
        super.changeUrl(jzDataSource, seekToInAdvance);
        //切换播放地址之后继续以1倍速播放
        if (jzDataSource.objects == null) {
            Object[] object = {1};
            jzDataSource.objects = object;
        }
        speedChange(1.0f);
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

    public void dismissLockView() {
        if (state != STATE_NORMAL
                && state != STATE_ERROR
                && state != STATE_AUTO_COMPLETE) {
            post(() -> {
                lock.setVisibility(GONE);
            });
        }
    }

    @Override
    public void startVideo() {
        if (isHaveNetWork()) {
            super.startVideo();
        } else {
            showNotNetWorkLayout();
        }
    }

    @Override
    public void changeUiToPreparing() {
        switch (screen) {
            case SCREEN_NORMAL:
            case SCREEN_FULLSCREEN:
                screenIV.setVisibility(GONE);
                titleTextView.setVisibility(GONE);
                batteryTimeLayout.setVisibility(GONE);
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
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
    public void setScreenNormal() {
        screen = SCREEN_NORMAL;
        fullscreenButton.setImageResource(cn.jzvd.R.drawable.jz_enlarge);
        backButton.setVisibility(View.VISIBLE);
        tinyBackImageView.setVisibility(View.INVISIBLE);
        changeStartButtonSize((int) getResources().getDimension(cn.jzvd.R.dimen.jz_start_button_w_h_normal));
        batteryTimeLayout.setVisibility(View.GONE);
        clarity.setVisibility(View.GONE);
        fullscreenButton.setVisibility(View.VISIBLE);
        next_bottom.setVisibility(View.GONE);
        tvSpeed.setVisibility(View.GONE);
        tvSelectPart.setVisibility(View.GONE);
        lock.setVisibility(View.GONE);
        changeUiToPlayingShow();
        startDismissControlViewTimer();
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        next_bottom.setVisibility(View.VISIBLE);
        tvSpeed.setVisibility(View.VISIBLE);
        tvSelectPart.setVisibility(View.VISIBLE);
        fullscreenButton.setVisibility(View.GONE);
        lock.setVisibility(View.VISIBLE);
        changeUiToPlayingShow();
        startDismissControlViewTimer();
        if (jzDataSource.objects == null) {
            Object[] object = {1};
            jzDataSource.objects = object;
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        titleTextView.setVisibility(VISIBLE);
        screenIV.setVisibility(VISIBLE);
    }

    @Override
    public void changeUiToPlayingShow() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                if (!isLock) {
                    setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                            View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                    updateStartImage();
                }
                lock.setVisibility(View.VISIBLE);
                break;
            case SCREEN_TINY:
                break;
        }
    }


    @Override
    public void changeUiToPlayingClear() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                lock.setVisibility(View.INVISIBLE);
                break;
            case SCREEN_TINY:
                break;
        }
    }

    @Override
    public void changeUiToPauseShow() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                lock.setVisibility(View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }

    }

    @Override
    public void changeUiToPauseClear() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                lock.setVisibility(View.INVISIBLE);
                break;
            case SCREEN_TINY:
                break;
        }
    }

    @Override
    public void onClickUiToggle() {
        if (bottomContainer.getVisibility() != View.VISIBLE) {
            setSystemTimeAndBattery();
            clarity.setText(jzDataSource.getCurrentKey().toString());
        }
        if (state == STATE_PREPARING) {
            changeUiToPreparing();
            if (bottomContainer.getVisibility() == View.VISIBLE) {
            } else {
                setSystemTimeAndBattery();
            }
        } else if (state == STATE_PLAYING) {
            if (isLock) {
                if (lock.getVisibility() == View.VISIBLE) {
                    lock.setVisibility(INVISIBLE);
                } else {
                    lock.setVisibility(View.VISIBLE);
                    goneLock();
                }
            } else {
                if (bottomContainer.getVisibility() == View.VISIBLE) {
                    changeUiToPlayingClear();
                } else {
                    changeUiToPlayingShow();
                }
            }
        } else if (state == STATE_PAUSE) {
            if (isLock) {
                if (lock.getVisibility() == View.VISIBLE) {
                    lock.setVisibility(INVISIBLE);
                } else {
                    lock.setVisibility(View.VISIBLE);
                    goneLock();
                }
            } else {
                if (bottomContainer.getVisibility() == View.VISIBLE) {
                    changeUiToPauseClear();
                } else {
                    changeUiToPauseShow();
                }
            }
        }
    }

    @Override
    public void changeStartButtonSize(int size) {
        //修改框架原本的图标大小
        size = (int) getResources().getDimension(R.dimen.jz_start_button_w_h_normal_ag);
        ViewGroup.LayoutParams lp = startButton.getLayoutParams();
        lp.height = size;
        lp.width = size;
        lp = loadingProgressBar.getLayoutParams();
        lp.height = size;
        lp.width = size;
    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        topContainer.setVisibility(topCon);
        bottomContainer.setVisibility(bottomCon);
        startButton.setVisibility(startBtn);
        loadingView.setVisibility(loadingPro);
        posterImageView.setVisibility(thumbImg);
        bottomProgressBar.setVisibility(bottomPro);
        mRetryLayout.setVisibility(retryLayout);
        fastForward.setVisibility(startBtn);
        quickRetreat.setVisibility(startBtn);
    }

    /**
     * 是否有网络并显示布局
     */
    public boolean isHaveNetWork() {
        if (!NetworkUtils.isConnected(this.getApplicationContext()) || !NetworkUtils.isAvailable(this.getApplicationContext())) {
            return false;
        }
        return true;
    }

    /**
     * 展示无网络布局
     */
    private void showNotNetWorkLayout() {
        if (notNetWorkLayout != null) {
            notNetWorkLayout.setVisibility(VISIBLE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(GONE);
        }
    }


    /**
     * 隐藏无网络布局
     */
    private void hideNotWorkLayout() {
        if (notNetWorkLayout == null) {
            return;
        }
        notNetWorkLayout.setVisibility(GONE);
    }

    /**
     * 点击播放下一集时设置按钮状态
     *
     * @param isNext
     */
    public void changeNextBottonUi(boolean isNext) {
        this.isNext = isNext;
        if (isNext) {
            next_bottom.setImageResource(R.mipmap.btn_movie_next);
            next_bottom.setClickable(true);
        } else {
            next_bottom.setImageResource(R.mipmap.btn_movie_unll_next);
            next_bottom.setClickable(false);
        }
    }


    public void hideProgress() {
        if (loadingView != null) {
            loadingView.setVisibility(GONE);
        }
    }

    public void showProgress() {
        if (loadingView.getVisibility() != View.VISIBLE) {
            loadingView.setVisibility(VISIBLE);
        }
    }

    public class DismissLockViewTimerTask extends TimerTask {

        @Override
        public void run() {
            dismissLockView();
        }
    }


    public void startDismissLockViewTimer() {
        cancelDismissControlViewTimer();
        mDismissLockViewTimer = new Timer();
        mDismissLockViewTimerTask = new DismissLockViewTimerTask();
        mDismissLockViewTimer.schedule(mDismissLockViewTimerTask, 2500);
    }

    public void cancelDismissLockViewTimer() {
        if (mDismissLockViewTimer != null) {
            mDismissLockViewTimer.cancel();
        }
        if (mDismissLockViewTimerTask != null) {
            mDismissLockViewTimerTask.cancel();
        }

    }

    /**
     * 改变倍数之后
     */
    public void speedChange(float speed) {
        if (speed == 1f) {
            tvSpeed.setText("倍数");
        } else {
            tvSpeed.setText(speed + "X");
        }
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

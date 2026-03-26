package cn.jzvd.demo.Tab_2_Custom.AGVideo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.Tab_2_Custom.AGVideo.view.LoadingView;
import cn.jzvd.demo.Tab_2_Custom.AGVideo.view.PlayAndPauseView;
import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.NetworkUtils;
import cn.jzvd.demo.utils.StatusBarUtil;

public class AGVideo extends JzvdStd {
    protected DismissLockViewTimerTask mDismissLockViewTimerTask;
    private JzVideoListener jzVideoListener;
    //视频控制布局
    private ImageView screenIV, quickRetreat, fastForward, start_bottom, next_bottom;
    private PlayAndPauseView playAndPauseView;
    private CheckBox lock;
    private TextView tvSpeed, tvSelectPart, next_set;
    private LinearLayout layout_bottom, layout_top;
    //无网络布局
    private LoadingView loadingView;
    //是否锁屏状态
    private boolean isLock = false;
    //是否有下一集
    private boolean isNext;
    private int nextTimerDate = 3;
    private Timer mDismissLockViewTimer, mDismissNextViewTimer;
    private DismissNextViewTimerTask mDismissNextViewTimerTask;
    private boolean clickPlayOrPause;

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
        screenIV = findViewById(R.id.screen);
        quickRetreat = findViewById(R.id.quick_retreat);
        fastForward = findViewById(R.id.fast_forward);
        start_bottom = findViewById(R.id.start_bottom);
        next_bottom = findViewById(R.id.next_bottom);
        playAndPauseView = findViewById(R.id.playAndPauseView);
        lock = findViewById(R.id.lock);
        tvSpeed = findViewById(R.id.tv_speed);
        tvSelectPart = findViewById(R.id.tv_select_parts);
        next_set = findViewById(R.id.next_set);
        layout_bottom = findViewById(R.id.layout_bottom);
        layout_top = findViewById(R.id.layout_top);
        loadingView = findViewById(R.id.player_newLoading);


        next_set.setOnClickListener(this);
        replayTextView.setOnClickListener(this);
        tvSpeed.setOnClickListener(this);
        tvSelectPart.setOnClickListener(this);
        start_bottom.setOnClickListener(this);
        playAndPauseView.setOnClickListener(this);
        next_bottom.setOnClickListener(this);
        quickRetreat.setOnClickListener(this);
        fastForward.setOnClickListener(this);
        if (screenIV != null) {
            screenIV.setOnClickListener(this);
        }
        lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLock = isChecked;
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
        if (id == R.id.start || id == R.id.start_bottom || id == R.id.playAndPauseView) {
            clickPlayOrPause = true;
            playAndPauseView.playOrPause();
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
        } else if (id == R.id.poster) {
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
        } else if (id == R.id.surface_container) {
            startDismissControlViewTimer();
        } else if (id == R.id.back_tiny) {
            clearFloatScreen();
        } else if (id == R.id.clarity) {
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
                TextView textView = (TextView) View.inflate(getContext(), cn.jzvd.R.layout.jz_layout_clarity_item, null);
                textView.setText(key);
                textView.setTag(j);
                layout.addView(textView);
                textView.setOnClickListener(mQualityListener);
                if (j == jzDataSource.currentUrlIndex) {
                    textView.setTextColor(Color.parseColor("#fff85959"));
                }
            }

            clarityPopWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
            clarityPopWindow.setContentView(layout);
            clarityPopWindow.showAsDropDown(clarity);
            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int offsetX = clarity.getMeasuredWidth() / 2 - layout.getMeasuredWidth() / 2;
            int offsetY = -layout.getMeasuredHeight() - clarity.getMeasuredHeight();
            clarityPopWindow.update(clarity, offsetX, offsetY, -1, -1);
        } else if (id == R.id.replay_text) {
            if (state == STATE_AUTO_COMPLETE) {
                replayTextView.setVisibility(View.GONE);
                next_set.setVisibility(View.GONE);
                //点击重播，取消下一集倒计时
                dismissNextView();
                cancelDismissNextViewTimer();

                //resetProgressAndTime();
                //mediaInterface.seekTo(0);
                changeUrl(jzDataSource, 0);
            }
        } else if (id == R.id.next_set) {
            dismissNextView();
            cancelDismissNextViewTimer();
            if (jzVideoListener != null) {
                jzVideoListener.nextClick();
            }
        } else if (id == R.id.back || id == R.id.top_back) {
            if (jzVideoListener != null) {
                jzVideoListener.backClick();
            }
        } else if (id == R.id.tv_speed) {
            if (jzVideoListener != null) {
                jzVideoListener.speedClick();
            }
        } else if (id == R.id.tv_select_parts) {
            if (jzVideoListener != null) {
                jzVideoListener.selectPartsClick();
            }
        } else if (id == R.id.next_bottom) {
            if (jzVideoListener != null) {
                jzVideoListener.nextClick();
            }
        } else if (id == R.id.quick_retreat) {
            long currentPosition = mediaInterface.getCurrentPosition();
            long totalTimeDuration = mediaInterface.getDuration();
            long seekToPosition = currentPosition - 15 * 1000;
            if (seekToPosition < 0) {
                seekToPosition = 0;
            }
            mediaInterface.seekTo(seekToPosition);
        } else if (id == R.id.fast_forward) {
            long currentPosition = mediaInterface.getCurrentPosition();
            long totalTimeDuration = mediaInterface.getDuration();
            long seekToPosition = currentPosition + 15 * 1000;
            if (seekToPosition > totalTimeDuration) {
                seekToPosition = totalTimeDuration;
            }
            mediaInterface.seekTo(seekToPosition);
        } else if (id == R.id.screen) {
            if (jzVideoListener != null) {
                jzVideoListener.throwingScreenClick();
            }
        }
    }

    private long getCurrentPositionPlaying() {
        long currentPositionWhenPlaying = 0;
        //这里不加判断 lastState != STATE_ERROR 会在播放错误时进入此判断，导致崩溃。
        if (state == STATE_PLAYING || state == STATE_PAUSE) {
            try {
                currentPositionWhenPlaying = mediaInterface.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return currentPositionWhenPlaying;
            }
        }
        return currentPositionWhenPlaying;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouch(v, event);
        int id = v.getId();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    moveChange(event);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        }
        return false;
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePreparingPlaying() {
        state = STATE_PREPARING_PLAYING;
        showProgress();
    }

    @Override
    public void onStatePreparingChangeUrl() {
        super.onStatePreparingChangeUrl();
    }


    @Override
    public void onStatePause() {
        super.onStatePause();
        updateStartImage();
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
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
//        super.onStatePlaying();
        Log.i(TAG, "onStatePlaying " + " [" + this.hashCode() + "] ");
        if (state == STATE_PREPARED) {//如果是准备完成视频后第一次播放，先判断是否需要跳转进度。
            mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (seekToInAdvance != 0) {
                mediaInterface.seekTo(seekToInAdvance);
                seekToInAdvance = 0;
            } else {
                long position = JZUtils.getSavedProgress(getContext(), jzDataSource.getCurrentUrl());
                if (position != 0) {
                    mediaInterface.seekTo(position);//这里为什么区分开呢，第一次的播放和resume播放是不一样的。 这里怎么区分是一个问题。然后
                }
            }
        }
        state = STATE_PLAYING;
        startProgressTimer();
        if (screenIV != null) {
            screenIV.setVisibility(VISIBLE);
        }
        titleTextView.setVisibility(VISIBLE);
        changeUiToPlayingShow();
        startDismissControlViewTimer();
    }

    @Override
    public void onCompletion() {
        Runtime.getRuntime().gc();
        Log.i(TAG, "onAutoCompletion " + " [" + this.hashCode() + "] ");
        cancelProgressTimer();
        dismissBrightnessDialog();
        dismissProgressDialog();
        dismissVolumeDialog();
        onStateAutoComplete();
        JZUtils.scanForActivity(getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        JZUtils.saveProgress(getContext(), jzDataSource.getCurrentUrl(), 0);
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        if (isNext) {
            startDismissNextViewTimer();
        }
    }

    @Override
    public void changeUiToPlayingShow() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                updateConfigChanged(screen);
                break;
            case SCREEN_FULLSCREEN:
                if (!isLock) {
                    setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                            View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                    updateStartImage();
                    updateConfigChanged(screen);
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
                if (!isLock) {
                    setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                            View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                }
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
                updateConfigChanged(screen);
                break;
            case SCREEN_FULLSCREEN:
                if (!isLock) {
                    setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                            View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                    updateStartImage();
                    updateConfigChanged(screen);
                }
                lock.setVisibility(View.VISIBLE);
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
                if (!isLock) {
                    setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                            View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                }
                lock.setVisibility(View.INVISIBLE);
                break;
            case SCREEN_TINY:
                break;
        }
    }

    @Override
    public void changeUiToComplete() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
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
     * 普通窗口下亮度、音量、播放进度的调节功能
     *
     * @param event
     */
    private void moveChange(MotionEvent event) {
        if (screen == SCREEN_NORMAL || screen == SCREEN_FULLSCREEN) {
            float x = event.getX();
            float y = event.getY();
            float deltaX = x - mDownX;
            float deltaY = y - mDownY;
            float absDeltaX = Math.abs(deltaX);
            float absDeltaY = Math.abs(deltaY);
            if (!mChangePosition && !mChangeVolume && !mChangeBrightness) {
                if (absDeltaX > THRESHOLD || absDeltaY > THRESHOLD) {
                    cancelProgressTimer();
                    if (absDeltaX >= THRESHOLD) {
                        // 全屏模式下的屏幕拖动
                        if (state != STATE_ERROR && state != STATE_AUTO_COMPLETE) {
                            mChangePosition = true;
                            mGestureDownPosition = getCurrentPositionWhenPlaying();
                        }
                    } else {
                        //如果垂直距离也大于阈值，则根据触摸点的位置决定是调节音量还是调节亮度
                        if (mDownX < mScreenWidth * 0.5f) {//左侧调节亮度
                            mChangeBrightness = true;
                            WindowManager.LayoutParams lp = JZUtils.scanForActivity(getContext()).getWindow().getAttributes();
                            if (lp.screenBrightness < 0) {
                                try {
                                    mGestureDownBrightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                                    Log.i(TAG, "current brightness: " + mGestureDownBrightness);
                                } catch (Settings.SettingNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                mGestureDownBrightness = lp.screenBrightness * 255;
                                Log.i(TAG, "current brightness: " + mGestureDownBrightness);
                            }
                        } else {
                            mChangeVolume = true;
                            if (mAudioManager == null) {
                                mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                            }
                            mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        }
                    }
                }
            }
            if (mChangePosition) {
                long totalTimeDuration = mediaInterface.getDuration();
                mSeekTimePosition = (long) (mGestureDownPosition + deltaX * totalTimeDuration / mScreenWidth);
                if (mSeekTimePosition > totalTimeDuration)
                    mSeekTimePosition = totalTimeDuration;
                String showTime = JZUtils.stringForTime(mSeekTimePosition);
                String totalTime = JZUtils.stringForTime(totalTimeDuration);
                showProgressDialog(deltaX, showTime, mSeekTimePosition, totalTime, totalTimeDuration);
            }
            if (mChangeVolume) {
                deltaY = -deltaY;
                int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0);
                //int volumePercent = (int) (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);
                showVolumeDialog(-deltaY, (mGestureDownVolume + deltaV) * 100 / max);
            }

            if (mChangeBrightness) {
                deltaY = -deltaY;
                int deltaV = (int) (255 * deltaY * 3 / mScreenHeight);
                WindowManager.LayoutParams params = JZUtils.scanForActivity(getContext()).getWindow().getAttributes();
                if (((mGestureDownBrightness + deltaV) / 255) >= 1) {//适应弧度
                    params.screenBrightness = 1;
                } else if (((mGestureDownBrightness + deltaV) / 255) <= 0) {
                    params.screenBrightness = 0.01f;
                } else {
                    params.screenBrightness = (mGestureDownBrightness + deltaV) / 255;
                }
                JZUtils.scanForActivity(getContext()).getWindow().setAttributes(params);
                //int brightnessPercent = (int) (mGestureDownBrightness * 100 / 255 + deltaY * 3 * 100 / mScreenHeight);
                showBrightnessDialog((int) (params.screenBrightness * 100));
            }
        }
    }

    @Override
    public void updateStartImage() {
        if (state == STATE_PLAYING) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(cn.jzvd.R.drawable.jz_click_pause_selector);
            start_bottom.setImageResource(cn.jzvd.R.drawable.jz_click_pause_selector);
            if (playAndPauseView != null) {
                playAndPauseView.play();
            }
        } else if (state == STATE_ERROR) {
            startButton.setVisibility(INVISIBLE);
            start_bottom.setImageResource(cn.jzvd.R.drawable.jz_click_play_selector);
            if (playAndPauseView != null) {
                playAndPauseView.pause();
            }
        } else if (state == STATE_AUTO_COMPLETE) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(cn.jzvd.R.drawable.jz_click_replay_selector);
            if (playAndPauseView != null) {
                playAndPauseView.pause();
            }
        } else {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(cn.jzvd.R.drawable.jz_click_play_selector);
            start_bottom.setImageResource(cn.jzvd.R.drawable.jz_click_play_selector);
            if (playAndPauseView != null) {
                playAndPauseView.pause();
            }
        }
    }

    public void updateConfigChanged(int screen) {
        if (screen == SCREEN_FULLSCREEN) {
            layout_bottom.setVisibility(VISIBLE);
            layout_top.setVisibility(VISIBLE);
        } else {
            layout_bottom.setVisibility(GONE);
            layout_top.setVisibility(GONE);
        }
    }

    /**
     * 下一集切换ui
     *
     * @param isNext
     */
    public void changeNextBottonUi(boolean isNext) {
        this.isNext = isNext;
        if (isNext) {
            next_bottom.setImageResource(cn.jzvd.R.drawable.jz_click_play_selector);
            next_bottom.setClickable(true);
        } else {
            next_bottom.setImageResource(cn.jzvd.R.drawable.jz_click_play_selector);
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

    public void startDismissLockViewTimer() {
        cancelDismissLockViewTimer();
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

    public void startDismissNextViewTimer() {
        cancelDismissNextViewTimer();
        nextTimerDate = 3;
        next_set.setVisibility(VISIBLE);
        next_set.setText(nextTimerDate + "秒后播放下一集");
        mDismissNextViewTimer = new Timer();
        mDismissNextViewTimerTask = new DismissNextViewTimerTask();
        mDismissNextViewTimer.schedule(mDismissNextViewTimerTask, 0, 1000);
    }

    public void cancelDismissNextViewTimer() {
        if (mDismissNextViewTimer != null) {
            mDismissNextViewTimer.cancel();
        }
        if (mDismissNextViewTimerTask != null) {
            mDismissNextViewTimerTask.cancel();
        }
    }

    private void dismissNextView() {
        next_set.setVisibility(GONE);
    }

    private void dismissLockView() {
        post(() -> {
            if (lock != null) {
                lock.setVisibility(INVISIBLE);
            }
        });
    }


    public interface JzVideoListener {

        void backClick();

        void nextClick();

        void throwingScreenClick();

        void selectPartsClick();

        void speedClick();

    }

    public class DismissLockViewTimerTask extends TimerTask {

        @Override
        public void run() {
            dismissLockView();
        }
    }

    public class DismissNextViewTimerTask extends TimerTask {

        @Override
        public void run() {
            post(() -> {
                if (nextTimerDate <= 0) {
                    dismissNextView();
                    cancelDismissNextViewTimer();
                    if (jzVideoListener != null) {
                        jzVideoListener.nextClick();
                    }
                } else {
                    next_set.setText(nextTimerDate + "秒后播放下一集");
                    nextTimerDate--;
                }
            });
        }
    }
}

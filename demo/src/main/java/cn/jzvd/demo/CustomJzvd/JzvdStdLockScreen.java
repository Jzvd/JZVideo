package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import cn.jzvd.JZUtils;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;

/**
 * 这里是不改源码得情况下
 */
public class JzvdStdLockScreen extends JzvdStd {
    float starX, startY;
    private boolean isLockScreen;
    private ImageView lockIv;

    public JzvdStdLockScreen(Context context) {
        super(context);
    }

    public JzvdStdLockScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //这里应该还没有判断完  目前还没有测试出什么问题  这里是拦截父亲得一些事件比如滑动快进 改变亮度
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                starX = event.getX();
                startY = event.getY();
                if (screen == SCREEN_FULLSCREEN && isLockScreen) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (screen == SCREEN_FULLSCREEN && isLockScreen) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (screen == SCREEN_FULLSCREEN && isLockScreen) {
                    //&& Math.abs(Math.abs(event.getX() - starX)) > ViewConfiguration.get(getContext()).getScaledTouchSlop()  && Math.abs(Math.abs(event.getY() - startY)) > ViewConfiguration.get(getContext()).getScaledTouchSlop()
                    if (event.getX() == starX || event.getY() == startY) {
                        startDismissControlViewTimer();
                        onClickUiToggle();
                        bottomProgressBar.setVisibility(VISIBLE);
                    }
                    return true;
                }
                break;
        }
        return super.onTouch(v, event);
    }


    @Override
    public void init(Context context) {
        super.init(context);
        lockIv = findViewById(R.id.lock);
        lockIv.setOnClickListener(this);
    }


    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
        if (screen == SCREEN_FULLSCREEN) {
            if (!isLockScreen) {
                if (bottomContainer.getVisibility() == View.VISIBLE) {
                    lockIv.setVisibility(View.VISIBLE);
                } else {
                    lockIv.setVisibility(View.GONE);
                }
            } else {
                if ((int) lockIv.getTag() == 1) {
                    bottomProgressBar.setVisibility(GONE);
                    if (lockIv.getVisibility() == View.GONE) {
                        lockIv.setVisibility(View.VISIBLE);
                    } else {
                        lockIv.setVisibility(View.GONE);
                    }

                }
            }

        }
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        if (isLockScreen) {
            bottomContainer.setVisibility(GONE);
            topContainer.setVisibility(GONE);
            startButton.setVisibility(GONE);
        }
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        lockIv.setBackgroundResource(R.mipmap.unlock);
        lockIv.setVisibility(View.VISIBLE);
    }

    @Override
    public void dissmissControlView() {
        super.dissmissControlView();
        post(() -> {
            if (screen == SCREEN_FULLSCREEN) {
                lockIv.setVisibility(View.GONE);
                bottomProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        if (screen == SCREEN_FULLSCREEN) {
            bottomProgressBar.setVisibility(GONE);
            if (isLockScreen) {
                topContainer.setVisibility(GONE);
                bottomContainer.setVisibility(GONE);
                startButton.setVisibility(GONE);
            } else {
                topContainer.setVisibility(VISIBLE);
                bottomContainer.setVisibility(VISIBLE);
                startButton.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
        if (screen == SCREEN_FULLSCREEN) {
            bottomProgressBar.setVisibility(GONE);
            lockIv.setVisibility(View.GONE);
        }
    }


    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        lockIv.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.lock_screen_jz_layout_std;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.lock:
                if (screen == SCREEN_FULLSCREEN) {
                    lockIv.setTag(1);
                    if (!isLockScreen) {
                        isLockScreen = true;
                        JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        lockIv.setBackgroundResource(R.mipmap.lock);
                        dissmissControlView();
                    } else {
                        JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        isLockScreen = false;
                        lockIv.setBackgroundResource(R.mipmap.unlock);
                        bottomContainer.setVisibility(VISIBLE);
                        bottomProgressBar.setVisibility(GONE);
                        topContainer.setVisibility(VISIBLE);
                        startButton.setVisibility(VISIBLE);
                    }
                }
                break;
        }
    }
}
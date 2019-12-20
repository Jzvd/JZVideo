package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import cn.jzvd.JZUtils;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;

/**
 * 这里是不改源码得情况下
 */
public class LockScreen extends JzvdStd {
    private boolean isLockScreen;
    private ImageView lockIv;
    float starX, startY;

    public LockScreen(Context context) {
        super(context);
    }

    public LockScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {   //这里应该还没有判断完  目前还没有测试出什么问题
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
                        Log.d("cxx", "fff");
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
    public void init(Context context) {
        super.init(context);
        lockIv = findViewById(R.id.lock);
        lockIv.setOnClickListener(this);
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
        if (screen == SCREEN_FULLSCREEN) {
            bottomProgressBar.setVisibility(GONE);
        }
    }

    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
        if (screen == SCREEN_FULLSCREEN) {
            if (lockIv.getVisibility() == VISIBLE) {
                lockIv.setVisibility(View.GONE);
            } else {
                lockIv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        lockIv.setBackgroundResource(R.drawable.unlock);
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
                    if (!isLockScreen) {
                        isLockScreen = true;
                        JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        lockIv.setBackgroundResource(R.drawable.lock);
                        dissmissControlView();
                    } else {
                        JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        isLockScreen = false;
                        lockIv.setBackgroundResource(R.drawable.unlock);
                        topContainer.setVisibility(VISIBLE);
                        bottomContainer.setVisibility(VISIBLE);
                        startButton.setVisibility(VISIBLE);

                    }
                }
                break;
        }
    }
}

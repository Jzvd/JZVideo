package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import cn.jzvd.JzvdStd;

public class JzvdStdTikTok extends JzvdStd {
    public JzvdStdTikTok(Context context) {
        super(context);
    }

    public JzvdStdTikTok(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        bottomContainer.setVisibility(GONE);
        topContainer.setVisibility(GONE);
        bottomProgressBar.setVisibility(VISIBLE);
    }


    //changeUiTo 真能能修改ui的方法
    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
        bottomContainer.setVisibility(GONE);
        topContainer.setVisibility(GONE);
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        bottomProgressBar.setVisibility(VISIBLE);
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
        bottomProgressBar.setVisibility(VISIBLE);
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        bottomProgressBar.setVisibility(VISIBLE);
    }

    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();
        bottomProgressBar.setVisibility(VISIBLE);
    }

    @Override
    public void changeUiToComplete() {
        super.changeUiToComplete();
    }

    @Override
    public void changeUiToError() {
        super.changeUiToError();
    }

    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
        Log.i(TAG, "click blank");
        startButton.performClick();
        bottomContainer.setVisibility(GONE);
        topContainer.setVisibility(GONE);
        bottomProgressBar.setVisibility(VISIBLE);
    }
}

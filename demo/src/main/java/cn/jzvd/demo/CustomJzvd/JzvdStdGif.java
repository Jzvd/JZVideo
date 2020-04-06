package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.GifCreateHelper;

/**
 * Created by dl on 2020/4/6.
 */
public class JzvdStdGif extends JzvdStd {

    GifCreateHelper mGifCreateHelper;

    public JzvdStdGif(Context context) {
        super(context);
    }

    public JzvdStdGif(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
    }

    /**
     *
     * @param jaoziVideoGifSaveListener  保存进度的回调函数
     */
    public void initGifHelper(GifCreateHelper.JaoziVideoGifSaveListener jaoziVideoGifSaveListener) {
        mGifCreateHelper = new GifCreateHelper(this, jaoziVideoGifSaveListener);
    }

    /**
     * @param jaoziVideoGifSaveListener  保存进度的回调函数
     * @param delay          每一帧之间的延时
     * @param inSampleSize   采样率，最小值1 即：每隔inSampleSize个像素点，取一个读入到内存。越大处理越快
     * @param smallScale     缩小倍数，越大处理越快
     * @param gifPeriod      gif时长，毫秒
     * @param gifPath        gif文件的存储路径
     */
    public void initGifHelper(GifCreateHelper.JaoziVideoGifSaveListener jaoziVideoGifSaveListener,
                               int delay, int inSampleSize, int smallScale, int gifPeriod, String gifPath) {
        mGifCreateHelper = new GifCreateHelper(this,jaoziVideoGifSaveListener,delay,inSampleSize,smallScale,gifPeriod,gifPath);
    }

    /**
     * 开始生成gif
     */
    public void startGif() {
        mGifCreateHelper.startGif();
        if(Jzvd.STATE_PLAYING==state){
            startButton.performClick();
        }

    }

    /**
     * 开始生成gif
     * @param bitmapFromTime  gif图在视频中的开始时间
     * @param vedioUrl         视频链接
     */
    public void startGif(long bitmapFromTime,String vedioUrl) {
        mGifCreateHelper.startGif(bitmapFromTime,vedioUrl);
        if(Jzvd.STATE_PLAYING==state){
            startButton.performClick();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == cn.jzvd.R.id.fullscreen) {
            Log.i(TAG, "onClick: fullscreen button");
        } else if (i == R.id.start) {
            Log.i(TAG, "onClick: start button");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouch(v, event);
        int id = v.getId();
        if (id == cn.jzvd.R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (mChangePosition) {
                        Log.i(TAG, "Touch screen seek position");
                    }
                    if (mChangeVolume) {
                        Log.i(TAG, "Touch screen change volume");
                    }
                    break;
            }
        }

        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_std;
    }

    @Override
    public void startVideo() {
        super.startVideo();
        Log.i(TAG, "startVideo");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        Log.i(TAG, "Seek position ");
    }

    @Override
    public void gotoScreenFullscreen() {
        super.gotoScreenFullscreen();
        Log.i(TAG, "goto Fullscreen");
    }

    @Override
    public void gotoScreenNormal() {
        super.gotoScreenNormal();
        Log.i(TAG, "quit Fullscreen");
    }

    @Override
    public void autoFullscreen(float x) {
        super.autoFullscreen(x);
        Log.i(TAG, "auto Fullscreen");
    }

    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
        Log.i(TAG, "click blank");
    }

    //onState 代表了播放器引擎的回调，播放视频各个过程的状态的回调
    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        Log.i(TAG, "Auto complete");
    }

    //changeUiTo 真能能修改ui的方法
    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
    }

    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();
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
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }

}

package cn.jzvd.demo.CustomJzvd;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;
import cn.jzvd.demo.Tab_3_List.GifCreateHelper;

/**
 * Created by dl on 2020/4/6.
 */
public class JzvdStdGetGif extends JzvdStd implements GifCreateHelper.JzGifListener {

    GifCreateHelper mGifCreateHelper;

    TextView tv_hint;
    FrameLayout fl_hint_region;
    ImageView convert_to_gif;


    //Gif panel region
    FrameLayout gif_pannel;
    ImageView iv_gif_back;
    TextView tv_gif_next;
    JzvdStd jz_video_center;
    LinearLayout keyFrame_container;
    FrameLayout fl_phone_focus;
    TextView tv_red_line;

    String saveGifPathName;
    long current;

    public JzvdStdGetGif(Context context) {
        super(context);
    }

    public JzvdStdGetGif(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);

        tv_hint = findViewById(R.id.tv_hint);
        fl_hint_region = findViewById(R.id.fl_hint_region);
        convert_to_gif = findViewById(R.id.convert_to_gif);
        gif_pannel = findViewById(R.id.gif_pannel);
        iv_gif_back = findViewById(R.id.iv_gif_back);
        tv_gif_next = findViewById(R.id.tv_gif_next);
        jz_video_center = findViewById(R.id.jz_video_center);
        keyFrame_container = findViewById(R.id.keyFrame_container);
        fl_phone_focus = findViewById(R.id.fl_phone_focus);
        tv_red_line = findViewById(R.id.tv_red_line);

        convert_to_gif.setOnClickListener((v -> {
            gif_pannel.setVisibility(View.VISIBLE);
//
            if (TextUtils.isEmpty(saveGifPathName)) {
                saveGifPathName = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/jiaozi-" + System.currentTimeMillis() + ".gif";
            }
            mGifCreateHelper = new GifCreateHelper(this, this, 200, 1, 300, 200, 5000, saveGifPathName);
            current = System.currentTimeMillis();
//            mGifCreateHelper.startGif();//这个函数里用了jzvd的两个参数。

            initGifPanelRegion();
            try {
                mediaInterface.pause();
                onStatePause();
                JZUtils.saveProgress(getContext(), Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl(), getCurrentPositionWhenPlaying());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }));

        topContainer.setOnTouchListener((v, event) -> true);
        fl_hint_region.setOnTouchListener((v, event) -> true);
    }

    private long selectDuration = 15000;//可供用户挑选的总时长(一般直接固定为15s 单位：毫秒)
    private float fingerStartX = -999; //底部截留框开始滑动的起始位置
    private float phoneFocusStartX = 0f; //底部截留框开始滑动的起始位置

    private void initGifPanelRegion() {
        int bitmapWidth = JZUtils.dip2px(getContext(), 125);
        int bitmapHeight = JZUtils.dip2px(getContext(), 70);
        iv_gif_back.setOnClickListener((v -> {
            gif_pannel.setVisibility(View.GONE);
            // 妥协产物，需要重构
            startVideo();
            iv_gif_back.postDelayed(() -> {
                mediaInterface.seekTo(JZUtils.getSavedProgress(getContext(), Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl()));
            }, 500);

        }));
        tv_gif_next.setOnClickListener((v -> {
//            gif_pannel.setVisibility(View.GONE);
            tv_hint.setText("正在创建Gif...");
            fl_hint_region.setVisibility(View.VISIBLE);

            mGifCreateHelper.getPeriodGif(gifStartTime, gifEndTime, bitmapWidth, bitmapHeight);
        }));


        //总体逻辑：先根据视频总时长，计算出可供选择的开始时间和结束时间，然后根据可供选择的开始时间和结束时间，计算出可供选择的关键帧的时间点，然后根据关键帧的时间点，获取关键帧的图片，然后展示出来，然后用户选择，然后生成gif
        // 1. 计算出可供选择的开始时间和结束时间
        //从当前播放时间的前一秒开始算，如果当前播放时间小于1秒，则从0开始算
        long startTime = mediaInterface.getCurrentPosition() - 1000;
        startTime = startTime < 0 ? 0 : startTime;
        long endTime = startTime + selectDuration;
        endTime = endTime > mediaInterface.getDuration() ? mediaInterface.getDuration() : endTime;
        // 2. 计算出可供选择的关键帧的时间点
        //固定取5张图片
        int keyFrameCount = 5;
        long realDuration = endTime - startTime;
        long interval = realDuration / (keyFrameCount - 1);
        List<Long> bitmapTime = Lists.newArrayList(startTime, startTime + interval, startTime + interval * 2, startTime + interval * 3, endTime);
        List<Bitmap> keyFrameImages = mGifCreateHelper.getBitmaps(bitmapTime, bitmapWidth, bitmapHeight);
        keyFrame_container.removeAllViews();
        for (Bitmap keyFrameImage : keyFrameImages) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(keyFrameImage);
            keyFrame_container.addView(imageView, new LinearLayout.LayoutParams(bitmapWidth, bitmapHeight));
        }

        // iv_phone_focus随着手指滑动
        float iv_phone_focus_width = JZUtils.dip2px(getContext(), 100);//和布局中的宽一致
        float maxLeftMargin = keyFrameCount * bitmapWidth - iv_phone_focus_width;
        long finalStartTime = startTime;
        FrameLayout.LayoutParams layoutParamsTemp = (FrameLayout.LayoutParams) fl_phone_focus.getLayoutParams();
        layoutParamsTemp.leftMargin=0;
        fl_phone_focus.setLayoutParams(layoutParamsTemp);
        fl_phone_focus.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
//                    Log.e("Jzvd-gif", "getRawX:"+event.getRawX());
                    if (fingerStartX == -999) {
                        fingerStartX = event.getRawX();
                        phoneFocusStartX = ((FrameLayout.LayoutParams) v.getLayoutParams()).leftMargin;
                    }
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                    layoutParams.leftMargin = (int) (phoneFocusStartX + event.getRawX() - fingerStartX);
                    if (layoutParams.leftMargin < 0) {
                        layoutParams.leftMargin = 0;
                    }
                    if (layoutParams.leftMargin > maxLeftMargin) {
                        layoutParams.leftMargin = (int) maxLeftMargin;
                    }
                    v.setLayoutParams(layoutParams);
//                    Log.e("Jzvd-gif", "leftMargin:"+((FrameLayout.LayoutParams)v.getLayoutParams()).leftMargin);
                    break;
                case MotionEvent.ACTION_UP:
                    fingerStartX = -999;
                    int leftMargin = ((FrameLayout.LayoutParams) v.getLayoutParams()).leftMargin;
                    float selectStartTime = finalStartTime + (leftMargin / (bitmapWidth * keyFrameCount * 1.0f)) * realDuration;
                    startCenterVideo((long) selectStartTime, (long) selectStartTime + interval);
                    break;
            }
            return true;
        });


        // 红线循环滑动
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tv_red_line, "translationX", 0, iv_phone_focus_width);
        objectAnimator.setDuration(interval);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);


        // 中间区域的视频播放
        jz_video_center.setUp((String) jzDataSource.getCurrentUrl(), "");
        jz_video_center.startVideo();
        jz_video_center.postDelayed(() -> {
            startCenterVideo(finalStartTime, finalStartTime + interval);
            objectAnimator.start();
        }, 500);
    }

    Timer timer;//这个timer是为了循环播放视频而设置的
    private long gifStartTime = 0;//gif的开始时间
    private long gifEndTime = 0;//gif的结束时间

    private void startCenterVideo(long startTime, long endTime) {
        Log.e("Jzvd-gif", "startTime:" + startTime + ",endTime:" + endTime);
        if (timer != null) {
            timer.cancel();
        }

        gifStartTime = startTime;
        gifEndTime = endTime;
        jz_video_center.mediaInterface.seekTo(startTime);
        timer = new Timer();
        // 每隔500毫秒检查一下当前播放时间，如果超过了endTime，则seek到startTime
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (jz_video_center.mediaInterface != null) {
                    long currentPosition = jz_video_center.mediaInterface.getCurrentPosition();
                    if (currentPosition >= endTime) {
                        jz_video_center.mediaInterface.seekTo(startTime);
                    }
                }
            }
        }, 0, 500);
    }


    @Override
    public void process(int curPosition, int total, String status) {
        Log.e("Jzvd-gif", status + "  " + curPosition + "/" + total + "  time: " + (System.currentTimeMillis() - current)+" thread： "+Thread.currentThread().getName());
        tv_hint.post(() -> tv_hint.setText(curPosition + "/" + total + " " + status));
    }

    @Override
    public void result(boolean success, File file) {
        fl_hint_region.post(() -> {
            fl_hint_region.setVisibility(View.GONE);
            iv_gif_back.performClick();
            if (gifListener != null) {
                gifListener.result(success, file);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_gif;
    }

    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
        if (screen == SCREEN_FULLSCREEN) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                convert_to_gif.setVisibility(View.VISIBLE);
            } else {
                convert_to_gif.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        convert_to_gif.setVisibility(View.VISIBLE);
    }

    @Override
    public void dissmissControlView() {
        super.dissmissControlView();
        post(() -> {
            if (screen == SCREEN_FULLSCREEN) {
                convert_to_gif.setVisibility(View.GONE);
                bottomProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
        if (screen == SCREEN_FULLSCREEN) {
            bottomProgressBar.setVisibility(GONE);
            convert_to_gif.setVisibility(View.GONE);
        }
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        convert_to_gif.setVisibility(View.GONE);
    }

    @Override
    public void reset() {
        posterImageView.setImageBitmap(textureView.getBitmap());
        super.reset();
        cancelDismissControlViewTimer();
        unregisterWifiListener(getApplicationContext());
    }

    /**
     * 设置保存gif的路径,如果不设置会放到默认的DCIM目录下
     *
     * @param saveGifPathName
     */
    public void setSaveGifPathName(String saveGifPathName) {
        this.saveGifPathName = saveGifPathName;
    }

    private GifListener gifListener;

    /**
     * 设置gif创建结果回调
     *
     * @param gifListener
     */
    public void setGifListener(GifListener gifListener) {
        this.gifListener = gifListener;
    }

    public interface GifListener {
        public void result(boolean success, File file);
    }
}

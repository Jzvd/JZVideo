package cn.jzvd.demo.CustomJzvd;

import java.io.File;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.GifCreateHelper;

/**
 * Created by dl on 2020/4/6.
 */
public class JzvdStdGif extends JzvdStd implements GifCreateHelper.JzGifListener {

    GifCreateHelper mGifCreateHelper;

    long current;

    TextView tv_hint;
    FrameLayout fl_hint_region;
    ImageView convert_to_gif;

    public JzvdStdGif(Context context) {
        super(context);
    }

    public JzvdStdGif(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);

        tv_hint = findViewById(R.id.tv_hint);
        fl_hint_region = findViewById(R.id.fl_hint_region);
        convert_to_gif = findViewById(R.id.convert_to_gif);

        //详细使用方式
//        myJzvdStd.initGifHelper(jaoziVideoGifSaveListener,delay,inSampleSize,smallScale,gifPeriod, gifPath)
        //快速使用方式
        initGifHelper(this);

        convert_to_gif.setOnClickListener((v -> {
            clickStartGif(v);
        }));

        topContainer.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        fl_hint_region.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    /**
     * @param jzGifListener 保存进度的回调函数
     */
    public void initGifHelper(GifCreateHelper.JzGifListener jzGifListener) {
        mGifCreateHelper = new GifCreateHelper(this, jzGifListener);
    }

    /**
     * @param jzGifListener 保存进度的回调函数
     * @param delay                     每一帧之间的延时
     * @param inSampleSize              采样率，最小值1 即：每隔inSampleSize个像素点，取一个读入到内存。越大处理越快
     * @param smallScale                缩小倍数，越大处理越快
     * @param gifPeriod                 gif时长，毫秒
     * @param gifPath                   gif文件的存储路径
     */
    public void initGifHelper(GifCreateHelper.JzGifListener jzGifListener,
                              int delay, int inSampleSize, int smallScale, int gifPeriod, String gifPath) {
        mGifCreateHelper = new GifCreateHelper(this, jzGifListener, delay, inSampleSize, smallScale, gifPeriod, gifPath);
    }

    /**
     * 开始生成gif
     */
    public void startGif() {
        mGifCreateHelper.startGif();
        try {
            mediaInterface.pause();
            onStatePause();
        }catch (Exception e){}

    }

    /**
     * 开始生成gif
     *
     * @param bitmapFromTime gif图在视频中的开始时间
     * @param vedioUrl       视频链接
     */
    public void startGif(long bitmapFromTime, String vedioUrl) {
        mGifCreateHelper.startGif(bitmapFromTime, vedioUrl);
        if (Jzvd.STATE_PLAYING == state) {
            startButton.performClick();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_gif;
    }

    public void clickStartGif(View view) {
        tv_hint.setText("正在创建Gif...");
        fl_hint_region.setVisibility(View.VISIBLE);
        current=System.currentTimeMillis();
        startGif();

//        HttpProxyCacheServer proxy = ApplicationDemo.getProxy(this);
//        //startGif的两种使用方式
//        if(proxy.isCached(urlCrude)){
//            //被缓存了直接读取缓存
//            startGif();
//        }else{
//            //没被缓存读取原始链接（速度较慢）
//            startGif(getCurrentPositionWhenPlaying(),urlCrude);
//        }
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
    public void process(int curPosition, int total, String status) {
        Log.e("fffs",status+"  "+curPosition+"/"+total+"  curruentTime: "+(System.currentTimeMillis()-current));
        post(new Runnable() {
            @Override
            public void run() {
                tv_hint.setText(status+"  "+curPosition+"/"+total);
            }
        });
    }

    @Override
    public void result(boolean success, File file) {
        fl_hint_region.setVisibility(View.GONE);
        Toast.makeText(getContext(), "创建成功", Toast.LENGTH_LONG).show();
    }
}

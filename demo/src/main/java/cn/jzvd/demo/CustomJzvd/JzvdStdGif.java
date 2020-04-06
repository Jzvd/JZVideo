package cn.jzvd.demo.CustomJzvd;

import java.io.File;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.GifCreateHelper;

/**
 * Created by dl on 2020/4/6.
 */
public class JzvdStdGif extends JzvdStd {

    GifCreateHelper mGifCreateHelper;

    TextView tv_hint;
    FrameLayout fl_hint_region;

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

        initGifHelper();
        findViewById(R.id.convert_to_gif).setOnClickListener((v -> {
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
     * @param jaoziVideoGifSaveListener 保存进度的回调函数
     */
    public void initGifHelper(GifCreateHelper.JaoziVideoGifSaveListener jaoziVideoGifSaveListener) {
        mGifCreateHelper = new GifCreateHelper(this, jaoziVideoGifSaveListener);
    }

    /**
     * @param jaoziVideoGifSaveListener 保存进度的回调函数
     * @param delay                     每一帧之间的延时
     * @param inSampleSize              采样率，最小值1 即：每隔inSampleSize个像素点，取一个读入到内存。越大处理越快
     * @param smallScale                缩小倍数，越大处理越快
     * @param gifPeriod                 gif时长，毫秒
     * @param gifPath                   gif文件的存储路径
     */
    public void initGifHelper(GifCreateHelper.JaoziVideoGifSaveListener jaoziVideoGifSaveListener,
                              int delay, int inSampleSize, int smallScale, int gifPeriod, String gifPath) {
        mGifCreateHelper = new GifCreateHelper(this, jaoziVideoGifSaveListener, delay, inSampleSize, smallScale, gifPeriod, gifPath);
    }

    /**
     * 开始生成gif
     */
    public void startGif() {
        mGifCreateHelper.startGif();
        if (Jzvd.STATE_PLAYING == state) {
            startButton.performClick();
        }

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

    long current;
    private void initGifHelper() {
        //详细使用方式
//        myJzvdStd.initGifHelper(jaoziVideoGifSaveListener,delay,inSampleSize,smallScale,gifPeriod, gifPath)

        //快速使用方式
        initGifHelper(new GifCreateHelper.JaoziVideoGifSaveListener() {
            @Override
            public void result(boolean success, File file) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        fl_hint_region.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "创建成功", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void process(int curPosition, int total,String status) {
                Log.e("fffs",status+"  "+curPosition+"/"+total+"  curruentTime: "+(System.currentTimeMillis()-current));
                post(new Runnable() {
                    @Override
                    public void run() {
                        tv_hint.setText(status+"  "+curPosition+"/"+total);
                    }
                });
            }
        });
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
}

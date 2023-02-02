package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

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


        convert_to_gif.setOnClickListener((v -> {

            tv_hint.setText("正在创建Gif...");
            fl_hint_region.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(saveGifPathName)) {
                saveGifPathName = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/jiaozi-" + System.currentTimeMillis() + ".gif";
            }
            mGifCreateHelper = new GifCreateHelper(this, this, 200, 1, 300, 200, 5000, saveGifPathName);
            current = System.currentTimeMillis();
            mGifCreateHelper.startGif();//这个函数里用了jzvd的两个参数。
            try {
                mediaInterface.pause();
                onStatePause();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }));

        topContainer.setOnTouchListener((v, event) -> true);
        fl_hint_region.setOnTouchListener((v, event) -> true);
    }


    @Override
    public void process(int curPosition, int total, String status) {
        Log.e("Jzvd-gif", status + "  " + curPosition + "/" + total + "  time: " + (System.currentTimeMillis() - current));
        post(() -> tv_hint.setText(curPosition + "/" + total + " " + status));
    }

    @Override
    public void result(boolean success, File file) {
        fl_hint_region.setVisibility(View.GONE);
        if (gifListener != null) {
            gifListener.result(success, file);
        }
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

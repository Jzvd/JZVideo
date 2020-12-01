package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.LongDef;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;

/**
 * 根据视频宽高自适应全屏方向
 */
public class JzvdStdAutoOrizental extends JzvdStd {

    public JzvdStdAutoOrizental(Context context) {
        super(context);
    }

    public JzvdStdAutoOrizental(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onVideoSizeChanged(int width, int height) {
        super.onVideoSizeChanged(width, height);
        if (width > 0 && height > 0) {
            if (height > width) {
                Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            } else {
                Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }
}

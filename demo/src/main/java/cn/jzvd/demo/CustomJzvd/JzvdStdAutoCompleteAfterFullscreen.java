package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZUtils;
import cn.jzvd.JzvdStd;

/**
 * 全屏状态播放完成，不退出全屏
 * Created by Nathen on 2016/11/26.
 */
public class JzvdStdAutoCompleteAfterFullscreen extends JzvdStd {
    public JzvdStdAutoCompleteAfterFullscreen(Context context) {
        super(context);
    }

    public JzvdStdAutoCompleteAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onCompletion() {
        if (screen == SCREEN_FULLSCREEN) {
            onStateAutoComplete();
        } else {
            super.onCompletion();
        }

    }
}

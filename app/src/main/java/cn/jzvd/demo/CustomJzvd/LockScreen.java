package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import cn.jzvd.JZUtils;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;

public class LockScreen extends JzvdStd {
    private boolean isLockScreen=true;
    private ImageView lockIv;

    public LockScreen(Context context) {
        super(context);
    }

    public LockScreen(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void init(Context context) {
        super.init(context);
        lockIv = findViewById(R.id.lock);
        lockIv.setOnClickListener(this);
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        lockIv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        lockIv.setVisibility(View.VISIBLE);
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
                    if (isLockScreen) {
                        isLockScreen=false;
                        JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else {
                        isLockScreen=true;
                        JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

                    }
                }
                break;
        }
    }
}

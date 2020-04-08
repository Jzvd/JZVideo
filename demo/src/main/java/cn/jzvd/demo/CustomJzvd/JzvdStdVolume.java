package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;

public class JzvdStdVolume extends JzvdStd {
    ImageView ivVolume;
    boolean volumeOpen;

    public JzvdStdVolume(Context context) {
        super(context);
    }

    public JzvdStdVolume(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        ivVolume = findViewById(R.id.volume);
        ivVolume.setOnClickListener(this);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (screen == SCREEN_FULLSCREEN) {
            mediaInterface.setVolume(1f, 1f);
            ivVolume.setImageResource(R.drawable.ic_volume_open);
        } else {
            mediaInterface.setVolume(volumeOpen ? 1f : 0f, volumeOpen ? 1f : 0f);
            ivVolume.setImageResource(volumeOpen ? R.drawable.ic_volume_open : R.drawable.ic_volume_close);
        }
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        if (mediaInterface != null && !volumeOpen)
            mediaInterface.setVolume(0f, 0f);
        ivVolume.setImageResource(volumeOpen ? R.drawable.ic_volume_open : R.drawable.ic_volume_close);
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        if (mediaInterface != null)
            mediaInterface.setVolume(1f, 1f);
        ivVolume.setImageResource(R.drawable.ic_volume_open);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.volume) {
            volumeOpen = !volumeOpen;
            mediaInterface.setVolume(volumeOpen ? 1f : 0f, volumeOpen ? 1f : 0f);
            ivVolume.setImageResource(volumeOpen ? R.drawable.ic_volume_open : R.drawable.ic_volume_close);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_std_with_volume_button;
    }
}
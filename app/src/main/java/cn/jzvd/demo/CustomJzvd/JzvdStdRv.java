package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.jzvd.JzvdStd;

public class JzvdStdRv extends JzvdStd {
    private ClickUi clickUi;

    public void setClickUi(ClickUi clickUi) {
        this.clickUi = clickUi;
    }

    public JzvdStdRv(Context context) {
        super(context);
    }

    public JzvdStdRv(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == cn.jzvd.R.id.surface_container &&
                (state == STATE_PLAYING ||
                        state == STATE_PAUSE)) {
            if (clickUi != null) clickUi.onClickUiToggle();
        }
        super.onClick(v);
    }

    @Override
    public void onClickUiToggle() {
        bottomContainer.setVisibility(View.GONE);
        super.onClickUiToggle();
    }

    public interface ClickUi {
        void onClickUiToggle();
    }

}

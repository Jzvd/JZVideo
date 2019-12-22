package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
    public void onClickUiToggle() {
        bottomContainer.setVisibility(View.GONE);
        super.onClickUiToggle();
        Log.d(TAG, "onClickUiToggle: ");
        if (clickUi != null) clickUi.onClickUiToggle();
    }

    public interface ClickUi {
        void onClickUiToggle();
    }

}

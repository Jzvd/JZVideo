package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;

import cn.jzvd.demo.R;

public class MyJzvdStdNoTitleNoClarity extends MyJzvdStd {

    public MyJzvdStdNoTitleNoClarity(Context context) {
        super(context);
    }

    public MyJzvdStdNoTitleNoClarity(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_jzstd_notitle;
    }

}

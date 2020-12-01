package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.core.content.ContextCompat;

import cn.jzvd.JzvdStd;
import cn.jzvd.demo.BuildConfig;
import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.DipAndPx;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;
import cn.jzvd.demo.utils.CenteredImageSpan;

import java.util.HashMap;

public class JzvdDanmu extends JzvdStd {
    public DanmakuView danmakuView;
    private DanmakuContext danmakuContext;
    private BaseDanmakuParser danmakuParser;


    public JzvdDanmu(Context context) {
        super(context);
    }

    public JzvdDanmu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_danmu;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        danmakuView = findViewById(R.id.jz_danmu);
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行,可设置多种类型限制行数
        danmakuContext = DanmakuContext.create();
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f)
                .setScaleTextSize(1.2f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40);
        danmakuParser = new BaseDanmakuParser() {
            @Override
            protected IDanmakus parse() {
                return new Danmakus();
            }
        };
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                danmakuView.start();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuView.showFPS(BuildConfig.DEBUG);
        danmakuView.enableDanmakuDrawingCache(true);
    }

    //onState 代表了播放器引擎的回调，播放视频各个过程的状态的回调
    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        if (danmakuView.isPrepared()) {
            danmakuView.restart();
        }
        danmakuView.prepare(danmakuParser, danmakuContext);
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        if (danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }

    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        if (danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    }

    public void releaseDanMu() {
        danmakuView.release();
        danmakuView = null;
    }

    @Override
    public void onStateError() {
        super.onStateError();
        danmakuView.release();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        danmakuView.stop();
        danmakuView.clear();
        danmakuView.clearDanmakusOnScreen();
    }

    public void showDanmmu() {
        danmakuView.show();
    }

    public void hideDanmmu() {
        danmakuView.hide();
    }

    /**
     * 发送文字弹幕
     *
     * @param text   弹幕文字
     * @param isSelf 是不是自己发的
     */
    public void addDanmaku(String text, boolean isSelf) {
        danmakuContext.setCacheStuffer(new SpannedCacheStuffer(), null);
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null) {
            return;
        }
        danmaku.text = text;
        danmaku.priority = 3;
        danmaku.isLive = false;
        danmaku.setTime(danmakuView.getCurrentTime() + 1200);
        danmaku.textSize = DipAndPx.dip2px(getContext(), isSelf ? 20 : 12);
        danmaku.textColor = isSelf ? Color.BLUE : Color.WHITE;
        danmaku.textShadowColor = Color.GRAY;
        danmaku.underlineColor = isSelf ? Color.GREEN : Color.TRANSPARENT;
        danmaku.borderColor = isSelf ? Color.GREEN : Color.TRANSPARENT;
        danmakuView.addDanmaku(danmaku);
    }

    /**
     * 发送自定义弹幕
     */
    public void addDanmakuWithDrawable() {
        danmakuContext.setCacheStuffer(new BackgroundCacheStuffer(), null);
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null) {
            return;
        }
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher);
        int size = DipAndPx.dip2px(getContext(), 20);
        drawable.setBounds(0, 0, size, size);
        danmaku.text = createSpannable(drawable);
        danmaku.priority = 4;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = false;
        danmaku.setTime(danmakuView.getCurrentTime() + 1200);
        danmaku.textSize = DipAndPx.dip2px(getContext(), 12);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.WHITE;
        danmakuView.addDanmaku(danmaku);

    }

    private SpannableStringBuilder createSpannable(Drawable drawable) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        CenteredImageSpan span = new CenteredImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(" 自定义弹幕起飞~");
        return spannableStringBuilder;
    }

    /**
     * 绘制背景(自定义弹幕样式)
     */
    private class BackgroundCacheStuffer extends SpannedCacheStuffer {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
//            danmaku.padding = 5;  // 在背景绘制模式下增加padding
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#65777777"));//黑色 普通
            int radius = DipAndPx.dip2px(getContext(), 10);
            canvas.drawRoundRect(new RectF(left, top, left + danmaku.paintWidth, top + danmaku.paintHeight), radius, radius, paint);
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
        }
    }


}

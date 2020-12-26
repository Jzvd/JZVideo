package cn.jzvd.demo.Tab_4_More;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jzvd.jzvideo.UrlsKt;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;

/**
 * Created by Nathen on 16/10/13.
 */

public class WebViewActivity extends AppCompatActivity {
    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.webview));
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_webview);
        mWebView = findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JZCallBack(), "jzvd");
        mWebView.loadUrl("file:///android_asset/jzvd.html");
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class JZCallBack {

        @JavascriptInterface
        public void adViewJiaoZiVideoPlayer(final int width, final int height, final int top, final int left, final int index) {
            runOnUiThread(() -> {
                if (index == 0) {
                    JzvdStd jzvdStd = new JzvdStd(WebViewActivity.this);
                    jzvdStd.setUp(UrlsKt.getVideos()[16], UrlsKt.getTitles()[16], Jzvd.SCREEN_NORMAL);
                    Glide.with(WebViewActivity.this).load(UrlsKt.getThumbnails()[16]).into(jzvdStd.posterImageView);

                    ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                    layoutParams.y = JZUtils.dip2px(WebViewActivity.this, top);
                    layoutParams.x = JZUtils.dip2px(WebViewActivity.this, left);
                    layoutParams.height = JZUtils.dip2px(WebViewActivity.this, height);
                    layoutParams.width = JZUtils.dip2px(WebViewActivity.this, width);

                    LinearLayout linearLayout = new LinearLayout(WebViewActivity.this);
                    linearLayout.addView(jzvdStd);
                    mWebView.addView(linearLayout, layoutParams);
                } else {
                    JzvdStd jzvdStd = new JzvdStd(WebViewActivity.this);
                    jzvdStd.setUp(UrlsKt.getVideos()[16], UrlsKt.getTitles()[16], Jzvd.SCREEN_NORMAL);
                    Glide.with(WebViewActivity.this).load(UrlsKt.getThumbnails()[16]).into(jzvdStd.posterImageView);

                    ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                    layoutParams.y = JZUtils.dip2px(WebViewActivity.this, top);
                    layoutParams.x = JZUtils.dip2px(WebViewActivity.this, left);
                    layoutParams.height = JZUtils.dip2px(WebViewActivity.this, height);
                    layoutParams.width = JZUtils.dip2px(WebViewActivity.this, width);

                    LinearLayout linearLayout = new LinearLayout(WebViewActivity.this);
                    linearLayout.addView(jzvdStd);
                    mWebView.addView(linearLayout, layoutParams);
                }

            });

        }
    }
}

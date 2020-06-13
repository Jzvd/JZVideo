package cn.jzvd.demo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.jzvd.demo.Tab_4_Other.DirectPlayActivity;
import cn.jzvd.demo.Tab_4_Other.LocalVideoActivity;
import cn.jzvd.demo.Tab_4_Other.WebViewActivity;

/**
 * Created by pengan.li on 2020/5/8.
 */
public class Fragment_4_Other extends Fragment implements View.OnClickListener {

    TextView versionTextView;
    private Button mDirectPlay, mWebView, mLocalVideo;

    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return appVersionName;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);
//                View.inflate(getContext(), R.layout.fragment_other, null);
        mDirectPlay = view.findViewById(R.id.direct_play);
        mWebView = view.findViewById(R.id.web_view);
        mLocalVideo = view.findViewById(R.id.local_video);
        versionTextView = view.findViewById(R.id.version);
        mDirectPlay.setOnClickListener(this);
        mWebView.setOnClickListener(this);
        mLocalVideo.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        versionTextView.setText("Version " + getAppVersionName(getContext()));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.direct_play:
                startActivity(new Intent(getContext(), DirectPlayActivity.class));
                break;
            case R.id.web_view:
                startActivity(new Intent(getContext(), WebViewActivity.class));
                break;
            case R.id.local_video:
                startActivity(new Intent(getContext(), LocalVideoActivity.class));
                break;
        }

    }
}

package cn.jzvd.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.CustomJzvd.JzvdStdAutoOrizental;
import cn.jzvd.demo.CustomJzvd.JzvdStdRound;
import cn.jzvd.demo.Tab_2_Custom.AGVideo.AGVideoActivity;
import cn.jzvd.demo.CustomJzvd.JzvdStdAutoCompleteAfterFullscreen;
import cn.jzvd.demo.CustomJzvd.JzvdStdLockScreen;
import cn.jzvd.demo.CustomJzvd.JzvdStdMp3;
import cn.jzvd.demo.CustomJzvd.JzvdStdShowShareButtonAfterFullscreen;
import cn.jzvd.demo.CustomJzvd.JzvdStdShowTextureViewAfterAutoComplete;
import cn.jzvd.demo.CustomJzvd.JzvdStdShowTitleAfterFullscreen;
import cn.jzvd.demo.CustomJzvd.JzvdStdSpeed;
import cn.jzvd.demo.CustomJzvd.JzvdStdVolume;
import cn.jzvd.demo.CustomJzvd.JzvdStdVolumeAfterFullscreen;

/**
 * Created by pengan.li on 2020/5/8.
 * 展示饺子最基本的用法
 */
public class Fragment_2_Custom extends Fragment implements View.OnClickListener {

    JzvdStdShowShareButtonAfterFullscreen jzvdStdWithShareButton;
    JzvdStdShowTitleAfterFullscreen jzvdStdShowTitleAfterFullscreen;
    JzvdStdShowTextureViewAfterAutoComplete jzvdStdShowTextureViewAfterAutoComplete;
    JzvdStdAutoCompleteAfterFullscreen jzvdStdAutoCompleteAfterFullscreen;
    JzvdStdVolumeAfterFullscreen jzvdStdVolumeAfterFullscreen;
    JzvdStdMp3 jzvdStdMp3;
    JzvdStdSpeed jzvdStdSpeed;
    JzvdStdLockScreen lockScreen;
    JzvdStdVolume jzvdStdVolume;
    JzvdStdRound jzvdStdRound;
    JzvdStdAutoOrizental jzvdStdAutoOrizental;

    JzvdStd jzvdStd_1_1, jzvdStd_16_9;
    JzvdStd jzNoTitle;
    Button agVideo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_custom, null);

        agVideo = view.findViewById(R.id.agvideo);
        jzNoTitle = view.findViewById(R.id.jz_notitle);
        lockScreen = view.findViewById(R.id.lock_screen);
        jzvdStdRound = view.findViewById(R.id.jz_round);
        jzvdStdWithShareButton = view.findViewById(R.id.custom_videoplayer_standard_with_share_button);
        jzvdStdShowTitleAfterFullscreen = view.findViewById(R.id.custom_videoplayer_standard_show_title_after_fullscreen);
        jzvdStdShowTextureViewAfterAutoComplete = view.findViewById(R.id.custom_videoplayer_standard_show_textureview_aoto_complete);
        jzvdStdAutoCompleteAfterFullscreen = view.findViewById(R.id.custom_videoplayer_standard_aoto_complete);
        jzvdStd_1_1 = view.findViewById(R.id.jz_videoplayer_1_1);
        jzvdStd_16_9 = view.findViewById(R.id.jz_videoplayer_16_9);
        jzvdStdVolumeAfterFullscreen = view.findViewById(R.id.jz_videoplayer_volume);
        jzvdStdMp3 = view.findViewById(R.id.jz_videoplayer_mp3);
        jzvdStdSpeed = view.findViewById(R.id.jz_videoplayer_speed);
        jzvdStdVolume = view.findViewById(R.id.custom_videoplayer_standard_with_volume_button);
        jzvdStdAutoOrizental = view.findViewById(R.id.jz_auto_oriental);

        agVideo.setOnClickListener(this);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        jzvdStdAutoOrizental.setUp("https://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子大小任意");
        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(jzvdStdAutoOrizental.posterImageView);

        jzvdStdRound.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子圆角");
        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(jzvdStdRound.posterImageView);


        jzNoTitle.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子叫啥也显示不出来啊");
        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(jzNoTitle.posterImageView);

        lockScreen.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子定身");
        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(lockScreen.posterImageView);


        jzvdStdWithShareButton.setUp(Urls.videoUrlList[3], "饺子想呼吸", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosterList[3])
                .into(jzvdStdWithShareButton.posterImageView);


        jzvdStdShowTitleAfterFullscreen.setUp(Urls.videoUrlList[4], "饺子想摇头", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosterList[4])
                .into(jzvdStdShowTitleAfterFullscreen.posterImageView);

        jzvdStdShowTextureViewAfterAutoComplete.setUp(Urls.videoUrlList[5], "饺子想旅行", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosterList[5])
                .into(jzvdStdShowTextureViewAfterAutoComplete.posterImageView);

        jzvdStdAutoCompleteAfterFullscreen.setUp(Urls.videoUrls[0][1], "饺子没来", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStdAutoCompleteAfterFullscreen.posterImageView);

        jzvdStd_1_1.setUp(Urls.videoUrls[0][1], "饺子有事吗", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStd_1_1.posterImageView);
        jzvdStd_1_1.widthRatio = 1;
        jzvdStd_1_1.heightRatio = 1;

        jzvdStd_16_9.setUp(Urls.videoUrls[0][1], "饺子来不了", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStd_16_9.posterImageView);
        jzvdStd_16_9.widthRatio = 16;
        jzvdStd_16_9.heightRatio = 9;

        jzvdStdVolumeAfterFullscreen.setUp(Urls.videoUrls[0][1], "饺子摇摆", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStdVolumeAfterFullscreen.posterImageView);

        jzvdStdMp3.setUp(Urls.videoUrls[0][1],
                "饺子你听", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStdMp3.posterImageView);


        jzvdStdSpeed.setUp(Urls.videoUrls[0][1],
                "饺子快点", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStdSpeed.posterImageView);


        jzvdStdVolume.setUp(Urls.videoUrls[0][1],
                "饺子吃莽莽", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(Urls.videoPosters[0][1])
                .into(jzvdStdVolume.posterImageView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.agvideo) {
            startActivity(new Intent(getContext(), AGVideoActivity.class));
        }
    }
}

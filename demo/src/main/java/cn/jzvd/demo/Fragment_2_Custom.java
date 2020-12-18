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

import org.jzvd.jzvideo.UrlsKt;

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
        jzvdStdAutoOrizental.setUp(UrlsKt.getVideos()[0], UrlsKt.getTitles()[0]);
        Glide.with(this).load(UrlsKt.getThumbnails()[0]).into(jzvdStdAutoOrizental.posterImageView);

        jzvdStdRound.setUp(UrlsKt.getVideos()[1], UrlsKt.getTitles()[1]);
        Glide.with(this).load(UrlsKt.getThumbnails()[1]).into(jzvdStdRound.posterImageView);


        jzNoTitle.setUp(UrlsKt.getVideos()[2], UrlsKt.getTitles()[2]);
        Glide.with(this).load(UrlsKt.getThumbnails()[2]).into(jzNoTitle.posterImageView);

        lockScreen.setUp(UrlsKt.getVideos()[3], UrlsKt.getTitles()[3]);
        Glide.with(this).load(UrlsKt.getThumbnails()[3]).into(lockScreen.posterImageView);


        jzvdStdWithShareButton.setUp(UrlsKt.getVideos()[4], UrlsKt.getTitles()[4], Jzvd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[4]).into(jzvdStdWithShareButton.posterImageView);


        jzvdStdShowTitleAfterFullscreen.setUp(UrlsKt.getVideos()[5], UrlsKt.getTitles()[5], Jzvd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[5]).into(jzvdStdShowTitleAfterFullscreen.posterImageView);

        jzvdStdShowTextureViewAfterAutoComplete.setUp(UrlsKt.getVideos()[6], UrlsKt.getTitles()[6], Jzvd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[6]).into(jzvdStdShowTextureViewAfterAutoComplete.posterImageView);

        jzvdStdAutoCompleteAfterFullscreen.setUp(UrlsKt.getVideos()[7], UrlsKt.getTitles()[7], Jzvd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[7]).into(jzvdStdAutoCompleteAfterFullscreen.posterImageView);

        jzvdStd_1_1.setUp(UrlsKt.getVideos()[8], UrlsKt.getTitles()[8], Jzvd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[8]).into(jzvdStd_1_1.posterImageView);
        jzvdStd_1_1.widthRatio = 1;
        jzvdStd_1_1.heightRatio = 1;

        jzvdStd_16_9.setUp(UrlsKt.getVideos()[9], UrlsKt.getTitles()[9], Jzvd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[9]).into(jzvdStd_16_9.posterImageView);
        jzvdStd_16_9.widthRatio = 16;
        jzvdStd_16_9.heightRatio = 9;

        jzvdStdVolumeAfterFullscreen.setUp(UrlsKt.getVideos()[10], UrlsKt.getTitles()[10], Jzvd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[10]).into(jzvdStdVolumeAfterFullscreen.posterImageView);

        jzvdStdMp3.setUp(UrlsKt.getVideos()[11], UrlsKt.getTitles()[11], Jzvd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[11]).into(jzvdStdMp3.posterImageView);


        jzvdStdSpeed.setUp(UrlsKt.getVideos()[12], UrlsKt.getTitles()[12], Jzvd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[12]).into(jzvdStdSpeed.posterImageView);


        jzvdStdVolume.setUp(UrlsKt.getVideos()[13], UrlsKt.getTitles()[13], Jzvd.SCREEN_NORMAL);
        Glide.with(this).load(UrlsKt.getThumbnails()[13]).into(jzvdStdVolume.posterImageView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.agvideo) {
            startActivity(new Intent(getContext(), AGVideoActivity.class));
        }
    }
}

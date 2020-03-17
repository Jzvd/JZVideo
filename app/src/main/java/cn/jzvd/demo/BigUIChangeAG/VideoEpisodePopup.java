package cn.jzvd.demo.BigUIChangeAG;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.DpOrPxUtils;

public class VideoEpisodePopup extends PopupWindow {
    private Context mC;
    private LayoutInflater inflater;
    private View contentView;
    private RecyclerView episodeRecycler;
    private VideoEpisodeAdapter episodeAdapter;
    private List<AGEpsodeEntity> episodeList;
    private EpisodeClickListener episondeClickListener;
    /**
     * 当前正在播放的集数
     */
    private int playNum = 0;

    public EpisodeClickListener getEpisondeClickListener() {
        return episondeClickListener;
    }

    public void setEpisondeClickListener(EpisodeClickListener episondeClickListener) {
        this.episondeClickListener = episondeClickListener;
    }

    public VideoEpisodePopup(Context context, List<AGEpsodeEntity> entities){
        super(context);
        this.mC=context;
        mC=context;
        this.episodeList=entities;
        inflater=(LayoutInflater)mC.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView=inflater.inflate(R.layout.popup_video_episode,null);
        setContentView(contentView);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setWidth(DpOrPxUtils.dip2px(context,320));
        setOutsideTouchable(true);
        //不设置该属性，弹窗于屏幕边框会有缝隙并且背景不是半透明
        setBackgroundDrawable(new BitmapDrawable());
        episodeRecycler = contentView.findViewById(R.id.video_episode);
        episodeRecycler.setLayoutManager(new GridLayoutManager(context, 5));
        episodeAdapter = new VideoEpisodeAdapter(mC,episodeList);
        episodeRecycler.setAdapter(episodeAdapter);

        episodeAdapter.setmOnItemClickListener(new VideoEpisodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                if (episondeClickListener!=null){
                    episondeClickListener.onEpisodeClickListener(episodeList.get(position),position);
                }
                //更换当前正在播放的集数
                if (playNum<1){
                    playNum=1;
                }
                episodeList.get(playNum - 1).setPlay(false);
                playNum = position + 1;
                episodeList.get(playNum - 1).setPlay(true);
                episodeAdapter.notifyDataSetChanged();
//                dismiss();
            }
        });
    }

    public void setPlayNum(int playNum) {
        if (this.playNum != 0) {
            episodeList.get(this.playNum - 1).setPlay(false);
            this.playNum = playNum;
            episodeList.get(playNum - 1).setPlay(true);
        } else {
            this.playNum = playNum;
            episodeList.get(this.playNum - 1).setPlay(true);
        }
    }


    public interface EpisodeClickListener {
        /**
         * 选集发生变化
         * @param entity
         * @param position
         */
        void onEpisodeClickListener(AGEpsodeEntity entity, int position);
    }
}

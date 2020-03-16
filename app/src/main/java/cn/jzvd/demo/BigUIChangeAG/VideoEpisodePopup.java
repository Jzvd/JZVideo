package cn.jzvd.demo.BigUIChangeAG;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.List;

import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.DpOrPxUtils;

public class VideoEpisodePopup extends PopupWindow {
    private Context mC;
    private LayoutInflater inflater;
    private View contentView;
    public VideoEpisodePopup(Context context, List<AGEpsodeEntity> entities){
        super(context);
        this.mC=context;
        mC=context;
        inflater=(LayoutInflater)mC.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView=inflater.inflate(R.layout.popup_video_episode,null);
        setContentView(contentView);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setWidth(DpOrPxUtils.dip2px(context,320));
        setOutsideTouchable(true);
        //不设置该属性，弹窗于屏幕边框会有缝隙并且背景不是半透明
        setBackgroundDrawable(new BitmapDrawable());
    }
}

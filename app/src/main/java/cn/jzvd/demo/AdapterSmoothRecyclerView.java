package cn.jzvd.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.AutoPlayUtils;
import cn.jzvd.demo.CustomJzvd.JzvdStdRv;
import cn.jzvd.demo.CustomJzvd.ViewAttr;

public class AdapterSmoothRecyclerView extends RecyclerView.Adapter<AdapterSmoothRecyclerView.MyViewHolder> {

    public static final String TAG = "AdapterSmoothRecyclerView";
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Context context;
    private OnVideoClick onVideoClick;

    public void setOnVideoClick(OnVideoClick onVideoClick) {
        this.onVideoClick = onVideoClick;
    }

    public AdapterSmoothRecyclerView(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_smooth_videoview, parent,
                false));
        return holder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        JzvdStdRv jzvdStdRv;
        if (JzvdStdRv.CURRENT_JZVD != null && AutoPlayUtils.positionInList == position) {
            ViewParent parent = JzvdStdRv.CURRENT_JZVD.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(JzvdStdRv.CURRENT_JZVD);
            }
            holder.container.addView(JzvdStdRv.CURRENT_JZVD, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            jzvdStdRv = (JzvdStdRv) JzvdStdRv.CURRENT_JZVD;
        } else {
            if (holder.container.getChildCount() == 0) {
                jzvdStdRv = new JzvdStdRv(holder.container.getContext());
                holder.container.addView(jzvdStdRv,
                        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                jzvdStdRv = (JzvdStdRv) holder.container.getChildAt(0);
            }
            jzvdStdRv.setUp(
                    VideoConstant.videoUrls[0][position],
                    VideoConstant.videoTitles[0][position], Jzvd.SCREEN_NORMAL);
            Glide.with(holder.container.getContext()).load(VideoConstant.videoThumbs[0][position])
                    .into(jzvdStdRv.thumbImageView);
        }
        jzvdStdRv.setId(R.id.jzvdplayer);
        jzvdStdRv.setAtList(true);
        jzvdStdRv.setClickUi(new JzvdStdRv.ClickUi() {
            @Override
            public void onClickUiToggle() {
                AutoPlayUtils.positionInList = position;
                jzvdStdRv.setAtList(false);
                ViewAttr attr = new ViewAttr();
                int[] location = new int[2];
                holder.container.getLocationOnScreen(location);
                attr.setX(location[0]);
                attr.setY(location[1]);
                if (onVideoClick != null) onVideoClick.videoClick(attr, position);
                jzvdStdRv.setClickUi(null);
            }

            @Override
            public void onClickStart() {
                AutoPlayUtils.positionInList = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout container;

        public MyViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.surface_container);
        }
    }

    public interface OnVideoClick {
        void videoClick(ViewAttr viewAttr, int position);
    }

}

package cn.jzvd.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.JzvdStdRv;

public class AdapterSmoothRecyclerView extends RecyclerView.Adapter<AdapterSmoothRecyclerView.MyViewHolder> {

    public static final String TAG = "AdapterSmoothRecyclerView";
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Context context;

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
        Log.i(TAG, "onBindViewHolder [" + holder.jzvdStd.hashCode() + "] position=" + position);

        holder.jzvdStd.setClickUi(new JzvdStdRv.ClickUi() {
            @Override
            public void onClickUiToggle() {
                JzvdStdRv.setCurrentJzvd(holder.jzvdStd);
                ViewCompat.setTransitionName(holder.container, "videoView");
                Activity activity = (Activity) holder.itemView.getContext();
                Intent intent = new Intent(activity, ActivityListViewDetail.class);
                // 这里指定了共享的视图元素
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.container, "videoView");
                ActivityCompat.startActivity(activity, intent, options.toBundle());
                holder.jzvdStd.setClickUi(null);
            }
        });
        if (holder.container.getChildCount() == 0) {
            ViewParent parent = JzvdStdRv.CURRENT_JZVD.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(JzvdStdRv.CURRENT_JZVD);
            }
            holder.container.addView(JzvdStdRv.CURRENT_JZVD, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }else {
            holder.jzvdStd.setUp(
                    VideoConstant.videoUrls[0][position],
                    VideoConstant.videoTitles[0][position], Jzvd.SCREEN_NORMAL);
            Glide.with(holder.jzvdStd.getContext()).load(VideoConstant.videoThumbs[0][position]).into(holder.jzvdStd.thumbImageView);
        }
    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        JzvdStdRv jzvdStd;
        FrameLayout container;

        public MyViewHolder(View itemView) {
            super(itemView);
            jzvdStd = itemView.findViewById(R.id.videoplayer);
            container = itemView.findViewById(R.id.surface_container);
        }
    }

}

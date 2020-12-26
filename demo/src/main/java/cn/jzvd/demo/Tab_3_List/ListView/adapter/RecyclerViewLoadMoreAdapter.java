package cn.jzvd.demo.Tab_3_List.ListView.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.VideoEntity;

public class RecyclerViewLoadMoreAdapter extends RecyclerView.Adapter<RecyclerViewLoadMoreAdapter.MyViewHolder> {

    public static final String TAG = "RecyclerViewLoadMoreAdapter";
    ArrayList<VideoEntity> videos;
    private Context context;

    public RecyclerViewLoadMoreAdapter(Context context, ArrayList<VideoEntity> videos) {
        this.context = context;
        this.videos = videos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_videoview, parent,
                false));
        return holder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder [" + holder.jzvdStd.hashCode() + "] position=" + position);
        VideoEntity videoEntity = videos.get(position);
        holder.jzvdStd.setUp(videoEntity.getUrl(), videoEntity.getTitle(), Jzvd.SCREEN_NORMAL);
        Glide.with(holder.jzvdStd.getContext()).load(videoEntity.getThumb()).into(holder.jzvdStd.posterImageView);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        JzvdStd jzvdStd;

        public MyViewHolder(View itemView) {
            super(itemView);
            jzvdStd = itemView.findViewById(R.id.videoplayer);
        }
    }

}

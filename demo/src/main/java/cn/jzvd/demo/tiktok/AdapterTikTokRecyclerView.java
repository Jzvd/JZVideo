package cn.jzvd.demo.tiktok;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.JzvdStdTikTok;
import cn.jzvd.demo.R;
import cn.jzvd.demo.VideoConstant;

public class AdapterTikTokRecyclerView extends RecyclerView.Adapter<AdapterTikTokRecyclerView.MyViewHolder> {

    public static final String TAG = "AdapterTikTokRecyclerView";
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Context context;

    public AdapterTikTokRecyclerView(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_tiktok, parent,
                false));
        return holder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder [" + holder.jzvdStd.hashCode() + "] position=" + position);

        JZDataSource jzDataSource = new JZDataSource(VideoConstant.videoUrls[3][position],
                VideoConstant.videoTitles[0][position]);
        jzDataSource.looping = true;
        holder.jzvdStd.setUp(jzDataSource,Jzvd.SCREEN_NORMAL);
        Glide.with(holder.jzvdStd.getContext()).load(VideoConstant.videoPosters[0][position]).into(holder.jzvdStd.posterImageView);
    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        JzvdStdTikTok jzvdStd;

        public MyViewHolder(View itemView) {
            super(itemView);
            jzvdStd = itemView.findViewById(R.id.videoplayer);
        }
    }

}

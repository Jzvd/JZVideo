package cn.jzvd.demo.ListView.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.JzvdStdTinyWindow;
import cn.jzvd.demo.R;
import cn.jzvd.demo.Urls;

public class RecyclerViewTinyAdapter extends RecyclerView.Adapter<RecyclerViewTinyAdapter.MyViewHolder> {

    public static final String TAG = "AdapterRecyclerView";
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Context context;

    public RecyclerViewTinyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_videoview_tiny, parent,
                false));
        return holder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder [" + holder.jzvdStd.hashCode() + "] position=" + position);
        if (holder.jzvdStd.getTag() != null)
            holder.jzvdStd = ((View) holder.jzvdStd.getTag()).findViewById(R.id.videoplayer);
        holder.jzvdStd.setUp(
                Urls.videoUrls[0][position],
                Urls.videoTitles[0][position], Jzvd.SCREEN_NORMAL);
        Glide.with(holder.jzvdStd.getContext()).load(Urls.videoPosters[0][position]).into(holder.jzvdStd.posterImageView);
    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        JzvdStdTinyWindow jzvdStd;

        public MyViewHolder(View itemView) {
            super(itemView);
            jzvdStd = itemView.findViewById(R.id.videoplayer);
            jzvdStd.setTag(itemView);
        }
    }

}

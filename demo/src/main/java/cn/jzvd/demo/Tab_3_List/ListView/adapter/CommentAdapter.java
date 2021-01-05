package cn.jzvd.demo.Tab_3_List.ListView.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jzvd.jzvideo.UrlsKt;

import cn.jzvd.demo.R;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_comment, parent,
                false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.ivHead.getContext()).load(
                UrlsKt.getPl1()[position])
                .into(holder.ivHead);
        holder.tvName.setText(
                UrlsKt.getTl1()[position]);
        holder.tvContent.setText(
                UrlsKt.getVl1()[position]);
    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }


     class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHead;
        TextView tvName;
        TextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHead = itemView.findViewById(R.id.iv_head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}

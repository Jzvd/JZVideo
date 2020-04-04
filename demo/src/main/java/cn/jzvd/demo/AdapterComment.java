package cn.jzvd.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder> {
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_comment, parent,
                false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.ivHead.getContext()).load(VideoConstant.videoPosters[0][position])
                .into(holder.ivHead);
        holder.tvName.setText(VideoConstant.videoTitles[0][position]);
        holder.tvContent.setText(VideoConstant.videoTitles[0][position]);
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

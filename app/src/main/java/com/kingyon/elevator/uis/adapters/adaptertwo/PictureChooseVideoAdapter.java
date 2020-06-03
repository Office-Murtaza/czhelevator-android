package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.VideoInfo;
import com.zhaoss.weixinrecorded.util.TimeUtils;

import java.util.List;

import static com.kingyon.elevator.photopicker.MimeType.getVideoDuration;

/**
 * @Created By Admin  on 2020/4/29
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class PictureChooseVideoAdapter extends RecyclerView.Adapter<PictureChooseVideoAdapter.ViewHolder> {
    private List<VideoInfo> mVideoInfos;
    private Context context;
    public static String videopath = "";
    private int mposition = -1;

    public PictureChooseVideoAdapter(Context context,List<VideoInfo> list){
        this.context = context;
        this.mVideoInfos = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_choose,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        videopath = "";
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将点击的位置传出去
                mposition = holder.getAdapterPosition();
                //在点击监听里最好写入setVisibility(View.VISIBLE);这样可以避免效果会闪
                holder.tv_xznum.setText("√");
                holder.tv_xznum.setVisibility(View.VISIBLE);
                //刷新界面 notify 通知Data 数据set设置Changed变化
                //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
                notifyDataSetChanged();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder!=null){
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(mVideoInfos.get(position).thumbnailData));
            holder.tv_video_time.setText(TimeUtils.secondToTime((int) (getVideoDuration(mVideoInfos.get(position).data)/1000)));
            if (position == mposition) {
                holder.tv_xznum.setVisibility(View.VISIBLE);
                videopath = mVideoInfos.get(position).data;
            } else {
                holder.tv_xznum.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return mVideoInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_xznum,tv_video_time;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_video_image);
            tv_xznum = itemView.findViewById(R.id.tv_xznum);
            tv_video_time = itemView.findViewById(R.id.tv_video_time);
        }
    }
}

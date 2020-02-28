package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.MsgCommentEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 * 消息详情数据适配器
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<MsgCommentEntity> messageList;
    private Context context;
    protected LayoutInflater inflater;
    private BaseOnItemClick<MsgCommentEntity> baseOnItemClick;
    private BaseOnItemClick<MsgCommentEntity> baseOnLongItemClick;

    public CommentsAdapter(Context context, List<MsgCommentEntity> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.inflater = LayoutInflater.from(context);
    }


    public void reflashData(List<MsgCommentEntity> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public void setBaseOnItemClick(BaseOnItemClick<MsgCommentEntity> baseOnItemClick) {
        this.baseOnItemClick = baseOnItemClick;
    }

    public void setBaseOnLongItemClick(BaseOnItemClick<MsgCommentEntity> baseOnLongItemClick) {
        this.baseOnLongItemClick = baseOnLongItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comment_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            MsgCommentEntity msgCommentEntity = messageList.get(position);
            holder.item_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baseOnItemClick.onItemClick(msgCommentEntity, position);
                }
            });
            holder.item_container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (baseOnLongItemClick != null) {
                        baseOnLongItemClick.onItemClick(msgCommentEntity, position);
                    }
                    return true;
                }
            });
            GlideUtils.loadImage(context, msgCommentEntity.getPhotoUrl(), holder.user_head);
            GlideUtils.loadImage(context, msgCommentEntity.getCoverUrl(), holder.news_img);
            holder.user_name.setText(msgCommentEntity.getNickname());
            holder.msg_time.setText(msgCommentEntity.getCreateTime());
            holder.msg_content.setText(msgCommentEntity.getComment());
            if (msgCommentEntity.isRead()) {
                holder.unread_view.setVisibility(View.GONE);
            } else {
                holder.unread_view.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView user_head;
        TextView user_name;
        TextView msg_content;
        TextView msg_time;
        ImageView news_img;
        RelativeLayout item_container;
        View unread_view;


        public ViewHolder(View itemView) {
            super(itemView);
            user_head = itemView.findViewById(R.id.user_head);
            user_name = itemView.findViewById(R.id.user_name);
            msg_content = itemView.findViewById(R.id.msg_content);
            msg_time = itemView.findViewById(R.id.msg_time);
            news_img = itemView.findViewById(R.id.news_img);
            unread_view = itemView.findViewById(R.id.unread_view);
            item_container = itemView.findViewById(R.id.item_container);
        }
    }

}

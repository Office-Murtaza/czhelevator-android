package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.MsgNoticeEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 * 消息首页数据适配器
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<MsgNoticeEntity> messageList;
    private Context context;
    protected LayoutInflater inflater;
    private BaseOnItemClick<MsgNoticeEntity> baseOnItemClick;
    private BaseOnItemClick<MsgNoticeEntity> baseOnLongItemClick;

    public MessageAdapter(Context context, List<MsgNoticeEntity> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.inflater = LayoutInflater.from(context);
    }


    public void reflashData(List<MsgNoticeEntity> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public void setBaseOnItemClick(BaseOnItemClick<MsgNoticeEntity> baseOnItemClick) {
        this.baseOnItemClick = baseOnItemClick;
    }

    public void setBaseOnLongItemClick(BaseOnItemClick<MsgNoticeEntity> baseOnLongItemClick) {
        this.baseOnLongItemClick = baseOnLongItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.message_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            MsgNoticeEntity msgNoticeEntity = messageList.get(position);
            holder.message_title.setText(msgNoticeEntity.getTitle());
            if (msgNoticeEntity.getContent() == null||msgNoticeEntity.getContent().equals("")) {
                holder.message_content.setText("");
                holder.message_content.setVisibility(View.GONE);
                if (msgNoticeEntity.getImage() == null||msgNoticeEntity.getImage().equals("")) {
                    holder.msg_img.setVisibility(View.GONE);
                } else {
                    holder.msg_img.setVisibility(View.VISIBLE);
                    GlideUtils.loadImage(context, msgNoticeEntity.getImage(), holder.msg_img);
                }
            } else {
                holder.message_content.setVisibility(View.VISIBLE);
                holder.msg_img.setVisibility(View.GONE);
                holder.message_content.setText(msgNoticeEntity.getContent());
            }
            holder.message_date.setText(msgNoticeEntity.getTime());
            holder.item_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (baseOnItemClick != null) {
                        baseOnItemClick.onItemClick(msgNoticeEntity, position);
                    }
                }
            });
            holder.item_container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (baseOnLongItemClick != null) {
                        baseOnLongItemClick.onItemClick(msgNoticeEntity, position);
                    }
                    return true;
                }
            });
            if (msgNoticeEntity.isIsRead()) {
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

        TextView message_title;
        TextView message_content;
        TextView message_date;
        LinearLayout item_container;
        ImageView msg_img;
        View unread_view;

        public ViewHolder(View itemView) {
            super(itemView);
            message_title = itemView.findViewById(R.id.message_title);
            message_content = itemView.findViewById(R.id.message_content);
            message_date = itemView.findViewById(R.id.message_date);
            item_container = itemView.findViewById(R.id.item_container);
            msg_img = itemView.findViewById(R.id.msg_img);
            unread_view = itemView.findViewById(R.id.unread_view);
        }
    }

}

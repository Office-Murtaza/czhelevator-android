package com.kingyon.elevator.uis.adapters.adapterone;

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
import com.kingyon.elevator.entities.DianZanEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 * 消息详情数据适配器
 */
public class DianZanAdapter extends RecyclerView.Adapter<DianZanAdapter.ViewHolder> {

    private List<DianZanEntity> dianZanEntityList;
    private Context context;
    protected LayoutInflater inflater;
    private BaseOnItemClick<DianZanEntity> baseOnItemClick;
    private BaseOnItemClick<DianZanEntity> baseOnLongItemClick;

    public DianZanAdapter(Context context, List<DianZanEntity> dianZanEntityList) {
        this.context = context;
        this.dianZanEntityList = dianZanEntityList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setBaseOnItemClick(BaseOnItemClick<DianZanEntity> baseOnItemClick) {
        this.baseOnItemClick = baseOnItemClick;
    }

    public void setBaseOnLongItemClick(BaseOnItemClick<DianZanEntity> baseOnLongItemClick) {
        this.baseOnLongItemClick = baseOnLongItemClick;
    }

    public void reflashData(List<DianZanEntity> dianZanEntityList) {
        this.dianZanEntityList = dianZanEntityList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dianzan_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            DianZanEntity dianZanEntity = dianZanEntityList.get(position);
            GlideUtils.loadImage(context, dianZanEntity.getPhotoUrl(), holder.user_head);
            GlideUtils.loadImage(context, dianZanEntity.getCoverUrl(), holder.news_img);
            holder.msg_time.setText(dianZanEntity.getCreateTime());
            holder.user_name.setText(dianZanEntity.getNickname());
            holder.item_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (baseOnItemClick != null) {
                        baseOnItemClick.onItemClick(dianZanEntity, position);
                    }
                }
            });
            holder.item_container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (baseOnLongItemClick != null) {
                        baseOnLongItemClick.onItemClick(dianZanEntity, position);
                    }
                    return true;
                }
            });
            if (dianZanEntity.isRead()) {
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
        return dianZanEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView user_name;
        TextView msg_time;
        ImageView user_head;
        ImageView news_img;
        RelativeLayout item_container;
        View unread_view;

        public ViewHolder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            msg_time = itemView.findViewById(R.id.msg_time);
            user_head = itemView.findViewById(R.id.user_head);
            news_img = itemView.findViewById(R.id.news_img);
            unread_view = itemView.findViewById(R.id.unread_view);
            item_container = itemView.findViewById(R.id.item_container);
        }
    }

}

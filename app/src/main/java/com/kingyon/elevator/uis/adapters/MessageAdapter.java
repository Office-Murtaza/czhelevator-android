package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.IncomeDetailsEntity;
import com.kingyon.elevator.entities.PushMessageEntity;
import com.kingyon.elevator.utils.DensityUtil;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 * 消息首页数据适配器
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Object> messageList;
    private Context context;
    protected LayoutInflater inflater;

    public MessageAdapter(Context context, List<Object> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.inflater = LayoutInflater.from(context);
    }


    public void reflashData(List<Object> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.message_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

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
        RelativeLayout item_container;


        public ViewHolder(View itemView) {
            super(itemView);
            message_title = itemView.findViewById(R.id.message_title);
            message_content = itemView.findViewById(R.id.message_content);
            message_date = itemView.findViewById(R.id.message_date);
            item_container = itemView.findViewById(R.id.item_container);
        }
    }

}

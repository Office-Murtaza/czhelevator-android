package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.MassageListMentiy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created By Admin  on 2020/6/1
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MessageNewsAdapter extends RecyclerView.Adapter<MessageNewsAdapter.ViewHolder> {
    Context context;
    List<MassageListMentiy> listMentiys;


    public MessageNewsAdapter(Context context, List<MassageListMentiy> listMentiys) {
        this.context = context;
        this.listMentiys = listMentiys;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_message_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MassageListMentiy mentiy = listMentiys.get(position);
        holder.tvConent.setText(mentiy.content);
        holder.tvTitle.setText(mentiy.title);
        holder.tvTime.setText("14-45");

    }

    @Override
    public int getItemCount() {
        return listMentiys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_conent)
        TextView tvConent;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

package com.kingyon.elevator.uis.adapters.adaptertwo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.PointClassicEntiy;
import com.kingyon.elevator.entities.entities.ReportContent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created By Admin  on 2020/6/18
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class UserReportAdapter extends RecyclerView.Adapter<UserReportAdapter.ViewHolder> {
        Context context;
        public List<ReportContent> list;
        ItmeOnclick itmeOnclick;
        public UserReportAdapter(Context context, List<ReportContent> list, ItmeOnclick itmeOnclick) {
            this.context = context;
            this.itmeOnclick = itmeOnclick;
            this.list = list;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.itme_community_adapter, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserReportAdapter.ViewHolder holder, int position) {
            ReportContent pointClassicEntiy = list.get(position);
            holder.tvTitle.setText(pointClassicEntiy.content);
            holder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itmeOnclick.itmeOnclick(pointClassicEntiy.id);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_title)
            TextView tvTitle;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }


        public interface ItmeOnclick{
            void itmeOnclick(int id);
        }
}

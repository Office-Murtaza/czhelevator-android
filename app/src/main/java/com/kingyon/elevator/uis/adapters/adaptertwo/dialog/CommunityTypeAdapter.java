package com.kingyon.elevator.uis.adapters.adaptertwo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.PointClassicEntiy;
import com.kingyon.elevator.uis.dialogs.CommunityTypeDialog;
import com.kingyon.elevator.uis.dialogs.CommunityTypeTwoDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created By Admin  on 2020/6/18
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CommunityTypeAdapter extends RecyclerView.Adapter<CommunityTypeAdapter.ViewHolder> {
    Context context;
    ConentEntity<PointClassicEntiy> pointClassicEntiyConentEntity;
    public List<PointClassicEntiy.ChildBean> child;
    ItmeOnclick itmeOnclick;
    public CommunityTypeAdapter(Context context,ConentEntity<PointClassicEntiy> pointClassicEntiyConentEntity,ItmeOnclick itmeOnclick) {
        this.context = context;
        this.itmeOnclick = itmeOnclick;
        this.pointClassicEntiyConentEntity = pointClassicEntiyConentEntity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_community_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PointClassicEntiy pointClassicEntiy = pointClassicEntiyConentEntity.getContent().get(position);
            holder.tvTitle.setText(pointClassicEntiy.pointName);
            holder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  itmeOnclick.itmeOnclick(pointClassicEntiy.child,pointClassicEntiy);
                }
            });

    }

    @Override
    public int getItemCount() {
       return pointClassicEntiyConentEntity.getContent().size();
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
        void itmeOnclick(List<PointClassicEntiy.ChildBean> child, PointClassicEntiy pointClassicEntiy);
    }
}

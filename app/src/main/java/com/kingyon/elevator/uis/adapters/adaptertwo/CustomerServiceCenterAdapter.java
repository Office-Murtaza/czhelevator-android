package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.czh.myversiontwo.utils.StringContent;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.WikipediaEntiy;
import com.kingyon.elevator.uis.actiivty2.user.CustomerServiceCenterActivity;
import com.kingyon.elevator.uis.activities.WebViewActivity;
import com.kingyon.elevator.utils.MyActivityUtils;

import java.util.List;

import static com.czh.myversiontwo.utils.Constance.WEB_ACTIVITY;

/**
 * @Created By Admin  on 2020/7/1
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CustomerServiceCenterAdapter extends RecyclerView.Adapter<CustomerServiceCenterAdapter.ViewHoder> {
    private Context context;
    List<WikipediaEntiy.WikipediaBean.ItemBean> item;
    public CustomerServiceCenterAdapter(Context context, List<WikipediaEntiy.WikipediaBean.ItemBean> item){
        this.context = context;
        this.item = item;
    }
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.itme_customer_service_center,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.textView.setText(item.get(position).getName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(item.get(position).getContent());
                ActivityUtils.setActivity(WEB_ACTIVITY,"title",item.get(position).getName(),"content",item.get(position).getContent());
            }
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHoder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
        }
    }
}

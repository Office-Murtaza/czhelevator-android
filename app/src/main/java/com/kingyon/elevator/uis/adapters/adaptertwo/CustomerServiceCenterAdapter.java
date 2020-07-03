package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.actiivty2.user.CustomerServiceCenterActivity;

/**
 * @Created By Admin  on 2020/7/1
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CustomerServiceCenterAdapter extends RecyclerView.Adapter<CustomerServiceCenterAdapter.ViewHoder> {
    private Context context;
    private String src;

    public CustomerServiceCenterAdapter(Context context,String src){
        this.context = context;
        this.src = src;
    }
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.itme_customer_service_center,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.textView.setText("="+src);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHoder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
        }
    }
}

package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.IndustryEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created By Admin  on 2020/6/18
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TabooChooseAdapter extends RecyclerView.Adapter<TabooChooseAdapter.ViewHoder> {
    Context context;
    List<IndustryEntity> list;
    OnItmeonClick onItmeonClick;
    private int lastClickPosition=-1;

    public TabooChooseAdapter(Context context, List<IndustryEntity> list) {
        this.context = context;
        this.list = list;
    }
    public void setClick(OnItmeonClick onItmeonClick){
        this.onItmeonClick = onItmeonClick;
    }

    public void multipleChoose(int position) {
        IndustryEntity listItemBean = list.get(position);
        if (listItemBean.isChoose()) {
            listItemBean.setChoose(false);
        } else {
            listItemBean.setChoose(true);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_taboochoose, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.tvTitle.setText(list.get(position).getName());

        holder.imgCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItmeonClick!=null) {
                    onItmeonClick.onClock(position);
                }
            }
        });
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItmeonClick!=null) {
                    onItmeonClick.onClock(position);
                }
            }
        });
        if (list.get(position).isChoose()) {
            holder.imgCek.setSelected(true);
        }else {
            holder.imgCek.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_cek)
        ImageView imgCek;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        public ViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

   public interface OnItmeonClick{
        void onClock(int position );
    }
}

package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.IncomeDetailsEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 * 收入或者支出详情列表适配器
 */
public class BindedCardAdapter extends RecyclerView.Adapter<BindedCardAdapter.ViewHolder> {

    private List<IncomeDetailsEntity> incomeDetailsEntityList;
    private Context context;
    protected LayoutInflater inflater;
    private BaseOnItemClick<Integer> baseOnItemClick;

    public BindedCardAdapter(Context context, List<IncomeDetailsEntity> incomeDetailsEntityList) {
        this.context = context;
        this.incomeDetailsEntityList = incomeDetailsEntityList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setBaseOnItemClick(BaseOnItemClick<Integer> baseOnItemClick) {
        this.baseOnItemClick = baseOnItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bank_card_item_layout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseOnItemClick.onItemClick(position, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return incomeDetailsEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item_container;

        public ViewHolder(View itemView) {
            super(itemView);
            item_container = itemView.findViewById(R.id.item_container);
        }
    }

}

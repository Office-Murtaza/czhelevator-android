package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.IncomeDetailsEntity;
import com.kingyon.elevator.entities.IncomeEntity;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 * 收入或者支出详情列表适配器
 */
public class IncomeDetailsAdapter extends RecyclerView.Adapter<IncomeDetailsAdapter.ViewHolder> {

    private List<IncomeDetailsEntity> incomeDetailsEntityList;
    private Context context;
    protected LayoutInflater inflater;

    public IncomeDetailsAdapter(Context context, List<IncomeDetailsEntity> incomeDetailsEntityList) {
        this.context = context;
        this.incomeDetailsEntityList = incomeDetailsEntityList;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.income_details_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return incomeDetailsEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
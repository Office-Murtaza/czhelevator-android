package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


    public void reflashData(List<IncomeDetailsEntity> incomeDetailsEntityList) {
        this.incomeDetailsEntityList = incomeDetailsEntityList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.income_details_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       try {
           IncomeDetailsEntity incomeDetailsEntity = incomeDetailsEntityList.get(position);
           holder.tv_income_date.setText(incomeDetailsEntity.getDateValue());
           holder.tv_income_type.setText(incomeDetailsEntity.getTypeName());
           holder.tv_income_money.setText("¥"+incomeDetailsEntity.getAmount());
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    @Override
    public int getItemCount() {
        return incomeDetailsEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_income_date;
        TextView tv_income_type;
        TextView tv_income_money;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_income_date = itemView.findViewById(R.id.tv_income_date);
            tv_income_type = itemView.findViewById(R.id.tv_income_type);
            tv_income_money = itemView.findViewById(R.id.tv_income_money);
        }
    }

}

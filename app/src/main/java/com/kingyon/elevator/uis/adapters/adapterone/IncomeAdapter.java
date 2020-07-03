package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.YesterdayIncomeEntity;
import com.kingyon.elevator.entities.entities.EarningsYesterdayEnity;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.TimeUtil;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 */
public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {

    private List<EarningsYesterdayEnity> incomeEntityList;
    private Context context;
    protected LayoutInflater inflater;
    String type;


    public IncomeAdapter(Context context, List<EarningsYesterdayEnity> incomeEntityList,String type) {
        this.context = context;
        this.incomeEntityList = incomeEntityList;
        this.type = type;
        this.inflater = LayoutInflater.from(context);
    }


    public void reflashData(List<EarningsYesterdayEnity> incomeEntityList) {
        this.incomeEntityList = incomeEntityList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.income_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            EarningsYesterdayEnity yesterdayIncomeEntity = incomeEntityList.get(position);
            if (type.equals("1")){
                holder.tv_income_money.setText("¥\t" + yesterdayIncomeEntity.yesterdayMoney);
                holder.tv_income_time.setText(TimeUtil.getAllTime(Long.parseLong(yesterdayIncomeEntity.createTime)));
                String str3 = "<font color='#FF0000'><big>·</big></font>来源:"+ FormatUtils.getInstance().incomeType(yesterdayIncomeEntity.source);
                holder.tv_income_type.setText(Html.fromHtml(str3));
            }else {
                holder.tv_income_money.setText("¥\t" + yesterdayIncomeEntity.withdrawal);
                holder.tv_income_time.setText(TimeUtil.getAllTime(Long.parseLong(yesterdayIncomeEntity.createTime)));
                String str3 = "<font color='#FF0000'><big>·</big></font>来源:"+ FormatUtils.getInstance().incomeType(yesterdayIncomeEntity.source);
                holder.tv_income_type.setText(Html.fromHtml(str3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return incomeEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_income_time;
        TextView tv_income_money;
        TextView tv_income_type;
        RelativeLayout item_container;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_income_time = itemView.findViewById(R.id.tv_income_time);
            tv_income_money = itemView.findViewById(R.id.tv_income_money);
            tv_income_type = itemView.findViewById(R.id.tv_income_type);
            item_container = itemView.findViewById(R.id.item_container);
        }
    }

}

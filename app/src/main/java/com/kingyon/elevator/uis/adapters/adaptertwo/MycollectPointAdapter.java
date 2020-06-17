package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.RecommendHouseEntiy;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ADPOINT_DETAILS;
import static com.czh.myversiontwo.utils.DistanceUtils.distance;
import static com.czh.myversiontwo.utils.StringContent.STRING_COMMUNITY_CODE;
import static com.czh.myversiontwo.utils.StringContent.STRING_PRICE;

/**
 * @Created By Admin  on 2020/6/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MycollectPointAdapter extends RecyclerView.Adapter<MycollectPointAdapter.ViewHolder> {
    BaseActivity context;
    private List<RecommendHouseEntiy> list;
    private OnItemClickListener onItemClickListener = null;
    public MycollectPointAdapter(BaseActivity context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_my_collect_point, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecommendHouseEntiy data = list.get(position);
        holder.tv_price.setText(Html.fromHtml(String.format(STRING_PRICE,data.priceBusiness)));
        holder.tv_address.setText(data.address);
        holder.tv_community_code.setText(String.format(STRING_COMMUNITY_CODE,data.numberUnit,data.numberElevator));
        holder.tv_community_name.setText(data.name);
        holder.tv_distance.setText(distance(data.distanceM));

        holder.tv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.ll_itme_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(data.id);
                ActivityUtils.setActivity(ACTIVITY_ADPOINT_DETAILS,"panID",String.valueOf(data.id));
            }
        });
    }
    public void addData(List<RecommendHouseEntiy> list) {
        this.list = list;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_community_name)
        TextView tv_community_name;
        @BindView(R.id.tv_distance)
        TextView tv_distance;
        @BindView(R.id.tv_address)
        TextView tv_address;
        @BindView(R.id.tv_community_code)
        TextView tv_community_code;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.tv_collect)
        TextView tv_collect;
        @BindView(R.id.ll_itme_root)
        LinearLayout ll_itme_root;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}

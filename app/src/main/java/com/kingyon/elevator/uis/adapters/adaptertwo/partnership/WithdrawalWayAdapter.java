package com.kingyon.elevator.uis.adapters.adaptertwo.partnership;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.entities.UserCashTypeListEnity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationWithdrawActivity;
import com.kingyon.elevator.utils.AccountNumUtils;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.RuntimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created By Admin  on 2020/6/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class WithdrawalWayAdapter extends RecyclerView.Adapter<WithdrawalWayAdapter.ViewHolder> {

    private Context context;
    List<UserCashTypeListEnity> userCashTypeListEnities;
    CooperationInfoNewEntity entity1;
    String type;
    OnClick   onClick;
    public WithdrawalWayAdapter(Context context,List<UserCashTypeListEnity> userCashTypeListEnities,CooperationInfoNewEntity entity,String type) {
        this.context = context;
        this.userCashTypeListEnities = userCashTypeListEnities;
        this.entity1 = entity;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_withdrawal_way, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserCashTypeListEnity enity = userCashTypeListEnities.get(position);
            switch (enity.cashType){
                case 1:
                    holder.imgIcon.setImageResource(R.mipmap.ic_cashout_bank);
                    holder.tvName.setText("银行卡");
                    holder.tvZfb.setText(enity.openingBank+"("+(enity.cashAccount.substring(enity.cashAccount.length()-4))+")");
                    break;
                case 2:
                    holder.imgIcon.setImageResource(R.mipmap.ic_cashout_alipay);
                    holder.tvName.setText("支付宝");
                    holder.tvZfb.setText(AccountNumUtils.hidePhoneNum(enity.cashAccount));
                    break;
                case 3:
                    holder.imgIcon.setImageResource(R.mipmap.ic_cashout_wechat);
                    holder.tvName.setText("微信");
                    holder.tvZfb.setText(AccountNumUtils.hidePhoneNum(enity.cashAccount));
                    break;
            }
            holder.llZfb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type.equals("1")) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(CommonUtil.KEY_VALUE_2, enity);
                        bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity1);
                        MyActivityUtils.goActivity(context, CooperationWithdrawActivity.class, bundle);
                    }else {
                        onClick.OnClick(enity);
                    }
                }
            });
    }

    public void setOnClick(OnClick onClick){
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return userCashTypeListEnities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_zfb)
        TextView tvZfb;
        @BindView(R.id.ll_zfb)
        LinearLayout llZfb;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public  interface OnClick{
        void OnClick(UserCashTypeListEnity enity);
    }
}

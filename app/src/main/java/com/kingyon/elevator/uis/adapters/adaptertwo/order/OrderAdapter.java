package com.kingyon.elevator.uis.adapters.adaptertwo.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.uis.activities.order.OrderDetailsActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.TimeUtil;

import java.util.List;

import static com.czh.myversiontwo.utils.CodeType.OEDER_COMPLETE;
import static com.czh.myversiontwo.utils.CodeType.OEDER_REJECT;
import static com.czh.myversiontwo.utils.CodeType.OEDER_RELEASEING;
import static com.czh.myversiontwo.utils.CodeType.OEDER_WAIT;
import static com.czh.myversiontwo.utils.CodeType.OEDER_WAITRELEASE;
import static com.czh.myversiontwo.utils.StringContent.ORADER_NUMBER;

/**
 * @Created By Admin  on 2020/6/11
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    BaseActivity context;
    List<OrderDetailsEntity> list;
    public OrderAdapter(BaseActivity context){
        this.context = context;
    }
    public void addData(    List<OrderDetailsEntity> list){
        this.list = list;
    }
    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_order_lits,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        OrderDetailsEntity order = list.get(position);
        holder.tv_order_time.setText( String.format("下单时间：%s", TimeUtil.getAllTime(order.getCreateTime())));
        holder.tv_title.setText(order.getName());
        holder.tv_point.setText("点位：共"+order.getTotalScreen()+"面屏");
        holder.tv_type.setText("类型："+ FormatUtils.getInstance().getPlanType(order.getOrderType()));
        holder.tv_number.setText(String.format(ORADER_NUMBER,order.getTotalScreen(),CommonUtil.getTwoFloat(order.getRealPrice())));
        if (order.getLstHousingBean().size()>0) {
            GlideUtils.loadRoundCornersImage(context, order.getLstHousingBean().get(0).housePic, holder.img_img, 20);
        }
//        holder.img_status.setVisibility(View.GONE);
//        holder.tv_again.setVisibility(View.GONE);
        switch (order.getOrderStatus()){
            case OEDER_WAITRELEASE:
                holder.tv_status.setText("待发布");
                switch (order.getAuditState()){
                    case OEDER_WAIT:
                        /*审核中*/
                        holder.img_status.setVisibility(View.VISIBLE);
                        holder.img_status.setImageResource(R.mipmap.im_order_audit_wait);
                        break;
                    case OEDER_REJECT:
                        /*未通过*/
                        holder.img_status.setVisibility(View.VISIBLE);
                        holder.img_status.setImageResource(R.mipmap.im_order_audit_fail);
                        break;
                }
                break;
            case OEDER_COMPLETE:
                holder.tv_status.setText("已完成");
                holder.tv_again.setVisibility(View.VISIBLE);
                break;
            case OEDER_RELEASEING:
                holder.tv_status.setText("发布中");
                break;
                default:

        }
        holder.rl_itme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(order.getOrderSn());
                Bundle bundle = new Bundle();
                bundle.putString(CommonUtil.KEY_VALUE_1, order.getOrderSn());
                context.startActivity(OrderDetailsActivity.class, bundle);
            }
        });
/*   */
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_again,tv_number,tv_type,tv_point,tv_title,tv_order_time,tv_status;
        ImageView img_status,img_img;
        RelativeLayout rl_itme;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_again = itemView.findViewById(R.id.tv_status);
            tv_number = itemView.findViewById(R.id.tv_number);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_point = itemView.findViewById(R.id.tv_point);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_order_time = itemView.findViewById(R.id.tv_order_time);
            tv_status = itemView.findViewById(R.id.tv_status);
            img_status = itemView.findViewById(R.id.img_status);
            img_img = itemView.findViewById(R.id.img_img);
            rl_itme = itemView.findViewById(R.id.rl_itme);
        }
    }
}

package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.czh.myversiontwo.utils.DialogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.RecommendHouseEntiy;
import com.kingyon.elevator.uis.dialogs.AdvertisPutDialog;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import java.util.List;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ADPOINT_DETAILS;
import static com.czh.myversiontwo.utils.DistanceUtils.distance;
import static com.czh.myversiontwo.utils.StringContent.STRING_COMMUNITY_CODE;
import static com.czh.myversiontwo.utils.StringContent.STRING_PRICE;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isCertification;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * @Created By Admin  on 2020/5/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class RecommendHouseAdapter extends RecyclerView.Adapter<RecommendHouseAdapter.ViewHolder> {
    BaseActivity context;
    private List<RecommendHouseEntiy> list;
    private OnItemClickListener onItemClickListener = null;

    public RecommendHouseAdapter(BaseActivity context){
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_recommend_house,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder!=null){

            RecommendHouseEntiy data = list.get(position);
            holder.tv_price.setText(Html.fromHtml(String.format(STRING_PRICE,data.priceBusiness)));
            holder.tv_address.setText(data.address);
            holder.tv_community_code.setText(String.format(STRING_COMMUNITY_CODE,data.numberUnit,data.numberElevator));
            holder.tv_community_name.setText(data.name);
            holder.tv_distance.setText(distance((int) data.distanceM));

            holder.img_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (onItemClickListener != null) {
//                        onItemClickListener.onItemClick(v, position);
//                        new AdvertisPutDialog(context,data.id,data.name);
//                    }
                    if (isToken(context)){
                        if (isCertification()){
                            DialogUtils.shwoCertificationDialog(context);
                        }else {
                            AdvertisPutDialog advertisPutDialog = new AdvertisPutDialog(context, data.id, data.name);
                            advertisPutDialog.show();
                        }
                    }else {
                        ActivityUtils.setLoginActivity();
                    }
                }
            });
            holder.ll_itme_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.e(data.id);
                    ActivityUtils.setActivity(ACTIVITY_ADPOINT_DETAILS,"panID",String.valueOf(data.id),"distanceM",distance((int) data.distanceM));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<RecommendHouseEntiy> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_add;
        TextView tv_price,tv_community_code,tv_address,tv_distance,tv_community_name;
        LinearLayout ll_itme_root;
        public ViewHolder(View itemView) {
            super(itemView);
            img_add = itemView.findViewById(R.id.img_add);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_community_code = itemView.findViewById(R.id.tv_community_code);
            tv_distance = itemView.findViewById(R.id.tv_distance);
            tv_community_name = itemView.findViewById(R.id.tv_community_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            ll_itme_root = itemView.findViewById(R.id.ll_itme_root);
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

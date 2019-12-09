package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.BindAccountEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.kingyon.elevator.utils.AccountNumUtils;
import com.kingyon.elevator.utils.PublicFuncation;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 * 收入或者支出详情列表适配器
 */
public class BindedCardAdapter extends RecyclerView.Adapter<BindedCardAdapter.ViewHolder> {

    private List<BindAccountEntity> bindAccountEntityList;
    private Context context;
    protected LayoutInflater inflater;
    private BaseOnItemClick<BindAccountEntity> baseOnItemClick;

    public BindedCardAdapter(Context context, List<BindAccountEntity> bindAccountEntityList) {
        this.context = context;
        this.bindAccountEntityList = bindAccountEntityList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setBaseOnItemClick(BaseOnItemClick<BindAccountEntity> baseOnItemClick) {
        this.baseOnItemClick = baseOnItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bank_card_item_layout, parent, false);
        return new ViewHolder(view);
    }


    public void reflashData(List<BindAccountEntity> bindAccountEntityList) {
        this.bindAccountEntityList = bindAccountEntityList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BindAccountEntity bindAccountEntity = bindAccountEntityList.get(position);
        if (bindAccountEntity.getCashType() == 1) {
            GlideUtils.loadLocalImage(context, R.mipmap.zhanghao_yinghangkatubiao, holder.card_type_img);
            holder.cash_account.setText(AccountNumUtils.hideBankCardNum(bindAccountEntity.getCashAccount()));
        } else {
            GlideUtils.loadLocalImage(context, R.mipmap.zhanghao_zhifubaotubaio, holder.card_type_img);
            if (PublicFuncation.checkEmail(bindAccountEntity.getCashAccount()) || PublicFuncation.isMobileNO(bindAccountEntity.getCashAccount())) {
                holder.cash_account.setText(AccountNumUtils.hidePhoneNum(bindAccountEntity.getCashAccount()));
            } else {
                holder.cash_account.setText(bindAccountEntity.getCashAccount());
            }
        }
        holder.cash_name.setText(bindAccountEntity.getCashName());
        holder.item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseOnItemClick.onItemClick(bindAccountEntity, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bindAccountEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item_container;
        ImageView card_type_img;
        TextView cash_account;
        TextView cash_name;


        public ViewHolder(View itemView) {
            super(itemView);
            item_container = itemView.findViewById(R.id.item_container);
            card_type_img = itemView.findViewById(R.id.card_type_img);
            cash_account = itemView.findViewById(R.id.cash_account);
            cash_name = itemView.findViewById(R.id.cash_name);
        }
    }

}

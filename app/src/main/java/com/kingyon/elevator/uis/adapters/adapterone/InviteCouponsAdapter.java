package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class InviteCouponsAdapter extends BaseAdapterWithHF<CouponItemEntity> {
    public InviteCouponsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getFooterCount() {
        return 0;
    }

    @Override
    public int getHeaderCount() {
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_invite_coupon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapterWithHF.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        CouponItemEntity item = getItemData(position);
        boolean discount = TextUtils.equals(Constants.CouponType.DISCOUNT, item.getCoupontype());
        holder.tvType.setText(discount ? "折扣券" : "代金券");
        holder.tvDiscount.setText(getSpan(discount ? String.format("%s折", CommonUtil.getMayTwoFloat(item.getDiscount())) : String.format("%s元", item.getMoney())));
    }

    private CharSequence getSpan(String format) {
        SpannableString spannableString = new SpannableString(format);
        spannableString.setSpan(new AbsoluteSizeSpan(18, true), format.length() - 1, format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public class ViewHolder extends BaseAdapterWithHF.ViewHolder {
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_discount)
        TextView tvDiscount;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

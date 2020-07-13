package com.kingyon.elevator.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.GoPlaceAnOrderEntity;
import com.kingyon.elevator.uis.adapters.adapterone.HousingAdPriceAdapter;
import com.kingyon.elevator.utils.DensityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date:2018/12/24
 * Time:15:24
 * author:songpeng
 * Email:1531603384@qq.com
 * 订单明细查询对话框
 */
public class OrderDetailedTipsDialog extends Dialog {
    private GoPlaceAnOrderEntity goPlaceAnOrderEntity;
    private double zhekouPrice;
    private double couponsPrice;
    private double allPrice;
    private double realMoney;
    @BindView(R.id.housing_list_view)
    ListView housing_list_view;
    @BindView(R.id.all_money)
    TextView all_money;
    @BindView(R.id.all_coupons_money)
    TextView all_coupons_money;
    @BindView(R.id.zhekou_money)
    TextView zhekou_money;
    @BindView(R.id.coupons_money)
    TextView coupons_money;
    @BindView(R.id.all_real_price_money)
    TextView all_real_price_money;
    HousingAdPriceAdapter housingAdPriceAdapter;

    public OrderDetailedTipsDialog(Context context, GoPlaceAnOrderEntity goPlaceAnOrderEntity, double allPrice, double realMoney, double zhekouPrice, double couponsPrice) {
        super(context, R.style.MyDialog);
        this.goPlaceAnOrderEntity = goPlaceAnOrderEntity;
        this.zhekouPrice = zhekouPrice;
        this.couponsPrice = couponsPrice;
        this.allPrice = allPrice;
        this.realMoney = realMoney;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detailed_tips_dialog_layout);
        ButterKnife.bind(this);
        all_money.setText("¥" + allPrice);
        all_coupons_money.setText("-¥" + (zhekouPrice + couponsPrice));
        coupons_money.setText("-¥" + (zhekouPrice + couponsPrice));
        zhekou_money.setText("-¥" + zhekouPrice);
//        coupons_money.setText("-¥" + couponsPrice);
        all_real_price_money.setText("¥" + realMoney);
        housingAdPriceAdapter = new HousingAdPriceAdapter(getContext(), goPlaceAnOrderEntity.getTotalDayCount()
                , goPlaceAnOrderEntity.getPlanType(), goPlaceAnOrderEntity.getCellItemEntityArrayList());
        housing_list_view.setAdapter(housingAdPriceAdapter);
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        //设置窗口宽度为充满全屏
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        params.y = DensityUtil.dip2px(78);
        getWindow().setAttributes(params);
    }
}

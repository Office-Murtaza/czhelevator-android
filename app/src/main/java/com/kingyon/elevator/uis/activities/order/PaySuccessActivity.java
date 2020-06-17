package com.kingyon.elevator.uis.activities.order;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.TabEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_PAY_SUCCESS;

/**
 * Created by GongLi on 2019/1/4.
 * Email：lc824767150@163.com
 */
@Route(path = ACTIVITY_PAY_SUCCESS)
public class PaySuccessActivity extends BaseSwipeBackActivity {

    @BindView(R.id.tv_pay_time)
    TextView tvPayTime;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.tv_pay_sum)
    TextView tvPaySum;
    @Autowired
    String orderId;
    @Autowired
    String payType;
    @Autowired
    String priceActual;

    @Override
    protected String getTitleText() {

        return "支付成功";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_pay_success;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

//        tvPayTime.setText(TimeUtil.getAllTimeNoSecond(detailsEntity.getPayTime()));
        switch (payType) {
            case Constants.PayType.ALI_PAY:
                tvPayType.setText("支付宝");
                break;
            case Constants.PayType.WX_PAY:
                tvPayType.setText("微信");
                break;
            case Constants.PayType.BALANCE_PAY:
                tvPayType.setText("余额");
                break;
            case Constants.PayType.FREE:
                tvPayType.setText("免费");
                break;
            case Constants.PayType.APPLY:
                tvPayType.setText("苹果内购");
                break;
            case Constants.PayType.OFFLINE:
                tvPayType.setText("线下");
                break;
            default:
                tvPayType.setText("");
                break;
        }
        LogUtils.e(payType,orderId);
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日 HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        tvDiscount.setText(orderId);
        tvPayTime.setText(formatter.format(curDate));
        tvPaySum.setText(priceActual+"");

    }

    @OnClick({R.id.tv_order, R.id.tv_homepage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_order:
                Bundle bundle = new Bundle();
                bundle.putString(CommonUtil.KEY_VALUE_1, orderId);
                startActivity(OrderDetailsActivity.class, bundle);
                finish();
                break;
            case R.id.tv_homepage:
                ActivityUtil.finishAllNotMain();
                EventBus.getDefault().post(new TabEntity(0));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RuntimeUtils.goPlaceAnOrderEntity = null;
    }
}

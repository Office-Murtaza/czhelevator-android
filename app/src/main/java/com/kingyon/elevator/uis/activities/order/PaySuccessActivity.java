package com.kingyon.elevator.uis.activities.order;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.TabEntity;
import com.kingyon.elevator.entities.entities.DetailsEntily;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.fragments.main.PlanNewFragment;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.leo.afbaselibrary.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.pre_v_back)
    ImageView preVBack;

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
        LogUtils.e(orderId);
        showProgressDialog(getString(R.string.wait),false);
        NetService.getInstance().orderDetailSimple(orderId)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<DetailsEntily>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                        if (ex.getCode()==100200){
                            ActivityUtils.setLoginActivity();
                        }
                    }

                    @Override
                    public void onNext(DetailsEntily detailsEntily) {

                        tvPayType.setText(FormatUtils.getInstance().getPayWay(detailsEntily.payWay));
                        tvDiscount.setText(detailsEntily.orderSn);
                        tvPayTime.setText(TimeUtil.getAllTimeNoSecond(detailsEntily.payTime));
                        tvPaySum.setText(detailsEntily.realPrice + "");
                        hideProgress();
                    }
                });
//        LogUtils.e(payType, orderId);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//        Date curDate = new Date(System.currentTimeMillis());
//        tvDiscount.setText(orderId);
//        tvPayTime.setText(formatter.format(curDate));
//        tvPaySum.setText(priceActual + "");
////        tvPayTime.setText(TimeUtil.getAllTimeNoSecond(detailsEntity.getPayTime()));
//        switch (payType) {
//            case Constants.PayType.ALI_PAY:
//                tvPayType.setText("支付宝");
//                break;
//            case Constants.PayType.WX_PAY:
//                tvPayType.setText("微信");
//                break;
//            case Constants.PayType.BALANCE_PAY:
//                tvPayType.setText("余额");
//                break;
//            case Constants.PayType.FREE:
//                tvPayType.setText("免费");
//                break;
//            case Constants.PayType.APPLY:
//                tvPayType.setText("苹果内购");
//                break;
//            case Constants.PayType.OFFLINE:
//                tvPayType.setText("线下");
//                break;
//            default:
//                tvPayType.setText("");
//                break;
//        }


    }

    @OnClick({R.id.tv_order, R.id.tv_homepage,R.id.pre_v_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_order:
                Bundle bundle = new Bundle();
                bundle.putString(CommonUtil.KEY_VALUE_1, orderId);
                startActivity(OrderDetailsActivity.class, bundle);
                finish();
                break;
            case R.id.tv_homepage:
            case R.id.pre_v_back:
//                ActivityUtil.finishAllNotMain();
                EventBus.getDefault().post(new TabEntity(2));
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//            ActivityUtil.finishAllNotMain();
            EventBus.getDefault().post(new TabEntity(2));
//            startActivity(PlanNewFragment.class);
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RuntimeUtils.goPlaceAnOrderEntity = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

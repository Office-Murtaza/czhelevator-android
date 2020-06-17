package com.kingyon.elevator.uis.activities.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.utils.DialogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.entities.DataEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.widgets.StateLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isCertification;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 * 我的钱包
 */

public class MyWalletActivity extends BaseActivity {
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.pre_v_back)
    ImageView preVBack;
    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.ll_wallet)
    LinearLayout llWallet;
    @BindView(R.id.ll_shopping)
    LinearLayout llShopping;
    @BindView(R.id.ll_activity)
    LinearLayout llActivity;
    @BindView(R.id.ll_academy)
    LinearLayout llAcademy;
    @BindView(R.id.image_banner)
    ImageView imageBanner;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    private Float myWallet;
    @Override
    public int getContentViewId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().myWallet()
                .subscribe(new CustomApiCallback<DataEntity<Float>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
//                        showToast(ex.getDisplayMessage());
                        LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                        hideProgress();
                        finish();
                    }

                    @Override
                    public void onNext(DataEntity<Float> floatDataEntity) {
                        hideProgress();
                        if (floatDataEntity == null) {
                            throw new ResultException(9001, "没有获取到钱包余额");
                        }
                        myWallet = floatDataEntity.getData();
                        tvBalance.setText(String.format("￥%s", CommonUtil.getMayTwoFloat(myWallet != null ? myWallet : 0)));
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.pre_v_back, R.id.tv_right, R.id.tv_recharge, R.id.ll_wallet, R.id.ll_shopping, R.id.ll_activity, R.id.ll_academy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_back:
                finish();
                break;
            case R.id.tv_right:
                /*明细*/
                startActivity(MyWalletDetailsActivity.class);
                break;
            case R.id.tv_recharge:
                /*充值*/
                if (isCertification()) {
                    DialogUtils.shwoCertificationDialog(this);
                } else {
                    startActivityForResult(RechargeActivity.class, CommonUtil.REQ_CODE_1);
                }
                break;
            case R.id.ll_wallet:
                break;
            case R.id.ll_shopping:
                break;
            case R.id.ll_activity:
                break;
            case R.id.ll_academy:
                /*优惠卷*/
                startActivity(MyCouponsActivty.class);
                break;
        }
    }
}

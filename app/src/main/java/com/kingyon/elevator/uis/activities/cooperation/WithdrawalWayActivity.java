package com.kingyon.elevator.uis.activities.cooperation;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.entities.UserCashTypeListEnity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.partnership.WithdrawalWayAdapter;
import com.kingyon.elevator.uis.dialogs.AppAccountDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/6/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 选择提现方式
 */
public class WithdrawalWayActivity extends BaseActivity {

    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rcv_list)
    RecyclerView rcvList;
    private int page = 0;
    private CooperationInfoNewEntity entity;

    @Override
    public int getContentViewId() {
        return R.layout.activity_withdrawal_way;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        if (entity == null) {
            entity = new CooperationInfoNewEntity();
        }
        initData(page);
        tvTopTitle.setText("选择提现方式");

    }

    private void initData(int page) {
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().steUserCashTypeList(page)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<List<UserCashTypeListEnity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                    }

                    @Override
                    public void onNext(List<UserCashTypeListEnity> userCashTypeListEnities) {
                        hideProgress();
                        WithdrawalWayAdapter withdrawalWayAdapter = new WithdrawalWayAdapter(WithdrawalWayActivity.this, userCashTypeListEnities,entity,"1");
                        rcvList.setAdapter(withdrawalWayAdapter);
                        rcvList.setLayoutManager(new GridLayoutManager(WithdrawalWayActivity.this, 1, GridLayoutManager.VERTICAL, false));

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_right:
                /*添加账户*/
                AppAccountDialog appAccountDialog = new AppAccountDialog(this, entity);
                appAccountDialog.show();

                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(page);
    }
}

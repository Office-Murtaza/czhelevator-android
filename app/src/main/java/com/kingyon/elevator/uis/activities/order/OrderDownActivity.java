package com.kingyon.elevator.uis.activities.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.NormalElemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.adapterone.DownReasonsAdapter;
import com.kingyon.elevator.uis.dialogs.OfflineDialog;
import com.kingyon.elevator.uis.widgets.FullyGridLayoutManager;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DealScrollRecyclerView;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateLoadingActivity;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class OrderDownActivity extends BaseStateLoadingActivity {
    @BindView(R.id.rv_reasons)
    RecyclerView rvReasons;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;
    @BindView(R.id.tv_text_number)
    TextView tvTextNumber;

    private DownReasonsAdapter reasonsAdapter;
    private String orderId;

    @Override
    protected String getTitleText() {
        orderId = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        return "申请广告下播";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_down;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        rvReasons.addItemDecoration(new GridSpacingItemDecoration(2, ScreenUtil.dp2px(16), true));
        reasonsAdapter = new DownReasonsAdapter(this);
        DealScrollRecyclerView.getInstance().dealAdapter(reasonsAdapter, rvReasons, new FullyGridLayoutManager(this, 2));
        reasonsAdapter.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<NormalElemEntity>() {
            @Override
            public void onItemClick(View view, int position, NormalElemEntity entity, BaseAdapterWithHF<NormalElemEntity> baseAdaper) {
                if (entity != null) {
                    List<NormalElemEntity> datas = baseAdaper.getDatas();
                    for (NormalElemEntity item : datas) {
                        item.setChoosed(entity == item);
                    }
                }
                baseAdaper.notifyDataSetChanged();
            }
        });
        etRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>50){
                    ToastUtils.showToast(OrderDownActivity.this,"最多只能输入50字符",1000);
                }else {
                    tvTextNumber.setText(s.toString().length() + "/50");
                }
            }
        });
    }

    @Override
    protected void loadData() {
        NetService.getInstance().downAdTags()
                .compose(this.<List<NormalElemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<NormalElemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(List<NormalElemEntity> normalElemEntities) {
                        if (normalElemEntities == null || normalElemEntities.size() < 1) {
                            throw new ResultException(9001, "没有原因可供选择");
                        }
                        reasonsAdapter.refreshDatas(normalElemEntities);
                        loadingComplete(STATE_CONTENT);
                    }
                });
    }

    @OnClick(R.id.tv_ensure)
    public void onViewClicked() {
        NormalElemEntity reason = getChoosedReason();
        if (reason == null) {
            showToast("请选择一个原因");
            return;
        }
        OfflineDialog dialog = new OfflineDialog(this);
        dialog.onSuccessful(new IsSuccess() {
            @Override
            public void isSuccess(boolean success) {
                if (success){
                    showProgressDialog(getString(R.string.wait),false);
                    tvEnsure.setEnabled(false);
                    NetService.getInstance().downAd(orderId, reason.getObjectId(), etRemark.getText().toString())
                            .compose(OrderDownActivity.this.<String>bindLifeCycle())
                            .subscribe(new CustomApiCallback<String>() {
                                @Override
                                protected void onResultError(ApiException ex) {
                                    showToast(ex.getDisplayMessage());
                                    tvEnsure.setEnabled(true);
                                    hideProgress();
                                }
                                @Override
                                public void onNext(String s) {
                                    showToast("申请下播成功");
                                    tvEnsure.setEnabled(true);
                                    hideProgress();
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            });
                    }
                }
            });
        dialog.show();
    }


    private NormalElemEntity getChoosedReason() {
        NormalElemEntity result = null;
        if (reasonsAdapter != null) {
            List<NormalElemEntity> datas = reasonsAdapter.getDatas();
            for (NormalElemEntity item : datas) {
                if (item.isChoosed()) {
                    result = item;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

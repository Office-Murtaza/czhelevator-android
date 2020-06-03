package com.kingyon.elevator.uis.activities.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.NormalElemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.adapterone.DownReasonsAdapter;
import com.kingyon.elevator.uis.widgets.FullyGridLayoutManager;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DealScrollRecyclerView;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateLoadingActivity;
import com.leo.afbaselibrary.utils.ScreenUtil;

import java.util.List;

import butterknife.BindView;
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

    private DownReasonsAdapter reasonsAdapter;
    private long orderId;

    @Override
    protected String getTitleText() {
        orderId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
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
        showProgressDialog(getString(R.string.wait));
        tvEnsure.setEnabled(false);
        NetService.getInstance().downAd(orderId, reason.getObjectId(), etRemark.getText().toString())
                .compose(this.<String>bindLifeCycle())
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
}

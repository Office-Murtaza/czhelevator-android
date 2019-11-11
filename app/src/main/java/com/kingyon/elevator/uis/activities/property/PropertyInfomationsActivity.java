package com.kingyon.elevator.uis.activities.property;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.entities.PropertyInfomationInfo;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/15.
 * Email：lc824767150@163.com
 */

public class PropertyInfomationsActivity extends BaseStateRefreshingLoadingActivity<OrderDetailsEntity> {

    @BindView(R.id.pre_v_right)
    TextView preVRight;
    @BindView(R.id.tv_free)
    TextView tvFree;

    @Override
    protected String getTitleText() {
        return "便民信息";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_property_infomation;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setText("新增");
        preVRight.setVisibility(View.GONE);
        tvFree.setVisibility(View.GONE);
    }

    @Override
    protected MultiItemTypeAdapter<OrderDetailsEntity> getAdapter() {
        return new BaseAdapter<OrderDetailsEntity>(this, R.layout.activity_property_infomation_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, OrderDetailsEntity item, int position) {
                holder.setTextNotHide(R.id.tv_content, item.getAdvertising() != null ? item.getAdvertising().getTitle() : (TextUtils.isEmpty(item.getInformationAdContent()) ? "" : item.getInformationAdContent()));
                holder.setTextNotHide(R.id.tv_time, String.format("发布时间：%s", TimeUtil.getYmdCh(item.getCreateTime())));
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, OrderDetailsEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(CommonUtil.KEY_VALUE_1, item);
            startActivity(PropertyInfomationDetailsActivity.class, bundle);
        }
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().propertyInfomationList(page)
                .compose(this.<PropertyInfomationInfo>bindLifeCycle())
                .subscribe(new CustomApiCallback<PropertyInfomationInfo>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PropertyInfomationInfo propertyInfomationInfo) {
                        PageListEntity<OrderDetailsEntity> infomationPage = propertyInfomationInfo.getInfomationPage();
                        if (infomationPage == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                            Integer freeNumber = propertyInfomationInfo.getFreeNumber();
                            if (freeNumber == null) {
                                throw new ResultException(9001, "返回参数异常");
                            }
                            tvFree.setText(String.format("当前剩余免费条数：%s", freeNumber));
                            tvFree.setVisibility(View.VISIBLE);
                            preVRight.setVisibility(freeNumber > 0 ? View.VISIBLE : View.GONE);
                        }
                        if (infomationPage.getContent() != null) {
                            mItems.addAll(infomationPage.getContent());
                        }
                        loadingComplete(true, infomationPage.getTotalPages());
                    }
                });
    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
        startActivityForResult(PropertyPublishInfomationActivity.class, CommonUtil.REQ_CODE_1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode) {
            autoRefresh();
        }
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }
}

package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gerry.scaledelete.DeletedImageScanDialog;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.ImageScan;
import com.kingyon.elevator.entities.InvoiceEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class MyInvoiceActivity extends BaseStateRefreshingLoadingActivity<InvoiceEntity> {
    @BindView(R.id.pre_v_right)
    TextView preVRight;

    @Override
    protected String getTitleText() {
        return "开票记录";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_invoice;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setText("开具发票");
    }

    @Override
    protected MultiItemTypeAdapter<InvoiceEntity> getAdapter() {
        return new BaseAdapter<InvoiceEntity>(this, R.layout.activity_my_invoice_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, InvoiceEntity item, int position) {
                holder.setTextNotHide(R.id.tv_time, TimeUtil.getAllTimeNoSecond(item.getTime()));
                holder.setTextNotHide(R.id.tv_type, FormatUtils.getInstance().getInvoiceType(item.getInvoiceType()));
                holder.setTextNotHide(R.id.tv_content, item.getContent());
                holder.setTextNotHide(R.id.tv_sum, CommonUtil.getTwoFloat(item.getInvoiceAmount()));
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, InvoiceEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            if (TextUtils.isEmpty(item.getInvoiceImg())) {
                showToast("暂无电子发票图片");
            } else {
                DeletedImageScanDialog deletedImageScanDialog = new DeletedImageScanDialog(this, new ImageScan(item.getInvoiceImg()), null);
                deletedImageScanDialog.showOne();
            }
        }
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().invoiceList(page)
                .compose(this.<PageListEntity<InvoiceEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<InvoiceEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<InvoiceEntity> invoiceEntityPageListEntity) {
                        if (invoiceEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (invoiceEntityPageListEntity.getContent() != null) {
                            mItems.addAll(invoiceEntityPageListEntity.getContent());
                        }
                        loadingComplete(true, invoiceEntityPageListEntity.getTotalPages());
                    }
                });
    }

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.background)
                .size(ScreenUtil.dp2px(10))
                .build());
    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
        startActivityForResult(InvoiceApplyActivity.class, CommonUtil.REQ_CODE_1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode) {
            autoRefresh();
        }
    }
}

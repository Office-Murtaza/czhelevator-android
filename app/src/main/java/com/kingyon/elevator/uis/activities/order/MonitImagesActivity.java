package com.kingyon.elevator.uis.activities.order;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gerry.scaledelete.DeletedImageScanDialog;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.AdDetectingEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.ScreenUtil;

import java.util.List;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class MonitImagesActivity extends BaseStateRefreshingLoadingActivity<AdDetectingEntity> {

    private String orderId;
    private long deviceId;

    @Override
    protected String getTitleText() {
        orderId = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        deviceId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_2,0);
        return "监播表";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_monit_images;
    }

    @Override
    protected MultiItemTypeAdapter<AdDetectingEntity> getAdapter() {
        return new BaseAdapter<AdDetectingEntity>(this, R.layout.activity_monit_images_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, AdDetectingEntity item, int position) {
                holder.setImage(R.id.img_cover, item.getImgPath());
                holder.setTextNotHide(R.id.tv_name, item.getName());
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, AdDetectingEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null && mItems.size() > 0) {
            DeletedImageScanDialog dialog = new DeletedImageScanDialog(this, mItems, position, null);
            dialog.show(position, false, false);
        }
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().adPlayList(orderId, deviceId, page)
                .compose(this.<PageListEntity<AdDetectingEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<AdDetectingEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<AdDetectingEntity> adDetectingEntityPageListEntity) {
                        if (adDetectingEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        List<AdDetectingEntity> datas = adDetectingEntityPageListEntity.getContent();
                        if (datas != null) {
                            mItems.addAll(datas);
                        }
                        loadingComplete(true, adDetectingEntityPageListEntity.getTotalPages());
                        if (tvTitle != null) {
                            tvTitle.setText(String.format("监播表(%s)", adDetectingEntityPageListEntity.getTotalElements()));
                        }
                    }
                });
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position >= 0 && position < mItems.size()) ? 1 : 3;
            }
        });
        return gridLayoutManager;
    }

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtil.dp2px(14), true));
    }
}

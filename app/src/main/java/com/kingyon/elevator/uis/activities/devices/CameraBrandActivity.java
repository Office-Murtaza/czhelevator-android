package com.kingyon.elevator.uis.activities.devices;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CameraBrandEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by GongLi on 2019/2/20.
 * Email：lc824767150@163.com
 */

public class CameraBrandActivity extends BaseStateRefreshingLoadingActivity<CameraBrandEntity> {
    @Override
    protected String getTitleText() {
        return "摄像头品牌";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_camera_brand;
    }

    @Override
    protected MultiItemTypeAdapter<CameraBrandEntity> getAdapter() {
        return new BaseAdapter<CameraBrandEntity>(this, R.layout.activity_camera_brand_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, CameraBrandEntity item, int position) {
                holder.setTextNotHide(R.id.tv_name, item.getName());
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, CameraBrandEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            Intent intent = new Intent();
            intent.putExtra(CommonUtil.KEY_VALUE_1, item);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void loadData(int page) {
        NetService.getInstance().cameraBrandInfo()
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<List<CameraBrandEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 1);
                    }

                    @Override
                    public void onNext(List<CameraBrandEntity> cameraBrandEntities) {
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (cameraBrandEntities != null) {
                            mItems.addAll(cameraBrandEntities);
                        }
                        loadingComplete(true, 1);
                    }
                });
    }

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .drawable(R.drawable.white_margin_sixteen_divider)
                .sizeResId(R.dimen.spacing_divider)
                .build());
    }
}

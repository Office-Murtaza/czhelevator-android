package com.kingyon.elevator.uis.activities.homepage;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.NormalOptionEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import java.util.List;

/**
 * Created by GongLi on 2018/12/26.
 * Email：lc824767150@163.com
 */

public class WikiListActivity extends BaseStateRefreshingLoadingActivity<NormalOptionEntity> {
    @Override
    protected String getTitleText() {
        return "投放百科";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_wiki_list;
    }

    @Override
    protected MultiItemTypeAdapter<NormalOptionEntity> getAdapter() {
        return new BaseAdapter<NormalOptionEntity>(this, R.layout.activity_wiki_list_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, NormalOptionEntity item, int position) {
                holder.setTextNotHide(R.id.tv_name, item.getTitle());
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, NormalOptionEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonUtil.KEY_VALUE_1, item.getObjectId());
            bundle.putString(CommonUtil.KEY_VALUE_2, item.getTitle());
            startActivity(WikiDetailsActivity.class, bundle);
        }
    }

    @Override
    protected void loadData(int page) {
        NetService.getInstance().liftknowledges()
                .compose(this.<List<NormalOptionEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<NormalOptionEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 1);
                    }

                    @Override
                    public void onNext(List<NormalOptionEntity> normalOptionEntities) {
                        mItems.clear();
                        if (normalOptionEntities != null) {
                            mItems.addAll(normalOptionEntities);
                        }
                        loadingComplete(true, 1);
                    }
                });
    }
}

package com.kingyon.elevator.uis.activities.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.NormalElemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.ScreenUtil;

import java.util.List;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class TemplateListClassifyActivity extends BaseStateRefreshingLoadingActivity<NormalElemEntity> {

    private NormalElemEntity choosed;
    private NormalElemEntity choosedCahe;
    private boolean dataInit;

    @Override
    protected String getTitleText() {
        choosed = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        choosedCahe = choosed;
        return "选择分类";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_template_list_classify;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, ScreenUtil.dp2px(16), true));
    }

    @Override
    protected MultiItemTypeAdapter<NormalElemEntity> getAdapter() {
        return new BaseAdapter<NormalElemEntity>(this, R.layout.activity_template_list_classify_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, NormalElemEntity item, int position) {
                holder.setTextNotHide(R.id.tv_name, item.getName());
                holder.setSelected(R.id.fl_classify, item.isChoosed());
                holder.setSelected(R.id.img_selected, item.isChoosed());
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, NormalElemEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            for (NormalElemEntity entity : mItems) {
                if (entity == item) {
                    entity.setChoosed(!entity.isChoosed());
                } else {
                    entity.setChoosed(false);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private NormalElemEntity getChoosedItem() {
        NormalElemEntity result = null;
        for (NormalElemEntity entity : mItems) {
            if (entity.isChoosed()) {
                result = entity;
                break;
            }
        }
        return result;
    }

    @Override
    protected void loadData(final int page) {
        if (choosedCahe == null) {
            choosedCahe = getChoosedItem();
        }
        NetService.getInstance().getAdTempletType()
                .compose(this.<List<NormalElemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<NormalElemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 1);
                    }

                    @Override
                    public void onNext(List<NormalElemEntity> templates) {
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (templates != null) {
                            if (choosedCahe != null) {
                                for (NormalElemEntity entity : templates) {
                                    entity.setChoosed(entity.getObjectId() == choosed.getObjectId());
                                }
                                choosedCahe = null;
                                dataInit = true;
                            }
                            mItems.addAll(templates);
                        }
                        loadingComplete(true, 1);
                    }
                });
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(this, 2);
    }

    @Override
    public void onBackPressed() {
        NormalElemEntity choosedItem = getChoosedItem();
        if (choosedItem == null && !dataInit) {
            choosedItem = choosedCahe;
        }
        Intent intent = new Intent();
        if (choosedItem != null) {
            intent.putExtra(CommonUtil.KEY_VALUE_1, choosedItem);
        }
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }
}

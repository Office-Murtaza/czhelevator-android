package com.kingyon.elevator.uis.activities.homepage;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.kingyon.elevator.utils.JumpUtils;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.ScreenUtil;

/**
 * Created by GongLi on 2018/12/26.
 * Email：lc824767150@163.com
 */

public class RecommendListActivity extends BaseStateRefreshingLoadingActivity<ADEntity> {
    @Override
    protected String getTitleText() {
        return "精品案例";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_recommend_list;
    }

    @Override
    protected MultiItemTypeAdapter<ADEntity> getAdapter() {
        return new BaseAdapter<ADEntity>(this, R.layout.activity_recommend_list_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, ADEntity item, int position) {
                FormatUtils.getInstance().updateAdCoverShow(mContext, item
                        , holder.getView(R.id.fl_full_video), (ImageView) holder.getView(R.id.img_full_video)
                        , (ImageView) holder.getView(R.id.img_full_image)
                        , holder.getView(R.id.ll_incise), (ImageView) holder.getView(R.id.img_top), (ImageView) holder.getView(R.id.img_bottom));
                holder.setTextNotHide(R.id.tv_name, item.getTitle());
                holder.setText(R.id.tv_industry, item.getAdIndustry());
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, ADEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            JumpUtils.getInstance().jumpToAdPreview(this, item,"ad");
        }
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().recommendAds(page)
                .compose(this.<PageListEntity<ADEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<ADEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<ADEntity> adEntityPageListEntity) {
                        if (adEntityPageListEntity == null || adEntityPageListEntity.getContent() == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(adEntityPageListEntity.getContent());
                        loadingComplete(true, adEntityPageListEntity.getTotalPages());
                    }
                });
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, ScreenUtil.dp2px(10), true));
        return new GridLayoutManager(this, 2);
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }
}

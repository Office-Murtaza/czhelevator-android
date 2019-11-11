package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gerry.scaledelete.DeletedImageScanDialog;
import com.gerry.scaledelete.ScanleImageUrl;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.FeedBackEntity;
import com.kingyon.elevator.entities.ImageScan;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.ImageShowAdapter;
import com.kingyon.elevator.uis.widgets.BlankRecyclerView;
import com.kingyon.elevator.uis.widgets.FullyGridLayoutManager;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DealScrollRecyclerView;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class FeedBackActivity extends BaseStateRefreshingLoadingActivity<FeedBackEntity> {
    @Override
    protected String getTitleText() {
        return "意见反馈";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected MultiItemTypeAdapter<FeedBackEntity> getAdapter() {
        return new BaseAdapter<FeedBackEntity>(this, R.layout.activity_feed_back_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, FeedBackEntity item, int position) {
                holder.setTextNotHide(R.id.tv_name, item.getTitile());
                BlankRecyclerView recyclerView = holder.getView(R.id.rv_images);
                ImageShowAdapter imageAdapter = (ImageShowAdapter) recyclerView.getAdapter();
                if (imageAdapter == null) {
                    imageAdapter = new ImageShowAdapter(mContext);
                    imageAdapter.setShowSize(true);
                    DealScrollRecyclerView.getInstance().dealAdapter(imageAdapter, recyclerView, new FullyGridLayoutManager(mContext, 3));
                    imageAdapter.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<Object>() {
                        @Override
                        public void onItemClick(View view, int position, Object object, BaseAdapterWithHF<Object> baseAdaper) {
                            showImageScan(position, baseAdaper);
                        }
                    });
                }
                imageAdapter.refreshDatas(item.getImages());
                holder.setTextNotHide(R.id.tv_time, TimeUtil.getRecentlyTime(item.getTime()));
            }

            @Override
            public CommonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final CommonHolder holder = super.onCreateViewHolder(parent, viewType);
                BlankRecyclerView recyclerView = holder.getView(R.id.rv_images);
                recyclerView.setBlankListener(new BlankRecyclerView.BlankListener() {
                    @Override
                    public void onBlankClick(BlankRecyclerView recyclerView) {
                        if (mOnItemClickListener != null) {
                            int position = holder.getAdapterPosition();
                            mOnItemClickListener.onItemClick(recyclerView, holder, (position >= 0 && position < mItems.size()) ? mItems.get(position) : null, position);
                        }
                    }
                });
                return holder;
            }

            private void showImageScan(int position, BaseAdapterWithHF<Object> baseAdaper) {
                if (baseAdaper.getItemRealCount() > 0) {
                    List<ScanleImageUrl> urls = new ArrayList<>();
                    List<Object> datas = baseAdaper.getDatas();
                    for (Object obj : datas) {
                        urls.add(new ImageScan(obj.toString()));
                    }
                    if (urls.size() > 0 && urls.size() == datas.size()) {
                        DeletedImageScanDialog dialog = new DeletedImageScanDialog(mContext, urls, position, null);
                        dialog.show(position, false, false);
                    }
                }
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, FeedBackEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonUtil.KEY_VALUE_1, item.getObjectId());
            startActivity(FeedBackDetailsActivity.class, bundle);
        }
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().myFeedBackList(page)
                .compose(this.<PageListEntity<FeedBackEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<FeedBackEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 10000);
                    }

                    @Override
                    public void onNext(PageListEntity<FeedBackEntity> feedBackEntityPageListEntity) {
                        if (feedBackEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (feedBackEntityPageListEntity.getContent() != null) {
                            mItems.addAll(feedBackEntityPageListEntity.getContent());
                        }
                        loadingComplete(true, feedBackEntityPageListEntity.getTotalPages());
                    }
                });
    }

    @OnClick(R.id.tv_create)
    public void onViewClicked() {
        startActivityForResult(FeedBackEditActivity.class, CommonUtil.REQ_CODE_1);
    }

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.background)
                .size(ScreenUtil.dp2px(10))
                .build());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode) {
            autoRefresh();
        }
    }
}

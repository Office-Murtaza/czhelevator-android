package com.kingyon.elevator.uis.activities.user;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.homepage.CellDetailsActivity;
import com.kingyon.elevator.uis.widgets.GroupStickyDecoration;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.leo.afbaselibrary.listeners.OnClickWithObjects;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Calendar;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class MyCollectActivity extends BaseStateRefreshingLoadingActivity<CellItemEntity> implements GroupStickyDecoration.GroupStickyListener {
    private String today;
    private String yesterDay;
    private boolean notFirstIn;

    @Override
    protected String getTitleText() {
        return "我的收藏";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mRecyclerView.addItemDecoration(new GroupStickyDecoration(this, this));
    }

    @Override
    protected MultiItemTypeAdapter<CellItemEntity> getAdapter() {
        return new BaseAdapter<CellItemEntity>(this, R.layout.activity_my_collect_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, CellItemEntity item, int position) {
                holder.setImage(R.id.img_cover, item.getCellLogo());
                holder.setTextNotHide(R.id.tv_name, item.getCellName());
                holder.setTextNotHide(R.id.tv_lift, FormatUtils.getInstance().getCellLift(item.getLiftNum()));
                holder.setTextNotHide(R.id.tv_unit, FormatUtils.getInstance().getCellUnit(item.getUnitNum()));
                holder.setTextNotHide(R.id.tv_price, FormatUtils.getInstance().getCellPrice(item.getBusinessAdPrice()));
                holder.setTextNotHide(R.id.tv_address, item.getAddress());
                holder.setTextNotHide(R.id.tv_distance, FormatUtils.getInstance().getCellDistance(item.getLongitude(), item.getLatitude(), item.getDistance()));
                holder.setOnClickListener(R.id.tv_cancel, new OnClickWithObjects(item) {
                    @Override
                    public void onClick(View view, Object[] objects) {
                        cancelCollect(((CellItemEntity) objects[0]));
                    }
                });
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, CellItemEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonUtil.KEY_VALUE_1, item.getObjctId());
            startActivity(CellDetailsActivity.class, bundle);
        }
    }

    @Override
    protected void onResume() {
        if (notFirstIn) {
            onfresh();
        } else {
            notFirstIn = true;
        }
        super.onResume();
    }

    private void cancelCollect(final CellItemEntity entity) {
        NetService.getInstance().cancelCollect(String.valueOf(entity.getObjctId()))
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("取消成功");
                        int indexOf = mItems.indexOf(entity);
                        if (indexOf >= 0 && mItems.size() > indexOf) {
                            mItems.remove(entity);
                        }
                        mAdapter.notifyItemRemoved(indexOf);
                    }
                });
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().myCollects(page)
                .compose(this.<PageListEntity<CellItemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<CellItemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<CellItemEntity> cellItemEntityPageListEntity) {
                        if (cellItemEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (cellItemEntityPageListEntity.getContent() != null) {
                            mItems.addAll(cellItemEntityPageListEntity.getContent());
                        }
                        loadingComplete(true, cellItemEntityPageListEntity.getTotalPages());
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

    @Override
    public String getGroupName(int position) {
        String result = null;
        if (position >= 0 && position < mItems.size()) {
            CellItemEntity item = mItems.get(position);
            result = getTimeStr(item.getCollectTime());
        }
        return result;
    }

    private String getTimeStr(long time) {
        String result;
        if (isToday(time)) {
            result = "今天";
        } else if (isYesterday(time)) {
            result = "昨天";
        } else {
            result = TimeUtil.getYMdTime(time);
        }
        return result;
    }

    private boolean isToday(long time) {
        initTime();
        return TextUtils.equals(TimeUtil.getYMdTime(time), today);
    }

    private boolean isYesterday(long time) {
        initTime();
        return TextUtils.equals(TimeUtil.getYMdTime(time), yesterDay);
    }

    private void initTime() {
        if (today == null || yesterDay == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            today = TimeUtil.getYMdTime(calendar.getTimeInMillis());
            calendar.add(Calendar.DATE, -1);
            yesterDay = TimeUtil.getYMdTime(calendar.getTimeInMillis());
        }
    }
}

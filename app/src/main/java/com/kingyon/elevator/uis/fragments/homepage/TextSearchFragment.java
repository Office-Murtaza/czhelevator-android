package com.kingyon.elevator.uis.fragments.homepage;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.others.AddCellToPlanPresenter;
import com.kingyon.elevator.others.OnParamsChangeInterface;
import com.kingyon.elevator.uis.activities.homepage.CellDetailsActivity;
import com.kingyon.elevator.uis.activities.homepage.SearchActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.leo.afbaselibrary.listeners.OnClickWithObjects;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshLoadingFragment;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class TextSearchFragment extends BaseStateRefreshLoadingFragment<CellItemEntity> implements OnParamsChangeInterface {
    private String keyWord;
    private AMapCityEntity cityEntity;
    private String distance;
    private String areaIds;
    private String cellType;
    private AddCellToPlanPresenter addCellToPlanPresenter;
    private int[] clickPosition = new int[2];

    public static TextSearchFragment newInstance(String keyWord, AMapCityEntity cityEntity, String distance, String areaIds, String cellType) {
        Bundle args = new Bundle();
        args.putString(CommonUtil.KEY_VALUE_1, keyWord);
        args.putParcelable(CommonUtil.KEY_VALUE_2, cityEntity);
        if (distance != null) {
            args.putString(CommonUtil.KEY_VALUE_3, distance);
        }
        if (areaIds != null) {
            args.putString(CommonUtil.KEY_VALUE_4, areaIds);
        }
        if (cellType != null) {
            args.putString(CommonUtil.KEY_VALUE_5, cellType);
        }
        TextSearchFragment fragment = new TextSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_text_search;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            keyWord = arguments.getString(CommonUtil.KEY_VALUE_1);
            cityEntity = arguments.getParcelable(CommonUtil.KEY_VALUE_2);
            distance = arguments.getString(CommonUtil.KEY_VALUE_3);
            areaIds = arguments.getString(CommonUtil.KEY_VALUE_4);
            cellType = arguments.getString(CommonUtil.KEY_VALUE_5);
        }
        super.init(savedInstanceState);
        addCellToPlanPresenter = new AddCellToPlanPresenter((BaseActivity) getActivity());
    }

    @Override
    protected MultiItemTypeAdapter<CellItemEntity> getAdapter() {
        return new BaseAdapter<CellItemEntity>(getContext(), R.layout.fragment_homepage_cell, mItems) {
            @Override
            protected void convert(CommonHolder holder, CellItemEntity item, int position) {
                holder.setSelected(R.id.item_root, position % 2 == 1);
                holder.setImage(R.id.img_cover, item.getCellLogo());
                holder.setTextNotHide(R.id.tv_name, item.getCellName());
                holder.setTextNotHide(R.id.tv_lift, FormatUtils.getInstance().getCellLift(item.getLiftNum()));
                holder.setTextNotHide(R.id.tv_unit, FormatUtils.getInstance().getCellUnit(item.getUnitNum()));
                holder.setTextNotHide(R.id.tv_price, FormatUtils.getInstance().getCellPrice(item.getBusinessAdPrice()));
                holder.setTextNotHide(R.id.tv_address, item.getAddress());
                holder.setTextNotHide(R.id.tv_distance, FormatUtils.getInstance().getCellDistance(item.getLongitude(), item.getLatitude(), item.getDistance()));
                holder.setOnClickListener(R.id.img_plan, new OnClickWithObjects(item) {
                    @Override
                    public void onClick(View view, Object[] objects) {
                        if (addCellToPlanPresenter != null) {
                            view.getLocationOnScreen(clickPosition);
                            RuntimeUtils.mapOrListAddPositionAnimation = clickPosition;
                            RuntimeUtils.animationImagePath = ((CellItemEntity) objects[0]).getCellLogo();
                            addCellToPlanPresenter.showPlanPicker(((CellItemEntity) objects[0]).getObjctId(),false);
                        }
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
    protected void loadData(final int page) {
        NetService.getInstance().searchCell(keyWord != null ? keyWord : "", cityEntity
                , distance != null ? distance : "", areaIds != null ? areaIds : ""
                , cellType != null ? cellType : "", page
                , AppContent.getInstance().getLocation())
                .compose(this.<PageListEntity<CellItemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<CellItemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                        FragmentActivity activity = getActivity();
                        if (activity instanceof SearchActivity) {
                            ((SearchActivity) activity).onSearchResult(null);
                        }
                    }

                    @Override
                    public void onNext(PageListEntity<CellItemEntity> cellItemEntityPageListEntity) {


                        if (cellItemEntityPageListEntity == null || cellItemEntityPageListEntity.getContent() == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(cellItemEntityPageListEntity.getContent());
                        loadingComplete(true, cellItemEntityPageListEntity.getTotalPages());
                        FragmentActivity activity = getActivity();
                        if (activity instanceof SearchActivity) {
                            ((SearchActivity) activity).onSearchResult(mItems);
                        }
                    }
                });
    }

    @Override
    public void onParamsChange(Object... objects) {
        keyWord = (String) objects[0];
        cityEntity = (AMapCityEntity) objects[1];
        distance = (String) objects[2];
        areaIds = (String) objects[3];
        cellType = (String) objects[4];
        autoRefresh();
    }

    @Override
    protected String getEmptyTip() {
        return "未匹配到任何数据";
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }

    @Override
    public void onDestroy() {
        if (addCellToPlanPresenter != null) {
            addCellToPlanPresenter.onDestroy();
            addCellToPlanPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }


}

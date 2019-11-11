package com.kingyon.elevator.uis.pops;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.uis.adapters.SearchAreaAdapter;
import com.kingyon.elevator.uis.adapters.SearchCellTypeAdapter;
import com.kingyon.elevator.uis.adapters.SearchDistanceAdapter;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2018/12/28.
 * Email：lc824767150@163.com
 */

public class SearchAreaWindow extends PopupWindow implements PopupWindow.OnDismissListener {


    @BindView(R.id.tv_instance)
    TextView tvInstance;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.rv_distance)
    RecyclerView rvDistance;
    @BindView(R.id.rv_area)
    RecyclerView rvArea;

    private OnAreaResultListener onAreaResultListener;
    private SearchDistanceAdapter distanceAdapter;
    private SearchCellTypeAdapter areaAdapter;

    public SearchAreaWindow(Context context, OnAreaResultListener onAreaResultListener) {
        super(context);
        this.onAreaResultListener = onAreaResultListener;
        View view = LayoutInflater.from(context).inflate(R.layout.activity_search_search_area, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setOnDismissListener(this);

        rvDistance.setLayoutManager(new LinearLayoutManager(context));
        distanceAdapter = new SearchDistanceAdapter(context, R.layout.activity_search_city_area_item);
        rvDistance.setAdapter(distanceAdapter);
        List<NormalParamEntity> entities = new ArrayList<>();

        entities.add(new NormalParamEntity("", "不限"));
        entities.add(new NormalParamEntity("1000", "1KM"));
        entities.add(new NormalParamEntity("2000", "2KM"));
        entities.add(new NormalParamEntity("5000", "5KM"));
        distanceAdapter.refreshDatas(entities);
        distanceAdapter.clearSelected();

        rvArea.setLayoutManager(new LinearLayoutManager(context));
        areaAdapter = new SearchAreaAdapter(context, R.layout.activity_search_city_area_item);
        rvArea.setAdapter(areaAdapter);

        onTypeClicked(tvInstance);
    }

    @OnClick({R.id.tv_instance, R.id.tv_area})
    public void onTypeClicked(View view) {
        boolean showDistance = view.getId() == R.id.tv_instance;
        boolean showArea = view.getId() == R.id.tv_area;
        tvInstance.setSelected(showDistance);
        tvArea.setSelected(showArea);
        rvDistance.setVisibility(showDistance ? View.VISIBLE : View.GONE);
        rvArea.setVisibility(showArea ? View.VISIBLE : View.GONE);
        if (rvDistance.getVisibility() == View.GONE) {
            distanceAdapter.clearSelected();
        }
        if (rvArea.getVisibility() == View.GONE) {
            areaAdapter.clearSelected();
        }
    }

    @OnClick({R.id.tv_reset, R.id.tv_ensure})
    public void onResultClicked(View view) {
        if (view.getId() == R.id.tv_reset) {
            distanceAdapter.clearSelected();
            areaAdapter.clearSelected();
        }
        dismiss();
    }

    public void show(BaseActivity baseActivity, AMapCityEntity cityEntity, String distance, List<NormalParamEntity> areaDatas, View view) {
        showAsDropDown(view);
        areaAdapter.refreshDatas(areaDatas);
        areaAdapter.reset();
        distanceAdapter.chooseDiatance(distance);
    }

    public void clearAreas() {
        if (areaAdapter != null) {
            areaAdapter.clearDatas();
            onTypeClicked(tvInstance);
        }
    }

    private void updateAreaData(final BaseActivity baseActivity, AMapCityEntity cityEntity) {

    }

    @Override
    public void onDismiss() {
        if (onAreaResultListener != null) {
            String[] distanceParams = distanceAdapter.getChoosedParams();
            String[] areaParams = areaAdapter.getChoosedParams();
            onAreaResultListener.onAreaResult(distanceParams[0], distanceParams[1], areaParams[0], areaParams[1]);
        }
    }


    public interface OnAreaResultListener {
        void onAreaResult(String distanceNames, String distanceTypes, String areaNames, String areaCodes);
    }
}

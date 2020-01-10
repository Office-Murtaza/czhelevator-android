package com.kingyon.elevator.uis.fragments.homepage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.blankj.utilcode.util.ScreenUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.CityCellEntity;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.others.AddCellToPlanPresenter;
import com.kingyon.elevator.others.OnParamsChangeInterface;
import com.kingyon.elevator.uis.activities.homepage.CellDetailsActivity;
import com.kingyon.elevator.uis.adapters.SearchCellsAdapter;
import com.kingyon.elevator.uis.widgets.ProportionFrameLayout;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.orhanobut.logger.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class MapSearchFragment extends BaseFragment implements OnParamsChangeInterface, SearchCellsAdapter.OnPagerClickListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.vp_cells)
    ViewPager vpCells;
    @BindView(R.id.pfl_cells)
    ProportionFrameLayout pflCells;

    private SearchCellsAdapter cellsAdapter;

    private final int cityZoomLevel = 10;
    private LinkedHashMap<Long, Marker> markersMap = new LinkedHashMap<>();
    private LinkedHashMap<Long, Marker> cityMarkersMap = new LinkedHashMap<>();
    private boolean showCityMarkers;
    //初始化地图控制器对象
    private AMap aMap;
    //定义一个UiSettings对象
    private UiSettings mUiSettings;

    private AddCellToPlanPresenter addCellToPlanPresenter;
    private int[] clickPosition = new int[2];

    public static MapSearchFragment newInstance(AMapCityEntity entity) {
        Bundle args = new Bundle();
        MapSearchFragment fragment = new MapSearchFragment();
        if (entity != null) {
            args.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_map_search;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        addCellToPlanPresenter = new AddCellToPlanPresenter((BaseActivity) getActivity());
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
            mUiSettings.setZoomControlsEnabled(false);
            AMapCityEntity cityEntity = getArguments() != null ? (AMapCityEntity) getArguments().getParcelable(CommonUtil.KEY_VALUE_1) : null;
            if (cityEntity != null && !TextUtils.isEmpty(cityEntity.getCenter())) {
                double[] centerLatLon = FormatUtils.getInstance().getCenterLatLon(cityEntity.getCenter());
                if (centerLatLon[0] != 0 && centerLatLon[1] != 0) {
                    moveMapToPositon(centerLatLon[0], centerLatLon[1], cityZoomLevel - 0.1f);
                }
            } else {
                LocationEntity location = AppContent.getInstance().getLocation();
                if (location != null && location.getLongitude() != null && location.getLatitude() != null) {
                    moveMapToPositon(location.getLongitude(), location.getLatitude(), cityZoomLevel - 0.1f);
                }
            }
            aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    float curZoomLevel = cameraPosition.zoom;
                    Logger.i("zoomlevel:" + curZoomLevel);
                    if (curZoomLevel < cityZoomLevel && !showCityMarkers) {
                        showCityMarkers = true;
                        updateMarkersShow();
                    } else if (curZoomLevel >= cityZoomLevel && showCityMarkers) {
                        showCityMarkers = false;
                        updateMarkersShow();
                    }
                }
            });
            aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (pflCells.getVisibility() == View.VISIBLE) {
                        pflCells.setVisibility(View.GONE);
                    }
                }
            });
            aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    long clickCellId = 0;
                    Set<Map.Entry<Long, Marker>> entries = markersMap.entrySet();
                    for (Map.Entry<Long, Marker> entry : entries) {
                        if (TextUtils.equals(String.valueOf(entry.getKey()), marker.getTitle())) {
                            clickCellId = entry.getKey();
                            break;
                        }
                    }
                    if (clickCellId != 0) {
                        showClickId(clickCellId);
                    }
                    return true;
                }
            });
        }
        getCityCellNums();
    }

    public void showClickId(long clickCellId) {
        pflCells.setVisibility(View.VISIBLE);
        Integer index = cellsAdapter.getIdIndex(clickCellId);
        if (index != null) {
            vpCells.setCurrentItem(index, false);
        }
    }

    private void getCityCellNums() {
        NetService.getInstance().cityCellNums()
                .compose(this.<List<CityCellEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<CityCellEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast("获取小区数量失败");
                    }

                    @Override
                    public void onNext(List<CityCellEntity> cityCellEntities) {
                        if (cityCellEntities == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
//                        if (App.getInstance().isDebug()) {
//                            cityCellEntities = new Gson().fromJson("[{\"cityName\":\"成都市\",\"longitude\":104.065735,\"latitude\":30.659462,\"cellCount\":5000,\"cityCode\":510100},{\"cityName\":\"重庆市\",\"longitude\":106.504962,\"latitude\":29.533155,\"cellCount\":4000,\"cityCode\":500100}]"
//                                    , new TypeToken<List<CityCellEntity>>() {
//                                    }.getType());
//                        }
                        showCellNumsMarkers(cityCellEntities);
                    }
                });
    }

    @Override
    public void onParamsChange(Object... objects) {
        List<CellItemEntity> cells = (List<CellItemEntity>) objects[0];
        AMapCityEntity cityEntity = (AMapCityEntity) objects[1];
        if (cityEntity != null && !TextUtils.isEmpty(cityEntity.getCenter())) {
            double[] centerLatLon = FormatUtils.getInstance().getCenterLatLon(cityEntity.getCenter());
            if (centerLatLon[0] != 0 && centerLatLon[1] != 0) {
                moveMapToPositon(centerLatLon[0], centerLatLon[1], cityZoomLevel - 0.1f);
            }
        } else {
            LocationEntity location = AppContent.getInstance().getLocation();
            if (location != null && location.getLongitude() != null && location.getLatitude() != null) {
                moveMapToPositon(location.getLongitude(), location.getLatitude(), cityZoomLevel - 0.1f);
            }
        }
        if (aMap != null) {
            //移除多余元素
            Iterator<Map.Entry<Long, Marker>> iterator = markersMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, Marker> entry = iterator.next();
                if (!containMarker(entry.getKey(), cells)) {
                    Marker maker = entry.getValue();
                    if (!maker.isRemoved()) {
                        maker.remove();
                    }
                    maker.destroy();
                    iterator.remove();
                    maker = null;
                }
            }
            //添加marker
            Set<Long> cellIds = markersMap.keySet();
            for (CellItemEntity item : cells) {
                if (!cellIds.contains(item.getObjctId())) {
                    MarkerOptions markerOptions = createMarkerOptions(item);
                    Marker marker = aMap.addMarker(markerOptions);
                    markersMap.put(item.getObjctId(), marker);
                }
            }
            moveToCurCellsRange(cells);
            updateIsShowCity();
            updateMarkersShow();
        }
        notifyCellsPager(cells);
    }

    private void notifyCellsPager(List<CellItemEntity> entities) {
        if (cellsAdapter == null) {
            cellsAdapter = new SearchCellsAdapter(getContext(), entities);
            cellsAdapter.setOnPagerClickListener(this);
            vpCells.setAdapter(cellsAdapter);
            vpCells.addOnPageChangeListener(this);
        } else {
            cellsAdapter.setBannerEntities(entities);
            cellsAdapter.notifyDataSetChanged();
            if (vpCells.getAdapter() != null) {
                vpCells.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBannerClickListener(View view, int position, CellItemEntity item, List<CellItemEntity> datas) {
        if (view.getId() == R.id.tv_choose) {
            if (addCellToPlanPresenter != null) {
                clickPosition[0] = ScreenUtils.getScreenWidth() - DensityUtil.dip2px(16);
                clickPosition[1] = ScreenUtils.getScreenHeight()- DensityUtil.dip2px(16);
                RuntimeUtils.mapOrListAddPositionAnimation=clickPosition;
                RuntimeUtils.animationImagePath = item.getCellLogo();
                addCellToPlanPresenter.showPlanPicker(item.getObjctId(),false);
            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonUtil.KEY_VALUE_1, item.getObjctId());
            startActivity(CellDetailsActivity.class, bundle);
        }
    }

    private void moveToCurCellsRange(List<CellItemEntity> cells) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (CellItemEntity item : cells) {
            builder.include(new LatLng(item.getLatitude(), item.getLongitude()));
        }
//        LatLngBounds.Builder builder = LatLngBounds.builder();
//        builder.include(new LatLng(cells.get(0).getLatitude(), cells.get(0).getLongitude()));
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), ScreenUtil.dp2px(24)));
//        mapView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (aMap.getCameraPosition().zoom > aMap.getMaxZoomLevel() - 2.5f) {
//                    aMap.moveCamera(CameraUpdateFactory.zoomTo(aMap.getMaxZoomLevel() - 2.5f));
//                }
//            }
//        }, 40);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBoundsRect(builder.build()
                , ScreenUtil.dp2px(48), ScreenUtil.dp2px(48), ScreenUtil.dp2px(76), ScreenUtil.dp2px(8));
        aMap.animateCamera(cameraUpdate, 500
                , new AMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        mapView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (aMap.getCameraPosition().zoom > aMap.getMaxZoomLevel() - 2.5f) {
                                    aMap.animateCamera(CameraUpdateFactory.zoomTo(aMap.getMaxZoomLevel() - 2.5f), 100, null);
                                }
                            }
                        }, 60);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    /**
     * 往地图上添加marker
     */
    public MarkerOptions createMarkerOptions(CellItemEntity cell) {
        View view = View.inflate(getContext(), R.layout.view_search_cell_marker, null);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvLift = view.findViewById(R.id.tv_lift);
        tvName.setText(cell.getCellName());
        tvLift.setText(String.format("电梯数%s个", cell.getLiftNum()));
        Bitmap bitmap = convertViewToBitmap(view);
        return new MarkerOptions()
                .title(String.valueOf(cell.getObjctId()))
                .position(new LatLng(cell.getLatitude(), cell.getLongitude()))
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }

    public void moveMapToPositon(double longitude, double latitude, float zoomLevel) {
        if (aMap != null) {
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), zoomLevel, 0, 0));
            aMap.moveCamera(mCameraUpdate);
        }
    }

    //view 转bitmap
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    private boolean containMarker(Long objectId, List<CellItemEntity> cellItemEntities) {
        boolean contain = false;
        for (CellItemEntity item : cellItemEntities) {
            if (objectId.longValue() == item.getObjctId()) {
                contain = true;
                break;
            }
        }
        return contain;
    }

    private void showCellNumsMarkers(List<CityCellEntity> cityCellEntities) {
        Set<Long> cityIds = cityMarkersMap.keySet();
        for (CityCellEntity item : cityCellEntities) {
            if (!cityIds.contains(item.getCityCode())) {
                MarkerOptions markerOptions = createCityMarkerOptions(item);
                Marker marker = aMap.addMarker(markerOptions);
                cityMarkersMap.put(item.getCityCode(), marker);
            }
        }
        updateIsShowCity();
        updateMarkersShow();
    }

    private void updateIsShowCity() {
        float curZoomLevel = aMap.getCameraPosition().zoom;
        showCityMarkers = curZoomLevel < cityZoomLevel;
    }

    private MarkerOptions createCityMarkerOptions(CityCellEntity item) {
        View view = View.inflate(getContext(), R.layout.view_search_city_marker, null);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvCells = view.findViewById(R.id.tv_cells);
        tvName.setText(item.getCityName());
        tvCells.setText(String.format("小区%s个", item.getCellCount()));
        Bitmap bitmap = convertViewToBitmap(view);
        return new MarkerOptions()
                .title(String.valueOf(item.getCityName()))
                .position(new LatLng(item.getLatitude(), item.getLongitude()))
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }

    private void updateMarkersShow() {
        Collection<Marker> citiesMarkers = cityMarkersMap.values();
        for (Marker city : citiesMarkers) {
            city.setVisible(showCityMarkers);
        }

        Collection<Marker> cellesMarkers = markersMap.values();
        for (Marker cell : cellesMarkers) {
            cell.setVisible(!showCityMarkers);
        }
    }

    @Override
    public void onDestroyView() {
        if (addCellToPlanPresenter != null) {
            addCellToPlanPresenter.onDestroy();
            addCellToPlanPresenter = null;
        }
        mapView.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        long selectedId = cellsAdapter.getSelectedId(position);
//        Marker marker = markersMap.get(selectedId);
//
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }
}

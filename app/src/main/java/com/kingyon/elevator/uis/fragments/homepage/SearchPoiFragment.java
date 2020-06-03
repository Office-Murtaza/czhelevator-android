package com.kingyon.elevator.uis.fragments.homepage;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.others.OnParamsChangeInterface;
import com.kingyon.elevator.uis.activities.homepage.SearchHistoryActivity;
import com.kingyon.elevator.uis.adapters.adapterone.SearchPoiAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshLoadingFragment;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class SearchPoiFragment extends BaseStateRefreshLoadingFragment<Object> implements OnParamsChangeInterface {

    private String keyWord;
    private String city;
    private SearchPoiAdapter searchPoiAdapter;

    public static SearchPoiFragment newInstance(String keyWord, String city) {
        Bundle args = new Bundle();
        args.putString(CommonUtil.KEY_VALUE_1, keyWord);
        args.putString(CommonUtil.KEY_VALUE_2, city);
        SearchPoiFragment fragment = new SearchPoiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_search_poi;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            keyWord = getArguments().getString(CommonUtil.KEY_VALUE_1);
            city = getArguments().getString(CommonUtil.KEY_VALUE_2);
        }
        super.init(savedInstanceState);
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        searchPoiAdapter = new SearchPoiAdapter(getContext(), mItems);
        return searchPoiAdapter;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item instanceof PoiItem) {
            FragmentActivity activity = getActivity();
            if (activity instanceof SearchHistoryActivity) {
                ((SearchHistoryActivity) activity).realSearch(((PoiItem) item).getTitle());
            }
        }
    }

    @Override
    protected void loadData(int page) {
        if (TextUtils.isEmpty(keyWord)) {
            mItems.clear();
            loadingComplete(true, 1);
        } else {
            Observable.just(new String[]{keyWord, city})
                    .flatMap(new Func1<String[], Observable<ArrayList<PoiItem>>>() {
                        @Override
                        public Observable<ArrayList<PoiItem>> call(String[] strings) {
                            PoiSearch.Query query = new PoiSearch.Query(strings[0], "", strings[1]);
                            //第一个参数keyWord表示搜索字符串，
                            //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
                            //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
                            query.setPageSize(10);// 设置每页最多返回多少条poiitem
                            query.setPageNum(1);//设置查询页码
                            PoiSearch poiSearch = new PoiSearch(getContext(), query);

                            ArrayList<PoiItem> pois;
                            try {
                                PoiResult poiResult = poiSearch.searchPOI();
                                pois = poiResult.getPois();
                                if (pois == null) {
                                    pois = new ArrayList<>();
                                }
                            } catch (AMapException e) {
                                e.printStackTrace();
                                pois = null;
                            }
                            return Observable.just(pois);
                        }
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.<ArrayList<PoiItem>>bindLifeCycle())
                    .subscribe(new CustomApiCallback<ArrayList<PoiItem>>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                            refreshComplete(false, 1);
                        }

                        @Override
                        public void onNext(ArrayList<PoiItem> poiItems) {
                            if (poiItems == null) {
                                throw new ResultException(9002, "未检索到与此地点相似的结果");
                            }
                            mItems.clear();
                            if (!TextUtils.isEmpty(city)) {
                                mItems.add(String.format("以下是在%s搜索\"%s\"的结果", FormatUtils.getInstance().getCityName(city), keyWord));
                            }
                            mItems.addAll(poiItems);
                            searchPoiAdapter.setKeyWord(keyWord);
                            loadingComplete(true, 1);
                        }
                    });
        }
    }

    @Override
    public void onParamsChange(Object... objects) {
        keyWord = (String) objects[0];
        autoRefresh();
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }


}

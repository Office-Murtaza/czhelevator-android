package com.kingyon.elevator.uis.fragments.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.customview.SelfAdaptionImageView;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.AdNoticeWindowEntity;
import com.kingyon.elevator.entities.AnnouncementEntity;
import com.kingyon.elevator.entities.BannerEntity;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.HomepageClassifyEntity;
import com.kingyon.elevator.entities.HomepageDataEntity;
import com.kingyon.elevator.entities.HomepageLocationHolder;
import com.kingyon.elevator.entities.KeywordEntity;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.NewsEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.others.AddCellToPlanPresenter;
import com.kingyon.elevator.uis.activities.HtmlActivity;
import com.kingyon.elevator.uis.activities.MainActivity;
import com.kingyon.elevator.uis.activities.WebViewActivity;
import com.kingyon.elevator.uis.activities.homepage.CellDetailsActivity;
import com.kingyon.elevator.uis.activities.homepage.CityActivity;
import com.kingyon.elevator.uis.activities.homepage.RecommendListActivity;
import com.kingyon.elevator.uis.activities.homepage.SearchActivity;
import com.kingyon.elevator.uis.activities.homepage.SearchHistoryActivity;
import com.kingyon.elevator.uis.activities.homepage.WikiListActivity;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.adapterone.BannerAdaper;
import com.kingyon.elevator.uis.adapters.adapterone.ClassifyAdapter;
import com.kingyon.elevator.uis.adapters.adapterone.HomeCellsAdaper;
import com.kingyon.elevator.uis.adapters.adapterone.NewsListAdapter;
import com.kingyon.elevator.uis.widgets.FullyGridLayoutManager;
import com.kingyon.elevator.uis.widgets.FullyLinearLayoutManager;
import com.kingyon.elevator.uis.widgets.NewsItemDecornation;
import com.kingyon.elevator.uis.widgets.ProportionFrameLayout;
import com.kingyon.elevator.uis.widgets.viewpager.AutoScrollViewPager;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DealScrollRecyclerView;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.JumpUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshFragment;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ScreenUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by GongLi on 2018/12/24.
 * Email：lc824767150@163.com
 */

public class HomepageFragment extends BaseStateRefreshFragment implements BannerAdaper.OnPagerClickListener<BannerEntity>{
    @BindView(R.id.vp_banner)
    AutoScrollViewPager vpBanner;
    @BindView(R.id.pfl_banner)
    ProportionFrameLayout pflBanner;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.img_current_location)
    ImageView imgCurrentLocation;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.rv_classify)
    RecyclerView rvClassify;
    @BindView(R.id.rv_cell)
    RecyclerView rvCell;
    @BindView(R.id.nsv)
    NestedScrollView nsv;
    @BindView(R.id.tv_location_title)
    TextView tvLocationTitle;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.tv_search_title)
    TextView tvSearchTitle;
    @BindView(R.id.tv_map_title)
    TextView tvMapTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.main_notice_img)
    SelfAdaptionImageView main_notice_img;
    @BindView(R.id.news_container)
    FrameLayout news_container;
    @BindView(R.id.cat_more_news)
    TextView cat_more_news;
    @BindView(R.id.news_list_container)
    RecyclerView news_list_container;
    Unbinder unbinder;
    @BindView(R.id.img_knowledges)
    ImageView imgKnowledges;
    @BindView(R.id.img_examples)
    ImageView imgExamples;
//    @BindView(R.id.custom_view)
//    XRefreshView customView;
    private LinearLayoutManager linearLayoutManager;
    private List<NewsEntity> newsEntityList;
    private NewsListAdapter newsListAdapter;
    private DividerItemDecoration dividerItemDecoration;
    private BannerAdaper<BannerEntity> bannerAdaper;
    private ClassifyAdapter classifyAdapter;
    private HomeCellsAdaper cellsAdaper;
    private float limitedY;
    private LocationEntity locationEntity;
    private AddCellToPlanPresenter addCellToPlanPresenter;
    private int[] clickPosition = new int[2];
    int prage = 1;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_homepage;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        addCellToPlanPresenter = new AddCellToPlanPresenter((BaseActivity) getActivity());
        if (locationEntity == null) {
            if (TextUtils.isEmpty(DataSharedPreferences.getLocationStr())) {
                locationEntity = AppContent.getInstance().getLocation();
            } else {
                locationEntity = DataSharedPreferences.getLocationCache();
            }
        }
        StatusBarUtil.setHeadViewPadding(getActivity(), llTitle);
        EventBus.getDefault().register(this);
        initAdapters();
        initListenner();
        initNewsList();
        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imgSearch.setImageResource(TextUtils.isEmpty(s) ? R.drawable.ic_homepage_right : R.drawable.ic_homepage_clear);
            }
        });

    }

    /**
     * 初始化文章列表
     */
    private void initNewsList() {
        newsEntityList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        news_list_container.setLayoutManager(linearLayoutManager);
        newsListAdapter = new NewsListAdapter(getActivity(), newsEntityList);
        newsListAdapter.setBaseOnItemClick(new BaseOnItemClick<NewsEntity>() {
            @Override
            public void onItemClick(NewsEntity data, int position) {
                MyActivityUtils.goNewsDetailsActivity(getActivity(), data.getId());
            }
        });
        news_list_container.addItemDecoration(new NewsItemDecornation());
        news_list_container.setAdapter(newsListAdapter);
    }


    @Override
    public void onRefresh() {
        if (locationEntity == null) {
            if (TextUtils.isEmpty(DataSharedPreferences.getLocationStr())) {
                locationEntity = AppContent.getInstance().getLocation();
            } else {
                locationEntity = DataSharedPreferences.getLocationCache();
            }
        }
        LocationEntity locationParam = locationEntity != null ? locationEntity : DataSharedPreferences.getLocationCache();
        updateLocationUi(locationParam);
        NetService.getInstance().homepageDatas(locationParam)
                .compose(this.<HomepageDataEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<HomepageDataEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(final HomepageDataEntity homepageDataEntity) {
                        loadingComplete(STATE_CONTENT);
                        updateBanner(homepageDataEntity.getBanners());
                        updateClassifies();
                        updateNotice(homepageDataEntity.getAnnouncements());
                        tvNotice.postDelayed(() -> {
                            //优化首页卡顿，只加载15条数据下拉刷新
                            cellsAdaper.refreshDatas(homepageDataEntity.getCells().subList(0, 15));
                        }, 200);
                    }
                });
        loadAbsolutelyAd();
        loadNewsList();
    }


    /**
     * 加载首页固定位置的通知
     */
    private void loadAbsolutelyAd() {
        NetService.getInstance().getTipsList("0")
                .subscribe(new CustomApiCallback<List<AdNoticeWindowEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.d("广告加载失败：" + GsonUtils.toJson(ex));
                        main_notice_img.setVisibility(View.GONE);
                        main_notice_img.setOnClickListener(null);
                    }

                    @Override
                    public void onNext(List<AdNoticeWindowEntity> adNoticeWindowEntities) {
                        if (adNoticeWindowEntities != null && adNoticeWindowEntities.size() > 0) {
                            AdNoticeWindowEntity adNoticeWindowEntity = adNoticeWindowEntities.get(0);
                            LogUtils.d("广告数据：" + GsonUtils.toJson(adNoticeWindowEntity));
                            if (adNoticeWindowEntity.getShowType() == 2) {
                                GlideUtils.loadImage(getContext(), adNoticeWindowEntity.getShowContent(), main_notice_img);
//                                main_notice_img.setVisibility(View.VISIBLE);
                                if (adNoticeWindowEntity.isLink()) {
                                    main_notice_img.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            MyActivityUtils.goActivity(getContext(), WebViewActivity.class, adNoticeWindowEntity.getLinkUrl());
                                            LogUtils.d("点击跳转连接：" + adNoticeWindowEntity.getLinkUrl());
                                        }
                                    });
                                } else {

                                    main_notice_img.setOnClickListener(null);
                                }
                            } else {
                                main_notice_img.setVisibility(View.GONE);
                                main_notice_img.setOnClickListener(null);
                            }
                        } else {
                            main_notice_img.setVisibility(View.GONE);
                            main_notice_img.setOnClickListener(null);
                        }
                    }
                });
    }

    /**
     * 加载新闻数据
     */
    private void loadNewsList() {
        NetService.getInstance().getNewsList(0, 5, null, "")
                .subscribe(new CustomApiCallback<List<NewsEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.d("新闻加载失败：" + GsonUtils.toJson(ex));
                        news_container.setVisibility(View.GONE);
                        news_container.setOnClickListener(null);
                    }

                    @Override
                    public void onNext(List<NewsEntity> newsEntitys) {
                        if (newsEntitys != null && newsEntitys.size() > 0) {
                            news_container.setVisibility(View.VISIBLE);
                            newsEntityList = newsEntitys;
                            newsListAdapter.reflashData(newsEntityList);
                        } else {
                            news_container.setVisibility(View.GONE);
                            news_container.setOnClickListener(null);
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateLocationUi(LocationEntity entity) {
        String name = entity.getName() != null ? entity.getName() : "";
        tvLocation.setText(name);
        String cityName = FormatUtils.getInstance().getCityName(entity.getCity() != null ? entity.getCity() : "");
        if (cityName.length() > 5) {
            cityName = String.format("%s…", name.substring(0, 4));
        }
        tvLocationTitle.setText(cityName);
    }

    /**
     * 更新顶部广告内容
     *
     * @param notice 广告内容
     */
    private void updateNotice(List<AnnouncementEntity> notice) {
        StringBuilder stringBuilder = new StringBuilder();
        if (notice != null) {
            int maxIndex = notice.size() - 1;
            for (int i = 0; i <= maxIndex; i++) {
                String noticeContent = notice.get(i).getContent();
                if (!TextUtils.isEmpty(noticeContent)) {
                    if (i < maxIndex) {
                        stringBuilder.append(noticeContent).append(getString(R.string.notice_space));
                    } else {
                        stringBuilder.append(noticeContent);
                    }
                }
            }
        }
        if (tvNotice != null) {
            if (stringBuilder.length() > 0) {
                tvNotice.setVisibility(View.VISIBLE);
                tvNotice.setText(stringBuilder.toString());
                tvNotice.setFocusable(true);
                tvNotice.requestFocus();
            } else {
                tvNotice.setVisibility(View.GONE);
            }
        }
    }

    private void updateClassifies() {
        if (classifyAdapter.getItemRealCount() < 1) {
            List<HomepageClassifyEntity> classifyEntities = new ArrayList<>();
            classifyEntities.add(new HomepageClassifyEntity("迦黎药妆", R.drawable.ic_homepage_retail, getString(R.string.homepage_classify_jialiyaozhuang), 1));
            classifyEntities.add(new HomepageClassifyEntity("社区拼团", R.drawable.ic_homepage_travel, 2));
            classifyEntities.add(new HomepageClassifyEntity("生活服务", R.drawable.ic_homepage_game, 3));
            classifyAdapter.addDatas(classifyEntities);
        }
    }

    private void updateBanner(List<BannerEntity> banners) {
        if (banners == null || banners.size() < 1) {
            banners = new ArrayList<>();
            BannerEntity defaultBanner = new BannerEntity();
            defaultBanner.setType(Constants.BANNER_TYPE.IMAGE);
            defaultBanner.setImageUrl("http://cdn.tlwgz.com/img_homepge_banner_defult.png");
            banners.add(defaultBanner);
        }
        if (getContext() != null) {
            if (bannerAdaper == null) {
                bannerAdaper = new BannerAdaper<>(getContext(), banners);
                bannerAdaper.setOnPagerClickListener(this);
                vpBanner.setAdapter(bannerAdaper);
            } else {
                bannerAdaper.setBannerEntities(banners);
                bannerAdaper.notifyDataSetChanged();
                if (vpBanner.getAdapter() != null) {
                    vpBanner.getAdapter().notifyDataSetChanged();
                }
            }
//            if (bannerAdaper.getCount() > 1) {
//                new ViewPagerIndicator.Builder(getContext(), vpBanner, llIndicator, bannerAdaper.getCount())
//                        .setDotHeightByDp(3)
//                        .setDotWidthByDp(16)
//                        .setMarginByDp(6)
//                        .setSelectorDrawable(R.drawable.ic_space_banner_indicator)
//                        .build();
//            } else {
//                llIndicator.removeAllViews();
//            }
            pflBanner.setProporty((bannerAdaper != null && bannerAdaper.getCount() > 0) ? 2.25904f : ScreenUtil.getScreenWidth(getContext()) / (float) (ScreenUtil.dp2px(26) + ScreenUtil.getStatusBarHeight()));
            limitedY = 0;
        }
    }

    @OnClick({R.id.tv_location, R.id.img_current_location, R.id.tv_search, R.id.img_search,
            R.id.tv_text, R.id.img_map, R.id.img_knowledges, R.id.img_examples, R.id.tv_location_title,
            R.id.tv_search_title, R.id.tv_map_title, R.id.cat_more_news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
            case R.id.tv_location_title:
                startActivityForResult(CityActivity.class, 8001);
                break;
            case R.id.img_current_location:
                tvLocation.setSelected(false);
                FragmentActivity activity = getActivity();
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).checkLocation();
                }
                break;
            case R.id.tv_search:
            case R.id.tv_search_title:
                jumpToSearchActivity();
                break;
            case R.id.cat_more_news:
                MyActivityUtils.goFragmentContainerActivity(getActivity(), FragmentConstants.NewsRecommendationFragment);
                break;
            case R.id.img_search:
                if (TextUtils.isEmpty(tvSearch.getText())) {
                    jumpToSearchActivity();
                } else {
                    tvSearch.setText("");
                    tvSearchTitle.setText("");
                }
                break;
            case R.id.tv_text:
                Bundle searchBundle1 = getSearchBundle();
                searchBundle1.putBoolean(CommonUtil.KEY_VALUE_3, false);
                startActivity(SearchActivity.class, searchBundle1);
                break;
            case R.id.img_map:
            case R.id.tv_map_title:
                Bundle searchBundle2 = getSearchBundle();
                searchBundle2.putBoolean(CommonUtil.KEY_VALUE_3, true);
                startActivity(SearchActivity.class, searchBundle2);
                break;
            case R.id.img_knowledges:
                startActivity(WikiListActivity.class);
                break;
            case R.id.img_examples:
                startActivity(RecommendListActivity.class);
                break;
        }
    }

    private void jumpToSearchActivity() {
        startActivity(SearchHistoryActivity.class, getSearchBundle());
    }

    @NonNull
    private Bundle getSearchBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonUtil.KEY_VALUE_1, tvSearch.getText().toString());
        if (locationEntity == null) {
            if (TextUtils.isEmpty(DataSharedPreferences.getLocationStr())) {
                locationEntity = AppContent.getInstance().getLocation();
            } else {
                locationEntity = DataSharedPreferences.getLocationCache();
            }
        }
        LocationEntity city = locationEntity != null ? locationEntity : DataSharedPreferences.getLocationCache();
        bundle.putParcelable(CommonUtil.KEY_VALUE_2, new AMapCityEntity(city.getCity(), TextUtils.isEmpty(city.getCityCode()) ? "" : city.getCityCode()));
        return bundle;
    }

    private void initAdapters() {
        classifyAdapter = new ClassifyAdapter(getContext());
        DealScrollRecyclerView.getInstance().dealAdapter(classifyAdapter, rvClassify, new FullyGridLayoutManager(getContext(), 3));
//        rvCell.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
//                .drawable(R.drawable.white_margin_sixteen_divider)
//                .sizeResId(R.dimen.spacing_divider)
//                .build());
        cellsAdaper = new HomeCellsAdaper(getContext());
        DealScrollRecyclerView.getInstance().dealAdapter(cellsAdaper, rvCell, new FullyLinearLayoutManager(getContext()));
    }

    private void initListenner() {
        classifyAdapter.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<HomepageClassifyEntity>() {
            @Override
            public void onItemClick(View view, int position, HomepageClassifyEntity entity, BaseAdapterWithHF<HomepageClassifyEntity> baseAdaper) {
                if (entity != null && !TextUtils.isEmpty(entity.getLink())) {
                    if (getActivity() != null) {
                        if (entity.getLink() != null && entity.getLink().startsWith("http://jiali.gzzhkjw.com")) {
                            boolean success = AFUtil.openHtmlBySystem(getActivity(), entity.getLink());
                            if (!success) {
                                HtmlActivity.start((BaseActivity) getActivity(), "", entity.getLink());
                            }
                        } else {
                            HtmlActivity.start((BaseActivity) getActivity(), entity.getTitle(), entity.getLink());
                        }
                    }
                } else {
                    showToast("暂未开放");
                }
            }
        });
        cellsAdaper.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<CellItemEntity>() {
            @Override
            public void onItemClick(View view, int position, CellItemEntity entity, BaseAdapterWithHF<CellItemEntity> baseAdaper) {
                if (view.getId() == R.id.img_plan) {
                    if (addCellToPlanPresenter != null) {
                        view.getLocationOnScreen(clickPosition);
                        RuntimeUtils.clickPositionAnimation = clickPosition;
                        RuntimeUtils.animationImagePath = entity.getCellLogo();
                        LogUtils.d("当前点击的坐标：", GsonUtils.toJson(clickPosition));
                        addCellToPlanPresenter.showHomeOagePicker(entity.getObjctId(), null);
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong(CommonUtil.KEY_VALUE_1, entity.getObjctId());
                    startActivity(CellDetailsActivity.class, bundle);
                }
            }
        });
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (limitedY == 0) {
                    int titleHeight = ScreenUtil.dp2px(48);
                    limitedY = ScreenUtil.getScreenWidth(getContext()) / pflBanner.getProporty() + ScreenUtil.dp2px(168) - titleHeight - StatusBarUtil.getStatusBarHeight(getContext());
                    if (limitedY < titleHeight) {
                        limitedY = titleHeight;
                    }
                }
                if (scrollY == 0) {
                    if (llTitle.getVisibility() != View.GONE) {
                        llTitle.setVisibility(View.GONE);
                    }
//                    llTitle.setAlpha(0);
                } else if (scrollY < limitedY) {
                    if (llTitle.getVisibility() != View.GONE) {
                        llTitle.setVisibility(View.GONE);
                    }
//                    float percent = (limitedY - scrollY) / limitedY;
//                    int alpha = (int) (255 - percent * 255);
//                    llTitle.setAlpha(alpha / 255f);
                } else {
                    if (llTitle.getVisibility() != View.VISIBLE) {
                        llTitle.setVisibility(View.VISIBLE);
                    }
//                    llTitle.setAlpha(1);
//                    llTitle.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onBannerClickListener(int position, BannerEntity item, List<BannerEntity> datas) {
        JumpUtils.getInstance().onBannerClick((BaseActivity) getActivity(), item);
    }

    public void onLocationResult(boolean firstLocation) {
        if (tvLocation != null) {
            if (!tvLocation.isSelected()) {
                LocationEntity location = AppContent.getInstance().getLocation();
                if (firstLocation && !TextUtils.isEmpty(DataSharedPreferences.getLocationStr()) && locationEntity != null && location != null && !TextUtils.equals(location.getCity(), locationEntity.getCity())) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("提示")
                            .setMessage("是否切换到当前位置？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    locationEntity = AppContent.getInstance().getLocation();
                                    autoRefresh();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                } else {
                    locationEntity = AppContent.getInstance().getLocation();
                    onRefresh();
                }
                tvLocation.setSelected(true);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (BaseActivity.RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case 8001:
                    LocationEntity choosed = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (choosed != null) {
                        tvLocation.setSelected(true);
                        locationEntity = choosed;
                        autoRefresh();
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        if (addCellToPlanPresenter != null) {
            addCellToPlanPresenter.onDestroy();
            addCellToPlanPresenter = null;
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedKeyWord(KeywordEntity entity) {
        String s = entity.getKeyWord() != null ? entity.getKeyWord() : "";
        tvSearch.setText(s);
        tvSearchTitle.setText(s);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedLocation(HomepageLocationHolder holder) {
        if (holder != null && holder.getLocation() != null) {
            LocationEntity location = holder.getLocation();
            tvLocation.setSelected(true);
            locationEntity = location;
            autoRefresh();
        }
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


}

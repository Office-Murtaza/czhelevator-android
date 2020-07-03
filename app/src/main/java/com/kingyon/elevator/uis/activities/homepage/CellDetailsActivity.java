package com.kingyon.elevator.uis.activities.homepage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.gerry.scaledelete.DeletedImageScanDialog;
import com.gerry.scaledelete.ScanleImageUrl;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.CellDetailsEntity;
import com.kingyon.elevator.entities.ImageScan;
import com.kingyon.elevator.entities.ToPlanTab;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.others.AddCellToPlanPresenter;
import com.kingyon.elevator.uis.adapters.adapterone.BannerAdaper;
import com.kingyon.elevator.uis.widgets.ProportionFrameLayout;
import com.kingyon.elevator.uis.widgets.viewpager.AutoScrollViewPager;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.kingyon.elevator.utils.animationutils.AnimatorPath;
import com.kingyon.elevator.utils.animationutils.PathEvaluator;
import com.kingyon.elevator.utils.animationutils.PathPoint;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kingyon.elevator.application.App.getContext;

/**
 * Created by GongLi on 2018/12/29.
 * Email：lc824767150@163.com
 */

public class CellDetailsActivity extends BaseStateRefreshingActivity implements BannerAdaper.OnPagerClickListener<String>, ViewPager.OnPageChangeListener {
    @BindView(R.id.vp_banner)
    AutoScrollViewPager vpBanner;
    @BindView(R.id.ll_indicator)
    LinearLayout llIndicator;
    @BindView(R.id.tv_indicator)
    TextView tvIndicator;
    @BindView(R.id.pfl_banner)
    ProportionFrameLayout pflBanner;
    @BindView(R.id.pre_v_right)
    ImageView preVRight;
    @BindView(R.id.pfl_title)
    FrameLayout pflTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_flow)
    TextView tvFlow;
    @BindView(R.id.ll_flow)
    LinearLayout llFlow;
    @BindView(R.id.tv_cell_type)
    TextView tvCellType;
    @BindView(R.id.tv_lift)
    TextView tvLift;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_business)
    TextView tvBusiness;
    @BindView(R.id.tv_diy)
    TextView tvDiy;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.img_ad_show)
    ImageView imgAdShow;
    @BindView(R.id.tv_ad_tip)
    TextView tvAdTip;
    @BindView(R.id.tv_ad_type)
    TextView tvAdType;
    @BindView(R.id.nsv)
    NestedScrollView nsv;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.iv_gouwuche)
    ImageView iv_gouwuche;
    @BindView(R.id.iv_home_logo)
    ImageView iv_home_logo;
    private AnimatorPath path;//声明动画集合

    private long cellId;

    private CellDetailsEntity details;
    private BannerAdaper<String> bannerAdaper;
    private TextView[] adTypes;
    private AddCellToPlanPresenter addCellToPlanPresenter;
    private int[] animationStartPoint = new int[]{50, 50};
    private Boolean isCanBack = true;

    @Override
    protected String getTitleText() {
        cellId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        return "小区详情";
    }

    @Override
    public int getContentViewId() {
        StatusBarUtil.setTransparent(this, false);
        return R.layout.activity_cell_details;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        StatusBarUtil.setHeadViewPadding(this, pflTitle);
        adTypes = new TextView[]{tvBusiness, tvDiy, tvInfo};
        addCellToPlanPresenter = new AddCellToPlanPresenter(this);
        tvPrice.setText(getPriceSpan(0));
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0 && !mLayoutRefresh.isEnabled()) {
                    mLayoutRefresh.setEnabled(true);
                }
                if (scrollY != 0 && mLayoutRefresh.isEnabled()) {
                    mLayoutRefresh.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        NetService.getInstance().cellDetails(cellId, DataSharedPreferences.getCreatateAccount())
                .compose(this.<CellDetailsEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<CellDetailsEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(CellDetailsEntity cellDetailsEntity) {
                        if (cellDetailsEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        details = cellDetailsEntity;
                        updateUi(cellDetailsEntity);
                        if (TextUtils.isEmpty(getPlanType())) {
                            onAdTypeClick(tvBusiness);
                        }
                        loadingComplete(STATE_CONTENT);
                    }
                });
    }

    private void updateUi(CellDetailsEntity entity) {
        updateBanner(entity.cellBanner);
        preVRight.setSelected(entity.isCollect);
        tvName.setText(entity.name);
        tvAddress.setText(entity.regionName);
        iv_gouwuche.setVisibility(View.VISIBLE);
//        Double cellDistance = FormatUtils.getInstance().getCellDistanceNum(entity.getLongitude(), entity.getLatitude(), entity.getDistance());
//        if (cellDistance != null) {
//            if (cellDistance >= 1000) {
//                tvDistance.setText(String.format("距你%skm", CommonUtil.getMayOneFloat(cellDistance / 1000)));
//            } else {
//                tvDistance.setText(String.format("距你%sm", CommonUtil.getOneDigits(cellDistance)));
//            }
//        } else {
//            tvDistance.setText("");
//        }
//        tvTime.setText(TimeUtil.getYMdTime(entity.getEnterTime()));
//        tvFlow.setText(String.format("%s人次", entity.numberTraffic));
//        llFlow.setVisibility(entity.numberTraffic > 0 ? View.VISIBLE : View.GONE);
//        tvCellType.setText(FormatUtils.getInstance().getCellType(entity.type));
//        tvLift.setText(FormatUtils.getInstance().getCellLift(entity.numberFacility));
//        tvUnit.setText(FormatUtils.getInstance().getCellUnit(entity.numberUnit));
//        GlideUtils.loadImage(this, entity.urlCover, iv_home_logo);
    }

    private void updateBanner(List<String> banners) {
        if (bannerAdaper == null) {
            bannerAdaper = new BannerAdaper<>(this, banners);
            bannerAdaper.setOnPagerClickListener(this);
            vpBanner.setAdapter(bannerAdaper);
            vpBanner.addOnPageChangeListener(this);
        } else {
            bannerAdaper.setBannerEntities(banners);
            bannerAdaper.notifyDataSetChanged();
            if (vpBanner.getAdapter() != null) {
                vpBanner.getAdapter().notifyDataSetChanged();
            }
        }
//        if (bannerAdaper.getCount() > 1) {
//            new ViewPagerIndicator.Builder(this, vpBanner, llIndicator, bannerAdaper.getCount())
//                    .setDotHeightByDp(3)
//                    .setDotWidthByDp(16)
//                    .setMarginByDp(6)
//                    .setSelectorDrawable(R.drawable.ic_banner_indicator)
//                    .build();
//        } else {
//            llIndicator.removeAllViews();
//        }
        pflBanner.setProporty((bannerAdaper != null && bannerAdaper.getCount() > 0) ? 1.6875f : ScreenUtil.getScreenWidth(getContext()) / (float) ScreenUtil.dp2px(26));
        tvIndicator.setVisibility(bannerAdaper.getCount() > 1 ? View.VISIBLE : View.GONE);
        updateIndicatorText();
    }

    private void updateIndicatorText() {
        String indicatorText = String.format("%s/%s", (vpBanner.getCurrentItem() % bannerAdaper.getCount()) + 1, bannerAdaper.getCount());
        SpannableString spannableString = new SpannableString(indicatorText);
        spannableString.setSpan(new ForegroundColorSpan(0xFF979797), indicatorText.indexOf("/"), indicatorText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvIndicator.setText(spannableString);
    }

    @OnClick({R.id.pre_v_right, R.id.tv_business, R.id.tv_diy, R.id.tv_info, R.id.tv_add, R.id.iv_gouwuche})
    public void onViewClicked(View view) {
        if (details == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.pre_v_right:
                requestCollect(details);
                break;
            case R.id.tv_business:
            case R.id.tv_diy:
            case R.id.tv_info:
                onAdTypeClick(view);
                break;
            case R.id.tv_add:
                String planType = getPlanType();
                if (TextUtils.isEmpty(planType)) {
                    showToast("请选择广告类型");
                } else {
                    if (addCellToPlanPresenter != null) {
                        addCellToPlanPresenter.addCellToPlan(cellId, planType, this);
                    }
                }
                break;
            case R.id.iv_gouwuche:
                EventBus.getDefault().post(new ToPlanTab(getPlanType()));
                break;
        }
    }

    public void addToPlanSuccess(String planType) {
        //加入成功，开始执行动画
        startAddPlanAnimation();
    }

    /**
     * 执行加入购物车的动画
     */
    public void startAddPlanAnimation() {
        isCanBack = false;
        iv_home_logo.setX(100);
        iv_home_logo.setY(200);
        iv_home_logo.setVisibility(View.VISIBLE);
        path = new AnimatorPath();
        path.moveTo(100, 100);
        path.secondBesselCurveTo(ScreenUtils.getScreenWidth() / 2, 250,
                ScreenUtils.getScreenWidth() - DensityUtil.dip2px(55),
                ScreenUtils.getScreenHeight() - DensityUtil.dip2px(170));
        ObjectAnimator anim = ObjectAnimator.ofObject(this, "addPlan", new PathEvaluator(), path.getPoints().toArray());
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(1200);
        anim.start();
        iv_home_logo.animate().scaleX(0.1f).scaleY(0.1f).setDuration(1200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iv_home_logo.setVisibility(View.GONE);
                iv_home_logo.setScaleX(1f);
                iv_home_logo.setScaleY(1f);
                iv_gouwuche.setImageResource(R.mipmap.gouwuche);
                isCanBack = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    public void setAddPlan(PathPoint newLoc) {
        iv_home_logo.setTranslationX(newLoc.mX);
        iv_home_logo.setTranslationY(newLoc.mY);
    }

    private String getPlanType() {
        int selectId = 0;
        for (TextView tvType : adTypes) {
            if (tvType.isSelected()) {
                selectId = tvType.getId();
                break;
            }
        }
        String result;
        switch (selectId) {
            case R.id.tv_business:
                result = Constants.PLAN_TYPE.BUSINESS;
                break;
            case R.id.tv_diy:
                result = Constants.PLAN_TYPE.DIY;
                break;
            case R.id.tv_info:
                result = Constants.PLAN_TYPE.INFORMATION;
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    private void onAdTypeClick(View view) {
        if (adTypes == null) {
            return;
        }
        for (TextView tvType : adTypes) {
            if (view.getId() == tvType.getId()) {
//                tvType.setSelected(!tvType.isSelected());
                tvType.setSelected(true);
            } else {
                tvType.setSelected(false);
            }
        }
        updateAdTypePrice();
    }

    private void updateAdTypePrice() {
        int selectId = 0;
        for (TextView tvType : adTypes) {
            if (tvType.isSelected()) {
                selectId = tvType.getId();
                break;
            }
        }
//        switch (selectId) {
//            case R.id.tv_business:
//                tvAdType.setText("商业广告");
//                tvPrice.setText(getPriceSpan(details.priceBusiness));
//                imgAdShow.setImageResource(R.drawable.img_lift_ad_show_normal);
////                tvAdTip.setText("商业广告，以轮流播放的形式投放广告，价格实惠。");
//                tvAdTip.setText(details.getBusinessIntro() != null ? details.getBusinessIntro() : "");
//                break;
//            case R.id.tv_diy:
//                tvAdType.setText("DIY广告");
//                tvPrice.setText(getPriceSpan(details.getDiyAdPrice()));
//                imgAdShow.setImageResource(R.drawable.img_lift_ad_show_normal);
////                tvAdTip.setText("DIY广告，以独占屏幕播放的形式投放广告，投放效果好。");
//                tvAdTip.setText(details.getDiyIntro() != null ? details.getDiyIntro() : "");
//                break;
//            case R.id.tv_info:
//                tvAdType.setText("便民信息");
//                tvPrice.setText(getPriceSpan(details.getInformationAdPrice()));
//                imgAdShow.setImageResource(R.drawable.img_lift_ad_show_info);
////                tvAdTip.setText("便民信息，在屏幕顶部展示，物业通知等便民信息。");
//                tvAdTip.setText(details.getInformationIntro() != null ? details.getInformationIntro() : "");
//                break;
//            default:
//                tvAdType.setText("请先选择广告和屏幕类型");
//                tvPrice.setText(getPriceSpan(0));
//                imgAdShow.setImageDrawable(null);
//                tvAdTip.setText("");
//                break;
//        }
    }

    private CharSequence getPriceSpan(float price) {
        String priceStr = CommonUtil.getTwoFloat(price);
        SpannableString spannableString = new SpannableString(priceStr);
        int index = priceStr.indexOf(".") + 1;
        if (index > 0 && index < priceStr.length() - 1) {
            spannableString.setSpan(new AbsoluteSizeSpan(13, true), index, priceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    private void requestCollect(final CellDetailsEntity details) {
        preVRight.setEnabled(false);
        if (details.isCollect) {
            NetService.getInstance().cancelCollect(String.valueOf(details.id))
                    .compose(this.<String>bindLifeCycle())
                    .subscribe(new CustomApiCallback<String>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                            updateCollectState();
                        }

                        @Override
                        public void onNext(String s) {
                            showToast("取消成功");
//                            details.setCollect(false);
                            updateCollectState();
                        }
                    });
        } else {
//            NetService.getInstance().collectCell(details.getObjctId())
//                    .compose(this.<String>bindLifeCycle())
//                    .subscribe(new CustomApiCallback<String>() {
//                        @Override
//                        protected void onResultError(ApiException ex) {
//                            showToast(ex.getDisplayMessage());
//                            preVRight.setEnabled(true);
//                        }
//
//                        @Override
//                        public void onNext(String s) {
//                            showToast("收藏成功");
//                            details.setCollect(true);
//                            updateCollectState();
//                        }
//                    });
        }
    }

    private void updateCollectState() {
        preVRight.setEnabled(true);
//        preVRight.setSelected(details.isCollect());
    }

    @Override
    public void onBannerClickListener(int position, String item, List<String> datas) {
//        DeletedImageScanDialog deletedImageScanDialog = new DeletedImageScanDialog(this, new ImageScan(item), null);
//        deletedImageScanDialog.showOne();
        if (datas == null || datas.size() < 1) {
            return;
        }
        List<ScanleImageUrl> urls = new ArrayList<>();
        for (String s : datas) {
            urls.add(new ImageScan(s));
        }
        if (urls.size() > 0 && urls.size() == datas.size()) {
            DeletedImageScanDialog dialog = new DeletedImageScanDialog(this, urls, position, null);
            dialog.show(position % datas.size(), false, false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateIndicatorText();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        if (addCellToPlanPresenter != null) {
            addCellToPlanPresenter.onDestroy();
            addCellToPlanPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!isCanBack) {
            return;
        }
        super.onBackPressed();
    }
}

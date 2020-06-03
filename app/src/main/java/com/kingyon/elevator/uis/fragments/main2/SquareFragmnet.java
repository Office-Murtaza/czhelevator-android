package com.kingyon.elevator.uis.fragments.main2;

import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.kingyon.elevator.utils.utilstwo.MyLoader;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:广场
 */
public class SquareFragmnet extends BaseFragment implements OnBannerListener {
    @BindView(R.id.banner_square)
    Banner bannerSquare;
    @BindView(R.id.ll_shopping)
    LinearLayout llShopping;
    @BindView(R.id.ll_activity)
    LinearLayout llActivity;
    @BindView(R.id.ll_academy)
    LinearLayout llAcademy;
    @BindView(R.id.ll_guide)
    LinearLayout llGuide;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.tv_more1)
    TextView tvMore1;
    Unbinder unbinder;
    @BindView(R.id.ll_top)
    LinearLayout llTop;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_square;
    }

    @SuppressLint("NewApi")
    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setHeadViewPadding(getActivity(), llTop);
        List<String> imgs = new ArrayList<>();
        imgs.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg");
        imgs.add("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");
        imgs.add("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg");
        List titles = new ArrayList<>();
        titles.add("广告1");
        titles.add("广告2");
        titles.add("广告3");
        bannerSquare.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        bannerSquare.setImageLoader(new MyLoader());
        bannerSquare.setImages(imgs);
        bannerSquare.setBannerAnimation(Transformer.Default);
        bannerSquare.setBannerTitles(titles);
        bannerSquare.setDelayTime(3000);
        bannerSquare.isAutoPlay(true);
        bannerSquare.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 30);
            }
        });
        bannerSquare.setClipToOutline(true);
        bannerSquare.setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(this)
                .start();
    }

    @Override
    protected void dealLeackCanary() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_shopping, R.id.ll_activity, R.id.ll_academy, R.id.ll_guide, R.id.tv_more, R.id.tv_more1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_shopping:
                break;
            case R.id.ll_activity:
                break;
            case R.id.ll_academy:
                break;
            case R.id.ll_guide:
                break;
            case R.id.tv_more:
                break;
            case R.id.tv_more1:
                break;
        }
    }

    @Override
    public void OnBannerClick(int position) {

    }


}

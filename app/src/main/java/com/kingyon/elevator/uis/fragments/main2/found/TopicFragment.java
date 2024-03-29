package com.kingyon.elevator.uis.fragments.main2.found;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.HomeTopicEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.CustomFragmentPagerAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
import com.kingyon.elevator.utils.PublicFuncation;
import com.kingyon.elevator.view.ModifyTabLayout;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.widgets.StateLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Created By Admin  on 2020/5/12
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TopicFragment extends FoundFragemtUtils {

    @BindView(R.id.modiftTabLayout_topic)
    ModifyTabLayout tabLayout;
    @BindView(R.id.vp_topic)
    ViewPager vp;
    Unbinder unbinder;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;

    @Override
    protected void lazyLoad() {
//        if (PublicFuncation.isIntervalTenMin()) {
            initData();
//        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_topic1;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    private void initData() {
        stateLayout.showProgressView(getString(R.string.wait));
        NetService.getInstance().setQueryTopicLabel()
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<HomeTopicEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
//                        ToastUtils.showToast(getActivity(), ex.getDisplayMessage(), 1000);
                        stateLayout.showContentView();
                        hideProgress();
                        rlError.setVisibility(View.VISIBLE);
                        vp.setVisibility(View.GONE);
                        rlNull.setVisibility(View.GONE);

                    }
                    @Override
                    public void onNext(ConentEntity<HomeTopicEntity> conentEntity) {
                        LogUtils.e(conentEntity.getContent().toString());

                        rlError.setVisibility(View.GONE);
                        vp.setVisibility(View.VISIBLE);
                        rlNull.setVisibility(View.GONE);
                        tabLayout.setViewHeight(dp2px(30));
                        tabLayout.setBottomLineWidth(dp2px(10));
                        tabLayout.setBottomLineHeight(dp2px(3));
                        tabLayout.setBottomLineHeightBgResId(R.color.color_00000000);
                        tabLayout.setItemInnerPaddingLeft(dp2px(10));
                        tabLayout.setItemInnerPaddingRight(dp2px(10));
                        tabLayout.setInnerLeftMargin(dp2px(5));
                        tabLayout.setInnerRightMargin(dp2px(5));
                        tabLayout.setmTextColorSelect(ContextCompat.getColor(getActivity(), R.color.type1));
                        tabLayout.setmTextColorUnSelect(ContextCompat.getColor(getActivity(), R.color.type2));
                        tabLayout.setmTextBgUnSelectResId(R.drawable.bg_ad_type1);
                        tabLayout.setmTextBgSelectResId(R.drawable.bg_ad_type2);
                        tabLayout.setTextSize(16);
                        //等寬
                        if (conentEntity.getContent().size() <= 0) {
                            rlError.setVisibility(View.GONE);
                            vp.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                            hideProgress();
                        } else {
                            CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getChildFragmentManager());
                            adapter.cleanFrag();
                            for (int i = 0; i < conentEntity.getContent().size(); i++) {
                                adapter.addFrag(new TopicTypeFragment().setIndex(conentEntity.getContent().get(i).id,stateLayout),
                                        conentEntity.getContent().get(i).labelName);
                            }
                            vp.setAdapter(adapter);
                            vp.setOffscreenPageLimit(adapter.getCount());
                            tabLayout.setupWithViewPager(vp);
                        }
                    }
                });


    }

    @Override
    protected void dealLeackCanary() {

    }


    public int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.rl_error)
    public void onViewClicked() {
        initData();
    }
}

package com.kingyon.elevator.uis.fragments.main2.advertis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Created By Admin  on 2020/5/29
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class AdPointDetailsFragment extends BaseFragment {
    @BindView(R.id.img_image)
    ImageView imgImage;
    @BindView(R.id.tv_attribute)
    TextView tvAttribute;
    @BindView(R.id.tv_occupancy)
    TextView tvOccupancy;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_traffic)
    TextView tvTraffic;
    @BindView(R.id.tv_coverage)
    TextView tvCoverage;
    @BindView(R.id.tv_wsp)
    TextView tvWsp;
    @BindView(R.id.img_ad_collect)
    ImageView imgAdCollect;
    @BindView(R.id.tv_ad_collect)
    TextView tvAdCollect;
    @BindView(R.id.ll_ad_collect)
    LinearLayout llAdCollect;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String type;

    public AdPointDetailsFragment setIndex(String type) {
        this.type = type;
        return (this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_adpoint_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        switch (type) {
            case "1":
                tvTitle.setText("广告时长30秒，以轮流播放的形式投放，经济实惠");
                imgImage.setImageResource(R.mipmap.im_style_business_m);
                break;
            case "2":
                tvTitle.setText("广告时长60秒，以独占屏幕的形式投放，效果显著");
                imgImage.setImageResource(R.mipmap.im_style_diy_m);
                break;
            case "3":
                tvTitle.setText("免费发布文字类信息");
                imgImage.setImageResource(R.mipmap.im_style_service_m);
                break;
            default:
        }

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

    @OnClick(R.id.ll_ad_collect)
    public void onViewClicked() {
    }
}

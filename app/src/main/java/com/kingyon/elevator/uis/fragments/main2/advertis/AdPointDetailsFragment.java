package com.kingyon.elevator.uis.fragments.main2.advertis;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CellDetailsEntity;
import com.kingyon.elevator.uis.actiivty2.content.ArticleDetailsActivity;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.TimeUtil;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.ToastUtils;

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
@SuppressLint("ValidFragment")
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
    private CellDetailsEntity cellDetailsEntity;
    private String type;
    private String panID;

    @SuppressLint("ValidFragment")
    public AdPointDetailsFragment (CellDetailsEntity cellDetailsEntity, String type, String panID ) {
        this.cellDetailsEntity = cellDetailsEntity;
        this.type = type;
        this.panID = panID;

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_adpoint_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        try {
            tvOccupancy.setText(cellDetailsEntity.occupancyRate+"");
            tvPrice.setText(cellDetailsEntity.averageSellingPrice+"");
            tvTraffic.setText(cellDetailsEntity.numberTraffic+"");
            tvCoverage.setText(cellDetailsEntity.peopleCoverd+"");
            tvWsp.setText(cellDetailsEntity.throwWay+""/*+FormatUtils.getInstance().getCellDistance()*/);
            tvAttribute.setText(FormatUtils.getInstance().getCellType(cellDetailsEntity.type));
            switch (type) {
                case "1":
                    tvTitle.setText(cellDetailsEntity.businessIntro);
                    imgImage.setImageResource(R.mipmap.im_style_business_m);
                    break;
                case "2":
                    tvTitle.setText(cellDetailsEntity.diyIntro);
                    imgImage.setImageResource(R.mipmap.im_style_diy_m);
                    break;
                case "3":
                    tvTitle.setText(cellDetailsEntity.informationIntro);
                    imgImage.setImageResource(R.mipmap.im_style_service_m);
                    break;
                default:
            }

            if (cellDetailsEntity.isCollect){
                imgAdCollect.setImageResource(R.mipmap.ic_site_collest_off);
                Resources resources = getContext().getResources();
                Drawable btnDrawable = resources.getDrawable(R.mipmap.bg_site_collest_gray);
                llAdCollect.setBackgroundDrawable(btnDrawable);
                tvAdCollect.setText("已收藏此点位");
            }else {
                imgAdCollect.setImageResource(R.mipmap.ic_site_collest_on);
                Resources resources = getContext().getResources();
                Drawable btnDrawable = resources.getDrawable(R.mipmap.bg_site_collest_red);
                llAdCollect.setBackgroundDrawable(btnDrawable);
                tvAdCollect.setText("收藏此点位");
            }
            tvTime.setText("每天"+(Integer.parseInt(cellDetailsEntity.deviceSwitchOff.substring(0,2))-Integer.parseInt(cellDetailsEntity.deviceSwitchOn.substring(0,2)))+"小时");

        }catch (Exception e){
            LogUtils.e(e.toString());
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
      if (imgAdCollect!=null&&cellDetailsEntity!=null){
          if (cellDetailsEntity.isCollect){
              imgAdCollect.setImageResource(R.mipmap.ic_site_collest_off);
              Resources resources = getContext().getResources();
              Drawable btnDrawable = resources.getDrawable(R.mipmap.bg_site_collest_gray);
              llAdCollect.setBackgroundDrawable(btnDrawable);
              tvAdCollect.setText("已收藏此点位");
          }else {
              imgAdCollect.setImageResource(R.mipmap.ic_site_collest_on);
              Resources resources = getContext().getResources();
              Drawable btnDrawable = resources.getDrawable(R.mipmap.bg_site_collest_red);
              llAdCollect.setBackgroundDrawable(btnDrawable);
              tvAdCollect.setText("收藏此点位");
          }
      }
    }

    @OnClick(R.id.ll_ad_collect)
    public void onViewClicked() {
        /*收藏*/
        if (tvAdCollect.getText().toString().equals("收藏此点位")) {
            ConentUtils.httpAddCollect(panID, Constants.COLLECT_STATE.POINT, new ConentUtils.AddCollect() {
                @Override
                public void Collect(boolean is) {
                    if (is) {
                        cellDetailsEntity.isCollect = true;
                        imgAdCollect.setImageResource(R.mipmap.ic_site_collest_off);
                        Resources resources = getContext().getResources();
                        Drawable btnDrawable = resources.getDrawable(R.mipmap.bg_site_collest_gray);
                        llAdCollect.setBackgroundDrawable(btnDrawable);
                        ToastUtils.showToast(getActivity(), "收藏成功", 1000);
                        tvAdCollect.setText("已收藏此点位");
                    } else {
                        ToastUtils.showToast(getActivity(), "收藏失败", 1000);
                    }
                }
            });
        }else {
            ConentUtils.httpCancelCollect(panID, new ConentUtils.AddCollect() {
                @Override
                public void Collect(boolean is) {
                    if (is){
                        cellDetailsEntity.isCollect = false;
                        imgAdCollect.setImageResource(R.mipmap.ic_site_collest_on);
                        Resources resources = getContext().getResources();
                        Drawable btnDrawable = resources.getDrawable(R.mipmap.bg_site_collest_red);
                        llAdCollect.setBackgroundDrawable(btnDrawable);
                        tvAdCollect.setText("收藏此点位");
                        ToastUtils.showToast(getActivity(), "取消收藏成功", 1000);
                    }else {
                        ToastUtils.showToast(getActivity(), "取消收藏失败", 1000);
                    }
                }
            });
        }
    }
}

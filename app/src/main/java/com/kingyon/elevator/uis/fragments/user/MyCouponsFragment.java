package com.kingyon.elevator.uis.fragments.user;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.user.CouponDetailsActivity;
import com.kingyon.elevator.uis.activities.user.MyCouponsActivty;
import com.kingyon.elevator.uis.adapters.MyCouponsInvalidAdapter;
import com.kingyon.elevator.uis.adapters.MyCouponsNormalAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshLoadingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class MyCouponsFragment extends BaseStateRefreshLoadingFragment<Object> {
    private String status;
    private boolean normal;

    private List<CouponItemEntity> discountCoupons = new ArrayList<>();
    private List<CouponItemEntity> voucherCoupons = new ArrayList<>();
    private ArrayList<CouponItemEntity> choosedCoupons;

    public static MyCouponsFragment newInstance(String status) {
        Bundle args = new Bundle();
        args.putString(CommonUtil.KEY_VALUE_1, status);
        MyCouponsFragment fragment = new MyCouponsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_my_coupons;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            status = getArguments().getString(CommonUtil.KEY_VALUE_1);
            normal = TextUtils.equals(Constants.CouponStatus.NORMAL, status);
        }
        super.init(savedInstanceState);
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        MultiItemTypeAdapter<Object> adapter;
        if (normal) {
            adapter = new MyCouponsNormalAdapter(getContext(), mItems);
        } else {
            adapter = new MyCouponsInvalidAdapter(getContext(), mItems, TextUtils.equals(Constants.CouponStatus.EXPIRED, status));
        }
        return adapter;
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object item, int position) {
        super.onItemClick(view, holder, item, position);
        if (normal && item != null && item instanceof CouponItemEntity) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(CommonUtil.KEY_VALUE_1, (CouponItemEntity) item);
            startActivity(CouponDetailsActivity.class, bundle);
        }
    }

    @Override
    protected void loadData(final int page) {
//        if (FIRST_PAGE == page) {
//            choosedCoupons = new ArrayList<>();
//            choosedCoupons.addAll(getChoosedCoupons());
//        } else {
//            choosedCoupons = null;
//        }
        NetService.getInstance().getCoupons(status, page)
                .compose(this.<PageListEntity<CouponItemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<CouponItemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<CouponItemEntity> couponItemEntityPageListEntity) {
                        if (couponItemEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        List<CouponItemEntity> datas = couponItemEntityPageListEntity.getContent() != null ? couponItemEntityPageListEntity.getContent() : new ArrayList<CouponItemEntity>();
                        if (FIRST_PAGE == page) {
                            discountCoupons.clear();
                            voucherCoupons.clear();
                        }
                        for (CouponItemEntity item : datas) {
                            if (choosedCoupons != null) {
                                for (CouponItemEntity choosedCache : choosedCoupons) {
                                    if (item.getObjctId() == choosedCache.getObjctId()) {
                                        item.setChoosed(true);
                                    }
                                }
                            }
                            if (TextUtils.equals(Constants.CouponType.VOUCHER, item.getCoupontype())) {
                                voucherCoupons.add(item);
                            } else {
                                discountCoupons.add(item);
                            }
                        }
                        choosedCoupons = null;

                        mItems.clear();
                        if (discountCoupons.size() > 0) {
                            mItems.add(Constants.CouponType.DISCOUNT);
                            mItems.addAll(discountCoupons);
                        }
                        if (voucherCoupons.size() > 0) {
                            mItems.add(Constants.CouponType.VOUCHER);
                            mItems.addAll(voucherCoupons);
                        }
                        loadingComplete(true, couponItemEntityPageListEntity.getTotalPages());
                        FragmentActivity activity = getActivity();
                        if (activity instanceof MyCouponsActivty) {
                            ((MyCouponsActivty) activity).resultNumber(status, couponItemEntityPageListEntity.getTotalElements());
                        }
                    }
                });
    }

    public ArrayList<CouponItemEntity> getChoosedCoupons() {
        ArrayList<CouponItemEntity> chooseds = new ArrayList<>();
        for (Object object : mItems) {
            if (object instanceof CouponItemEntity) {
                CouponItemEntity coupon = (CouponItemEntity) object;
                if (coupon.isChoosed()) {
                    chooseds.add(coupon);
                }
            }
        }
        return chooseds;
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }
}

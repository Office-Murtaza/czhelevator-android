package com.kingyon.elevator.uis.activities.user;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.entities.RecommendInfoEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adapterone.InviteCouponsAdapter;
import com.kingyon.elevator.uis.widgets.FullyLinearLayoutManager;
import com.kingyon.elevator.utils.DealScrollRecyclerView;
import com.kingyon.library.social.BaseSharePramsProvider;
import com.kingyon.library.social.ShareDialog;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 * 2.0邀请界面
 */

public class InviteActivity extends BaseStateRefreshingActivity {
//    @BindView(R.id.pre_v_right)
//    TextView preVRight;
//    @BindView(R.id.tv_invite_award)
//    TextView tvInviteAward;
//    @BindView(R.id.tv_invite_person)
//    TextView tvInvitePerson;
//    @BindView(R.id.tv_coupons)
//    TextView tvCoupons;
    @BindView(R.id.rv_coupons)
    RecyclerView rvCoupons;

    private RecommendInfoEntity recommendInfo;
    private InviteCouponsAdapter couponsAdapter;
    private ShareDialog shareDialog;

    @Override
    protected String getTitleText() {
        return "邀请有礼";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_invitetwo;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        couponsAdapter = new InviteCouponsAdapter(this);

    }

    @Override
    public void onRefresh() {
        NetService.getInstance().recommedInfo()
                .compose(this.<RecommendInfoEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<RecommendInfoEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_CONTENT);
                    }

                    @Override
                    public void onNext(RecommendInfoEntity recommendInfoEntity) {
                        if (recommendInfoEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        recommendInfo = recommendInfoEntity;
//                        tvInviteAward.setText(getSpan(String.format("%s张", recommendInfoEntity.getReceiveCoupons())));
//                        tvInvitePerson.setText(getSpan(String.format("%s人", recommendInfoEntity.getRecommendPersons())));
                        List<CouponItemEntity> coupons = recommendInfoEntity.getCoupons();
//                        tvCoupons.setText(String.format("邀请一名好友，则获得优惠券%s张", coupons != null ? coupons.size() : 0));
                        couponsAdapter.refreshDatas(recommendInfoEntity.getCoupons());
                        loadingComplete(STATE_CONTENT);
                    }
                });
    }

    private CharSequence getSpan(String format) {
        SpannableString spannableString = new SpannableString(format);
        spannableString.setSpan(new AbsoluteSizeSpan(14, true), format.length() - 1, format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @OnClick({R.id.img_back, R.id.tv_invite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
//                startActivity(InviteListActivity.class);
                finish();
                break;
            case R.id.tv_invite:
                if (recommendInfo == null) {
                    return;
                }
                if (shareDialog == null) {
                    BaseSharePramsProvider<RecommendInfoEntity> baseSharePramsProvider = new BaseSharePramsProvider<>(this);
                    baseSharePramsProvider.setShareEntity(recommendInfo);
//                    shareDialog = new ShareDialog(this, baseSharePramsProvider);
                }
                shareDialog.show();
                break;
        }
    }
}

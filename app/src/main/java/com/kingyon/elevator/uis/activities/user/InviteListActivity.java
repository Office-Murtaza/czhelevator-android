package com.kingyon.elevator.uis.activities.user;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.RecommendInfoEntity;
import com.kingyon.elevator.entities.RecommendListInfo;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.TimeUtil;

import butterknife.BindView;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class InviteListActivity extends BaseStateRefreshingLoadingActivity<UserEntity> {
    @BindView(R.id.tv_person)
    TextView tvPerson;
    @BindView(R.id.tv_coupons)
    TextView tvCoupons;

    @Override
    protected String getTitleText() {
        return "推荐奖励明细";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_invite_list;
    }

    @Override
    protected MultiItemTypeAdapter<UserEntity> getAdapter() {
        return new BaseAdapter<UserEntity>(this, R.layout.activity_invite_list_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, UserEntity item, int position) {
                holder.setTextNotHide(R.id.tv_mobile, CommonUtil.getHideMobile(item.getPhone()));
                holder.setTextNotHide(R.id.tv_time, TimeUtil.getYMdTime(item.getRecommendTime() != null ? item.getRecommendTime() : 0));
            }
        };
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().recommendListInfo(page)
                .compose(this.<RecommendListInfo>bindLifeCycle())
                .subscribe(new CustomApiCallback<RecommendListInfo>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                        showState(STATE_CONTENT);
                    }

                    @Override
                    public void onNext(RecommendListInfo recommendListInfo) {
                        PageListEntity<UserEntity> userPage = recommendListInfo.getUserPage();
                        if (userPage == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                            RecommendInfoEntity recommendInfo = recommendListInfo.getRecommendInfo();
                            if (recommendInfo == null) {
                                throw new ResultException(9001, "返回参数异常");
                            }
                            tvPerson.setText(String.format("您已成功推荐%s位好友", recommendInfo.getRecommendPersons()));
                            tvCoupons.setText(String.format("获得%s张优惠券", recommendInfo.getReceiveCoupons()));
                        }
                        if (userPage.getContent() != null) {
                            mItems.addAll(userPage.getContent());
                        }
                        loadingComplete(true, userPage.getTotalPages());
                    }
                });
    }

    @NonNull
    @Override
    protected String getEmptyTip() {
        stateLayout.setEmptyViewTip("  ");
        return "  ";
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }
}

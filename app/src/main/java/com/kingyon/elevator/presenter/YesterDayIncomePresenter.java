package com.kingyon.elevator.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.YesterdayIncomeEntity;
import com.kingyon.elevator.entities.entities.ConentTxEntity;
import com.kingyon.elevator.entities.entities.EarningsYesterdayEnity;
import com.kingyon.elevator.entities.entities.StatisticalEnity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.view.YesterDayIncomeView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 */
public class YesterDayIncomePresenter extends BasePresenter<YesterDayIncomeView> {
    private int startPosition = 0;
    private int size = 30;
    private List<EarningsYesterdayEnity> yesterdayIncomeEntityList;

    public YesterDayIncomePresenter(Context mContext) {
        super(mContext);
        yesterdayIncomeEntityList = new ArrayList<>();
    }

    public List<EarningsYesterdayEnity> getYesterdayIncomeEntityList() {
        return yesterdayIncomeEntityList;
    }

    /**
     * 获取昨日收益数据
     */
    public void getYesterdayIncomeData(int reflashType) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getYesterdayIncomeDetailedList(startPosition )
                .subscribe(new CustomApiCallback<ConentTxEntity<StatisticalEnity<EarningsYesterdayEnity>>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (startPosition == 0) {
                                getView().showErrorView();
                            }
                        }
                    }

                    @Override
                    public void onNext(ConentTxEntity<StatisticalEnity<EarningsYesterdayEnity>> incomeDetailsEntities) {
                        LogUtils.e(incomeDetailsEntities.toString());
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showContentView();
                            if (reflashType == ReflashConstants.Refalshing) {
                                yesterdayIncomeEntityList = incomeDetailsEntities.pageContent.lstResponse;
                            } else {
                                yesterdayIncomeEntityList.addAll(incomeDetailsEntities.pageContent.lstResponse);
                                if (incomeDetailsEntities.pageContent.lstResponse.size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = yesterdayIncomeEntityList.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + incomeDetailsEntities.pageContent.lstResponse.size());
                            getView().showDetailsListData(yesterdayIncomeEntityList);
                        }
                    }
                });
    }

    /**
     * 获取已提现数据
     */
    public void getCashData(int reflashType) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getCashedList(startPosition + "", size + "")
                .subscribe(new CustomApiCallback<List<YesterdayIncomeEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (startPosition == 0) {
                                getView().showErrorView();
                            }
                        }
                    }

                    @Override
                    public void onNext(List<YesterdayIncomeEntity> incomeDetailsEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showContentView();
                            if (reflashType == ReflashConstants.Refalshing) {
//                                yesterdayIncomeEntityList = incomeDetailsEntities;
                            } else {
//                                yesterdayIncomeEntityList.addAll(incomeDetailsEntities);
                                if (incomeDetailsEntities.size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = yesterdayIncomeEntityList.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + incomeDetailsEntities.size());
                            getView().showDetailsListData(yesterdayIncomeEntityList);
                        }
                    }
                });
    }


}

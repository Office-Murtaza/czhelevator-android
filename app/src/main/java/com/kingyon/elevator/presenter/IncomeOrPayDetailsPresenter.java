package com.kingyon.elevator.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.IncomeDetailsEntity;
import com.kingyon.elevator.entities.MonthOrDayIncomeOrPayEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.view.IncomeOrPayDetailsView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By SongPeng  on 2019/11/27
 * Email : 1531603384@qq.com
 */
public class IncomeOrPayDetailsPresenter extends BasePresenter<IncomeOrPayDetailsView> {

    private int startPosition = 0;
    private int size = 40;
    private List<IncomeDetailsEntity> incomeDetailsEntityList;


    public IncomeOrPayDetailsPresenter(Context mContext) {
        super(mContext);
        incomeDetailsEntityList = new ArrayList<>();
    }


    public void getDetailsData(int reflashType, int selectIncomeOrPay, int selectCatType, int year, int month, int day) {
        String formatMonth = month < 10 ? "0" + month : month + "";
        String formatDay = day < 10 ? "0" + day : day + "";
        if (selectIncomeOrPay == 0) {
            //查询收入
            if (selectCatType == 0) {
                //查询月收入详情
                getIncomePayDataDayList(reflashType, Constants.QueryDataType.IncomeType,year + "-" + formatDay);
            } else {
                //查询日收入详情
                getDayIncomeData(reflashType, year + "-" + formatMonth + "-" + formatDay);
            }
        } else {
            //查询支出
            if (selectCatType == 0) {
                //查询月支出详情
                getIncomePayDataDayList(reflashType, Constants.QueryDataType.PayType,year + "-" + formatDay);
            } else {
                //查询日支出详情
                getDayPayData(reflashType, year + "-" + formatMonth + "-" + formatDay);
            }
        }
    }


    private void getDayIncomeData(int reflashType, String date) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getIncomeDetailedList(startPosition + "", size + "", date)
                .subscribe(new CustomApiCallback<List<IncomeDetailsEntity>>() {
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
                    public void onNext(List<IncomeDetailsEntity> incomeDetailsEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (reflashType == ReflashConstants.Refalshing) {
                                incomeDetailsEntityList = incomeDetailsEntities;
                            } else {
                                incomeDetailsEntityList.addAll(incomeDetailsEntities);
                                if (incomeDetailsEntities.size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = incomeDetailsEntityList.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + incomeDetailsEntities.size());
                            getView().showDetailsListData(incomeDetailsEntityList);
                        }
                    }
                });
    }


    private void getIncomePayDataDayList(int reflashType, String type, String date) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getIncomePayDataDayList(startPosition + "", size + "", type, date)
                .subscribe(new CustomApiCallback<List<IncomeDetailsEntity>>() {
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
                    public void onNext(List<IncomeDetailsEntity> incomeDetailsEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (reflashType == ReflashConstants.Refalshing) {
                                incomeDetailsEntityList = incomeDetailsEntities;
                            } else {
                                incomeDetailsEntityList.addAll(incomeDetailsEntities);
                                if (incomeDetailsEntities.size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = incomeDetailsEntityList.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + incomeDetailsEntities.size());
                            getView().showDetailsListData(incomeDetailsEntityList);
                        }
                    }
                });
    }

    private void getDayPayData(int reflashType, String date) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getPayDetailedList(startPosition + "", size + "", date)
                .subscribe(new CustomApiCallback<List<IncomeDetailsEntity>>() {
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
                    public void onNext(List<IncomeDetailsEntity> incomeDetailsEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (reflashType == ReflashConstants.Refalshing) {
                                incomeDetailsEntityList = incomeDetailsEntities;
                            } else {
                                incomeDetailsEntityList.addAll(incomeDetailsEntities);
                                if (incomeDetailsEntities.size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = incomeDetailsEntityList.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + incomeDetailsEntities.size());
                            getView().showDetailsListData(incomeDetailsEntityList);
                        }
                    }
                });
    }

    public List<IncomeDetailsEntity> getIncomeDetailsEntityList() {
        return incomeDetailsEntityList;
    }
}

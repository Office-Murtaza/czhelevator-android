package com.kingyon.elevator.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.IncomeDetailsEntity;
import com.kingyon.elevator.entities.MonthOrDayIncomeOrPayEntity;
import com.kingyon.elevator.entities.entities.BalancePaymentsEntily;
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

    private int startPosition = 1;
    private int size = 40;
    private  List<BalancePaymentsEntily.PageContentBean.LstResponseBean> lstResponse;;


    public IncomeOrPayDetailsPresenter(Context mContext) {
        super(mContext);
        lstResponse = new ArrayList<>();
    }


    public void getDetailsData(int reflashType, int selectIncomeOrPay, int selectCatType, int year, int month, int day) {
        String formatMonth = month < 10 ? "0" + month : month + "";
        String formatDay = day < 10 ? "0" + day : day + "";
        if (selectIncomeOrPay == 0) {
            //查询收入
            if (selectCatType == 0) {
                //查询月收入详情
                getIncomePayDataDayList(reflashType, "INCOME",year + "-" + formatDay);
            } else {
                //查询日收入详情
                getDayIncomeData(reflashType, "INCOME",year + "-" + formatMonth + "-" + formatDay);
            }
        } else {
            //查询支出
            if (selectCatType == 0) {
                //查询月支出详情
//                getIncomePayDataDayList(reflashType, Constants.QueryDataType.PayType,year + "-" + formatDay);
                getIncomePayDataDayList(reflashType, "PAY",year + "-" + formatDay);
            } else {
                //查询日支出详情
//                getDayPayData(reflashType, year + "-" + formatMonth + "-" + formatDay);
                getDayIncomeData(reflashType, "PAY",year + "-" + formatMonth + "-" + formatDay);
            }
        }
    }


    private void getDayIncomeData(int reflashType,String type, String date) {
        LogUtils.e(reflashType,type,date,startPosition);
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getMonthIncomeOrPayByDate(startPosition, type, date)
                .subscribe(new CustomApiCallback<BalancePaymentsEntily>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (startPosition == 0) {
                                getView().showErrorView();
                            }

                        }
                    }

                    @Override
                    public void onNext(BalancePaymentsEntily incomeDetailsEntities) {
                        LogUtils.e(incomeDetailsEntities.toString());
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (reflashType == ReflashConstants.Refalshing) {
                                lstResponse = incomeDetailsEntities.getPageContent().getLstResponse();
                            } else {
                                lstResponse.addAll(incomeDetailsEntities.getPageContent().getLstResponse());
                                if (incomeDetailsEntities.getPageContent().getLstResponse().size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = lstResponse.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + lstResponse.size());
                            getView().showDetailsListData(lstResponse,incomeDetailsEntities);
                        }
                    }
                });
    }


    private void getIncomePayDataDayList(int reflashType, String type, String date) {
        LogUtils.e(reflashType,type,date,startPosition);
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getYearIncomeOrPayByDate(startPosition, type, date)
                .subscribe(new CustomApiCallback<BalancePaymentsEntily>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (startPosition == 0) {
                                getView().showErrorView();
                            }

                        }
                    }

                    @Override
                    public void onNext(BalancePaymentsEntily incomeDetailsEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            LogUtils.e(incomeDetailsEntities.toString());

                            if (reflashType == ReflashConstants.Refalshing) {
                                lstResponse = incomeDetailsEntities.getPageContent().getLstResponse();
                            } else {
                                lstResponse.addAll(incomeDetailsEntities.getPageContent().getLstResponse());
                                if (incomeDetailsEntities.getPageContent().getLstResponse().size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = lstResponse.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + incomeDetailsEntities.getPageContent().getLstResponse().size());
                            getView().showDetailsListData(lstResponse,incomeDetailsEntities);
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
//                            if (reflashType == ReflashConstants.Refalshing) {
//                                lstResponse = incomeDetailsEntities;
//                            } else {
//                                lstResponse.addAll(incomeDetailsEntities);
//                                if (incomeDetailsEntities.size() == 0) {
//                                    getView().loadMoreIsComplete();
//                                }
//                            }
//                            startPosition = lstResponse.size();
//                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + incomeDetailsEntities.size());
//                            getView().showDetailsListData(lstResponse);
                        }
                    }
                });
    }

    public List<BalancePaymentsEntily.PageContentBean.LstResponseBean> getlstResponse() {
        return lstResponse;
    }
}

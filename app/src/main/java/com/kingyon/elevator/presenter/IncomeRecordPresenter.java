package com.kingyon.elevator.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.IncomeOrPayEntity;
import com.kingyon.elevator.entities.MonthOrDayIncomeOrPayEntity;
import com.kingyon.elevator.interfaces.PrivacyTipsListener;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.MainActivity;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.view.IncomeRecordView;
import com.leo.afbaselibrary.nets.entities.DataEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 */
public class IncomeRecordPresenter extends BasePresenter<IncomeRecordView> {

    public IncomeRecordPresenter(Context mContext) {
        super(mContext);
    }


    /**
     * 获取收益记录 总收益 收入 和支出数据
     *
     * @param type  当前选择的是年份查询还是月份
     * @param year  年份
     * @param month 月份
     */
    public void getIncomeAndPayData(int type, int year, int month) {
        getView().showProgressView();
        NetService.getInstance().getIncomeAndPayByDate(type == 0 ? year + "" : year + "-" + month)
                .subscribe(new CustomApiCallback<IncomeOrPayEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showErrorView();
                        }
                    }

                    @Override
                    public void onNext(IncomeOrPayEntity incomeOrPayEntity) {
                        if (isViewAttached()) {
                            getView().showContentView();
                            getView().showIncomeOrPayData(incomeOrPayEntity);
                        }
                    }
                });
    }


    /**
     * 筛选查询收益记录 总收益 收入 和支出数据
     *
     * @param type  当前选择的是年份查询还是月份
     * @param year  年份
     * @param month 月份
     */
    public void filterIncomeAndPayData(int type, int year, int month) {
        if (isViewAttached()) {
            getView().showProgressDialog("数据加载中", true);
        }
        NetService.getInstance().getIncomeAndPayByDate(type == 0 ? year + "" : year + "-" + month)
                .subscribe(new CustomApiCallback<IncomeOrPayEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showShortToast("数据加载失败，请重试");
                        }
                    }

                    @Override
                    public void onNext(IncomeOrPayEntity incomeOrPayEntity) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showIncomeOrPayData(incomeOrPayEntity);
                        }
                    }
                });
    }


    /**
     * 查询收入或者支出的数据 填充图表数据
     *
     * @param selectIncomeOrPay
     * @param selectCatType
     * @param year
     * @param month
     */
    public void getIncomePayDataPerDay(int selectIncomeOrPay, int selectCatType, int year, int month) {
        if (selectIncomeOrPay == 0) {
            //查询收入
            if (selectCatType == 0) {
                //查询年收入
                getYearOrMonthIncomeData(year + "");
            } else {
                //查询月收入
                getYearOrMonthIncomeData(year + "-" + month);
            }
        } else {
            //查询支出
            if (selectCatType == 0) {
                //查询年收入
                getYearOrMonthPayData(year + "");
            } else {
                //查询月收入
                getYearOrMonthPayData(year + "-" + month);
            }
        }
    }


    /**
     * 获取收入数据
     *
     * @param date
     */
    private void getYearOrMonthIncomeData(String date) {
        if (isViewAttached()) {
            getView().showProgressDialog("数据获取中", true);
        }
        NetService.getInstance().getYearIncomeOrPayByDate("1", date)
                .subscribe(new CustomApiCallback<List<MonthOrDayIncomeOrPayEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showShortToast("数据加载失败，请重试");
                        }
                    }

                    @Override
                    public void onNext(List<MonthOrDayIncomeOrPayEntity> monthOrDayIncomeOrPayEntityList) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            LogUtils.d("收入数据：" + monthOrDayIncomeOrPayEntityList.size());
                            //getView().showIncomeOrPayData(incomeOrPayEntity);
                        }
                    }
                });
    }


    /**
     * 获取支出数据
     *
     * @param date
     */
    private void getYearOrMonthPayData(String date) {
        if (isViewAttached()) {
            getView().showProgressDialog("数据获取中", true);
        }
        NetService.getInstance().getYearIncomeOrPayByDate("2", date)
                .subscribe(new CustomApiCallback<List<MonthOrDayIncomeOrPayEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showShortToast("数据加载失败，请重试");
                        }
                    }

                    @Override
                    public void onNext(List<MonthOrDayIncomeOrPayEntity> monthOrDayIncomeOrPayEntityList) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            LogUtils.d("支出数据：" + monthOrDayIncomeOrPayEntityList.size());
                            //getView().showIncomeOrPayData(incomeOrPayEntity);
                        }
                    }
                });
    }

}

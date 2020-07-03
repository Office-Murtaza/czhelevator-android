package com.kingyon.elevator.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.entities.IncomeOrPayEntity;
import com.kingyon.elevator.entities.MonthOrDayIncomeOrPayEntity;
import com.kingyon.elevator.entities.entities.EarningsTopEntity;
import com.kingyon.elevator.entities.entities.EarningsTwoYearlistEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.view.IncomeRecordView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

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
        LogUtils.e(type,year,month);
        if (isViewAttached()) {
            getView().showProgressDialog("数据加载中", true);
        }
        String currentMonth = month < 10 ? "0"+month :""+month;
        NetService.getInstance().setEarningsRecordMonth( year + "-" + currentMonth)
                .subscribe(new CustomApiCallback<EarningsTopEntity<EarningsTwoYearlistEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode());
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showErrorView();
                        }
                        getView().showIncomeOrPayData(null);
                    }

                    @Override
                    public void onNext(EarningsTopEntity<EarningsTwoYearlistEntity> incomeOrPayEntity) {
                        LogUtils.e(incomeOrPayEntity.toString());

                        if (isViewAttached()) {
                            getView().hideProgressDailog();
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
        LogUtils.e(type,year,month);
        if (isViewAttached()) {
            getView().showProgressDialog("数据加载中", true);
        }
//        if (type==1) {
            String currentMonth = month < 10 ? "0" + month : "" + month;
            NetService.getInstance().getIncomeAndPayByDate(year + "")
                    .subscribe(new CustomApiCallback<EarningsTopEntity<EarningsTwoYearlistEntity>>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            if (isViewAttached()) {
                                getView().hideProgressDailog();
                                getView().showShortToast("数据加载失败，请重试");
                            }
                        }

                        @Override
                        public void onNext(EarningsTopEntity<EarningsTwoYearlistEntity> incomeOrPayEntity) {
                            if (isViewAttached()) {
                                getView().hideProgressDailog();
                                getView().showIncomeOrPayData(incomeOrPayEntity);
                            }
                        }
                    });
//        }else {
//            getIncomeAndPayData(type, year, month);
//        }
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
        LogUtils.e(selectIncomeOrPay,selectCatType,year,month);
        if (isViewAttached()) {
            getView().showProgressDialog("数据加载中", true);
        }
//        if (isViewAttached()) {
//            getView().showChartLoadingTips("图表数据加载中...");
//        }
//        String currentMonth = month < 10 ? "0"+month :""+month;
//        if (selectIncomeOrPay == 0) {
//            //查询收入
//            if (selectCatType == 0) {
//                //查询年收入
//                getYearIncomeData(year + "");
//            } else {
//                //查询月收入
//                getMonthIncomeData(year + "-" + currentMonth);
//            }
//        } else {
//            //查询支出
//            if (selectCatType == 0) {
//                //查询年收入
//                getYearPayData(year + "");
//            } else {
//                //查询月收入
//                getMonthPayData(year + "-" + currentMonth);
//            }
//        }
        if (selectCatType==0){
            /*年*/
            filterIncomeAndPayData(selectCatType,year,month);
        }else {
            getIncomeAndPayData(selectCatType,year,month);
        }

    }

}

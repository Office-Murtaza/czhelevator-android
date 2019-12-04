package com.kingyon.elevator.view;

import com.kingyon.elevator.entities.IncomeOrPayEntity;
import com.kingyon.elevator.entities.MonthOrDayIncomeOrPayEntity;
import com.kingyon.elevator.mvpbase.BaseView;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 */
public interface IncomeRecordView extends BaseView {

    void showIncomeOrPayData(IncomeOrPayEntity incomeOrPayEntity);


    /**
     * 显示图表数据
     * @param monthOrDayIncomeOrPayEntity
     */
    void showChartData(MonthOrDayIncomeOrPayEntity monthOrDayIncomeOrPayEntity);


    void  showChartLoadingTips(String tips);


    void  hideChartLoadingTips();
}

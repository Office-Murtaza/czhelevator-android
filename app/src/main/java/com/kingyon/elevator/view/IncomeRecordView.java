package com.kingyon.elevator.view;

import com.kingyon.elevator.entities.IncomeOrPayEntity;
import com.kingyon.elevator.mvpbase.BaseView;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 */
public interface IncomeRecordView extends BaseView {

    void showIncomeOrPayData(IncomeOrPayEntity incomeOrPayEntity);



}

package com.kingyon.elevator.view;

import com.kingyon.elevator.entities.YesterdayIncomeEntity;
import com.kingyon.elevator.mvpbase.BaseView;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 */
public interface YesterDayIncomeView extends BaseView {

    void showDetailsListData(List<YesterdayIncomeEntity> incomeDetailsEntities);

    /**
     * 已经全部加载完成了
     */
    void  loadMoreIsComplete();
}

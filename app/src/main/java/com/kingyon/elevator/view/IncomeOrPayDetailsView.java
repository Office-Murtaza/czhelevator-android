package com.kingyon.elevator.view;

import com.kingyon.elevator.entities.IncomeDetailsEntity;
import com.kingyon.elevator.mvpbase.BaseView;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/27
 * Email : 1531603384@qq.com
 */
public interface IncomeOrPayDetailsView extends BaseView {

    void showDetailsListData(List<IncomeDetailsEntity> incomeDetailsEntities);

    /**
     * 已经全部加载完成了
     */
    void  loadMoreIsComplete();
}




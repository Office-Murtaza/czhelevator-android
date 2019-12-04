package com.kingyon.elevator.view;

import com.kingyon.elevator.entities.BindAccountEntity;
import com.kingyon.elevator.mvpbase.BaseView;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/27
 * Email : 1531603384@qq.com
 */
public interface CashMethodSettingView extends BaseView {

    void showListData(List<BindAccountEntity> bindAccountEntities);
}

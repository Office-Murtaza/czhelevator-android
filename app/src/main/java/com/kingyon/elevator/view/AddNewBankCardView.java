package com.kingyon.elevator.view;

import com.kingyon.elevator.mvpbase.BaseView;

/**
 * Created By SongPeng  on 2019/11/27
 * Email : 1531603384@qq.com
 */
public interface AddNewBankCardView extends BaseView {

    void bindSuccess(String bingType,String account, String name, String kaihuhang);
}

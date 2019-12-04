package com.kingyon.elevator.view;

import com.kingyon.elevator.mvpbase.BaseView;

/**
 * Created By SongPeng  on 2019/12/3
 * Email : 1531603384@qq.com
 */
public interface EditPayPasswordFragmentView extends BaseView {


    void  checkPayPwdIsInit(Boolean isInit);

    void showCountDownTime(int downtime);

    void payPasswordEditSuccess();


    void  clearAllInputPwd();
}

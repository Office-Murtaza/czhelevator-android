package com.kingyon.elevator.presenter;

import android.content.Context;

import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.view.YesterDayIncomeView;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 */
public class YesterDayIncomePresenter extends BasePresenter<YesterDayIncomeView> {


    public YesterDayIncomePresenter(Context mContext) {
        super(mContext);
    }


    /**
     * 获取昨日收益数据
     */
    public void  getYesterdayIncomeData(){

    }


}

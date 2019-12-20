package com.kingyon.elevator.interfaces;

import com.kingyon.elevator.entities.SelectDateEntity;

/**
 * Created By SongPeng  on 2019/12/16
 * Email : 1531603384@qq.com
 */
public interface PlanSelectDateLinsener {

    void confirmSelectDate(SelectDateEntity startTime, SelectDateEntity endTime);

    void dialogShowSuccess();

}

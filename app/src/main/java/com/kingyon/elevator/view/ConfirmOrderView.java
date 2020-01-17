package com.kingyon.elevator.view;

import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.AutoCalculationDiscountEntity;
import com.kingyon.elevator.entities.CommitOrderEntiy;
import com.kingyon.elevator.entities.OrderIdentityEntity;
import com.kingyon.elevator.mvpbase.BaseView;

/**
 * Created By SongPeng  on 2019/12/23
 * Email : 1531603384@qq.com
 */
public interface ConfirmOrderView extends BaseView {

    void showCouponsInfo(AutoCalculationDiscountEntity autoCalculationDiscountEntity);

    /**
     * 设置用户认证信息
     *
     * @param orderIdentityEntity
     */
    void setIdentityInfo(OrderIdentityEntity orderIdentityEntity);


    void adUploadSuccess(ADEntity adEntity);

    void orderCommitSuccess(CommitOrderEntiy commitOrderEntiy);

    void showManualSelectCouponsInfo(AutoCalculationDiscountEntity autoCalculationDiscountEntity);
}

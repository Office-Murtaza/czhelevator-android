package com.kingyon.elevator.uis.activities.property;

import com.kingyon.elevator.entities.IncomeStatisticsEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.cooperation.CooperationEarningsActivity;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;

public class PropertyEarningsActivity extends CooperationEarningsActivity {
    @Override
    protected void loadData(int page) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        NetService.getInstance().propertyEarningsDetails(startTime, endTime, page)
                .compose(this.<PageListEntity<IncomeStatisticsEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<IncomeStatisticsEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<IncomeStatisticsEntity> incomeStatisticsEntityPageListEntity) {
                        if (incomeStatisticsEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            updateTotal(incomeStatisticsEntityPageListEntity.getTotalAmount());
                            mItems.clear();
                        }
                        if (incomeStatisticsEntityPageListEntity.getContent() != null) {
                            mItems.addAll(incomeStatisticsEntityPageListEntity.getContent());
                        }
                        loadingComplete(true, incomeStatisticsEntityPageListEntity.getTotalPages());
                    }
                });
    }
}

package com.kingyon.elevator.uis.activities.cooperation;

import com.kingyon.elevator.entities.IncomeStatisticsEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class OpticalFeeActivity extends PropertyFeeActivity {
    @Override
    protected String getTitleText() {
        return "光纤费";
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().partnerOpticalFeeRecords(page)
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

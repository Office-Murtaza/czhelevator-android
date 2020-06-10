package com.kingyon.elevator.utils.utilstwo;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.entities.entities.PlanNumberEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

/**
 * @Created By Admin  on 2020/6/10
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class AdUtils {
    public static int planNumber = 0;
    public static void httpPlannuber() {
        NetService.getInstance().setAdPlan()
                .subscribe(new CustomApiCallback<PlanNumberEntiy>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                    LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(PlanNumberEntiy planNumberEntiy) {
//                        getNumber.getnumber(planNumberEntiy.num);
                        LogUtils.e(planNumberEntiy.num);
                        planNumber = planNumberEntiy.num;
                    }
                });
    }
    public interface GetNumber{
        void getnumber(int number);
    }
}

package com.kingyon.elevator.utils.utilstwo;

import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

/**
 * @Created By Admin  on 2020/7/7
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MassageUtils {

    public static void httpDel(String robot_id,IsSuccess isSuccess){
        NetService.getInstance().removeRobot(robot_id)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        isSuccess.isSuccess(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccess.isSuccess(true);
                    }
                });
    }


}

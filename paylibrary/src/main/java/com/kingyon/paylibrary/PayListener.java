package com.kingyon.paylibrary;

import com.kingyon.paylibrary.entitys.PayWay;

/**
 * Created by gongli on 2017/7/14 16:15
 * email: lc824767150@163.com
 */

public interface PayListener {
    void onPaySuccess(PayWay payWay);

    void onPayFailure(PayWay payWay, String reason);

    void onPayCancel(PayWay payWay);

    void onPayConfirm(PayWay payWay);
}

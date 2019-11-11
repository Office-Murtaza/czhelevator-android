package com.kingyon.paylibrary.alipay;

/**
 * Created by Leo on 2015/12/21
 */
public class AliPayOrder {

    private String parameter;

    private String sign;

    public AliPayOrder(String parameter, String sign) {
        this.parameter = parameter;
        this.sign = sign;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

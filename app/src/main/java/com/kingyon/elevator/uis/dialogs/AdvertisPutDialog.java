package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.advertis.AdPointDetailsActivity;
import com.kingyon.elevator.utils.utilstwo.AdUtils;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.view.AnimManager;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;

import org.greenrobot.eventbus.EventBus;

import static com.czh.myversiontwo.utils.CodeType.ADV_BUSINESS;
import static com.czh.myversiontwo.utils.CodeType.ADV_DAY;
import static com.czh.myversiontwo.utils.CodeType.ADV_INFORMATION;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isLogin;

/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:自己删除
 */
public class AdvertisPutDialog extends Dialog implements View.OnClickListener{
    protected BaseActivity mContext;
    TextView tv_day,tv_business,tv_convenience,share_btn_cancel;
    protected ProgressDialog promotWaitBar;
    private int planId;
    private String planName;
    View startView,endView;
    String imageUrl,typestast;
    boolean  diy,bus,con ,btn= true;

    /**
     *  planId 小区id
     *  planName 计划名字
     *
     * */
    public AdvertisPutDialog(@NonNull BaseActivity context,int planId,String planName,View startView,
                             View endView,String imageUrl,String type) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.mContext = context;
        this.planId = planId;
        this.planName = planName;
        this.startView = startView;
        this.endView = endView;
        this.imageUrl = imageUrl;
        this.typestast = type;
        setContentView(getLayoutId());
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(com.kingyon.library.social.R.style.dialog_show_anim);
            window.setGravity(Gravity.BOTTOM);
        }

    }


    protected int getLayoutId() {
        return R.layout.dialog_advertis_put;
    }

    public void setPaddingTop(int top) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = top;
            window.setAttributes(lp);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        tv_convenience = findViewById(R.id.tv_convenience);
        tv_business = findViewById(R.id.tv_business);
        tv_day = findViewById(R.id.tv_day);
        share_btn_cancel = findViewById(R.id.share_btn_cancel);

        tv_day.setOnClickListener(this);
        share_btn_cancel.setOnClickListener(this);
        tv_business.setOnClickListener(this);
        tv_convenience.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_convenience:
                /*便民*/
                initTview();
//                if (con) {
                    httpAdd(ADV_INFORMATION);
//                }else {
//                    ToastUtils.showToast(mContext,"请稍后网络延迟",1000);
//                }
                break;
            case R.id.tv_day:
                /*day*/
                initTview();
//                if (diy) {
                    httpAdd(ADV_DAY);
//                }else {
//                    ToastUtils.showToast(mContext,"请稍后网络延迟",1000);
//                }
                break;
            case R.id.tv_business:
                /*商业*/
                initTview();
//                if (bus) {
                    httpAdd(ADV_BUSINESS);
//                }else {
//                    ToastUtils.showToast(mContext,"请稍后网络延迟",1000);
//                }
                break;
            case R.id.share_btn_cancel:
//                if (btn) {
                    dismiss();
//                }else {
//                    ToastUtils.showToast(mContext,"请稍后网络延迟",1000);
//                }
                break;

        }

    }

    private void initTview() {
      diy = false;
      bus= false;
      con = false;
      btn = false;
    }
    private void initJview() {
        diy = true;
        bus= true;
        con = true;
        btn = true;
    }

    public void httpAdd(String type) {
        LogUtils.e(planId,planName,type);
        mContext.showProgressDialog("请稍后",false);
        NetService.getInstance().plansAddCells(type, String.valueOf(planId))
                .compose(mContext.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showToast(mContext,ex.getDisplayMessage(),1000);
                        isLogin(ex.getCode());
                        mContext.hideProgress();
                        dismiss();
                        initJview();
                    }

                    @Override
                    public void onNext(String s) {
                        mContext.hideProgress();
                        ToastUtils.showToast(mContext,"添加成功",1000);
                        dismiss();
                        initJview();
                        AdUtils.httpPlannuber();
                        AdUtils.type = type;
                        if (typestast.equals("1")) {
                            AnimManager animManager = new AnimManager.Builder()
                                    .with(mContext)
                                    .startView(startView)
                                    .endView(endView)
                                    .imageUrl(imageUrl)
                                    .time(1000)
                                    .animHeight(40)
                                    .animWidth(40)
                                    .build();
                            animManager.startAnim();
                        }
                    }
                });

    }

}

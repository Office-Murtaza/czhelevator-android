package com.kingyon.elevator.uis.activities.cooperation;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.AdNoticeWindowEntity;
import com.kingyon.elevator.entities.CooperationEntity;
import com.kingyon.elevator.entities.CooperationIdentityEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.entities.PartnershipStatusEntily;
import com.kingyon.elevator.entities.entities.PublicEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.fragments.cooperation.CooperationIdentityFragment;
import com.kingyon.elevator.uis.fragments.cooperation.CooperationInfoFragment;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.PublicFuncation;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingActivity;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class CooperationActivity extends BaseStateRefreshingActivity {

    /*申请界面*/
    private CooperationIdentityFragment identityFragment;
    /*合伙人*/
    private CooperationInfoFragment infoFragment;
    boolean authed;
    String status;
    boolean isBePartner;
    CooperationIdentityEntity identity;
    CooperationInfoNewEntity info;
    @Override
    protected String getTitleText() {
        return "我要合作";
    }

    @Override
    public int getContentViewId() {
        StatusBarUtil.setTransparent(this, false);
        EventBus.getDefault().register(this);
        return R.layout.activity_cooperation;
    }

    @Override
    public void onRefresh() {
        NetService.getInstance().cooperationInfo()
                .compose(this.<CooperationEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<CooperationEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode());
//                        loadingComplete(STATE_ERROR);
                        if (ex.getCode()==-102){
                            loadingComplete(STATE_CONTENT);
                            showFragment(false, null, null);
                        }else if (ex.getCode()==1002){
                            LogUtils.e("");
                        }else {
                            showToast(ex.getDisplayMessage());
                            finish();
                        }
                    }

                    @Override
                    public void onNext(CooperationEntity cooperationEntity) {
                        mLayoutRefresh.setEnabled(false);
                        LogUtils.e(cooperationEntity.toString());
                        CooperationIdentityEntity identity = cooperationEntity.getIdentity();
                        CooperationInfoNewEntity info = cooperationEntity.getInfo();
                        if (!cooperationEntity.isBePartner() && identity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        boolean authed = cooperationEntity.isBePartner() || TextUtils.equals(Constants.COOPERATION_STATUS.SUCCESS, identity.getStatus());
                        if (authed && info == null) {
//                            throw new ResultException(9001, "返回参数异常");
                            showToast("数据异常，请重试");
                            finish();
                        }
                        //未认证时不先设置密码，认证过的才需要先设置密码
                        if (authed) {
                            if (!info.isSetPayPassword()) {
                                //未设置支付密码
                                showToast("您还未设置支付密码，请先设置支付密码");
                                loadingComplete(STATE_CONTENT);
                                MyActivityUtils.goFragmentContainerActivity(CooperationActivity.this, FragmentConstants.SetPasswordFragment, "partner");
                                finish();
                            } else {
                                loadingComplete(STATE_CONTENT);
                                showFragment(authed, identity, info);
                            }
                        } else {
                            loadingComplete(STATE_CONTENT);
                            showFragment(false, identity, info);
                        }
//                        loadingComplete(STATE_CONTENT);
//                        showFragment(authed, identity, info);
                    }
                });

    }


    private void showFragment(boolean authed, CooperationIdentityEntity identity, CooperationInfoNewEntity info) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (identityFragment != null) {
            fragmentTransaction.hide(identityFragment);
        }
        if (infoFragment != null) {
            fragmentTransaction.hide(infoFragment);
        }
        if (authed) {
            if (infoFragment == null) {
                infoFragment = CooperationInfoFragment.newInstance(info);
                fragmentTransaction.add(R.id.fl_content, infoFragment);
            } else {
                fragmentTransaction.show(infoFragment);
                infoFragment.onParamsChange(info);
            }
        } else {
            if (identityFragment == null) {
                identityFragment = CooperationIdentityFragment.newInstance(identity);
                fragmentTransaction.add(R.id.fl_content, identityFragment);
            } else {
                fragmentTransaction.show(identityFragment);
                identityFragment.onParamsChange(identity);
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == 8001) {
                if (identityFragment != null) {
                    identityFragment.onActivityResult(requestCode, resultCode, data);
                }
            } else {
                autoRefresh();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventHandler(EventBusObjectEntity eventBusObjectEntity) {
        if (eventBusObjectEntity.getEventCode() == EventBusConstants.ReflashPartnerInfo) {
            LogUtils.d("提现成功刷新合伙人信息----------------------------------");
            autoRefresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}

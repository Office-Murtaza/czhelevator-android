package com.kingyon.elevator.uis.activities.cooperation;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CooperationEntity;
import com.kingyon.elevator.entities.CooperationIdentityEntity;
import com.kingyon.elevator.entities.CooperationInfoEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.fragments.cooperation.CooperationIdentityFragment;
import com.kingyon.elevator.uis.fragments.cooperation.CooperationInfoFragment;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingActivity;

public class CooperationActivity extends BaseStateRefreshingActivity {

    private CooperationIdentityFragment identityFragment;
    private CooperationInfoFragment infoFragment;

    @Override
    protected String getTitleText() {
        return "我要合作";
    }

    @Override
    public int getContentViewId() {
        StatusBarUtil.setTransparent(this, false);
        return R.layout.activity_cooperation;
    }

    @Override
    public void onRefresh() {
        NetService.getInstance().cooperationInfo()
                .compose(this.<CooperationEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<CooperationEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(CooperationEntity cooperationEntity) {
                        CooperationIdentityEntity identity = cooperationEntity.getIdentity();
                        CooperationInfoEntity info = cooperationEntity.getInfo();
                        if (!cooperationEntity.isBePartner() && identity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        boolean authed = cooperationEntity.isBePartner() || TextUtils.equals(Constants.IDENTITY_STATUS.AUTHED, identity.getStatus());
                        if (authed && info == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        loadingComplete(STATE_CONTENT);
                        showFragment(authed, identity, info);
                    }
                });
    }

    private void showFragment(boolean authed, CooperationIdentityEntity identity, CooperationInfoEntity info) {
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
}

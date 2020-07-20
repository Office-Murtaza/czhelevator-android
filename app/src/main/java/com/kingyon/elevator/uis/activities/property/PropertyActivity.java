package com.kingyon.elevator.uis.activities.property;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.PropertyEntity;
import com.kingyon.elevator.entities.PropertyIdentityEntity;
import com.kingyon.elevator.entities.PropertyInfoEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.fragments.property.PropertyIdentityFragment;
import com.kingyon.elevator.uis.fragments.property.PropertyInfoFragment;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingActivity;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 * 物业首页
 */

public class PropertyActivity extends BaseStateRefreshingActivity {
    /*申请*/
    private PropertyIdentityFragment identityFragment;
    /*物业中心*/
    private PropertyInfoFragment infoFragment;
    private boolean notFirstIn;

    @Override
    protected String getTitleText() {
        return "物业管理";
    }

    @Override
    public int getContentViewId() {
//        StatusBarUtil.setTransparent(this, false);
        return R.layout.activity_property;
    }

    @Override
    public void onRefresh() {
        NetService.getInstance().propertyInfo()
                .compose(this.<PropertyEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<PropertyEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(PropertyEntity propertyEntity) {
                        PropertyIdentityEntity identity = propertyEntity.getIdentity();
                        PropertyInfoEntity info = propertyEntity.getInfo();
                        boolean propertyCell = propertyEntity.isBePropertyCell();
                        boolean propertyCompany = propertyEntity.isBePropertyCompany();
                        if (propertyCell || propertyCompany) {
                            if (info == null) {
                                throw new ResultException(9001, "返回参数异常");
                            }
                            loadingComplete(STATE_CONTENT);
                            showFragment(true, null, info, propertyCell);
                        } else {
                            if (identity == null) {
                                throw new ResultException(9001, "返回参数异常");
                            }
                            boolean authed = TextUtils.equals(Constants.IDENTITY_STATUS.AUTHED, identity.getStatus());
                            if (authed && info == null) {
                                throw new ResultException(9001, "返回参数异常");
                            }
                            loadingComplete(STATE_CONTENT);
                            showFragment(authed, identity, info, false);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        if (notFirstIn) {
            onRefresh();
        } else {
            notFirstIn = true;
        }
        super.onResume();
    }

    private void showFragment(boolean authed, PropertyIdentityEntity identity, PropertyInfoEntity info, boolean propertyCell) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (identityFragment != null) {
            fragmentTransaction.hide(identityFragment);
        }
        if (infoFragment != null) {
            fragmentTransaction.hide(infoFragment);
        }
        if (propertyCell) {
            if (infoFragment == null) {
                infoFragment = PropertyInfoFragment.newInstance(info, true);
                fragmentTransaction.add(R.id.fl_content, infoFragment);
            } else {
                fragmentTransaction.show(infoFragment);
                infoFragment.onParamsChange(info);
            }
        } else {
            if (authed) {
                if (infoFragment == null) {
                    infoFragment = PropertyInfoFragment.newInstance(info, false);
                    fragmentTransaction.add(R.id.fl_content, infoFragment);
                } else {
                    fragmentTransaction.show(infoFragment);
                    infoFragment.onParamsChange(info);
                }
            } else {
                if (identityFragment == null) {
                    identityFragment = PropertyIdentityFragment.newInstance(identity);
                    fragmentTransaction.add(R.id.fl_content, identityFragment);
                } else {
                    fragmentTransaction.show(identityFragment);
                    identityFragment.onParamsChange(identity);
                }
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

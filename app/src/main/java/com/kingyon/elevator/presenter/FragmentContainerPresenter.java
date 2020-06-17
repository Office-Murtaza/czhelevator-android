package com.kingyon.elevator.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CooperationEntity;
import com.kingyon.elevator.entities.CooperationIdentityEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.view.FragmentContainerView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;

import static com.kingyon.elevator.constants.ReflashConstants.STATE_CONTENT;
import static com.kingyon.elevator.constants.ReflashConstants.STATE_ERROR;
import static com.kingyon.elevator.constants.ReflashConstants.STATE_PROGRESS;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 */
public class FragmentContainerPresenter extends BasePresenter<FragmentContainerView> {

    public FragmentContainerPresenter(Context mContext) {
        super(mContext);
    }

    /**
     * 获取合伙人信息
     */
    public void getPartnerInfo() {
        getView().loadingComplete(STATE_PROGRESS);
        NetService.getInstance().cooperationInfo()
                .subscribe(new CustomApiCallback<CooperationEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().loadingComplete(STATE_ERROR);
                        }
                    }

                    @Override
                    public void onNext(CooperationEntity cooperationEntity) {
                        CooperationIdentityEntity identity = cooperationEntity.getIdentity();
                        CooperationInfoNewEntity info = cooperationEntity.getInfo();
                        if (!cooperationEntity.isBePartner() && identity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        boolean authed = cooperationEntity.isBePartner() || TextUtils.equals(Constants.IDENTITY_STATUS.AUTHED, identity.getStatus());
                        if (authed && info == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (isViewAttached()) {
                            getView().loadingComplete(STATE_CONTENT);
                            getView().goPartnerDetailsInfo(authed, identity, info);
                        }
                    }
                });
    }
}

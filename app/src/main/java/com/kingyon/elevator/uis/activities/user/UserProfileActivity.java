package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.InputEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.actiivty2.login.LoginActiivty;
import com.kingyon.elevator.uis.activities.InputActivity;
import com.kingyon.elevator.uis.activities.password.LoginActivity;
import com.kingyon.elevator.uis.activities.password.ModifyPasswordActivity;
import com.kingyon.elevator.uis.activities.password.ModifyPhoneFirstActivity;
import com.kingyon.elevator.uis.activities.password.ResetPasswordActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.PictureSelectorUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingActivity;
import com.leo.afbaselibrary.utils.GlideUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class UserProfileActivity extends BaseStateRefreshingActivity {

    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_pwd)
    TextView tvPwd;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;

    private final int headCode = 1;
    private final int nickCode = 3;

    @Override
    protected String getTitleText() {
        return "个人资料";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_profile;
    }

    @Override
    public void onRefresh() {
        NetService.getInstance().userProfile()
                .compose(this.<UserEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<UserEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(UserEntity userEntity) {
                        if (userEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        DataSharedPreferences.saveUserBean(userEntity);
                        loadingComplete(STATE_CONTENT);
                        showUserInfo(userEntity);
                    }
                });
    }

    private void showUserInfo(UserEntity userEntity) {
        GlideUtils.loadAvatarImage(this, userEntity.getAvatar(), imgHead);
        tvNick.setText(userEntity.getNikeName() != null ? userEntity.getNikeName() : "");
        tvPwd.setText("******");
        tvMobile.setText(CommonUtil.getHideMobile(userEntity.getPhone()));
    }

    @OnClick({R.id.ll_head, R.id.ll_nick, R.id.ll_pwd, R.id.ll_mobile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_head:
                PictureSelectorUtil.showPictureSelector(this, headCode);
                break;
            case R.id.ll_nick:
                InputActivity.start(this, nickCode, new InputEntity(10, InputType.TYPE_CLASS_TEXT, "昵称", tvNick.getText().toString(), "请输入昵称", 1));
                break;
            case R.id.ll_pwd:
                startActivity(ModifyPasswordActivity.class);
                break;
            case R.id.ll_mobile:
                UserEntity userBean = DataSharedPreferences.getUserBean();
                Bundle bundle = new Bundle();
                if (userBean != null && !TextUtils.isEmpty(userBean.getPhone())) {
                    bundle.putString(CommonUtil.KEY_VALUE_1, userBean.getPhone());
                    startActivity(ModifyPhoneFirstActivity.class, bundle);
                } else {
                    bundle.putBoolean(CommonUtil.KEY_VALUE_1, true);
                    startActivity(LoginActiivty.class, bundle);
                }
                break;
        }
    }

    private void modify(final int viewId, final String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(getKey(viewId), s);
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().userEidtProfile(params)
                .compose(this.<UserEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<UserEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                    }

                    @Override
                    public void onNext(UserEntity userEntity) {
                        hideProgress();
                        if (userEntity != null) {
                            DataSharedPreferences.saveUserBean(userEntity);
                        }
                        showUserInfo(userEntity);
                    }
                });
    }

    public String getKey(int id) {
        String result = "";
        switch (id) {
            case R.id.ll_head:
                result = "avatar";
                break;
            case R.id.ll_nick:
                result = "nikeName";
                break;
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case headCode:
                    ArrayList<String> mSelectPath2 = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    if (mSelectPath2 != null && mSelectPath2.size() > 0) {
                        showProgressDialog(getString(R.string.wait));
                        NetService.getInstance().uploadFile(this, new File(mSelectPath2.get(0)), new NetUpload.OnUploadCompletedListener() {
                            @Override
                            public void uploadSuccess(List<String> images,List<String> hash, JSONObject response) {
                                LogUtils.e(images,hash,response);
                                if (images != null && images.size() > 0) {
                                    modify(R.id.ll_head, images.get(0));
                                } else {
                                    showToast("上传头像出错");
                                    hideProgress();
                                }
                            }

                            @Override
                            public void uploadFailed(ApiException ex) {
                                showToast(ex.getDisplayMessage());
                                hideProgress();
                            }
                        });
                    }
                    break;
                case nickCode:
                    modify(R.id.ll_nick, data.getStringExtra(CommonUtil.KEY_VALUE_1));
                    break;
            }
        }
    }
}

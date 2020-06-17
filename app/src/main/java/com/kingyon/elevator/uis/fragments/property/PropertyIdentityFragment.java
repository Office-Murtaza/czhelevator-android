package com.kingyon.elevator.uis.fragments.property;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.PropertyIdentityEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.others.OnParamsChangeInterface;
import com.kingyon.elevator.uis.activities.property.PropertyActivity;
import com.kingyon.elevator.uis.widgets.ProportionFrameLayout;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.PictureSelectorUtil;
import com.kingyon.elevator.utils.SoftKeyboardUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.GlideUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class PropertyIdentityFragment extends BaseFragment implements OnParamsChangeInterface {


    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.ll_authing)
    LinearLayout llAuthing;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.et_company)
    EditText etCompany;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.img_cert)
    ImageView imgCert;
    @BindView(R.id.fl_cert)
    ProportionFrameLayout flCert;
    @BindView(R.id.tv_apply)
    TextView tvApply;
    @BindView(R.id.ll_apply)
    LinearLayout llApply;
    //    @BindView(R.id.nsv)
//    NestedScrollView nsv;
    @BindView(R.id.fl_apply)
    FrameLayout flApply;

    private PropertyIdentityEntity entity;
    private SoftKeyboardUtils softKeyboardUtils;

    public static PropertyIdentityFragment newInstance(PropertyIdentityEntity entity) {
        Bundle args = new Bundle();
        args.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        PropertyIdentityFragment fragment = new PropertyIdentityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_property_identity;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            entity = getArguments().getParcelable(CommonUtil.KEY_VALUE_1);
        } else {
            entity = new PropertyIdentityEntity();
        }
        StatusBarUtil.setHeadViewPadding(getActivity(), flTitle);
        updateUI(entity);
        softKeyboardUtils = new SoftKeyboardUtils(getActivity(), llApply);
        softKeyboardUtils.setOnKeyboardChangeListener(new SoftKeyboardUtils.OnKeyboardChangeListener() {
            @Override
            public void onKeyboardChange(boolean show) {
//                if (show) {
//                    llApply.setPadding(ScreenUtil.dp2px(16), ScreenUtil.dp2px(30), ScreenUtil.dp2px(16), ScreenUtil.dp2px(140));
//                } else {
//                    llApply.setPadding(ScreenUtil.dp2px(16), ScreenUtil.dp2px(30), ScreenUtil.dp2px(16), ScreenUtil.dp2px(30));
//                }
            }
        });
//        llApply.post(new Runnable() {
//            @Override
//            public void run() {
//                ViewGroup.LayoutParams layoutParams = nsv.getLayoutParams();
//                if (layoutParams != null) {
//                    layoutParams.height = llApply.getHeight() + ScreenUtil.dp2px(16);
//                    nsv.setLayoutParams(layoutParams);
//                }
//            }
//        });
    }

    @Override
    public void onParamsChange(Object... objects) {
        entity = (PropertyIdentityEntity) objects[0];
        updateUI(entity);
    }

    private void updateUI(PropertyIdentityEntity entity) {
        boolean authing = TextUtils.equals(entity.getStatus(), Constants.IDENTITY_STATUS.AUTHING);
        if (authing) {
            flApply.setVisibility(View.GONE);
            llAuthing.setVisibility(View.VISIBLE);
        } else {
            llAuthing.setVisibility(View.GONE);
            flApply.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(CommonUtil.getEditText(etCompany))) {
                String companyName = entity.getCompanyName();
                etCompany.setText(companyName != null ? companyName : "");
                etCompany.setSelection(etCompany.getText().length());
            }

            if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
                String personName = entity.getPersonName();
                etName.setText(personName != null ? personName : "");
                etName.setSelection(etName.getText().length());
            }

            if (TextUtils.isEmpty(CommonUtil.getEditText(etMobile))) {
                String phone = entity.getPhone();
                etMobile.setText(phone != null ? phone : "");
                etMobile.setSelection(etMobile.getText().length());
            }

            if (flCert.getTag() == null) {
                String certificate = entity.getCertificate();
                if (!TextUtils.isEmpty(certificate)) {
                    flCert.setTag(certificate);
                    GlideUtils.loadImage(getContext(), certificate, imgCert);
                }
            }

            tvNotice.post(new Runnable() {
                @Override
                public void run() {
                    updateNotice();
                }
            });
        }
    }

    private void updateNotice() {
        boolean failed = TextUtils.equals(entity.getStatus(), Constants.IDENTITY_STATUS.FAILD);
        String reason = entity.getFaildReason();
        if (failed && !TextUtils.isEmpty(reason)) {
            tvNotice.setVisibility(View.VISIBLE);
            tvNotice.setText(String.format("失败原因：%s", reason));
            tvNotice.setFocusable(true);
            tvNotice.requestFocus();
        } else {
            tvNotice.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.pre_v_back, R.id.fl_cert, R.id.tv_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_back:
                FragmentActivity activity = getActivity();
                if (activity != null && !activity.isFinishing()) {
                    activity.finish();
                }
                break;
            case R.id.fl_cert:
                PictureSelectorUtil.showPictureSelectorNoCrop((BaseActivity) getActivity(), 8001);
                break;
            case R.id.tv_apply:
                onApplyClick();
                break;
        }
    }

    private void onApplyClick() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
            showToast("请输入联系人姓名");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etMobile))) {
            showToast("请输入联系人手机号码");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etCompany))) {
            showToast("请输入公司名称");
            return;
        }
        String certificate = (String) flCert.getTag();
        if (TextUtils.isEmpty(certificate)) {
            showToast("请上传营业执照");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        tvApply.setEnabled(false);
        KeyBoardUtils.closeKeybord(getActivity());
        NetService.getInstance().propertyApply(etCompany.getText().toString(), etName.getText().toString()
                , etMobile.getText().toString(), certificate)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        tvApply.setEnabled(true);
                        hideProgress();
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("保存成功");
                        FragmentActivity activity = getActivity();
                        if (activity != null && activity instanceof PropertyActivity) {
                            ((PropertyActivity) activity).onRefresh();
                        }
                        tvApply.setEnabled(true);
                        hideProgress();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 8001:
                    ArrayList<String> facePath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    if (facePath != null && facePath.size() > 0) {
                        showProgressDialog(getString(R.string.wait));
                        NetService.getInstance().uploadFile((BaseActivity) getActivity(), new File(facePath.get(0)), new NetUpload.OnUploadCompletedListener() {
                            @Override
                            public void uploadSuccess(List<String> images, List<String> hash,JSONObject response) {
                                LogUtils.e(images,hash,response);
                                if (images != null && images.size() > 0) {
                                    String certUrl = images.get(0);
                                    flCert.setTag(certUrl);
                                    GlideUtils.loadImage(getContext(), certUrl, imgCert);
                                    hideProgress();
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
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (softKeyboardUtils != null) {
            softKeyboardUtils.disable();
        }
        super.onDestroyView();
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }


}

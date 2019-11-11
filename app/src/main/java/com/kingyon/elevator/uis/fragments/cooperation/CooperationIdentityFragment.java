package com.kingyon.elevator.uis.fragments.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.CooperationIdentityEntity;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.others.OnParamsChangeInterface;
import com.kingyon.elevator.uis.activities.cooperation.CooperationActivity;
import com.kingyon.elevator.uis.activities.homepage.CityActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.utils.KeyboardChangeListener;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.SoftKeyboardUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class CooperationIdentityFragment extends BaseFragment implements OnParamsChangeInterface {

    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.ll_authing)
    LinearLayout llAuthing;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_apply)
    TextView tvApply;
    @BindView(R.id.ll_apply)
    LinearLayout llApply;
    @BindView(R.id.fl_apply)
    FrameLayout flApply;

    private CooperationIdentityEntity entity;
    private SoftKeyboardUtils softKeyboardUtils;

    public static CooperationIdentityFragment newInstance(CooperationIdentityEntity entity) {
        Bundle args = new Bundle();
        args.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        CooperationIdentityFragment fragment = new CooperationIdentityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_cooperation_identity;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            entity = getArguments().getParcelable(CommonUtil.KEY_VALUE_1);
        } else {
            entity = new CooperationIdentityEntity();
        }
        StatusBarUtil.setHeadViewPadding(getActivity(), flTitle);
        updateUI(entity);
        softKeyboardUtils = new SoftKeyboardUtils(getActivity(), llApply);
        softKeyboardUtils.setOnKeyboardChangeListener(new SoftKeyboardUtils.OnKeyboardChangeListener() {
            @Override
            public void onKeyboardChange(boolean show) {
//                if (show) {
//                    llApply.setPadding(ScreenUtil.dp2px(16), ScreenUtil.dp2px(30), ScreenUtil.dp2px(16), ScreenUtil.dp2px(240));
//                } else {
//                    llApply.setPadding(ScreenUtil.dp2px(16), ScreenUtil.dp2px(30), ScreenUtil.dp2px(16), ScreenUtil.dp2px(30));
//                }
            }
        });
    }

    @Override
    public void onParamsChange(Object... objects) {
        entity = (CooperationIdentityEntity) objects[0];
        updateUI(entity);
    }

    @OnClick({R.id.pre_v_back, R.id.tv_city, R.id.tv_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_back:
                FragmentActivity activity = getActivity();
                if (activity != null && !activity.isFinishing()) {
                    activity.finish();
                }
                break;
            case R.id.tv_city:
                startActivityForResult(CityActivity.class, 8001);
                break;
            case R.id.tv_apply:
                onApplyClick();
                break;
        }
    }

    private void onApplyClick() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
            showToast("请输入真实姓名");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etMobile))) {
            showToast("请输入手机号码");
            return;
        }
        AMapCityEntity city = (AMapCityEntity) tvCity.getTag();
        if (city == null) {
            showToast("请选择城市");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        tvApply.setEnabled(false);
        KeyBoardUtils.closeKeybord(getActivity());
        NetService.getInstance().cooperationApply(etName.getText().toString()
                , etMobile.getText().toString(), city)
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
                        if (activity != null && activity instanceof CooperationActivity) {
                            ((CooperationActivity) activity).onRefresh();
                        }
                        tvApply.setEnabled(true);
                        hideProgress();
                    }
                });
    }

    private void updateUI(CooperationIdentityEntity entity) {
        boolean authing = TextUtils.equals(entity.getStatus(), Constants.IDENTITY_STATUS.AUTHING);
        if (authing) {
            flApply.setVisibility(View.GONE);
            llAuthing.setVisibility(View.VISIBLE);
        } else {
            llAuthing.setVisibility(View.GONE);
            flApply.setVisibility(View.VISIBLE);

            if (tvCity.getTag() == null) {
                AMapCityEntity city = entity.getCity();
                if (city != null) {
                    tvCity.setTag(city);
                    tvCity.setText(city.getName());
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 8001:
                    LocationEntity choosed = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (choosed != null) {
                        AMapCityEntity cityEntity = new AMapCityEntity();
                        cityEntity.setName(choosed.getCity());
                        cityEntity.setAdcode(TextUtils.isEmpty(choosed.getCityCode()) ? "" : choosed.getCityCode());
                        tvCity.setTag(cityEntity);
                        tvCity.setText(cityEntity.getName());
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

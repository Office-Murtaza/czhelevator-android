package com.kingyon.elevator.uis.fragments.cooperation;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.CooperationIdentityEntity;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.others.OnParamsChangeInterface;
import com.kingyon.elevator.uis.activities.cooperation.CooperationActivity;
import com.kingyon.elevator.uis.activities.homepage.CityActivity;
import com.kingyon.elevator.uis.activities.user.IdentityInfoActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.SoftKeyboardUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
    private  UserEntity userEntity;
    private CooperationIdentityEntity entity;
    private SoftKeyboardUtils softKeyboardUtils;
    private LocationEntity locationEntity;
    String cityCode;
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
        if (TextUtils.isEmpty(DataSharedPreferences.getLocationStr())) {
            locationEntity = AppContent.getInstance().getLocation();
        } else {
            locationEntity = DataSharedPreferences.getLocationCache();
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
        httpgeren();

        try {
            InputStreamReader isr = new InputStreamReader(getActivity().getAssets().open("gd_district.json"),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            br.close();
            isr.close();
            //直接传入JSONObject来构造一个实例
            JSONArray jsonArray = new JSONArray(builder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject.optJSONArray("districts");
                for (int c= 0;c<jsonArray1.length();c++){
                    JSONObject object1 = jsonArray1.optJSONObject(c);
                    if (object1.optString("name").equals(locationEntity.getCity())){
                        cityCode = object1.getString("adcode");
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
            e.printStackTrace();
        }

    }

    private void httpgeren() {
        NetService.getInstance().userProfile()
                .compose(this.<UserEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<UserEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(UserEntity userEntity1) {
                        LogUtils.e(userEntity1.toString());
                        userEntity = userEntity1;
                        String authStatus = userEntity.getAuthStatus() != null ? userEntity.getAuthStatus() : "";
                        switch (authStatus) {
                            case Constants.IDENTITY_STATUS.AUTHING:
                                shwoDialog("您已提交个人认证申请，工作人员正在审核处理中，预计需要5个工作日。",false,false);
                                break;
                            default:
                                break;
                        }

                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        httpgeren();
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
        /*if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
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
                });*/

        String authStatus = userEntity.getAuthStatus() != null ? userEntity.getAuthStatus() : "";
        switch (authStatus) {
            case Constants.IDENTITY_STATUS.AUTHING:
                shwoDialog("您已提交个人认证申请，工作人员正在审核处理中，预计需要5个工作日。",false,true);
                break;
            case Constants.IDENTITY_STATUS.FAILD:
                shwoDialog("个人认证失败,请重新提交",true,false);
                break;
            case Constants.IDENTITY_STATUS.AUTHED:
                httpSubmit(userEntity.getFullname(),userEntity.getPhone());
                break;
            case Constants.IDENTITY_STATUS.NO_AUTH:
                shwoDialog("为了更好的为您提供服务，申请成为合伙人请先完成资质认证。",true,false);
                break;
            default:
                break;
        }
    }

    private void httpSubmit(String nikeName, String phone) {
        LogUtils.e(nikeName,phone,locationEntity.toString(),cityCode);
        showProgressDialog(getString(R.string.wait));
        tvApply.setEnabled(false);
        KeyBoardUtils.closeKeybord(getActivity());
        NetService.getInstance().cooperationApply(nikeName
                , phone, cityCode)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        shwoDialog("您已提交合伙人申请，工作人员正在审核处理中，预计需要5个工作日。",false,true);
                        tvApply.setEnabled(true);
                        hideProgress();
                    }

                    @Override
                    public void onNext(String s) {
//                        showToast("申请成功");
                        ToastUtils.showToast(getActivity(),"申请成功",1000,20);
                        getActivity().finish();
                        FragmentActivity activity = getActivity();
                        if (activity != null && activity instanceof CooperationActivity) {
                            ((CooperationActivity) activity).onRefresh();
                        }
                        tvApply.setEnabled(true);
                        hideProgress();
                    }
                });
    }

    private void shwoDialog(String src,boolean isattestation,boolean is) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.show();
        /* 添加对话框自定义布局 */
        alertDialog.setContentView(R.layout.layout_partner);
        /* 获取对话框窗口 */
        Window window = alertDialog.getWindow();
        /* 设置显示窗口的宽高 */
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        /* 设置窗口显示位置 */
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        TextView tv_content = window.findViewById(R.id.tv_content);
        TextView tv_next = window.findViewById(R.id.tv_next);
        TextView tv_cancel = window.findViewById(R.id.tv_cancel);
        tv_content.setText(src);
        if (isattestation){
            tv_cancel.setVisibility(View.VISIBLE);
            tv_next.setText("去认证");

        }
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isattestation){
                    alertDialog.dismiss();
                    startActivity(IdentityInfoActivity.class);
                }else {
                    if (is){
                        alertDialog.dismiss();
                    }else {
                        getActivity().finish();
                    }
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    private void updateUI(CooperationIdentityEntity entity) {
        switch (entity.getStatus()){

            case Constants.IDENTITY_STATUS.AUTHING:
//                认证中
                shwoDialog("您已提交合伙人申请，工作人员正在审核处理中，预计需要5个工作日。",false,false);
                break;
            case Constants.IDENTITY_STATUS.FAILD:
//                失败
                shwoDialog("您提交的申请，工作人员审核不通过，请重新提交。",false,true);
                break;


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

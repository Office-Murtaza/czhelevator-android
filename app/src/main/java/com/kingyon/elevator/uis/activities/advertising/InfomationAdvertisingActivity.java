package com.kingyon.elevator.uis.activities.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/3.
 * Email：lc824767150@163.com
 */

public class InfomationAdvertisingActivity extends BaseSwipeBackActivity {
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_length)
    TextView tvLength;

    private ADEntity adEntity;

    @Override
    protected String getTitleText() {
        adEntity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        return "便民信息";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_infomation_advertising;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvLength.setText(String.format("%s/100", s.length()));
            }
        });
        etContent.setText((adEntity != null && adEntity.getTitle() != null) ? adEntity.getTitle() : "");
        etContent.setSelection(etContent.getText().length());
    }

    @OnClick(R.id.tv_save)
    public void onViewClicked() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etContent))) {
            showToast("还没有输入任何内容");
            return;
        }
        if (adEntity != null && TextUtils.equals(adEntity.getTitle(), etContent.getText().toString())) {
            showToast("还没有对内容做任何修改");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().createOrEidtAd(adEntity != null ? adEntity.getObjctId() : null
                , true, Constants.PLAN_TYPE.INFORMATION, Constants.AD_SCREEN_TYPE.INFORMATION
                , etContent.getText().toString(), null, null, null, null, null)
                .compose(this.<ADEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<ADEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                    }

                    @Override
                    public void onNext(ADEntity adEntity) {
                        hideProgress();
                        Intent intent = new Intent();
                        if (adEntity != null) {
                            intent.putExtra(CommonUtil.KEY_VALUE_1, adEntity);
                        }
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }
}

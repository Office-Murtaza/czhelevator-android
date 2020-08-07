package com.kingyon.elevator.uis.activities.devices;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.LiftElemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/18.
 * Email：lc824767150@163.com
 * 电梯编辑
 */

public class LiftEditActivity extends BaseSwipeBackActivity {
    @BindView(R.id.et_sn)
    EditText etSn;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_max)
    EditText etMax;
    @BindView(R.id.et_negative)
    EditText etNegative;
    @BindView(R.id.et_base)
    EditText etBase;
    @BindView(R.id.tv_create)
    TextView tvCreate;

    private boolean edit;
    private LiftElemEntity entity;
    private long unitId;

    @Override
    protected String getTitleText() {
        edit = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_1, false);
        entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_2);
        unitId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_3, 0);
        return edit ? "编辑电梯" : "添加电梯";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_lift_edit;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (edit && entity != null) {

            etSn.setText(entity.getLiftNo() != null ? entity.getLiftNo() : "");
            etSn.setSelection(etSn.getText().length());

            etName.setText(entity.getName() != null ? entity.getName() : "");
            etName.setSelection(etName.getText().length());

            etMax.setText(entity.getHighest() != null ? String.valueOf(entity.getHighest()) : "");
            etMax.setSelection(etMax.getText().length());

            etNegative.setText(entity.getLowest() != null ? String.valueOf(entity.getLowest()) : "");
            etNegative.setSelection(etNegative.getText().length());

            etBase.setText(entity.getBase() != null ? String.valueOf(entity.getBase()) : "");
            etBase.setSelection(etBase.getText().length());
        }
    }

    @OnClick(R.id.tv_create)
    public void onViewClicked() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etSn))) {
            showToast("请输入电梯注册代码");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
            showToast("请输入电梯号");
            return;
        }

        if (TextUtils.isEmpty(CommonUtil.getEditText(etMax))) {
            showToast("请输入最高楼层");
            return;
        }
        Integer max;
        try {
            max = Integer.parseInt(CommonUtil.getEditText(etMax));
        } catch (NumberFormatException ex) {
            max = null;
        }
        if (max == null) {
            showToast("请输入正确的最高楼层");
            return;
        }

        if (TextUtils.isEmpty(CommonUtil.getEditText(etNegative))) {
            showToast("请输入负楼层");
            return;
        }
        Integer min;
        try {
            min = Integer.parseInt(CommonUtil.getEditText(etNegative));
        } catch (NumberFormatException ex) {
            min = null;
        }
        if (min == null) {
            showToast("请输入正确的负楼层");
            return;
        }

        if (TextUtils.isEmpty(CommonUtil.getEditText(etBase))) {
            showToast("请输入基层楼层");
            return;
        }
        Integer base;
        try {
            base = Integer.parseInt(CommonUtil.getEditText(etBase));
        } catch (NumberFormatException ex) {
            base = null;
        }
        if (base == null) {
            showToast("请输入正确的基层楼层");
            return;
        }

        tvCreate.setEnabled(false);
        showProgressDialog(getString(R.string.wait),true);
        NetService.getInstance().addLift(edit ? entity.getObjectId() : null, unitId
                , etSn.getText().toString(), etName.getText().toString(), max, min, base)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                        tvCreate.setEnabled(true);
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("操作成功");
                        hideProgress();
                        tvCreate.setEnabled(true);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }
}

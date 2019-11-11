package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.PlanItemEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class PlanEditDialog extends Dialog {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_business)
    TextView tvBusiness;
    @BindView(R.id.tv_diy)
    TextView tvDiy;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;

    private PlanItemEntity entity;

    private TextView[] tvTypes;

    private OnResultListener onResultListener;

    public PlanEditDialog(Context context, OnResultListener onResultListener) {
        super(context, R.style.normal_dialog_corner);
        this.onResultListener = onResultListener;
        setContentView(R.layout.dialog_plan_edit);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.92);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvTypes = new TextView[]{tvBusiness, tvDiy, tvInfo};
    }

    @OnClick({R.id.tv_business, R.id.tv_diy, R.id.tv_info})
    public void onViewClicked(View view) {
        onTypeChoosed(view.getId());
    }

    private void onTypeChoosed(int viewId) {
        if (tvTypes == null) {
            return;
        }
        for (TextView tvType : tvTypes) {
            tvType.setSelected(tvType.getId() == viewId);
        }
    }

    private String getType() {
        int resId = 0;
        for (TextView tvType : tvTypes) {
            if (tvType.isSelected()) {
                resId = tvType.getId();
                break;
            }
        }
        String result;
        switch (resId) {
            case R.id.tv_business:
                result = Constants.PLAN_TYPE.BUSINESS;
                break;
            case R.id.tv_diy:
                result = Constants.PLAN_TYPE.DIY;
                break;
            case R.id.tv_info:
                result = Constants.PLAN_TYPE.INFORMATION;
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public void show(PlanItemEntity entity, BaseActivity activity) {
        this.entity = entity;
        show();
        if (etContent != null) {
            etContent.setText(entity == null ? "" : entity.getPlanName());
            etContent.setSelection(etContent.getText().length());
            KeyBoardUtils.openKeybord(etContent, activity);
        }
        if (tvTitle != null) {
            tvTitle.setText(entity == null ? "添加点位计划" : "编辑点位计划");
        }
        String type = entity != null && entity.getPlanType() != null ? entity.getPlanType() : "";
        switch (type) {
            case Constants.PLAN_TYPE.BUSINESS:
                onTypeChoosed(R.id.tv_business);
                break;
            case Constants.PLAN_TYPE.DIY:
                onTypeChoosed(R.id.tv_diy);
                break;
            case Constants.PLAN_TYPE.INFORMATION:
                onTypeChoosed(R.id.tv_info);
                break;
            default:
                onTypeChoosed(0);
                break;
        }
    }

    public void showType(BaseActivity activity, String type) {
        this.entity = null;
        show();
        if (etContent != null) {
            etContent.setText(entity == null ? "" : entity.getPlanName());
            etContent.setSelection(etContent.getText().length());
            KeyBoardUtils.openKeybord(etContent, activity);
        }
        if (tvTitle != null) {
            tvTitle.setText(entity == null ? "添加点位计划" : "编辑点位计划");
        }
        switch (type) {
            case Constants.PLAN_TYPE.BUSINESS:
                onTypeChoosed(R.id.tv_business);
                break;
            case Constants.PLAN_TYPE.DIY:
                onTypeChoosed(R.id.tv_diy);
                break;
            case Constants.PLAN_TYPE.INFORMATION:
                onTypeChoosed(R.id.tv_info);
                break;
            default:
                onTypeChoosed(0);
                break;
        }
    }

    @OnClick({R.id.img_close, R.id.tv_cancel, R.id.tv_ensure})
    public void onOperateClicked(View view) {
        if (view.getId() == R.id.tv_ensure) {
            String type = getType();
            if (TextUtils.isEmpty(type)) {
                ToastUtils.toast(getContext(), "您还没有选择类型");
                return;
            }
            if (TextUtils.isEmpty(CommonUtil.getEditText(etContent))) {
                ToastUtils.toast(getContext(), "您还没有输入名称");
                return;
            }
            String name = etContent.getText().toString();
            if (entity != null && TextUtils.equals(entity.getPlanName(), name) && TextUtils.equals(entity.getPlanType(), type)) {
                ToastUtils.toast(getContext(), "您还没有修改任何内容");
                return;
            }
            if (onResultListener != null) {
                boolean dismiss = onResultListener.onResultListner(entity, etContent.getText().toString(), type);
                if (dismiss) {
                    dismiss();
                }
            } else {
                dismiss();
            }
        } else {
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        View view = getCurrentFocus();
        if (view instanceof TextView) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mInputMethodManager != null) {
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        }
        super.dismiss();
    }

    public interface OnResultListener {
        boolean onResultListner(PlanItemEntity entity, String name, String type);
    }
}

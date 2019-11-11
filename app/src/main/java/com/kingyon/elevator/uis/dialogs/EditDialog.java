package com.kingyon.elevator.uis.dialogs;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.utils.ToastUtils;

/**
 * Created by GongLi on 2017/12/4.
 * Email：lc824767150@163.com
 */

public class EditDialog<T> extends NormalDialog {
    private T entity;
    private String content;
    private EditText etContent;
    private TextView tvTitle;
    private OnChangeClickListener<T> onChangeClickListener;

    private boolean notVerifyNone;
    private boolean notVerifyChange;

    public EditDialog(Context context, OnChangeClickListener<T> onChangeClickListener) {
        super(context);
        this.onChangeClickListener = onChangeClickListener;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_edit;
    }

    @Override
    protected void initViews() {
        super.initViews();
        setCanceledOnTouchOutside(false);
        etContent = findViewById(R.id.et_content);
        tvTitle = findViewById(R.id.tv_title);
        findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void onEnsure() {
        if (!notVerifyNone && TextUtils.isEmpty(CommonUtil.getEditText(etContent))) {
            ToastUtils.toast(getContext(), "您还没有输入任何内容");
            return;
        }
        if (!notVerifyChange && entity != null && TextUtils.equals(content, etContent.getText().toString())) {
            ToastUtils.toast(getContext(), "您还没有修改任何内容");
            return;
        }
        if (onChangeClickListener != null) {
            if (onChangeClickListener.onChangeClick(entity, etContent.getText().toString())) {
                dismiss();
            }
        } else {
            dismiss();
        }
    }

    @Override
    protected void onCancel() {
        dismiss();
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

    public void show(T entity, BaseActivity activity, String content, String hint) {
        this.entity = entity;
        this.content = content;
        show();
        if (etContent != null) {
            etContent.setHint(hint == null ? "" : hint);
            etContent.setText(content == null ? "" : content);
            etContent.setSelection(etContent.getText().toString().length());
            KeyBoardUtils.openKeybord(etContent, activity);
        }
        if (tvTitle != null) {
            tvTitle.setText(content == null ? "添加" : "编辑");
        }
    }

    public void show(T entity, BaseActivity activity, String content, String hint, boolean notVerifyNone, boolean notVerifyChange) {
        this.notVerifyNone = notVerifyNone;
        this.notVerifyChange = notVerifyChange;
        show(entity, activity, content, hint);
    }

    public void showMultiLines(T entity, BaseActivity activity, String title, String content, String hint, boolean notVerifyNone, boolean notVerifyChange) {
        this.notVerifyNone = notVerifyNone;
        this.notVerifyChange = notVerifyChange;
        show(entity, activity, content, hint);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        if (etContent != null) {
            ViewGroup.LayoutParams layoutParams = etContent.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                etContent.setLayoutParams(layoutParams);
            }
            etContent.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            etContent.setMinLines(4);
            etContent.setGravity(Gravity.TOP);
            etContent.setPadding(ScreenUtil.dp2px(12), ScreenUtil.dp2px(8), ScreenUtil.dp2px(12), ScreenUtil.dp2px(8));
        }
    }

    public void show(T entity, BaseActivity activity, String title, String content, String hint, boolean notVerifyNone, boolean notVerifyChange) {
        this.notVerifyNone = notVerifyNone;
        this.notVerifyChange = notVerifyChange;
        show(entity, activity, content, hint);
        if (tvTitle != null) {
            tvTitle.setText(String.format("%s%s", entity == null ? "添加" : "编辑", title));
        }
    }


    public interface OnChangeClickListener<T> {
        boolean onChangeClick(T entity, String result);
    }
}

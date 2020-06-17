package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.AdTempletEntity;

/**
 * Created by GongLi on 2019/2/13.
 * Email：lc824767150@163.com
 */

public class TemplateTextView extends AutoAdjustSizeEditText {
    private AdTempletEntity.ElementBean element;
    private boolean edit;

    public TemplateTextView(Context context) {
        super(context);
    }

    public TemplateTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addTextChangedListener(textWatcher);
    }

    @Override
    protected void onDetachedFromWindow() {
        removeTextChangedListener(textWatcher);
        super.onDetachedFromWindow();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (element != null) {
                element.setResource(String.valueOf(s));
            }
            if (TextUtils.isEmpty(s)) {
                if (!TextUtils.isEmpty(getHint())) {
                    fitText(getHint().toString(), getWidth(), getHeight());
                }
            }
        }
    };

    public AdTempletEntity.ElementBean getElement() {
        return element;
    }

    public void setElement(AdTempletEntity.ElementBean element) {
        this.element = element;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
        setEnabled(edit);
        setBackgroundResource(edit ? R.drawable.bg_template_text : 0);
        setHint(edit ? "点此输入" : " ");
    }
}

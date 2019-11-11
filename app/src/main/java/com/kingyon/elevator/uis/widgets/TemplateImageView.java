package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.AdTempletEntity;

/**
 * Created by GongLi on 2019/2/13.
 * Email：lc824767150@163.com
 */

public class TemplateImageView extends ImageView {
    private AdTempletEntity.ElementBean element;
    private boolean edit;

    public TemplateImageView(Context context) {
        super(context);
    }

    public TemplateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

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
        setBackgroundResource(edit ? R.drawable.bg_template_image : 0);
    }
}

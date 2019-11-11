package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.AdTempletEntity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ScreenUtil;

/**
 * Created by GongLi on 2019/2/13.
 * Email：lc824767150@163.com
 */

public class TemplateView extends FrameLayout implements View.OnClickListener {

    private AdTempletEntity template;
    private int viewWidth;
    private int viewHeight;
    private boolean edit;
    private ImageView imageView;

    private OnImageChooseClickListener onImageChooseClickListener;

    public TemplateView(@NonNull Context context) {
        super(context);
    }

    public TemplateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initImageView() {
        if (imageView == null) {
            imageView = (ImageView) inflate(getContext(), R.layout.image_template_image, null);
            addView(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            FrameLayout.LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.height = LayoutParams.MATCH_PARENT;
                layoutParams.width = LayoutParams.MATCH_PARENT;
                imageView.setLayoutParams(layoutParams);
            }
            imageView.setOnClickListener(this);
        }
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
        notifyEdit();
    }

    public void setSize(int viewWidth, int viewHeight) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    public AdTempletEntity getTemplate() {
        return template;
    }

    public void setTemplate(AdTempletEntity template) {
        this.template = template;
        initImageView();
        String path = template.getCover();
//        if (path != null && path.startsWith("http")) {
//            GlideUtils.loadImage(getContext(), path, imageView);
//        } else {
        if (!TextUtils.isEmpty(path)) {
            GlideUtils.loadImage(getContext(), path, imageView);
        }
//        }
        notifyElements();
    }

    private void notifyElements() {
        int childCount = getChildCount();
        clearAllTags(childCount);
        if (template != null && template.getElement() != null) {
            for (AdTempletEntity.ElementBean element : template.getElement()) {
                createElementView(element);
            }
        }
    }

    private void createElementView(AdTempletEntity.ElementBean element) {
        if (TextUtils.equals(Constants.TEMPLATE_ELEMENT_TYPE.IMAGE, element.getType())) {

            AutoAdjustSizeEditText autoAdjustSizeEditText = new AutoAdjustSizeEditText(getContext());
            addView(autoAdjustSizeEditText);
            int padding = ScreenUtil.dp2px(8);
            autoAdjustSizeEditText.setMaxTextSize(32);
            autoAdjustSizeEditText.setBackground(null);
            autoAdjustSizeEditText.setPadding(padding, padding, padding, padding);
            autoAdjustSizeEditText.setTextSize(AutoAdjustSizeEditText.DEFAULT_MAX_TEXT_SIZE);
            autoAdjustSizeEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            autoAdjustSizeEditText.setTextColor(0xD0000000);
//            autoAdjustSizeEditText.setTextColor(ContextCompat.getColor(getContext(), R.color.black_secondary));
//            autoAdjustSizeEditText.setTextColor(Color.argb(Color.alpha(Color.BLACK) / 2, Color.red(Color.BLACK), Color.green(Color.BLACK), Color.blue(Color.BLACK)));
            autoAdjustSizeEditText.setGravity(Gravity.CENTER);
            autoAdjustSizeEditText.setEnabled(false);


            TemplateImageView elementImage = new TemplateImageView(getContext());
            addView(elementImage);
            elementImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (!TextUtils.isEmpty(element.getResource())) {
                GlideUtils.loadImage(getContext(), element.getResource(), elementImage);
            }

            elementImage.setElement(element);
//            GlideUtils.loadImage(getContext(), "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1577759731,3108671782&fm=27&gp=0.jpg", elementImage);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) elementImage.getLayoutParams();
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) autoAdjustSizeEditText.getLayoutParams();

            int width = (int) (viewWidth * (element.getRight() - element.getLeft()));
            int height = (int) (viewHeight * (element.getBottom() - element.getTop()));
            if (width < 0) {
                width = 0;
            }
            if (height < 0) {
                height = 0;
            }

            if (layoutParams != null) {
                layoutParams.width = width;
                layoutParams.height = height;
                if (layoutParams1 != null) {
                    layoutParams1.width = width;
                    layoutParams1.height = height;
                }

                layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
                if (layoutParams1 != null) {
                    layoutParams1.gravity = Gravity.TOP | Gravity.LEFT;
                }

                int leftMargin = (int) (viewWidth * element.getLeft());
                int topMargin = (int) (viewHeight * element.getTop());
                layoutParams.setMargins(leftMargin, topMargin, 0, 0);
                if (layoutParams1 != null) {
                    layoutParams1.setMargins(leftMargin, topMargin, 0, 0);
                }

                elementImage.setLayoutParams(layoutParams);
                autoAdjustSizeEditText.setLayoutParams(layoutParams1);
            }
            autoAdjustSizeEditText.postInvalidate();
            autoAdjustSizeEditText.setText(edit ? "选择图片" : " ");
            if (!TextUtils.isEmpty(autoAdjustSizeEditText.getText())) {
                autoAdjustSizeEditText.fitText(autoAdjustSizeEditText.getText().toString(), width, height);
            }
            elementImage.setEdit(edit);
            elementImage.setOnClickListener(this);
            elementImage.postInvalidate();
        } else if (TextUtils.equals(Constants.TEMPLATE_ELEMENT_TYPE.TEXT, element.getType())) {
            TemplateTextView elementText = new TemplateTextView(getContext());
            addView(elementText);
            elementText.setPadding(0, 0, 0, 0);
            elementText.setTextSize(AutoAdjustSizeEditText.DEFAULT_MAX_TEXT_SIZE);
            elementText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            int textColor = Color.parseColor(element.getTextColor());
            elementText.setTextColor(textColor);
//            int hintColor = Color.parseColor(element.getHintColor());
            elementText.setHintTextColor(Color.argb(Color.alpha(textColor) / 2, Color.red(textColor), Color.green(textColor), Color.blue(textColor)));
            elementText.setGravity(Gravity.CENTER);

            elementText.setElement(element);
            elementText.setHint("点此输入");
            elementText.setText(element.getResource() != null ? element.getResource() : "");
//            elementText.setText("请输入文字");

            int width = (int) (viewWidth * (element.getRight() - element.getLeft()));
            int height = (int) (viewHeight * (element.getBottom() - element.getTop()));
            if (width < 0) {
                width = 0;
            }
            if (height < 0) {
                height = 0;
            }

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) elementText.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.width = width;
                layoutParams.height = height;

                layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
                int leftMargin = (int) (viewWidth * element.getLeft());
                int topMargin = (int) (viewHeight * element.getTop());
                layoutParams.setMargins(leftMargin, topMargin, 0, 0);

                elementText.setLayoutParams(layoutParams);
            }
            elementText.setEdit(edit);
            elementText.postInvalidate();

            if (!TextUtils.isEmpty(elementText.getHint())) {
                elementText.fitText(elementText.getHint().toString(), width, height);
            }
            if (!TextUtils.isEmpty(elementText.getText())) {
                elementText.fitText(elementText.getText().toString(), width, height);
            }
        }
    }

    private void clearAllTags(int childCount) {
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view instanceof TemplateImageView || view instanceof TemplateTextView) {
                removeView(view);
            }
        }
    }


    private void notifyEdit() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view instanceof TemplateImageView) {
                ((TemplateImageView) view).setEdit(edit);
            } else if (view instanceof TemplateTextView) {
                ((TemplateTextView) view).setEdit(edit);
            } else if (view instanceof AutoAdjustSizeEditText) {
                ((AutoAdjustSizeEditText) view).setText(edit ? "选择图片" : " ");
            }
        }
    }

    public OnImageChooseClickListener getOnImageChooseClickListener() {
        return onImageChooseClickListener;
    }

    public void setOnImageChooseClickListener(OnImageChooseClickListener onImageChooseClickListener) {
        this.onImageChooseClickListener = onImageChooseClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onImageChooseClickListener != null) {
            if (v instanceof TemplateImageView) {
                onImageChooseClickListener.onImageChoose((TemplateImageView) v);
            } else if (v instanceof ImageView) {
                onImageChooseClickListener.onImageChoose((ImageView) v, template);
            }
        }
    }

    public interface OnImageChooseClickListener {
        void onImageChoose(TemplateImageView templateImageView);

        void onImageChoose(ImageView templateImageView, AdTempletEntity template);
    }
}

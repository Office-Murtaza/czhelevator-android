package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/16.
 * Email：lc824767150@163.com
 */

public class ImageDialog extends Dialog {
    @BindView(R.id.imageView)
    SubsamplingScaleImageView imageView;

    private String fileName;

    public ImageDialog(Context context) {
        super(context, R.style.normal_dialog_white);
        setContentView(R.layout.dialog_image);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
    }

    public void show(String fileName) {
        this.fileName = fileName;
        show();
        getWindow().setWindowAnimations(R.style.BL_PopupAnimation);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);
        int width = options.outWidth;
        imageView.setZoomEnabled(false);
        imageView.setImage(ImageSource.uri(fileName), new ImageViewState(ScreenUtil.getScreenWidth(getContext()) / (float) width, new PointF(0, 0), 0));
    }

    @OnClick(R.id.img_clear)
    public void onViewClicked() {
        dismiss();
    }
}

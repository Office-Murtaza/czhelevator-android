package com.kingyon.elevator.uis.activities.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.AdTempletEntity;
import com.kingyon.elevator.uis.widgets.MaxHeightView;
import com.kingyon.elevator.uis.widgets.ProportionFrameLayout;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ScreenUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by GongLi on 2019/2/13.
 * Email：lc824767150@163.com
 */

public class TemplateDetailsActivity extends BaseSwipeBackActivity {

    @BindView(R.id.img_cover)
    ImageView imgCover;
    @BindView(R.id.pfl_cover)
    ProportionFrameLayout pflCover;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_use)
    TextView tvUse;
    @BindView(R.id.mhf_template)
    MaxHeightView mhfTemplate;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    private boolean splitScreen;
    private AdTempletEntity templet;

    @Override
    protected String getTitleText() {
        templet = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        splitScreen = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_2, false);
        return "详情";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_template_details;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        pflCover.setProporty(splitScreen ? Constants.adImageProperty : Constants.adScreenProperty);
        tvTime.setText(String.format("上传时间：%s", TimeUtil.getYmdTimeItalic(templet.getTime())));
        RxView.clicks(tvUse).throttleFirst(2, TimeUnit.SECONDS).subscribe(aVoid -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(CommonUtil.KEY_VALUE_1, templet);
            bundle.putBoolean(CommonUtil.KEY_VALUE_2, splitScreen);
            startActivityForResult(TemplateEditActivity.class, CommonUtil.REQ_CODE_1, bundle);
        });
//        imgCover.post(new Runnable() {
//            @Override
//            public void run() {
//                GlideUtils.loadImage(TemplateDetailsActivity.this, templet.getCover(), imgCover);
//            }
//        });
        mhfTemplate.post(() -> {
            int height = llContent.getHeight();
            mhfTemplate.setmMaxHeight(height - ScreenUtil.dp2px(52));
        });
        imgCover.post(() -> GlideUtils.loadImage(TemplateDetailsActivity.this, templet.getCover(), imgCover));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode && data != null) {
            boolean cache = data.getBooleanExtra(CommonUtil.KEY_VALUE_2, false);
            if (cache) {
                AdTempletEntity templetEntity = data.getParcelableExtra(CommonUtil.KEY_VALUE_3);
                if (templetEntity != null) {
                    this.templet = templetEntity;
                    imgCover.post(() -> GlideUtils.loadImage(TemplateDetailsActivity.this, templet.getCover(), imgCover));
                }
            } else {
                String templetResultPath = data.getStringExtra(CommonUtil.KEY_VALUE_1);
                if (!TextUtils.isEmpty(templetResultPath) && new File(templetResultPath).exists()) {
                    Intent intent = new Intent();
                    intent.putExtra(CommonUtil.KEY_VALUE_1, templetResultPath);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
    }
}

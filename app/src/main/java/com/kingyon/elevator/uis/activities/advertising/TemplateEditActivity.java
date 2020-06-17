package com.kingyon.elevator.uis.activities.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.AdTempletEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.uis.widgets.AdScreenProportionFrame;
import com.kingyon.elevator.uis.widgets.TemplateImageView;
import com.kingyon.elevator.uis.widgets.TemplateView;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.utils.PictureSelectorUtil;
import com.kingyon.elevator.utils.ViewBitmapUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2019/2/13.
 * Email：lc824767150@163.com
 */

public class TemplateEditActivity extends BaseSwipeBackActivity implements TemplateView.OnImageChooseClickListener {

    @BindView(R.id.pre_v_right)
    TextView preVRight;
    @BindView(R.id.v_template)
    TemplateView vTemplate;
    @BindView(R.id.ad_pfl)
    AdScreenProportionFrame adPfl;

    private boolean splitScreen;
    private AdTempletEntity templet;
    private TemplateImageView templateImageView;
    private ImageView templateBackgroundView;

    @Override
    protected String getTitleText() {
        templet = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
//        templet = AppContent.getInstance().getGson().fromJson("{\n" +
//                "    \"cover\":\"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2963503074,3706615654&fm=111&gp=0.jpg\",\n" +
//                "    \"element\":[\n" +
//                "        {\n" +
//                "            \"bottom\":0.75,\n" +
//                "            \"hintColor\":0,\n" +
//                "            \"left\":0.25,\n" +
//                "            \"objectId\":1,\n" +
//                "            \"resource\":\"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1577759731,3108671782&fm=27&gp=0.jpg\",\n" +
//                "            \"right\":0.75,\n" +
//                "            \"textColor\":0,\n" +
//                "            \"textSize\":0,\n" +
//                "            \"top\":0.25,\n" +
//                "            \"type\":\"IMAGE\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"bottom\":0.2,\n" +
//                "            \"hintColor\":2147483648,\n" +
//                "            \"left\":0.25,\n" +
//                "            \"objectId\":2,\n" +
//                "            \"resource\":\"请输入标题\",\n" +
//                "            \"right\":0.75,\n" +
//                "            \"textColor\":4278190080,\n" +
//                "            \"textSize\":0,\n" +
//                "            \"top\":0.05,\n" +
//                "            \"type\":\"TEXT\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"bottom\":0.95,\n" +
//                "            \"hintColor\":2147483648,\n" +
//                "            \"left\":0.25,\n" +
//                "            \"objectId\":2,\n" +
//                "            \"resource\":\"请输入标题\",\n" +
//                "            \"right\":0.75,\n" +
//                "            \"textColor\":4278190080,\n" +
//                "            \"textSize\":0,\n" +
//                "            \"top\":0.8,\n" +
//                "            \"type\":\"TEXT\"\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"name\":\"广告模板A\",\n" +
//                "    \"objectId\":1,\n" +
//                "    \"time\":1550023392000\n" +
//                "}", AdTempletEntity.class);
//        Logger.i(AppContent.getInstance().getGson().toJson(templet));
        splitScreen = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_2, false);
        return "编辑模板";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_template_edit;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        preVRight.setText("完成");
//        onViewClicked();
        adPfl.setProporty(splitScreen ? Constants.adImageProperty : Constants.adScreenProperty);
        adPfl.post(() -> {
            int width = adPfl.getWidth();
            int height = adPfl.getHeight();
            Logger.d(String.format("templateSize:%s*%s", width, height));
            vTemplate.setSize(width, height);
            vTemplate.setOnImageChooseClickListener(TemplateEditActivity.this);
            vTemplate.setEdit(true);
            vTemplate.setTemplate(templet);
        });
    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
//        preVRight.setSelected(!preVRight.isSelected());
//        preVRight.setText(preVRight.isSelected() ? "完成" : "编辑");
//        vTemplate.setEdit(preVRight.isSelected());

        showProgressDialog("正在保存...");
        Logger.i(AppContent.getInstance().getGson().toJson(vTemplate.getTemplate()));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        vTemplate.setEdit(false);
        Observable.just(ViewBitmapUtils.createBitmapOnHide(adPfl, 0))
                .subscribeOn(Schedulers.newThread())
                .flatMap(bitmap -> {
                    File file;
                    if (bitmap != null) {
                        file = ViewBitmapUtils.saveBitmap(TemplateEditActivity.this, bitmap, templet.getObjectId());
                    } else {
                        file = null;
                    }
                    return Observable.just(file);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomApiCallback<File>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast("生成图片失败");
                        hideProgress();
                        vTemplate.setEdit(true);
                    }

                    @Override
                    public void onNext(File file) {
                        if (file == null || !file.exists()) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        vTemplate.setEdit(true);
                        hideProgress();
                        Intent intent = new Intent();
                        intent.putExtra(CommonUtil.KEY_VALUE_1, file.getAbsolutePath());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }

    @Override
    public void onImageChoose(TemplateImageView templateImageView) {
        KeyBoardUtils.closeKeybord(this);
        this.templateImageView = templateImageView;
        PictureSelectorUtil.showPictureSelectorCropProperty(this, CommonUtil.REQ_CODE_1, templateImageView.getWidth() / (float) templateImageView.getHeight());
    }

    @Override
    public void onImageChoose(ImageView imageView, AdTempletEntity template) {
        KeyBoardUtils.closeKeybord(this);
        this.templateBackgroundView = imageView;
        PictureSelectorUtil.showPictureSelectorCropProperty(this, CommonUtil.REQ_CODE_2, imageView.getWidth() / (float) imageView.getHeight());
    }

    @Override
    public void onBackPressed() {
        AdTempletEntity templetEntity = vTemplate.getTemplate();
        if (templetEntity != null) {
            Intent intent = new Intent();
            intent.putExtra(CommonUtil.KEY_VALUE_2, true);
            intent.putExtra(CommonUtil.KEY_VALUE_3, templetEntity);
            setResult(RESULT_OK, intent);
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case CommonUtil.REQ_CODE_1:
                    ArrayList<String> imagePathes1 = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    if (imagePathes1 != null && imagePathes1.size() > 0) {
                        String imagePath = imagePathes1.get(0);
                        if (!TextUtils.isEmpty(imagePath) && templateImageView != null) {
                            String resource = String.format("file://%s", imagePath);
                            templateImageView.getElement().setResource(resource);
                            GlideUtils.loadImage(this, resource, templateImageView);
                        }
                    }
                    templateImageView = null;
                    break;
                case CommonUtil.REQ_CODE_2:
                    ArrayList<String> imagePathes2 = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    if (imagePathes2 != null && imagePathes2.size() > 0) {
                        String imagePath = imagePathes2.get(0);
                        if (!TextUtils.isEmpty(imagePath) && templateBackgroundView != null) {
                            String resource = String.format("file://%s", imagePath);
                            templet.setCover(resource);
                            GlideUtils.loadImage(this, resource, templateBackgroundView);
                        }
                    }
                    templateBackgroundView = null;
                    break;
            }
        }
    }
}

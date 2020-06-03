package com.kingyon.elevator.uis.activities.advertising;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gerry.scaledelete.DeletedImageScanDialog;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.AvInfoEntity;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;
import com.kingyon.elevator.entities.ImageScan;
import com.kingyon.elevator.entities.LocalMaterialEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.photopicker.MediaData;
import com.kingyon.elevator.uis.activities.PhotoPickerActivity;
import com.kingyon.elevator.uis.dialogs.ImageDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DBUtils;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.utils.MediaUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.leo.afbaselibrary.events.Event;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.GlideUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import me.kareluo.imaging.IMGEditActivity;

import static com.kingyon.elevator.utils.FileUtils.deleteDirWihtFile;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class AdEditActivity extends BaseSwipeBackActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_plan_type)
    TextView tvPlanType;
    @BindView(R.id.img_full_image)
    ImageView imgFullImage;
    @BindView(R.id.fl_full_image)
    FrameLayout flFullImage;
    @BindView(R.id.img_full_video)
    ImageView imgFullVideo;
    @BindView(R.id.fl_full_video)
    FrameLayout flFullVideo;
    @BindView(R.id.img_video_image)
    ImageView imgVideoImage;
    @BindView(R.id.fl_video_image)
    FrameLayout flVideoImage;
    @BindView(R.id.img_video)
    ImageView imgVideo;
    @BindView(R.id.img_video_clear)
    ImageView imgVideoClear;
    @BindView(R.id.tv_video_edit)
    TextView tvVideoEdit;
    //    @BindView(R.id.tv_video_tip)
//    TextView tvVideoTip;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;
    @BindView(R.id.ad_line)
    View adLine;
    @BindView(R.id.img_image)
    ImageView imgImage;
    @BindView(R.id.img_image_clear)
    ImageView imgImageClear;
    @BindView(R.id.tv_image_edit)
    TextView tvImageEdit;
    @BindView(R.id.fl_image)
    FrameLayout flImage;
    @BindView(R.id.tv_ration_full)
    TextView tvRatioFull;
    @BindView(R.id.ll_ratio_split)
    LinearLayout llRatioSplit;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_publish)
    TextView tvPublish;

    private boolean edit;
    private ADEntity entity;
    private String initType;

    private View[] adTypes;
    private View[] adTypeSelects;

    private String videoPath;
    private String imagePath;

    private String videoUrl;
    private String imageUrl;

    private String adScreenType;

    private OptionsPickerView planTypePicker;
    private List<NormalParamEntity> planTypeOptions;

    private boolean istj = false;

    @Override
    protected String getTitleText() {
        edit = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_1, false);
        entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_2);
        initType = getIntent().getStringExtra(CommonUtil.KEY_VALUE_3);
        LogUtils.e(entity,initType,edit);
        return edit ? "编辑广告" : "添加广告";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_ad_edit;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        adTypes = new View[]{flFullImage, flFullVideo, flVideoImage};
        adTypeSelects = new View[]{imgFullImage, imgFullVideo, imgVideoImage};
        if (!TextUtils.isEmpty(initType)) {
            if (TextUtils.equals(Constants.PLAN_TYPE.BUSINESS, initType)) {
                choosedType(new NormalParamEntity(Constants.PLAN_TYPE.BUSINESS, "商业"));
//                tvPlanType.setEnabled(false);
            } else if (TextUtils.equals(Constants.PLAN_TYPE.DIY, initType)) {
                choosedType(new NormalParamEntity(Constants.PLAN_TYPE.DIY, "DIY"));
//                tvPlanType.setEnabled(false);
            }
        }
        if (edit) {
            if (entity != null) {
                etName.setText(entity.getTitle() != null ? entity.getTitle() : "");
                etName.setSelection(etName.getText().length());
                tvPlanType.setText(FormatUtils.getInstance().getPlanType(entity.getPlanType()));
                tvPlanType.setTag(entity.getPlanType());
                adScreenType = entity.getScreenType();
//                tvPlanType.setEnabled(false);
                switch (entity.getScreenType()) {
                    case Constants.AD_SCREEN_TYPE.FULL_IMAGE:
                        onAdTypeClick(R.id.fl_full_image);
                        flImage.setTag(entity.getImageUrl());
                        GlideUtils.loadImage(this, entity.getImageUrl(), imgImage);
                        imgImageClear.setVisibility(View.VISIBLE);
                        tvImageEdit.setVisibility(View.VISIBLE);
                        break;
                    case Constants.AD_SCREEN_TYPE.FULL_VIDEO:
                        onAdTypeClick(R.id.fl_full_video);
                        flVideo.setTag(entity.getVideoUrl());
                        GlideUtils.loadVideoFrame(this, entity.getVideoUrl(), imgVideo);
                        imgVideoClear.setVisibility(View.VISIBLE);
                        tvVideoEdit.setVisibility(View.GONE);
//                        tvVideoTip.setVisibility(View.VISIBLE);
                        break;
                    case Constants.AD_SCREEN_TYPE.VIDEO_IMAGE:
                        onAdTypeClick(R.id.fl_video_image);
                        flVideo.setTag(entity.getVideoUrl());
                        GlideUtils.loadVideoFrame(this, entity.getVideoUrl(), imgVideo);
                        imgVideoClear.setVisibility(View.VISIBLE);
                        tvVideoEdit.setVisibility(View.GONE);
//                        tvVideoTip.setVisibility(View.VISIBLE);
                        flImage.setTag(entity.getImageUrl());
                        GlideUtils.loadImage(this, entity.getImageUrl(), imgImage);
                        imgImageClear.setVisibility(View.VISIBLE);
                        tvImageEdit.setVisibility(View.VISIBLE);
                        break;
                    default:
                        flImage.setVisibility(View.GONE);
                        break;
                }
            }
            tvVideoEdit.setVisibility(View.GONE);
        } else {
            onAdTypeClick(R.id.fl_full_image);
        }
    }

    @OnClick({R.id.tv_plan_type, R.id.fl_video, R.id.img_video_clear, R.id.tv_video_edit, R.id.fl_image, R.id.img_image_clear, R.id.tv_image_edit, R.id.tv_save, R.id.tv_publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_plan_type:
                if (edit){
                    showToast("当前页面不可操作广告类型");
                }else {
                    showPlanTypePicker();
                }
                break;
            case R.id.fl_video:
                String planType1 = (String) tvPlanType.getTag();
                if (TextUtils.isEmpty(planType1)) {
                    showToast("请先选择广告类型");
                    return;
                }
                String video = (String) flVideo.getTag();
                Bundle videoBundle = new Bundle();
                istj = true;
                if (TextUtils.isEmpty(video)) {
                    jumpToVideoChoose();
                } else {
                    String videoPath = flVideo.getTag() != null ? new String((String) flVideo.getTag()) : null;
                    videoBundle.putString(CommonUtil.KEY_VALUE_1, videoPath);
                    startActivity(NetVideoPlayActivity.class, videoBundle);
                }
                break;
            case R.id.img_video_clear:
                istj = true;
                adScreenType = null;
                flVideo.setTag(null);
                imgVideo.setImageDrawable(null);
                imgVideoClear.setVisibility(View.GONE);
                tvVideoEdit.setVisibility(View.GONE);
//                tvVideoTip.setVisibility(View.GONE);

                File file = new File("/sdcard/PDD/");
                deleteDirWihtFile(file);
                break;
            case R.id.tv_video_edit:

                videoEdit();
                break;
            case R.id.fl_image:
                String planType2 = (String) tvPlanType.getTag();
                if (TextUtils.isEmpty(planType2)) {
                    showToast("请先选择广告类型");
                    return;
                }

                String image = (String) flImage.getTag();
                if (TextUtils.isEmpty(image)) {
                    jumpToImageChoose();
                } else {
                    if (image.startsWith("http")) {
                        DeletedImageScanDialog deletedImageScanDialog = new DeletedImageScanDialog(this
                                , new ImageScan(image), null);
                        deletedImageScanDialog.showOne();
                    } else {
                        ImageDialog dialog = new ImageDialog(this);
                        dialog.show(image);
                    }
                }
                break;
            case R.id.img_image_clear:
                istj = true;
                adScreenType = null;
                flImage.setTag(null);
                imgImage.setImageDrawable(null);
                imgImageClear.setVisibility(View.GONE);
                tvImageEdit.setVisibility(View.GONE);
                break;
            case R.id.tv_image_edit:
                istj = true;
                imageEdit();
                break;
            case R.id.tv_save:
                break;
            case R.id.tv_publish:
                onPublishClick();
                break;
        }
    }

    private void jumpToImageChoose() {
//        Bundle imageBundle = new Bundle();
//        imageBundle.putBoolean(CommonUtil.KEY_VALUE_1, flVideoImage.isSelected());
//        startActivityForResult(ImageChooseActivity.class, CommonUtil.REQ_CODE_2, imageBundle);
        MyActivityUtils.goPhotoPickerActivity(this, Constants.FROM_TYPE_TO_SELECT_MEDIA.MYADSELECT, (String) tvPlanType.getTag());
    }

    private void imageEdit() {
        String imageLink = (String) flImage.getTag();
        if (TextUtils.isEmpty(imageLink)) {
            jumpToImageChoose();
        } else {
            if (imageLink.startsWith("http")) {
                LocalMaterialEntity localMateria = DBUtils.getInstance().getLocalMateria(imageLink);
                if (localMateria != null && new File(localMateria.getPath()).exists()) {
                    jumpToImageEdit(localMateria.getPath());
                } else {
                    jumpToImageChoose();
                }
            } else {
                File file = new File(imageLink);
                if (file.exists()) {
                    jumpToImageEdit(imageLink);
                } else {
                    jumpToImageChoose();
                }
            }
        }
    }

    private void jumpToImageEdit(String localPath) {
        File imageFile = new File(localPath);
        //判断是否是AndroidN以及更高的版本
//        Uri imageUri;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            imageUri = FileProvider.getUriForFile(this, AFUtil.getAppProcessName(this) + ".provider", imageFile);
//        } else {
//            imageUri = Uri.fromFile(imageFile);
//        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(IMGEditActivity.EXTRA_IMAGE_URI, Uri.fromFile(imageFile));
        bundle.putString(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, new File(getExternalCacheDir(), String.format("%s.jpg", UUID.randomUUID().toString())).getAbsolutePath());
        startActivityForResult(IMGEditActivity.class, CommonUtil.REQ_CODE_4, bundle);
    }

    private void videoEdit() {
        String videoLink = (String) flVideo.getTag();
        if (TextUtils.isEmpty(videoLink)) {
            jumpToVideoChoose();
        } else {
            if (videoLink.startsWith("http")) {
                LocalMaterialEntity localMateria = DBUtils.getInstance().getLocalMateria(videoLink);
                if (localMateria != null && new File(localMateria.getPath()).exists()) {
                    jumpToVideoEdit(localMateria.getPath());
                } else {
                    jumpToVideoChoose();
                }
            } else {
                File file = new File(videoLink);
                if (file.exists()) {
                    jumpToVideoEdit(videoLink);
                } else {
                    jumpToVideoChoose();
                }
            }
        }
    }

    private void jumpToVideoEdit(String localPath) {
        Bundle bundle = new Bundle();
//        long videoDuration = getVideoDuration();
//        if (MediaUtils.getInstance().getVideoDuring(localPath) > videoDuration) {
//            bundle.putString(VideoTrimActivity.VIDEO_FILE_PATH, localPath);
//            bundle.putLong(VideoTrimActivity.VIDEO_TRIM_DURATION, videoDuration);
//            startActivityForResult(VideoTrimActivity.class, CommonUtil.REQ_CODE_5, bundle);
//        } else {
//            bundle.putString(VideoEditActivity.MP4_PATH, localPath);
//            bundle.putInt(VideoEditActivity.PREVIOUS_ORIENTATION, 1);
//            bundle.putInt(VideoRecordActivity.RECORD_REQUEST, VideoRecordActivity.RECORD_EDIT_REQUEST);
//            startActivityForResult(VideoEditActivity.class, VideoRecordActivity.RECORD_EDIT_REQUEST, bundle);
//        }
    }

    private long getVideoDuration() {
        return (TextUtils.equals(Constants.PLAN_TYPE.DIY, (String) tvPlanType.getTag()) ? 61 : 16) * 1000 - 1;
    }

    private void jumpToVideoChoose() {
//        Bundle videoEditBundle = new Bundle();
//        videoEditBundle.putBoolean(CommonUtil.KEY_VALUE_1, flVideoImage.isSelected());
//        videoEditBundle.putString(CommonUtil.KEY_VALUE_2, (String) tvPlanType.getTag());
//        startActivityForResult(VideoChooseActivity.class, CommonUtil.REQ_CODE_1, videoEditBundle);
        MyActivityUtils.goPhotoPickerActivity(this, Constants.FROM_TYPE_TO_SELECT_MEDIA.MYADSELECT, (String) tvPlanType.getTag());
    }

    private void showPlanTypePicker() {
        if (planTypePicker == null || planTypeOptions == null) {
            planTypeOptions = new ArrayList<>();
            planTypeOptions.add(new NormalParamEntity(Constants.PLAN_TYPE.BUSINESS, "商业"));
            planTypeOptions.add(new NormalParamEntity(Constants.PLAN_TYPE.DIY, "DIY"));
            planTypePicker = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    if (planTypeOptions == null || planTypeOptions.size() <= options1) {
                        LogUtils.e("11111111111111");
                        return;

                    }
                    choosedType(planTypeOptions.get(options1));
                }
            }).setCyclic(false, false, false).build();
            planTypePicker.setPicker(planTypeOptions);
        }
        LogUtils.e("2222222222222");
        KeyBoardUtils.closeKeybord(this);
        planTypePicker.show();
    }

    private void choosedType(NormalParamEntity entity) {
        String oldPlanType = (String) tvPlanType.getTag();
        if (oldPlanType != null && !TextUtils.equals(oldPlanType, entity.getType())) {
//            imgVideo.setImageDrawable(null);
//            imgVideoClear.setVisibility(View.GONE);
//            tvVideoEdit.setVisibility(View.GONE);
//            tvVideoTip.setVisibility(View.GONE);

//            flVideo.setBackgroundResource(R.drawable.bg_ad_full);
            flVideo.setTag(null);
            imgVideo.setImageDrawable(null);

            imgVideoClear.setVisibility(View.GONE);
            tvVideoEdit.setVisibility(View.GONE);
//            tvVideoTip.setVisibility(View.GONE);
        }
        tvPlanType.setTag(entity.getType());
        tvPlanType.setText(entity.getName());
    }

    private void onPublishClick() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
            showToast("请输入广告名称");
            return;
        }
        if (TextUtils.isEmpty((String) tvPlanType.getTag())) {
            showToast("请选择广告类型");
            return;
        }
        if (TextUtils.isEmpty(adScreenType)) {
            showToast("没有广告资源");
            return;
        }
        if (!istj){
            showToast("当前广告内容未做修改");
            return;
        }
        videoPath = flVideo.getTag() != null ? new String((String) flVideo.getTag()) : null;
        imagePath = flImage.getTag() != null ? new String((String) flImage.getTag()) : null;
        videoUrl = videoPath;
        imageUrl = imagePath;
        if (TextUtils.equals(Constants.AD_SCREEN_TYPE.FULL_IMAGE, adScreenType)) {
            if (TextUtils.isEmpty(imagePath)) {
                showToast("请选择广告图片");
                return;
            }
            videoPath = null;
            videoUrl = null;
        }

        if (TextUtils.equals(Constants.AD_SCREEN_TYPE.FULL_VIDEO, adScreenType)) {
            if (TextUtils.isEmpty(videoPath)) {
                showToast("请选择广告视频");
                return;
            }
//            String planTypeTag = (String) tvPlanType.getTag();
//            if (MediaUtils.getInstance().getVideoDuring(videoPath) > getVideoDuration()) {
//                if (TextUtils.equals(Constants.PLAN_TYPE.BUSINESS, planTypeTag)) {
//                    showToast("视频时长大于15s，请编辑");
//                } else if (TextUtils.equals(Constants.PLAN_TYPE.DIY, planTypeTag)) {
//                    showToast("视频时长大于60s，请编辑");
//                } else {
//                    showToast("视频时长过长，请编辑");
//                }
//                return;
//            }
            imagePath = null;
            imageUrl = null;
        }

//        if (TextUtils.equals(Constants.AD_SCREEN_TYPE.VIDEO_IMAGE, adScreenType)) {
//            if (TextUtils.isEmpty(videoPath)) {
//                showToast("请选择广告视频");
//                return;
//            }
//            String planTypeTag = (String) tvPlanType.getTag();
//            if (MediaUtils.getInstance().getVideoDuring(videoPath) > getVideoDuration()) {
//                if (TextUtils.equals(Constants.PLAN_TYPE.BUSINESS, planTypeTag)) {
//                    showToast("视频时长大于15s，请编辑");
//                } else if (TextUtils.equals(Constants.PLAN_TYPE.DIY, planTypeTag)) {
//                    showToast("视频时长大于60s，请编辑");
//                } else {
//                    showToast("视频时长过长，请编辑");
//                }
//                return;
//            }
//            if (TextUtils.isEmpty(imagePath)) {
//                showToast("请选择广告图片");
//                return;
//            }
//        }
        tvPublish.setEnabled(false);
        showProgressDialog(getString(R.string.wait));
        publishVideo();
    }

    private void publishVideo() {
        if (videoPath == null) {
            publishImage(null);
        } else if (videoPath.startsWith("http")) {
            checkVideoCodecName(null);
        } else {
            showProgressDialog("视频上传中...");
            NetService.getInstance().uploadFile(this, new File(videoPath), new NetUpload.OnUploadCompletedListener() {
                @Override
                public void uploadSuccess(List<String> images,List<String> hash, JSONObject response) {
                    LogUtils.e(images,hash,response);
                    if (images != null && images.size() > 0) {
                        videoUrl = images.get(0);
                        checkVideoCodecName(hash.get(0));
                        File file = new File("/sdcard/PDD/");
                        deleteDirWihtFile(file);
                    } else {
                        hideProgress();
                        tvPublish.setEnabled(true);
                        showToast("上传视频失败");
                    }
                }

                @Override
                public void uploadFailed(ApiException ex) {
                    hideProgress();
                    tvPublish.setEnabled(true);
                    showToast("上传视频失败");
                }
            }, false);
        }
    }

    private void checkVideoCodecName(String hash) {
        publishImage(hash);
    }

    private void publishImage(String hash) {
        if (imagePath == null || imagePath.startsWith("http")) {
            publishAd(hash);
        } else {
            showProgressDialog("图片上传中...");
            NetService.getInstance().uploadFile(this, new File(imagePath), new NetUpload.OnUploadCompletedListener() {
                @Override
                public void uploadSuccess(List<String> images,List<String> hash,JSONObject response) {
                    LogUtils.e(images,hash,response);
                    if (images != null && images.size() > 0) {
                        imageUrl = images.get(0);

                        publishAd(response.optString("hash"));
                    } else {
                        hideProgress();
                        tvPublish.setEnabled(true);
                        showToast("上传图片失败");
                    }
                }

                @Override
                public void uploadFailed(ApiException ex) {
                    hideProgress();
                    tvPublish.setEnabled(true);
                    showToast("上传图片失败");
                }
            }, false);
        }
    }

    private void publishAd(String hashCode) {
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().createOrEidtAd(edit ? entity.getObjctId() : null, edit && entity.isOnlyInfo()
                , (String) tvPlanType.getTag(), adScreenType, etName.getText().toString(), videoUrl, imageUrl, null, videoPath, imagePath,hashCode)
                .compose(this.<ADEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<ADEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                        tvPublish.setEnabled(true);
                    }

                    @Override
                    public void onNext(ADEntity entity) {
                        tvPublish.setEnabled(true);
                        showToast("保存成功");
                        hideProgress();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    private String getAdType() {
        String result;
        if (adTypeSelects == null || adTypes == null) {
            result = null;
        } else {
            int selectedViewId = 0;
            for (View typeView : adTypes) {
                if (typeView.isSelected()) {
                    selectedViewId = typeView.getId();
                    break;
                }
            }
            switch (selectedViewId) {
                case R.id.fl_full_image:
                    result = Constants.AD_SCREEN_TYPE.FULL_IMAGE;
                    break;
                case R.id.fl_full_video:
                    result = Constants.AD_SCREEN_TYPE.FULL_VIDEO;
                    break;
                case R.id.fl_video_image:
                    result = Constants.AD_SCREEN_TYPE.VIDEO_IMAGE;
                    break;
                default:
                    result = null;
                    break;
            }
        }
        return result;
    }

    @OnClick({R.id.fl_full_image, R.id.fl_full_video, R.id.fl_video_image})
    public void onAdTypeClicked(View view) {
        if (!view.isSelected()) {
            onAdTypeClick(view.getId());
        }
    }

    private void onAdTypeClick(int viewId) {
        if (adTypeSelects == null || adTypes == null) {
            return;
        }
        for (int i = 0; i < adTypes.length; i++) {
            View typeView = adTypes[i];
            View typeSelectView = adTypeSelects[i];

            boolean selected = typeView.getId() == viewId;
            typeView.setSelected(selected);
            typeSelectView.setSelected(selected);
        }
        switch (viewId) {
            case R.id.fl_full_image:
                flImage.setVisibility(View.VISIBLE);
                flVideo.setVisibility(View.GONE);

                flImage.setBackgroundResource(R.drawable.bg_ad_full);
                flImage.setTag(null);
                imgImage.setImageDrawable(null);

                imgImageClear.setVisibility(View.GONE);
                tvImageEdit.setVisibility(View.GONE);
                adLine.setVisibility(View.GONE);
                showRatioTip(true);
                break;
            case R.id.fl_full_video:
                flVideo.setVisibility(View.VISIBLE);
                flImage.setVisibility(View.GONE);

                flVideo.setBackgroundResource(R.drawable.bg_ad_full);
                flVideo.setTag(null);
                imgVideo.setImageDrawable(null);

                imgVideoClear.setVisibility(View.GONE);
                tvVideoEdit.setVisibility(View.GONE);
//                tvVideoTip.setVisibility(View.GONE);
                adLine.setVisibility(View.GONE);

                showRatioTip(true);
                break;
            case R.id.fl_video_image:
                flVideo.setVisibility(View.VISIBLE);
                flImage.setVisibility(View.VISIBLE);

                flVideo.setBackgroundResource(R.drawable.bg_ad_video);
                flVideo.setTag(null);
                imgVideo.setImageDrawable(null);

                flImage.setBackgroundResource(R.drawable.bg_ad_image);
                flImage.setTag(null);
                imgImage.setImageDrawable(null);

                imgImageClear.setVisibility(View.GONE);
                tvImageEdit.setVisibility(View.GONE);
                imgVideoClear.setVisibility(View.GONE);
                tvVideoEdit.setVisibility(View.GONE);
//                tvVideoTip.setVisibility(View.GONE);
                adLine.setVisibility(View.VISIBLE);
                showRatioTip(false);
                break;
            default:
                flVideo.setVisibility(View.GONE);
                flImage.setVisibility(View.GONE);
                adLine.setVisibility(View.GONE);
                showRatioTip(false);
                break;
        }
    }

    private void showRatioTip(boolean fullType) {
        tvRatioFull.setVisibility(fullType ? View.VISIBLE : View.GONE);
        llRatioSplit.setVisibility(fullType ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            istj = true;
            switch (requestCode) {
                case CommonUtil.REQ_CODE_1:
                    String videoPath = data.getStringExtra(CommonUtil.KEY_VALUE_1);
                    if (!TextUtils.isEmpty(videoPath)) {
                        onVideoPathResult(videoPath);
                    }
                    break;
                case CommonUtil.REQ_CODE_2:
                    String imagePath = data.getStringExtra(CommonUtil.KEY_VALUE_1);
                    if (!TextUtils.isEmpty(imagePath)) {
                        onImagePathResult(imagePath);
                    }
                    break;
                case CommonUtil.REQ_CODE_4:
                    String savedPath = data.getStringExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH);
                    if (!TextUtils.isEmpty(savedPath) && new File(savedPath).exists()) {
                        onImagePathResult(savedPath);
                    }
                    break;
//                case VideoRecordActivity.RECORD_EDIT_REQUEST:
//                case CommonUtil.REQ_CODE_5:
//                    String recordPath = data.getStringExtra(VideoRecordActivity.RECORD_RESULT);
//                    if (!TextUtils.isEmpty(recordPath)) {
//                        onVideoPathResult(recordPath);
//                    }
                //  break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventHandler(EventBusObjectEntity eventBusObjectEntity) {
        if (eventBusObjectEntity.getEventCode() == EventBusConstants.VideoCropSuccessResult) {
            //这里返回的是裁剪过后的视频 时长要么为15s要么为60s
            istj = true;
            selectIsVideoHandlerView();
            String videoPath = (String) eventBusObjectEntity.getData();
            flVideo.setTag(videoPath);
            GlideUtils.loadLocalFrame(this, videoPath, imgVideo);
            imgVideoClear.setVisibility(View.VISIBLE);
            tvVideoEdit.setVisibility(View.GONE);
            adScreenType = Constants.AD_SCREEN_TYPE.FULL_VIDEO;
        }
        if (eventBusObjectEntity.getEventCode() == EventBusConstants.VideoOrImageSelectSuccess) {
            LogUtils.d("视频或图片选择成功--------------");
            istj = true;
            if (eventBusObjectEntity.getData() != null) {
                if (eventBusObjectEntity.getData() instanceof String) {
                    //返回的是图片
                    selectIsImage();
                    String imgPath = (String) eventBusObjectEntity.getData();
                    flImage.setTag(imgPath);
                    GlideUtils.loadImage(this, imgPath, imgImage);
                    imgImageClear.setVisibility(View.VISIBLE);
                    tvImageEdit.setVisibility(View.VISIBLE);
                    adScreenType = Constants.AD_SCREEN_TYPE.FULL_IMAGE;
                }
                if (eventBusObjectEntity.getData() instanceof MediaData) {
                    //返回的是视频
                    selectIsVideoHandlerView();
                    adScreenType = Constants.AD_SCREEN_TYPE.FULL_VIDEO;
                    MediaData mediaData = (MediaData) eventBusObjectEntity.getData();
                    LogUtils.d("视频或图片选择成功--------------", mediaData.getOriginalPath());
                    flVideo.setTag(mediaData.getOriginalPath());
                    GlideUtils.loadLocalFrame(this, mediaData.getOriginalPath(), imgVideo);
                    imgVideoClear.setVisibility(View.VISIBLE);
                    tvVideoEdit.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 选择的是视频时处理方式
     */
    private void selectIsVideoHandlerView() {
        flVideo.setVisibility(View.VISIBLE);
        flImage.setVisibility(View.GONE);

        flVideo.setBackgroundResource(R.drawable.bg_ad_full);
        flVideo.setTag(null);
        imgVideo.setImageDrawable(null);

        imgVideoClear.setVisibility(View.GONE);
        tvVideoEdit.setVisibility(View.GONE);
        adLine.setVisibility(View.GONE);
        showRatioTip(true);
    }

    /**
     * 选择的是图片时界面的处理
     */
    private void selectIsImage() {
        flImage.setVisibility(View.VISIBLE);
        flVideo.setVisibility(View.GONE);

        flImage.setBackgroundResource(R.drawable.bg_ad_full);
        flImage.setTag(null);
        imgImage.setImageDrawable(null);

        imgImageClear.setVisibility(View.GONE);
        tvImageEdit.setVisibility(View.GONE);
        adLine.setVisibility(View.GONE);
        showRatioTip(true);
    }

    private void onImagePathResult(String imagePath) {
        flImage.setTag(imagePath);
        if (imagePath.startsWith("http")) {
            GlideUtils.loadImage(this, imagePath, imgImage);
        } else {
            GlideUtils.loadImage(this, imagePath, imgImage);
        }
        imgImageClear.setVisibility(View.VISIBLE);
        tvImageEdit.setVisibility(View.VISIBLE);
    }

    private void onVideoPathResult(String videoPath) {
        flVideo.setTag(videoPath);
        if (videoPath.startsWith("http")) {
            GlideUtils.loadVideoFrame(this, videoPath, imgVideo);
        } else {
            GlideUtils.loadLocalFrame(this, videoPath, imgVideo);
        }
        imgVideoClear.setVisibility(View.VISIBLE);
        tvVideoEdit.setVisibility(View.VISIBLE);
//        tvVideoTip.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

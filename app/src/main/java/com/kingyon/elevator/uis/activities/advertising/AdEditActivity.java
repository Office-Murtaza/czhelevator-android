package com.kingyon.elevator.uis.activities.advertising;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.photopicker.MediaData;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.JumpUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.TimeUtil;
import com.kingyon.elevator.utils.utilstwo.DownloadManager;
import com.kingyon.elevator.videocrop.EditVideoActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.muzhi.camerasdk.MessageWrap;
import com.muzhi.camerasdk.model.CameraSdkParameterInfo;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.kareluo.imaging.IMGEditActivity;

import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_CODE;
import static com.czh.myversiontwo.utils.Constance.PREVIEW_AD_ACTIVITY;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 *
 * czh 修改广告
 */

public class AdEditActivity extends BaseSwipeBackActivity {


    @BindView(R.id.pre_v_back)
    ImageView preVBack;
    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.head_root)
    RelativeLayout headRoot;
    @BindView(R.id.tv_time_satr)
    TextView tvTimeSatr;
    @BindView(R.id.tv_ad_type)
    TextView tvAdType;
    @BindView(R.id.tv_total_day)
    TextView tvTotalDay;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.ll_top1)
    LinearLayout llTop1;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.tv_top)
    TextView tvTop;
    @BindView(R.id.img_delete1)
    ImageView imgDelete;
    @BindView(R.id.img_updata)
    ImageView imgUpdata;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.img_image)
    ImageView imgImage;
    @BindView(R.id.rl_ad)
    RelativeLayout rlAd;
    @BindView(R.id.tv_text_updata)
    TextView tvTextUpdata;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    private boolean edit;
    private OrderDetailsEntity entity;
    private String initType;
    SimpleDateFormat simpleDateFormat;
    private boolean isDelete = true;
    private String pathData = "";
    private int isHttp = 1;
    private boolean isImage;
    private CameraSdkParameterInfo mCameraSdkParameterInfo=new CameraSdkParameterInfo();
    ArrayList<String> listPath = new ArrayList<>();
    @Override
    protected String getTitleText() {
        edit = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_1, false);
        entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_2);
        initType = getIntent().getStringExtra(CommonUtil.KEY_VALUE_3);
        LogUtils.e(entity.toString(), initType, edit);
        return "修改广告";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_ad_edit;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        imgDelete.bringToFront();
        tvTimeSatr.setText(TimeUtil.times(entity.getAdStartTime()) + TimeUtil.getWeek(entity.getAdStartTime()/1000));
        tvEnd.setText(TimeUtil.times(entity.getAdEndTime()) + TimeUtil.getWeek(entity.getAdEndTime()/1000));
        etContent.setText(entity.getAdvertising().getName());
        tvNumber.setText(entity.getAdvertising().getName().length()+"/20");
        tvTotalDay.setText(TimeUtil.getDayNumber((entity.getAdEndTime()),(entity.getAdStartTime())));
        adShow(entity.getAdvertising());
        switch (entity.getOrderType()) {
            case "BUSINESS":
                tvAdType.setText("商业广告");
                rlAd.setVisibility(View.VISIBLE);
                break;
            case "DIY":
                tvAdType.setText("DIY广告");
                rlAd.setVisibility(View.VISIBLE);
                break;
            case "INFORMATION":
                tvAdType.setText("便民信息");
                rlAd.setVisibility(View.GONE);
                break;
        }
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                tvNumber.setText(s.length()+"/20");
            }
        });


    }

    private void adShow(ADEntity advertising) {
        switch (advertising.getTypeAdvertise()) {
            case Constants.AD_SCREEN_TYPE.FULL_VIDEO:
                /*全屏视频urlVideo*/
                GlideUtils.loadVideoFrame(this, advertising.getUrlVideo(), imgImage);
                break;
            case Constants.AD_SCREEN_TYPE.FULL_IMAGE:
                /*全屏图片*/
                GlideUtils.loadImage(this, advertising.getUrlImate(), imgImage);
                break;
            case Constants.AD_SCREEN_TYPE.VIDEO_IMAGE:
                /*上视频下图片*/
                break;
            case Constants.AD_SCREEN_TYPE.INFORMATION:
                /*便民信息*/
                rlAd.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.pre_v_back, R.id.img_image, R.id.img_delete1, R.id.img_updata, R.id.tv_next, R.id.tv_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_back:
                finish();
                break;
            case R.id.img_image:
                if (isDelete) {
                    JumpUtils.getInstance().jumpToAdPreview(this, entity.getAdvertising(), "order");
                }else {
                    if (isHttp==2){
                        ActivityUtils.setActivity(PREVIEW_AD_ACTIVITY,"type","image","path",pathData);
                    }else {
                        ActivityUtils.setActivity(PREVIEW_AD_ACTIVITY,"type","video","path",pathData);
                    }
                }
                break;
            case R.id.img_delete1:
                /*删除*/
                LogUtils.e("------------");
                isDelete = false;
                tvTextUpdata.setVisibility(View.VISIBLE);
                imgUpdata.setVisibility(View.VISIBLE);
                imgDelete.setVisibility(View.GONE);
                imgImage.setImageBitmap(null);
                tvEdit.setVisibility(View.GONE);
                imgImage.setClickable(false);
                break;
            case R.id.img_updata:
                imgImage.setClickable(true);
                /*添加视频图片*/
                MyActivityUtils.goPhotoPickerActivity(this, Constants.FROM_TYPE_TO_SELECT_MEDIA.MYADSELECT, entity.getOrderType());
                break;
            case R.id.tv_next:
                /*保存*/
                httpUpDataAd();
                break;
            case R.id.tv_edit:
                /*编辑*/
                pathEdit();
                break;
        }
    }

    private void httpUpDataAd() {
        if (etContent.getText().toString().isEmpty()){
            showToast("广告名称为空");
        }else if (pathData.isEmpty()){
            showToast("内容未修改");
        }else {
            if (isHttp==2){
                /*图篇*/
                uploadAdVideoOrImg(pathData,entity.getAdvertising().getPlanType(),"FULLIMAGE",etContent.getText().toString());
                LogUtils.e("---FULLIMAGE----");
            }else {
                /*视频*/
                LogUtils.e("++++++FULLVIDEO++++++");
                uploadAdVideoOrImg(pathData,entity.getAdvertising().getPlanType(),"FULLVIDEO",etContent.getText().toString());
            }
        }
    }

    private void pathEdit() {
        switch (isHttp){
            case 1:
                /*http内容*/
                switch (entity.getAdvertising().getTypeAdvertise()){
                    case Constants.AD_SCREEN_TYPE.FULL_VIDEO:
                        /*全屏视频urlVideo*/
                        isImage = false;
                        fileDown(entity.getAdvertising().getUrlVideo(),"ad.mp4");
                        break;
                    case Constants.AD_SCREEN_TYPE.FULL_IMAGE:
                        /*全屏图片*/
                        isImage = true;
                        fileDown(entity.getAdvertising().getUrlImate(),"ad.png");
                        break;
                }
                break;
            case 2:
                /*本地图片*/
                listPath.clear();
                listPath.add(pathData);
                mCameraSdkParameterInfo.setFilter_image(true);
                mCameraSdkParameterInfo.setSingle_mode(false);
                mCameraSdkParameterInfo.setShow_camera(false);
                mCameraSdkParameterInfo.setMax_image(6);
                mCameraSdkParameterInfo.setCroper_image(false);
                mCameraSdkParameterInfo.setImage_list(listPath);
                Intent intent = new Intent();
                intent.setClassName(getApplication(), "com.muzhi.camerasdk.FilterImageActivity");
                Bundle b=new Bundle();
                b.putSerializable(CameraSdkParameterInfo.EXTRA_PARAMETER, mCameraSdkParameterInfo);
                intent.putExtras(b);
                startActivityForResult(intent, CameraSdkParameterInfo.TAKE_PICTURE_PREVIEW);

                break;
            case 3:
                /*本地视频*/
                Intent intent1 = new Intent(AdEditActivity.this, EditVideoActivity.class);
                intent1.putExtra("path", pathData);
                intent1.putExtra("fromType",Constants.FROM_TYPE_TO_SELECT_MEDIA.MYADSELECT);
                startActivity(intent1);
                break;
        }


    }

    private void fileDown(String urlVideo,String name) {
        showProgressDialog(getString(R.string.wait),false);
        DownloadManager.download(urlVideo, getExternalCacheDir() + File.separator + "/PDD/",
                name, new DownloadManager.OnDownloadListener() {
            @Override
            public void onSuccess(File file) {
                hideProgress();
                LogUtils.e(file.toString(),file);
                if (isImage){
                    /*图片*/
                    listPath.clear();
                    listPath.add(file.toString());
                    mCameraSdkParameterInfo.setFilter_image(true);
                    mCameraSdkParameterInfo.setSingle_mode(false);
                    mCameraSdkParameterInfo.setShow_camera(false);
                    mCameraSdkParameterInfo.setMax_image(6);
                    mCameraSdkParameterInfo.setCroper_image(false);
                    mCameraSdkParameterInfo.setImage_list(listPath);
                    Intent intent = new Intent();
                    intent.setClassName(getApplication(), "com.muzhi.camerasdk.FilterImageActivity");
                    Bundle b=new Bundle();
                    b.putSerializable(CameraSdkParameterInfo.EXTRA_PARAMETER, mCameraSdkParameterInfo);
                    intent.putExtras(b);
                    startActivityForResult(intent, CameraSdkParameterInfo.TAKE_PICTURE_PREVIEW);
                }else {
                    /*视频*/
                    Intent intent = new Intent(AdEditActivity.this, EditVideoActivity.class);
                    intent.putExtra("path", file.toString());
                    intent.putExtra("fromType",Constants.FROM_TYPE_TO_SELECT_MEDIA.MYADSELECT);
                    startActivity(intent);
                }
            }

            @Override
            public void onProgress(int progress) {
                LogUtils.e(progress);
            }

            @Override
            public void onFail() {
                LogUtils.e("onFail");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
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
//                        onImagePathResult(imagePath);
                        LogUtils.e(imagePath);
                    }
                    break;
                case CommonUtil.REQ_CODE_4:
                    String savedPath = data.getStringExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH);
                    if (!TextUtils.isEmpty(savedPath) && new File(savedPath).exists()) {
//                        onImagePathResult(savedPath);
                        LogUtils.e(savedPath);
                    }
                    break;
            }
        }
    }
    private void onVideoPathResult(String videoPath) {
        LogUtils.e(videoPath);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventHandler(EventBusObjectEntity eventBusObjectEntity) {
        if (eventBusObjectEntity.getEventCode() == EventBusConstants.VideoCropSuccessResult) {
            //这里返回的是裁剪过后的视频 时长要么为15s要么为60s
            isHttp = 3;
            String videoPath = (String) eventBusObjectEntity.getData();
            LogUtils.e("视频或图片选择成功--------------", videoPath);
            pathData = videoPath;
            tvEdit.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.VISIBLE);
            tvTextUpdata.setVisibility(View.GONE);
            imgUpdata.setVisibility(View.GONE);
            GlideUtils.loadLocalFrame(this, videoPath, imgImage);
        }
        if (eventBusObjectEntity.getEventCode() == EventBusConstants.VideoOrImageSelectSuccess) {
            tvEdit.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.VISIBLE);
            tvTextUpdata.setVisibility(View.GONE);
            imgUpdata.setVisibility(View.GONE);
            if (eventBusObjectEntity.getData() != null) {
                if (eventBusObjectEntity.getData() instanceof String) {
                    //返回的是图片
                    isHttp = 2;
                    String imgPath = (String) eventBusObjectEntity.getData();
                    LogUtils.e("图片选择成功--------------", imgPath);
                    GlideUtils.loadImage(this, imgPath, imgImage);
                    pathData = imgPath;
                }
                if (eventBusObjectEntity.getData() instanceof MediaData) {
                    //返回的是视频
                    isHttp = 3;
                    MediaData mediaData = (MediaData) eventBusObjectEntity.getData();
                    LogUtils.e("视频选择成功--------------", mediaData.getOriginalPath());
                    GlideUtils.loadLocalFrame(this, mediaData.getOriginalPath(), imgImage);
                    pathData = mediaData.getOriginalPath();
                }
            }
        }

        LogUtils.e(eventBusObjectEntity.getEventCode());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageWrap message) {
        LogUtils.e(message.message.get(0));
        isHttp = 2;
        String imgPath = message.message.get(0);
        LogUtils.e("图片选择成功--------------", imgPath);
        GlideUtils.loadImage(this, imgPath, imgImage);
        pathData = imgPath;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 上传视频或图片
     *
     * @param resPath  视频或图片路径
     * @param planType 广告的类型
     */
    public void uploadAdVideoOrImg(String resPath, String planType, String screenType, String adName) {

        showProgressDialog("文件上传中，请等待...", false);
        Handler handler = new Handler();
        NetService.getInstance().uploadFileNoActivity(this, new File(resPath), new NetUpload.OnUploadCompletedListener() {
            @Override
            public void uploadSuccess(List<String> images, List<String> hash, JSONObject response) {
                LogUtils.e(images,hash,response);
                hideProgress();
                handler.post(() -> {
                    if (images != null && images.size() > 0) {
                        String uploadUrl = images.get(0);
                        commitAd(uploadUrl, planType, screenType, adName, resPath,response.optString("hash"), Long.parseLong(entity.getAdvertising().getObjectId()));
                        LogUtils.e("成功 提交服务器",uploadUrl);
                    } else {
                       showToast("上传失败，请重试");
                    }
                });
            }

            @Override
            public void uploadFailed(ApiException ex) {
                handler.post(() -> {

                      hideProgress();
                      showToast("上传失败");
                });
            }
        }, false);
    }


    /**
     * 提交广告到服务器
     *  @param url
     * @param planType   广告计划的类型
     * @param screenType 全屏图片还是视频
     * @param objctId
     */
    public void commitAd(String url, String planType, String screenType, String adName, String localPath, String hash, long objctId) {
        LogUtils.e(url,planType,screenType,adName,localPath,hash,objctId);
        String videoUrl = "";
        String imageUrl = "";
        String videoLocalPath = "";
        String imageLocalPath = "";
        if (screenType.equals(Constants.AD_SCREEN_TYPE.FULL_IMAGE)) {
            imageUrl = url;
            imageLocalPath = localPath;
        }
        if (screenType.equals(Constants.AD_SCREEN_TYPE.FULL_VIDEO)) {
            videoUrl = url;
            videoLocalPath = localPath;
        }
        LogUtils.e(objctId, false
                , planType, screenType, adName, videoUrl, imageUrl, null, videoLocalPath, imageLocalPath,hash);

        NetService.getInstance().createOrEidtAd(objctId, false
                , planType, screenType, adName, videoUrl, imageUrl, null, videoLocalPath, imageLocalPath,hash)
                .subscribe(new CustomApiCallback<ADEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                            LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(ADEntity entity) {
                       hideProgress();
                       finish();
                    }
                });
    }
}

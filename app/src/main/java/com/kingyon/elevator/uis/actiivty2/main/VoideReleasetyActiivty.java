package com.kingyon.elevator.uis.actiivty2.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.edit.MentionEditText;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.QueryTopicEntity;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.actiivty2.activityutils.VideoCoverActivity;
import com.kingyon.elevator.uis.actiivty2.input.TagList;
import com.kingyon.elevator.uis.activities.advertising.NetVideoPlayActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.kingyon.elevator.utils.utilstwo.VideoUtils;
import com.kingyon.elevator.videocrop.EditVideoActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_COVER;
import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_PATH;
import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_RELEASETY;
import static com.czh.myversiontwo.utils.CodeType.REQUEST_TAG_APPEND;
import static com.czh.myversiontwo.utils.CodeType.REQUEST_USER_APPEND;
import static com.czh.myversiontwo.utils.CodeType.TYPE_VIDEO;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDE_RELEASETY;


/**
 * @Created By Admin  on 2020/4/20
 * @Email : 163235610@qq.com
 * @Author:Mrczh 2765740671
 * @Instructions: 视频发布
 */
@Route(path = ACTIVITY_MAIN2_VOIDE_RELEASETY)
public class VoideReleasetyActiivty extends BaseActivity {
    @BindView(R.id.img_bake)
    ImageView imgBake;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_releaset)
    TextView tvReleaset;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.img_aite)
    ImageView imgAite;
    @BindView(R.id.img_huati)
    ImageView imgHuati;
    @BindView(R.id.tv_zishu)
    TextView tvZishu;
    @BindView(R.id.ll_bq)
    LinearLayout llBq;
    @BindView(R.id.image_addvideo)
    ImageView image_addvideo;
    @Autowired
    String videoPath;
    @BindView(R.id.img_video_play)
    ImageView imgVideoPlay;
    @BindView(R.id.tv_cover)
    TextView tvCover;
    @BindView(R.id.tv_choose)
    TextView tvChoose;
    @BindView(R.id.edit_title)
    EditText editTitle;
    @BindView(R.id.edit_conent)
    MentionEditText editConent;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.image_original)
    ImageView imageOriginal;
    @BindView(R.id.imag_reprinted)
    ImageView imagReprinted;
    String topicId;
    String atAccount;
    String videoCover;
    String videoCoverUrl;
    @BindView(R.id.tv_zhi_number)
    TextView tvZhiNumber;
    boolean isOriginal  = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        ButterKnife.bind(this);

    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_voide_releasty;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        ButterKnife.bind(this);
        initData();
        initView();
        
    }

    private void initView() {
        editConent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && !TextUtils.isEmpty(s)) {
                    char mentionChar = s.toString().charAt(start);
                    int selectionStart = editConent.getSelectionStart();
                    if (mentionChar == '@') {
                        startActivityForResult(UserSelectionActiivty.getIntent(VoideReleasetyActiivty.this), REQUEST_USER_APPEND);
                        editConent.getText().delete(selectionStart - 1, selectionStart);
                    } else if (mentionChar == '#') {
                        startActivityForResult(TopicSelectionActivity.getIntent(VoideReleasetyActiivty.this), REQUEST_TAG_APPEND);
                        editConent.getText().delete(selectionStart - 1, selectionStart);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvNumber.setText(s.toString().length()+"/500");
            }
        });

        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            tvZhiNumber.setText(s.toString().length()+"/30");

            }
        });
        
        
    }

    private void initData() {
//        LogUtils.e(videoPath);

        videoCover = VideoUtils.saveBitmap(VideoUtils.getVideoThumb(videoPath));
        LogUtils.e(VideoUtils.isCross(videoPath));
        LogUtils.e(videoCover + "图片地址");
        httpuUploadImage(videoCover);

    }

    private void httpuUploadImage(String videoCover) {
        LogUtils.e(videoCover);
        GlideUtils.loadImage(this, videoCover, image_addvideo);
        NetService.getInstance().uploadFile(this, new File(videoCover), new NetUpload.OnUploadCompletedListener() {
            @Override
            public void uploadSuccess(List<String> images, List<String> hash, JSONObject response) {
                videoCoverUrl = images.get(0);
                LogUtils.e(images, hash, response);
            }

            @Override
            public void uploadFailed(ApiException ex) {
                ToastUtils.showToast(VoideReleasetyActiivty.this, ex.getDisplayMessage(), 1000);
            }
        });
    }


    @OnClick({R.id.img_bake, R.id.tv_releaset, R.id.img_icon, R.id.img_aite, R.id.img_huati,
            R.id.image_addvideo, R.id.img_video_play, R.id.tv_cover,
            R.id.tv_choose, R.id.image_original, R.id.imag_reprinted})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.tv_releaset:
                httpUbdata();
                break;
            case R.id.img_icon:
            /*表情*/
                break;
            case R.id.img_aite:
//                @
                startActivityForResult(UserSelectionActiivty.getIntent(VoideReleasetyActiivty.this), REQUEST_USER_APPEND);
                break;
            case R.id.img_huati:
//                话题
                startActivityForResult(TopicSelectionActivity.getIntent(VoideReleasetyActiivty.this), REQUEST_TAG_APPEND);
                break;
          
            case R.id.image_addvideo:
//                ActivityUtils.setActivity(ACTIVITY_ACTIVITYUTILS_VIDEO_CHOOSE);

                break;
            case R.id.img_video_play:
                /*播放*/
                if (videoPath != null) {
                    Intent intent = new Intent(this, NetVideoPlayActivity.class);
                    intent.putExtra(CommonUtil.KEY_VALUE_1, videoPath);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(this, "视频文件未选择上", 1000);
                }

                break;
            case R.id.tv_cover:
/*封面*/

                Bundle bundle = new Bundle();
                bundle.putString("videoPath", videoPath);
                startActivityForResult(VideoCoverActivity.class, ACCESS_VOIDE_COVER, bundle);
                break;
            case R.id.tv_choose:
                RxPermissions rxPermissions = new RxPermissions(this);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                startAction();
                            } else {
                                Toast.makeText(this, "没有权限", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }, Throwable::printStackTrace);

                break;
            case R.id.image_original:
                initOriginal();
                isOriginal();
                isOriginal = true;
                break;
            case R.id.imag_reprinted:
                initOriginal();
                isReprinted();
                isOriginal = false;
                break;
            default:
        }
    }

    private void httpCover() {
        List<File> files = new ArrayList<>();
        files.clear();
        File file = new File(videoPath);
        files.add(file);
        showProgressDialog("请稍等...");
        NetService.getInstance().uploadFiles(this, files, new NetUpload.OnUploadCompletedListener() {
            @Override
            public void uploadSuccess(List<String> images, List<String> hash, JSONObject response) {
                hideProgress();
                Bundle bundle = new Bundle();
                bundle.putString("videoUrl", images.get(0));
                bundle.putString("videoPath", videoPath);
                startActivityForResult(VideoCoverActivity.class, ACCESS_VOIDE_COVER, bundle);
            }

            @Override
            public void uploadFailed(ApiException ex) {
                LogUtils.e(ex);
                hideProgress();
                ToastUtils.showToast(VoideReleasetyActiivty.this, ex.getDisplayMessage(), 1000);
            }
        }, false);

    }

    private void isReprinted() {
        imagReprinted.setImageResource(R.mipmap.btn_video_resource_on);
        isOriginal = false;
    }

    private void initOriginal() {
        imagReprinted.setImageResource(R.mipmap.btn_video_resource_off);
        imageOriginal.setImageResource(R.mipmap.btn_video_resource_off);
    }

    private void isOriginal() {
        imageOriginal.setImageResource(R.mipmap.btn_video_resource_on);
        isOriginal = true;
    }

    private void httpUbdata() {
        List<File> files = new ArrayList<>();
        files.clear();
        File file = new File(videoPath);
        files.add(file);
        int videoSize = (int) VideoUtils.getVideoSize(videoPath);
        long videoTime = VideoUtils.getVideoDuration(videoPath);
        CharSequence convertMetionString = editConent.getFormatCharSequence();
        LogUtils.e(editTitle.getText().toString(),
                editConent.getText().toString(), null, "视频地址", TYPE_VIDEO, "3", topicId, atAccount,
                videoSize, videoCoverUrl, videoTime);
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().uploadFiles(this, files, new NetUpload.OnUploadCompletedListener() {
            @Override
            public void uploadSuccess(List<String> images, List<String> hash, JSONObject response) {
                if (VideoUtils.isCross(videoPath)) {
                    OrdinaryActivity.httpContentPublish(VoideReleasetyActiivty.this, editTitle.getText().toString(),
                            String.valueOf(convertMetionString), null, images.get(0), TYPE_VIDEO, "3", topicId, atAccount,
                            videoSize, videoCoverUrl, videoTime, 0,isOriginal);
                } else {
                    OrdinaryActivity.httpContentPublish(VoideReleasetyActiivty.this, editTitle.getText().toString(),
                            String.valueOf(convertMetionString), null, images.get(0), TYPE_VIDEO, "3", topicId, atAccount,
                            videoSize, videoCoverUrl, videoTime, 1,isOriginal);
                }
            }

            @Override
            public void uploadFailed(ApiException ex) {
                LogUtils.e(ex);
                hideProgress();
                ToastUtils.showToast(VoideReleasetyActiivty.this, ex.getDisplayMessage(), 1000);
            }
        }, false);

    }

    private void startAction() {
        Matisse.from(this)
                .choose(MimeType.ofVideo1(), false)
                .countable(false)
                .capture(false)
                .captureStrategy(
                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                .maxSelectable(1)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .setOnSelectedListener((uriList, pathList) -> {
                    Log.e("onSelected", "onSelected: pathList=" + pathList);
                })
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(isChecked -> {
                    Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                })
                .forResult(ACCESS_VOIDE_PATH);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && null != data) {
            switch (requestCode) {
                case ACCESS_VOIDE_PATH:
                    LogUtils.e(Matisse.obtainPathResult(data), Matisse.obtainResult(data), Matisse.obtainOriginalState(data));
                    Bundle bundle = new Bundle();
                    bundle.putString("path", Matisse.obtainPathResult(data).get(0));
                    bundle.putInt("fromType", ACCESS_VOIDE_RELEASETY);
                    startActivityForResult(EditVideoActivity.class, ACCESS_VOIDE_RELEASETY, bundle);
                    break;
                case ACCESS_VOIDE_COVER:
                    httpuUploadImage(data.getStringExtra("videoCover"));
                    break;
                case ACCESS_VOIDE_RELEASETY:
                    LogUtils.e(data.getStringExtra("videoPath"));
                    videoPath = data.getStringExtra("videoPath");
                    httpuUploadImage(VideoUtils.saveBitmap(VideoUtils.getVideoThumb(videoPath)));
                    break;
                case REQUEST_USER_APPEND:
                    AttenionUserEntiy user = (AttenionUserEntiy) data.getSerializableExtra(UserSelectionActiivty.RESULT_USER);
                    atAccount = String.valueOf(user.followerAccount);
                    editConent.insert(user);
                    break;
                case REQUEST_TAG_APPEND:
                    QueryTopicEntity.PageContentBean tag = (QueryTopicEntity.PageContentBean) data.getSerializableExtra(TagList.RESULT_TAG);
                    editConent.insert(tag);
                    topicId = String.valueOf(tag.getId());
//                    newTopic = String.format(TAG_FORMAT, tag.getId(), tag.getTitle());
//                    LogUtils.e(newTopic);
                    break;

            }
        }
    }

}

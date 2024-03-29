package com.kingyon.elevator.uis.actiivty2.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import com.kingyon.elevator.uis.dialogs.SaveDraftsDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.kingyon.elevator.utils.utilstwo.StringUtils;
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

import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_CODE;
import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_COVER;
import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_PATH;
import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_RELEASETY;
import static com.czh.myversiontwo.utils.CodeType.REQUEST_TAG_APPEND;
import static com.czh.myversiontwo.utils.CodeType.REQUEST_USER_APPEND;
import static com.czh.myversiontwo.utils.CodeType.TYPE_ARTICLE;
import static com.czh.myversiontwo.utils.CodeType.TYPE_VIDEO;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDE_RELEASETY;
import static com.kingyon.elevator.data.DataSharedPreferences.SAVE_MICRO_ARTICLE_DRAFT;


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
    List<String> userList = new ArrayList<>();
    List<String> tagList = new ArrayList<>();
    String topicId;
    String atAccount;
    String videoCover;
    String videoCoverUrl;
    @BindView(R.id.tv_zhi_number)
    TextView tvZhiNumber;
    boolean isOriginal  = false;
    int videoHorizontalVertical;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        ButterKnife.bind(this);
        tvTitle.setText("视频发布");

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
                if ((s.toString().length()==1?s.toString():"").equals(" ")) {
                    showToast("开头不能为空格");
                    editConent.setText("");
                }else {
                    tvNumber.setText(s.toString().length() + "/500");
                }
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
                if ((s.toString().length()==1?s.toString():"").equals(" ")){
                    showToast("标题开头不能为空格");
                    editTitle.setText("");
                }else {
                    tvZhiNumber.setText(s.length() + "/30");
                }
            }
        });
        
        
    }

    private void initData() {
//        LogUtils.e(videoPath);

        videoCover = VideoUtils.saveBitmap(this,VideoUtils.getVideoThumb(videoPath));
        LogUtils.e(VideoUtils.isCross(videoPath));
        LogUtils.e(videoCover + "图片地址");
        httpuUploadImage(videoCover);
        if (VideoUtils.isCross(videoPath)) {
            videoHorizontalVertical  = 0;
        }else {
            videoHorizontalVertical = 1;
        }
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
//                initDialogBack();
                    finish();
                break;
            case R.id.tv_releaset:
                if (editTitle.getText().toString().isEmpty()){
                    showToast("标题不能为空");
                }else if (editConent.getText().toString().isEmpty()){
                    showToast("内容不能为空");
                }else if (videoCoverUrl==null){
                    showToast("请重新选择封面");
                }else {
                    httpUbdata();
                }
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
                /*重新选择*/
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
                isOriginal = false;
                break;
            case R.id.imag_reprinted:
                initOriginal();
                isReprinted();
                isOriginal = true;
                break;
            default:
        }
    }

    private void initDialogBack() {
        if (editTitle.getText().toString().isEmpty()){
            finish();
        }else {
            SaveDraftsDialog saveDraftsDialog = new SaveDraftsDialog(this);
            saveDraftsDialog.show();
            saveDraftsDialog.Clicked(new IsSuccess() {
                @Override
                public void isSuccess(boolean success) {
                    if (success){
                        saveContent();
                    }else {
                        finish();
                    }
                }
            });
        }
    }

    private void httpCover() {
        List<File> files = new ArrayList<>();
        files.clear();
        File file = new File(videoPath);
        files.add(file);
        showProgressDialog("请稍等...",true);
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
        isOriginal = true;
    }

    private void initOriginal() {
        imagReprinted.setImageResource(R.mipmap.btn_video_resource_off);
        imageOriginal.setImageResource(R.mipmap.btn_video_resource_off);
    }

    private void isOriginal() {
        imageOriginal.setImageResource(R.mipmap.btn_video_resource_on);
        isOriginal = false;
    }

    private void httpUbdata() {
        List<File> files = new ArrayList<>();
        files.clear();
        File file = new File(videoPath);
        files.add(file);
        int videoSize = (int) VideoUtils.getVideoSize(videoPath);
        long videoTime = VideoUtils.getVideoDuration(videoPath);
        CharSequence convertMetionString = editConent.getFormatCharSequence();

        tagList = StringUtils.getTagString(String.valueOf(editConent.getFormatCharSequence()));
        String topicId1 = tagList.toString().replace("[","");
        topicId = topicId1.replace("]","");
        userList = StringUtils.getUserString(String.valueOf(editConent.getFormatCharSequence()));
        String atAccount1 = userList.toString().replace("[","");
        atAccount = atAccount1.replace("]","");

        LogUtils.e(editTitle.getText().toString(),
                editConent.getText().toString(), null, "视频地址", TYPE_VIDEO, "3", topicId, atAccount,
                videoSize, videoCoverUrl, videoTime);
        showProgressDialog(getString(R.string.wait),false);
        NetService.getInstance().uploadFiles(this, files, new NetUpload.OnUploadCompletedListener() {
            @Override
            public void uploadSuccess(List<String> images, List<String> hash, JSONObject response) {
                    OrdinaryActivity.httpContentPublish(VoideReleasetyActiivty.this, editTitle.getText().toString(),
                            String.valueOf(convertMetionString), null, images.get(0), TYPE_VIDEO, "3", topicId, atAccount,
                            videoSize, videoCoverUrl, videoTime, videoHorizontalVertical,isOriginal);

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
                    Intent intent = new Intent(this, EditVideoActivity.class);
                    intent.putExtra("path",Matisse.obtainPathResult(data).get(0));
                    intent.putExtra("fromType",ACCESS_VOIDE_CODE);
                    startActivity(intent);
                    break;
                case ACCESS_VOIDE_COVER:
                    httpuUploadImage(data.getStringExtra("videoCover"));
                    break;
                case ACCESS_VOIDE_RELEASETY:
                    LogUtils.e(data.getStringExtra("videoPath"));
                    videoPath = data.getStringExtra("videoPath");
                    httpuUploadImage(VideoUtils.saveBitmap(this,VideoUtils.getVideoThumb(videoPath)));
                    if (VideoUtils.isCross(videoPath)) {
                        videoHorizontalVertical  = 0;
                    }else {
                        videoHorizontalVertical = 1;
                    }
                    break;
                case REQUEST_USER_APPEND:
                    AttenionUserEntiy user = (AttenionUserEntiy) data.getSerializableExtra(UserSelectionActiivty.RESULT_USER);
//                    atAccount = String.valueOf(user.followerAccount);
                    editConent.insert(user);
                    break;
                case REQUEST_TAG_APPEND:
                    QueryTopicEntity.PageContentBean tag = (QueryTopicEntity.PageContentBean) data.getSerializableExtra(TagList.RESULT_TAG);
                    editConent.insert(tag);
//                    topicId = String.valueOf(tag.getId());
//                    newTopic = String.format(TAG_FORMAT, tag.getId(), tag.getTitle());
//                    LogUtils.e(newTopic);
                    break;

            }
        }
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            SaveDraftsDialog saveDraftsDialog = new SaveDraftsDialog(this);
//            saveDraftsDialog.show();
//            saveDraftsDialog.Clicked(new IsSuccess() {
//                @Override
//                public void isSuccess(boolean success) {
//                    if (success){
//                        saveContent();
//                    }else {
//                        finish();
//                    }
//                }
//            });
//            return false;
//        }else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }

    private void saveContent() {
        SharedPreferences sharedPreferences= getSharedPreferences(SAVE_MICRO_ARTICLE_DRAFT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("title", editTitle.getText().toString());
        editor.putString("content",  String.valueOf(editConent.getFormatCharSequence()));
        editor.putString("type", TYPE_VIDEO);
        editor.putString("topicId", topicId);
        editor.putString("atAccount", atAccount);
        editor.putString("videoCover", videoCoverUrl);
        editor.putBoolean("isOriginal", isOriginal);
        editor.putInt("videoHorizontalVertical", videoHorizontalVertical);
        editor.putBoolean("marrid",false);
        editor.commit();
        finish();

    }
}

package com.kingyon.elevator.uis.actiivty2.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.QueryTopicEntity;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.actiivty2.input.TagList;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.kingyon.elevator.utils.utilstwo.SoftkeyboardUtils;
import com.kingyon.elevator.utils.utilstwo.VideoUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.rex.editor.view.RichEditor;
import com.rex.editor.view.RichEditorNew;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.ACCESS_IMAGE_PATH;
import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_PATH;
import static com.czh.myversiontwo.utils.CodeType.REQUEST_TAG_APPEND;
import static com.czh.myversiontwo.utils.CodeType.REQUEST_USER_APPEND;
import static com.czh.myversiontwo.utils.CodeType.TYPE_ARTICLE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_RELEASETY;


/**
 * Created By Admin  on 2020/4/17
 * Email : 163235610@qq.com
 *
 * @Author:Mrczh Instructions: 富文本编辑
 */
@Route(path = ACTIVITY_MAIN2_ARTICLE_RELEASETY)
public class ArticleReleaseActivity extends BaseActivity {


    @BindView(R.id.img_bake)
    ImageView imgBake;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_releaset)
    TextView tvReleaset;
    @BindView(R.id.edit_title)
    EditText editTitle;
    @BindView(R.id.richEditor)
    RichEditorNew richEditor;
    @BindView(R.id.img_blue)
    ImageView imgBlue;
    @BindView(R.id.img_tilt)
    ImageView imgTilt;
    @BindView(R.id.img_bold)
    ImageView imgBold;
    @BindView(R.id.img_level)
    ImageView imgLevel;
    @BindView(R.id.img_number)
    ImageView imgNumber;
    @BindView(R.id.img_point)
    ImageView imgPoint;
    @BindView(R.id.ll_font)
    LinearLayout llFont;
    @BindView(R.id.img_link)
    ImageView imgLink;
    @BindView(R.id.img_at)
    ImageView imgAt;
    @BindView(R.id.img_title)
    ImageView imgTitle;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.img_down)
    ImageButton imgDown;
    @BindView(R.id.img_topi)
    ImageButton imgTopi;
    @BindView(R.id.img_text)
    ImageButton imgText;
    @BindView(R.id.img_photo)
    ImageButton imgPhoto;
    @BindView(R.id.img_video)
    ImageButton imgVideo;
    @BindView(R.id.img_revoke1)
    ImageButton imgRevoke1;
    @BindView(R.id.img_revoke)
    ImageButton imgRevoke;
    @BindView(R.id.img_back)
    ImageButton imgBack;
    @BindView(R.id.img_set)
    ImageButton imgSet;
    @BindView(R.id.tv_yc)
    TextView tvYc;
    @BindView(R.id.tv_zz)
    TextView tvZz;
    @BindView(R.id.ll_sz)
    LinearLayout llSz;
    private boolean isblue = true;
    private boolean istilt = true;
    private boolean isbold = true;
    private boolean isNumber = true;
    private boolean isPoint = true;
    private boolean isLecel = true;
    private boolean isRichEditor = false;
    private boolean isText = true;
    private boolean isRevoke1 = true;
    private boolean isOriginal = true;
    private String TOP_USER = "&nbsp;<user style='color: #4dacee;' id='%s' name='%s'>@%s</user>&nbsp;";
    private String TOP_TAG = "&nbsp;<tag style='color: #4dacee;' id='%s' name='%s'>#%s#</tag>&nbsp;";
    List<File> files = new ArrayList<>();
    private String imageCover;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvTitle.setText("文章发布");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_artice_release;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        richEditor.setEditorFontColor(Color.BLACK);
        richEditor.setPadding(10, 10, 10, 10);
        richEditor.setPlaceholder("请输入文章内容");
        initEditro();
        initRichEditor();
    }

    private void initEditro() {
        richEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LogUtils.e(hasFocus);
                isRichEditor = hasFocus;
                if (!hasFocus) {
                    initCancel();
                }
            }
        });
        editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    initCancel();
                }
            }
        });
    }

    @OnClick({R.id.img_bake, R.id.tv_title, R.id.tv_releaset, R.id.edit_title, R.id.richEditor,
            R.id.img_blue, R.id.img_tilt, R.id.img_bold, R.id.img_level, R.id.img_number,
            R.id.img_point, R.id.ll_font, R.id.img_link, R.id.img_at, R.id.img_title,
            R.id.ll_add, R.id.img_down, R.id.img_topi, R.id.img_text, R.id.img_photo,
            R.id.img_video, R.id.img_revoke1, R.id.img_revoke, R.id.img_back,
            R.id.img_set,R.id.tv_yc, R.id.tv_zz, R.id.ll_sz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                /*返回*/
                initCancel();
                finish();
                SoftkeyboardUtils.hideInput(this);
                break;
            case R.id.tv_title:
                initCancel();
                break;
            case R.id.tv_releaset:
                initCancel();
                LogUtils.e(richEditor.getHtml());
                httpReleaset();

                break;
            case R.id.img_blue:
                /*加粗*/
                if (isblue) {
                    isblue = false;
                    imgBlue.setImageResource(R.mipmap.ic_article_front_blue);
                    richEditor.setBold();
                } else {
                    isblue = true;
                    imgBlue.setImageResource(R.mipmap.ic_article_front);
                    richEditor.setBold();
                }
                break;
            case R.id.img_tilt:
                /*斜体*/
                if (istilt) {
                    istilt = false;
                    imgTilt.setImageResource(R.mipmap.ic_article_tilt_blue);
                    richEditor.setItalic();
                } else {
                    istilt = true;
                    imgTilt.setImageResource(R.mipmap.ic_article_tilt);
                    richEditor.setItalic();
                }

                break;
            case R.id.img_bold:
                /*H*/
                if (isbold) {
                    isbold = false;
                    imgBold.setImageResource(R.mipmap.ic_article_bold_blue);
                    richEditor.setFontSize(6);
                } else {
                    isbold = true;
                    imgBold.setImageResource(R.mipmap.ic_article_bold);
                    richEditor.setFontSize(4);
                }
                break;
            case R.id.img_level:
                /*引号*/
                if (isLecel) {
                    isLecel = false;
                    richEditor.setBlockquote();
                } else {
                    isLecel = true;
                    richEditor.setNewLine();
                }

                break;
            case R.id.img_number:
                /*计数*/
                if (isNumber) {
                    isNumber = false;
                    richEditor.setNumbers();
                    imgNumber.setImageResource(R.mipmap.ic_article_number_blue);
                } else {
                    richEditor.setNumbers();
                    isNumber = true;
                    imgNumber.setImageResource(R.mipmap.ic_article_number);
                }
                break;
            case R.id.img_point:
                /*条码*/
                if (isPoint) {
                    isPoint = false;
                    imgPoint.setImageResource(R.mipmap.ic_article_point_blue);
                    richEditor.setBullets();
                } else {
                    isPoint = true;
                    imgPoint.setImageResource(R.mipmap.ic_article_point);
                    richEditor.setBullets();
                }

                break;
            case R.id.img_link:
                /*添加链接*/
                richEditor.insertLink("https://www.baidu.com", "百度");
                break;
            case R.id.img_at:
                /*艾特*/
                if (isRichEditor) {
                    startActivityForResult(UserSelectionActiivty.getIntent(ArticleReleaseActivity.this), REQUEST_USER_APPEND);
                }
                break;
            case R.id.img_title:
                /*分割线*/
                richEditor.insertImage("http://cdn.tlwgz.com//1590049100977.jpg", "picvision", "display: block; margin:0px auto 0;max-width:100%;");
                break;
            case R.id.img_down:
                /*关闭键盘*/
                initCancel();
                SoftkeyboardUtils.hideInput(this);
                break;
            case R.id.img_topi:
                /*话题*/
                if (isRichEditor) {
                    initCancel();
                    startActivityForResult(TopicSelectionActivity.getIntent(ArticleReleaseActivity.this), REQUEST_TAG_APPEND);
                }
                break;
            case R.id.img_text:
                /*文字设置*/
//                initCancel();
                if (isRichEditor) {
                    if (isText) {
                        isText = false;
                        imgText.setImageResource(R.mipmap.ic_rich_text_text_red);
                        llFont.setVisibility(View.VISIBLE);
                        llAdd.setVisibility(View.GONE);
                        imgRevoke1.setImageResource(R.mipmap.ic_rich_text_revoke1);
                    } else {
                        initCancel();
                        isText = true;
                        imgText.setImageResource(R.mipmap.ic_rich_text_text);
                    }
                }
                break;
            case R.id.img_photo:
                /*选择图片*/
                if (isRichEditor) {
                    initCancel();
                    RxPermissions rxPermissions = new RxPermissions(this);
                    rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(aBoolean -> {
                                if (aBoolean) {
                                    ConentUtils.startAction(this);
                                } else {
                                    Toast.makeText(this, "没有权限", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }, Throwable::printStackTrace);
                }
                break;
            case R.id.img_video:
                /*选择视频*/
                if (isRichEditor) {
                    initCancel();
                    RxPermissions rxPermissions1 = new RxPermissions(this);
                    rxPermissions1.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(aBoolean -> {
                                if (aBoolean) {
                                    ConentUtils.startVideoAction(this);
                                } else {
                                    Toast.makeText(this, "没有权限", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }, Throwable::printStackTrace);


                }
                break;
            case R.id.img_revoke1:
                /*加*/
//                initCancel();
                if (isRichEditor) {
                    if (isRevoke1) {
                        isRevoke1 = false;
                        imgRevoke1.setImageResource(R.mipmap.ic_rich_text_revoke_red);
                        llAdd.setVisibility(View.VISIBLE);
                        llFont.setVisibility(View.GONE);
                        imgText.setImageResource(R.mipmap.ic_rich_text_text);
                    } else {
                        isRevoke1 = true;
                        initCancel();
                        imgRevoke1.setImageResource(R.mipmap.ic_rich_text_revoke1);
                    }
                }
                break;
            case R.id.img_revoke:
                /*返回上一步*/
                if (isRichEditor) {
                    initCancel();
                    richEditor.undo();
                }
                break;
            case R.id.img_back:
                /*返回下一步*/
                if (isRichEditor) {
                    initCancel();
                    richEditor.redo();
                }
                break;
            case R.id.img_set:
                /*设置*/
                if (isRichEditor) {
                    initCancel();
                    SoftkeyboardUtils.hideInput(this);
                    llSz.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_yc:
                isOriginal  =true;
                tvYc.setTextColor(Color.parseColor("#ffff3049"));
                tvZz.setTextColor(Color.parseColor("#ff333333"));
                break;
            case R.id.tv_zz:
                isOriginal = false;
                tvZz.setTextColor(Color.parseColor("#ffff3049"));
                tvYc.setTextColor(Color.parseColor("#ff333333"));
                break;
            case R.id.ll_sz:

                break;
            default:
        }
    }

    private void httpReleaset() {
        if (editTitle.getText().toString().isEmpty()) {
            ToastUtils.showShort("标题不能为空");
        } else {
            OrdinaryActivity.httpContentPublish(this, editTitle.getText().toString(), richEditor.getHtml(), "", ""
                    , TYPE_ARTICLE, "1", "", "", 0, imageCover, 0, 3,isOriginal);

        }
    }


    private void initCancel() {
        llAdd.setVisibility(View.GONE);
        llFont.setVisibility(View.GONE);
        isRevoke1 = true;
        isText = true;
        imgText.setImageResource(R.mipmap.ic_rich_text_text);
        imgRevoke1.setImageResource(R.mipmap.ic_rich_text_revoke1);
        llSz.setVisibility(View.GONE);

    }

    private void initRichEditor() {
        richEditor.setOnDecorationChangeListener(new RichEditor.OnDecorationStateListener() {
            @Override
            public void onStateChangeListener(String text, List<RichEditor.Type> types) {
                llSz.setVisibility(View.GONE);
                if (types.contains(RichEditor.Type.BOLD)) {
                    imgBlue.setImageResource(R.mipmap.ic_article_front_blue);
                    isblue = false;
                } else {
                    imgBlue.setImageResource(R.mipmap.ic_article_front);
                    isblue = true;
                }
                if (types.contains(RichEditor.Type.ITALIC)) {
                    imgTilt.setImageResource(R.mipmap.ic_article_tilt_blue);
                    istilt = false;
                } else {
                    imgTilt.setImageResource(R.mipmap.ic_article_tilt);
                    istilt = true;
                }
                if (types.contains(RichEditor.Type.H6)) {
                    isbold = false;
                    imgBold.setImageResource(R.mipmap.ic_article_bold_blue);
                } else {
                    isbold = true;
                    imgBold.setImageResource(R.mipmap.ic_article_bold);
                }
                if (types.contains(RichEditor.Type.ORDEREDLIST)) {
                    isNumber = false;
                    imgNumber.setImageResource(R.mipmap.ic_article_number_blue);
                } else {
                    isNumber = true;
                    imgNumber.setImageResource(R.mipmap.ic_article_number);
                }
                if (types.contains(RichEditor.Type.UNORDEREDLIST)) {
                    isPoint = false;
                    imgPoint.setImageResource(R.mipmap.ic_article_point_blue);
                } else {
                    isPoint = true;
                    imgPoint.setImageResource(R.mipmap.ic_article_point);
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && null != data) {
            switch (requestCode) {
                case ACCESS_IMAGE_PATH:
                    LogUtils.e(Matisse.obtainPathResult(data));
                    files.clear();
                    for (int c = 0; c < Matisse.obtainPathResult(data).size(); c++) {
                        if (Matisse.obtainPathResult(data).get(c) != null) {
                            File file = new File(Matisse.obtainPathResult(data).get(c));
                            files.add(file);
                        }
                    }
                    showProgressDialog("图片上传中....");
                    NetService.getInstance().uploadFiles(this, files, new NetUpload.OnUploadCompletedListener() {
                        @Override
                        public void uploadSuccess(List<String> images, List<String> hash, JSONObject response) {
                            hideProgress();
                            LogUtils.e(images, hash, response);
                            imageCover = images.get(0);
                            int i;
                            for (i = 0; i < images.size(); i++) {
                                richEditor.insertImage(images.get(i));
                            }
                        }

                        @Override
                        public void uploadFailed(ApiException ex) {
                            hideProgress();
                            LogUtils.e(ex);
                            ToastUtils.showShort(ex.getDisplayMessage());
                        }
                    });
                    break;
                case ACCESS_VOIDE_PATH:
                    LogUtils.e(Matisse.obtainPathResult(data), Matisse.obtainResult(data), Matisse.obtainOriginalState(data));
                    String videoCover = VideoUtils.saveBitmap(VideoUtils.getVideoThumb(Matisse.obtainPathResult(data).get(0)));
                    showProgressDialog("视频上传中....");
                    NetService.getInstance().uploadFile(this, new File(videoCover), new NetUpload.OnUploadCompletedListener() {
                        @Override
                        public void uploadSuccess(List<String> images, List<String> hash, JSONObject response) {
                            String videoCoverUrl = images.get(0);
                            LogUtils.e(images, hash, response);
                            List<File> files = new ArrayList<>();
                            files.clear();
                            File file = new File(Matisse.obtainPathResult(data).get(0));
                            files.add(file);
                            httpVideo(videoCoverUrl, files);
                        }

                        @Override
                        public void uploadFailed(ApiException ex) {
                            hideProgress();
                            com.leo.afbaselibrary.utils.ToastUtils.showToast(ArticleReleaseActivity.this, ex.getDisplayMessage(), 1000);
                        }
                    });
                    break;

                case REQUEST_USER_APPEND:
                    AttenionUserEntiy user = (AttenionUserEntiy) data.getSerializableExtra(UserSelectionActiivty.RESULT_USER);
                    String atAccount = String.format(TOP_USER, user.followerAccount, user.nickname, user.nickname);
                    LogUtils.e(atAccount);
                    richEditor.setHtml(richEditor.getHtml() + atAccount);
                    break;
                case REQUEST_TAG_APPEND:
                    QueryTopicEntity.PageContentBean tag = (QueryTopicEntity.PageContentBean) data.getSerializableExtra(TagList.RESULT_TAG);

                    String newTopic = String.format(TOP_TAG, tag.getId(), tag.getTitle(), tag.getTitle());
                    LogUtils.e(newTopic);
                    richEditor.setHtml(richEditor.getHtml() + newTopic);
                    break;

            }
        }
    }

    private void httpVideo(String videoCoverUrl, List<File> files) {
        NetService.getInstance().uploadFiles(this, files, new NetUpload.OnUploadCompletedListener() {
            @Override
            public void uploadSuccess(List<String> images, List<String> hash, JSONObject response) {
                hideProgress();
                richEditor.insertVideo(images.get(0), "", videoCoverUrl);
            }

            @Override
            public void uploadFailed(ApiException ex) {
                LogUtils.e(ex);
                hideProgress();
                com.leo.afbaselibrary.utils.ToastUtils.showToast(ArticleReleaseActivity.this, ex.getDisplayMessage(), 1000);
            }
        }, false);


    }

}

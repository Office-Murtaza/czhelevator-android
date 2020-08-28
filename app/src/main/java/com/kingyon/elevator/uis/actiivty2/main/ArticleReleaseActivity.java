package com.kingyon.elevator.uis.actiivty2.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import com.kingyon.elevator.uis.dialogs.SaveDraftsDialog;
import com.kingyon.elevator.uis.dialogs.WebAddDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.HtmlUtil;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.kingyon.elevator.utils.utilstwo.SoftkeyboardUtils;
import com.kingyon.elevator.utils.utilstwo.StringUtils;
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
import static com.kingyon.elevator.data.DataSharedPreferences.SAVE_MICRO_ARTICLE_DRAFT;


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
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_number1)
    TextView tvNumber1;
    private boolean isblue = true;
    private boolean istilt = true;
    private boolean isbold = true;
    private boolean isNumber = true;
    private boolean isPoint = true;
    private boolean isLecel = true;
    private boolean isRichEditor = false;
    private boolean isText = true;
    private boolean isRevoke1 = true;
    private boolean isOriginal = false;
    private String TOP_USER = "&nbsp;<user id='%s' style='color: #4dacee;' name='%s'>@%s</user>&nbsp;";
    private String TOP_TAG = "&nbsp;<tag id='%s' style='color: #4dacee;' name='%s'>#%s#</tag>&nbsp;";
    List<File> files = new ArrayList<>();
    private String imageCover;

    List<String> userList = new ArrayList<>();
    List<String> tagList = new ArrayList<>();
    String topicId;
    String atAccount;

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
        initData();
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SAVE_MICRO_ARTICLE_DRAFT, Context.MODE_PRIVATE);
        String content = sharedPreferences.getString("content", "");
        String title = sharedPreferences.getString("title", "");
        imageCover = sharedPreferences.getString("imageCover", "");
        isOriginal = sharedPreferences.getBoolean("isOriginal", false);
        editTitle.setText(title + "");
        richEditor.setHtml(content);

    }

    private void initEditro() {
        richEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LogUtils.e(hasFocus);
                isRichEditor = hasFocus;
                if (!hasFocus) {
                    initCancel();
                    llBottom.setVisibility(View.GONE);
                } else {
                    llBottom.setVisibility(View.VISIBLE);
                }
            }
        });

        editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    initCancel();
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
                    tvNumber.setText(s.length() + "/30");
                }
            }
        });
        richEditor.setFadingEdgeLength(500);

        richEditor.setOnTextChangeListener(new RichEditorNew.OnTextChangeNewListener() {
            @Override
            public void onTextChange(String text) {
                LogUtils.e(text);
                tvNumber1.setText(HtmlUtil.delHTMLTag(text).length()+"");
                if (HtmlUtil.delHTMLTag(text).length()==0){
                    richEditor.setPlaceholder("请输入文章内容");
                }
                if (text.equals("<br>")){
                    richEditor.setHtml("");
                    richEditor.setPlaceholder("请输入文章内容");
                }
            }
        });

    }

    @OnClick({R.id.img_bake, R.id.tv_title, R.id.tv_releaset, R.id.edit_title, R.id.richEditor,
            R.id.img_blue, R.id.img_tilt, R.id.img_bold, R.id.img_level, R.id.img_number,
            R.id.img_point, R.id.ll_font, R.id.img_link, R.id.img_at, R.id.img_title,
            R.id.ll_add, R.id.img_down, R.id.img_topi, R.id.img_text, R.id.img_photo,
            R.id.img_video, R.id.img_revoke1, R.id.img_revoke, R.id.img_back,
            R.id.img_set, R.id.tv_yc, R.id.tv_zz, R.id.ll_sz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                /*返回*/
                initCancel();
                initDialogBack();
//                finish();
                SoftkeyboardUtils.hideInput(this);
                break;
            case R.id.tv_title:
                initCancel();
                break;
            case R.id.tv_releaset:
                initCancel();
                LogUtils.e(richEditor.getHtml());
                if (HtmlUtil.idVideoImage(richEditor.getHtml())){
                    httpReleaset();
                }else {
                    if (!HtmlUtil.delHTMLTag(richEditor.getHtml()).isEmpty()){
                        httpReleaset();
                    }else {
                        showToast("请输入内容");
                    }
                }

                LogUtils.e(HtmlUtil.delHTMLTag(richEditor.getHtml()),"11111111111111",HtmlUtil.idVideoImage(richEditor.getHtml()));
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
                WebAddDialog webAddDialog = new WebAddDialog(this);
                webAddDialog.OnGetConent(new WebAddDialog.IsSrcSuccess() {
                    @Override
                    public void onSuccess(String e, String e1) {
                        webAddDialog.dismiss();
                        if (e1.isEmpty()) {
                            richEditor.insertLink(e, e);
                        } else {
                            richEditor.insertLink(e, e1);
                        }
                    }
                });
                webAddDialog.show();

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
                isOriginal = false;
                tvYc.setTextColor(Color.parseColor("#ff3049"));
                tvZz.setTextColor(Color.parseColor("#333333"));
                LogUtils.e("===========");
                break;
            case R.id.tv_zz:
                isOriginal = true;
                tvZz.setTextColor(Color.parseColor("#ff3049"));
                tvYc.setTextColor(Color.parseColor("#333333"));
                LogUtils.e("222222222222222");
                break;
            case R.id.ll_sz:

                break;
            default:
        }
    }

    private void initDialogBack() {
        LogUtils.e(richEditor.getHtml().isEmpty(),"111111111111111",richEditor.getHtml());
        if (editTitle.getText().toString().isEmpty()&&richEditor.getHtml().isEmpty()||richEditor.getHtml().equals("<br>")) {
            SharedPreferences sharedPreferences = getSharedPreferences(SAVE_MICRO_ARTICLE_DRAFT, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            finish();
        } else {
            SaveDraftsDialog saveDraftsDialog = new SaveDraftsDialog(this);
            saveDraftsDialog.show();
            saveDraftsDialog.Clicked(new IsSuccess() {
                @Override
                public void isSuccess(boolean success) {
                    if (success) {
                        saveContent();
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences(SAVE_MICRO_ARTICLE_DRAFT, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        finish();
                    }
                }
            });
        }

    }

    private void httpReleaset() {
        if (editTitle.getText().toString().isEmpty()) {
            ToastUtils.showShort("标题不能为空");
        } else {

            tagList = StringUtils.getTagString(String.valueOf(richEditor.getHtml()));
            String topicId1 = tagList.toString().replace("[", "");
            topicId = topicId1.replace("]", "");
            userList = StringUtils.getUserString(String.valueOf(richEditor.getHtml()));
            String atAccount1 = userList.toString().replace("[", "");
            atAccount = atAccount1.replace("]", "");
            LogUtils.e(topicId, atAccount, tagList.toString(), userList.toString());
            OrdinaryActivity.httpContentPublish(this, editTitle.getText().toString(), richEditor.getHtml(), "", ""
                    , TYPE_ARTICLE, "1", topicId, atAccount, 0, imageCover, 0, 3, isOriginal);

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
                    showProgressDialog("图片上传中....", false);
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
                    String videoCover = VideoUtils.saveBitmap(this, VideoUtils.getVideoThumb(Matisse.obtainPathResult(data).get(0)));
                    showProgressDialog("视频上传中....", false);
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
                    if (richEditor.getHtml() != null) {
                        richEditor.setHtml(richEditor.getHtml() + atAccount);
                    } else {
                        richEditor.setHtml(atAccount);
                    }
                    break;
                case REQUEST_TAG_APPEND:
                    QueryTopicEntity.PageContentBean tag = (QueryTopicEntity.PageContentBean) data.getSerializableExtra(TagList.RESULT_TAG);
                    String newTopic = String.format(TOP_TAG, tag.getId(), tag.getTitle(), tag.getTitle());
                    LogUtils.e(newTopic);
                    if (richEditor.getHtml() != null) {
                        richEditor.setHtml(richEditor.getHtml() + newTopic);
                    } else {
                        richEditor.setHtml(newTopic);
                    }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            initDialogBack();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void saveContent() {
        SharedPreferences sharedPreferences = getSharedPreferences(SAVE_MICRO_ARTICLE_DRAFT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("title", editTitle.getText().toString());
        editor.putString("content", richEditor.getHtml());
        editor.putString("type", TYPE_ARTICLE);
        editor.putString("topicId", topicId);
        editor.putString("atAccount", atAccount);
        editor.putString("videoCover", imageCover);
        editor.putBoolean("isOriginal", isOriginal);
        editor.putInt("videoHorizontalVertical", 3);
        editor.putBoolean("marrid", false);
        editor.commit();
        finish();

    }
}

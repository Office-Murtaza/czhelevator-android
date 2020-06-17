package com.kingyon.elevator.uis.actiivty2.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bobomee.android.mentions.edit.MentionEditText;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.QueryTopicEntity;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.actiivty2.input.TagList;
import com.kingyon.elevator.uis.adapters.adaptertwo.ChooseAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.kingyon.elevator.utils.utilstwo.StringUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.litao.android.lib.Utils.GridSpacingItemDecoration;
import com.litao.android.lib.entity.PhotoEntry;
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
import me.kareluo.imaging.IMGEditActivity;

import static com.czh.myversiontwo.utils.CodeType.ACCESS_IMAGE_PATH;
import static com.czh.myversiontwo.utils.CodeType.REQUEST_TAG_APPEND;
import static com.czh.myversiontwo.utils.CodeType.REQUEST_USER_APPEND;
import static com.czh.myversiontwo.utils.CodeType.TYPE_WSQ;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_COMMUNITY_RELEASETY;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * @Author:Mrczh
 * Instructions:社区发布
 */

@Route(path = ACTIVITY_MAIN2_COMMUNITY_RELEASETY)
public class CommunityReleasetyActivity extends BaseActivity implements ChooseAdapter.OnItmeClickListener {
    @BindView(R.id.img_bake)
    ImageView imgBake;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_releaset)
    TextView tvReleaset;
    @BindView(R.id.edit_content)
    MentionEditText editContent;
    @BindView(R.id.rcv_list_img)
    RecyclerView rcvListImg;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.img_aite)
    ImageView imgAite;
    @BindView(R.id.img_huati)
    ImageView imgHuati;
    @BindView(R.id.tv_zishu)
    TextView tvZishu;
    ChooseAdapter mAdapter;
    @Autowired
    String imagePath;
    @Autowired
    String topicId1;
    @Autowired
    String title;
    List<PhotoEntry> list = new ArrayList<>();
    List<File> files = new ArrayList<>();
    List<String> userList = new ArrayList<>();
    List<String> tagList = new ArrayList<>();

    String topicId ;
    String atAccount;
    String TAG_FORMAT = "&nbsp;<tag id='%1$s' name='%2$s'>%2$s</tag>&nbsp;";
    String newTopic;
    StringBuffer sb = new StringBuffer();


    @Override
    public int getContentViewId() {
        return R.layout.activity_community_releaset;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        OrdinaryActivity.communityReleasetyActivity = this;
        showSoftInputFromWindow(editContent);
        if (topicId1!=null){
            topicId = topicId1;
            QueryTopicEntity.PageContentBean pageContentBean = new QueryTopicEntity.PageContentBean();
            pageContentBean.setTitle(title);
            pageContentBean.setId(Integer.parseInt(topicId1));
            editContent.insert(pageContentBean);
        }
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && !TextUtils.isEmpty(s)) {
                    char mentionChar = s.toString().charAt(start);
                    int selectionStart = editContent.getSelectionStart();
                    if (mentionChar == '@') {
                        startActivityForResult(UserSelectionActiivty.getIntent(CommunityReleasetyActivity.this), REQUEST_USER_APPEND);
                        editContent.getText().delete(selectionStart - 1, selectionStart);
                    } else if (mentionChar == '#') {
                        startActivityForResult(TopicSelectionActivity.getIntent(CommunityReleasetyActivity.this), REQUEST_TAG_APPEND);
                        editContent.getText().delete(selectionStart - 1, selectionStart);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            tvZishu.setText(s.toString().length()+"/500");
            }
        });
    }
    public void showSoftInputFromWindow(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAdapter = new ChooseAdapter(this,list);
        rcvListImg.setLayoutManager(new GridLayoutManager(this, 3));
        rcvListImg.setAdapter(mAdapter);
        rcvListImg.addItemDecoration(new GridSpacingItemDecoration(3, 4, true));

    }

    @OnClick({R.id.img_bake, R.id.tv_releaset,R.id.img_icon, R.id.img_aite, R.id.img_huati})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.tv_releaset:
//                发布
                httpRelease();
                break;
            case R.id.img_icon:
//                表情
                break;
            case R.id.img_aite:
//                @
                startActivityForResult(UserSelectionActiivty.getIntent(CommunityReleasetyActivity.this), REQUEST_USER_APPEND);
                break;
            case R.id.img_huati:
//                话题
                startActivityForResult(TopicSelectionActivity.getIntent(CommunityReleasetyActivity.this), REQUEST_TAG_APPEND);
                break;
                default:
        }
    }

    private void httpRelease() {
        tagList = StringUtils.getTagString(String.valueOf(editContent.getFormatCharSequence()));
        String topicId1 = tagList.toString().replace("[","");
        topicId = topicId1.replace("]","");
        userList = StringUtils.getUserString(String.valueOf(editContent.getFormatCharSequence()));
        String atAccount1 = userList.toString().replace("[","");
        atAccount = atAccount1.replace("]","");
        if (editContent.getText().toString().isEmpty()){
            ToastUtils.showShort("内容不能为空");
        }else {
            CharSequence convertMetionString = editContent.getFormatCharSequence();
            LogUtils.e(convertMetionString, list.size(), list.toString());
            files.clear();
            for (int c = 0; c < list.size(); c++) {
                if (list.get(c).getPath() != null) {
                    File file = new File(list.get(c).getPath());
                    files.add(file);
                }
            }
            if (files.size() > 0) {
                showProgressDialog(getString(R.string.wait));
                NetService.getInstance().uploadFiles(this, files, new NetUpload.OnUploadCompletedListener() {
                    @Override
                    public void uploadSuccess(List<String> images, List<String> hash, JSONObject response) {
                        hideProgress();
                        LogUtils.e(images, hash, response);
                        int i;
                        for (i = 0; i < images.size(); i++) {
                            sb.append("&_&" + images.get(i));
                        }
                        if (i == images.size()) {
                            LogUtils.e(sb.toString().substring(3), sb.toString());
                            String str = sb.toString().substring(3);
                            OrdinaryActivity.httpContentPublish(CommunityReleasetyActivity.this, "", String.valueOf(convertMetionString),
                                    str, null, TYPE_WSQ, "2", topicId, atAccount, 0, null, 0, 3);
                        }
                    }

                    @Override
                    public void uploadFailed(ApiException ex) {
                        hideProgress();
                        LogUtils.e(ex);
                        ToastUtils.showShort(ex.getDisplayMessage());
                    }
                });
            } else {
                OrdinaryActivity.httpContentPublish(CommunityReleasetyActivity.this, "", String.valueOf(convertMetionString),
                        null, null, TYPE_WSQ, "1", topicId, atAccount, 0, null, 0, 3);
            }
        }
    }

    @Override
    public void onItemClicked(int position) {
        if (position == mAdapter.getItemCount()-1) {
            list.clear();
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
        }
    }

    private void startAction() {
        Matisse.from(this)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(false)
                .captureStrategy(
                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                .maxSelectable(6)
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
                .forResult(ACCESS_IMAGE_PATH);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && null != data) {
            switch (requestCode) {
                case REQUEST_USER_APPEND:
                    AttenionUserEntiy user = (AttenionUserEntiy) data.getSerializableExtra(UserSelectionActiivty.RESULT_USER);
                    atAccount = String.valueOf(user.followerAccount);
                    editContent.insert(user);
//                    userList.add(String.valueOf(user.id));
                    break;
                case REQUEST_TAG_APPEND:
                    QueryTopicEntity.PageContentBean tag = (QueryTopicEntity.PageContentBean) data.getSerializableExtra(TagList.RESULT_TAG);
                    LogUtils.e(tag);
                    editContent.insert(tag);
                    topicId = String.valueOf(tag.getId());
                    newTopic = String.format(TAG_FORMAT, tag.getId(), tag.getTitle());
                    LogUtils.e(newTopic);
//                    tagList.add(String.valueOf(tag.getId()));
                    break;
                case ACCESS_IMAGE_PATH:
                    LogUtils.e(Matisse.obtainPathResult(data),Matisse.obtainResult(data),Matisse.obtainOriginalState(data));
                    for (int c= 0;c<Matisse.obtainPathResult(data).size();c++){
                        PhotoEntry photoEntry = new PhotoEntry();
                        photoEntry.setPath(Matisse.obtainPathResult(data).get(c));
                        list.add(photoEntry);
                     }
                    LogUtils.e(list.toString());
                    mAdapter = new ChooseAdapter(this,list);
                    rcvListImg.setLayoutManager(new GridLayoutManager(this, 3));
                    rcvListImg.setAdapter(mAdapter);
                    rcvListImg.addItemDecoration(new GridSpacingItemDecoration(3, 4, true));
                    break;
                case CommonUtil.REQ_CODE_4:
                    String savedPath = data.getStringExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH);
                    int number = data.getIntExtra("number",0);
                    if (!TextUtils.isEmpty(savedPath) && new File(savedPath).exists()) {
                        list.get(number).setPath(savedPath);
                        mAdapter.notifyDataSetChanged();
//                        LogUtils.e(list.toString());
//                        mAdapter = new ChooseAdapter(this,list);
//                        rcvListImg.setLayoutManager(new GridLayoutManager(this, 3));
//                        rcvListImg.setAdapter(mAdapter);
//                        rcvListImg.addItemDecoration(new GridSpacingItemDecoration(3, 4, true));

                    }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        initView();
    }
}

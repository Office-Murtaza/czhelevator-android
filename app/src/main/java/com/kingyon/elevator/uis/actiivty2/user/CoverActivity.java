package com.kingyon.elevator.uis.actiivty2.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.actiivty2.activityutils.CameraViewActivit;
import com.kingyon.elevator.uis.activities.user.UserProfileActivity;
import com.kingyon.elevator.utils.PictureSelectorUtil;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_COVER;

/**
 * @Created By Admin  on 2020/7/7
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */

@Route(path = ACTIVITY_COVER)
public class CoverActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_xc)
    TextView tvXc;
    @BindView(R.id.tv_pz)
    TextView tvPz;
    private final int headCode = 1;
    @Override
    public int getContentViewId() {
        return R.layout.activity_cover;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("选择封面");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_xc, R.id.tv_pz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_xc:
                PictureSelectorUtil.showPictureSelector(this, headCode);
                break;
            case R.id.tv_pz:
                startActivityForResult(CameraViewActivit.class, 101);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e(requestCode,RESULT_CANCELED,resultCode,RESULT_OK,data);
        if (requestCode != RESULT_CANCELED||resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case headCode:
                    try {
                        ArrayList<String> mSelectPath2 = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                        if (mSelectPath2 != null && mSelectPath2.size() > 0) {
                            showProgressDialog(getString(R.string.wait),true);
                            NetService.getInstance().uploadFile(this, new File(mSelectPath2.get(0)), new NetUpload.OnUploadCompletedListener() {
                                @Override
                                public void uploadSuccess(List<String> images, List<String> hash, JSONObject response) {
                                    LogUtils.e(images, hash, response);
                                    if (images != null && images.size() > 0) {
                                        ConentUtils.httpEidtProfile(CoverActivity.this, "",
                                                "", "", "", "", "", images.get(0), new ConentUtils.AddCollect() {
                                                    @Override
                                                    public void Collect(boolean is) {
                                                        if (is) {
                                                            finish();
                                                        }
                                                    }
                                                });
                                    } else {
                                        showToast("上传封面出错");
                                        hideProgress();
                                    }
                                }

                                @Override
                                public void uploadFailed(ApiException ex) {
                                    showToast(ex.getDisplayMessage());
                                    hideProgress();
                                    showToast("上传封面出错");
                                }
                            });
                        }
                    }catch (Exception e){
                        LogUtils.e(e.toString());
                    }
                    break;
                case 101:
                    try {
                        String path =data.getStringExtra("path");
                        LogUtils.e(path);
                        showProgressDialog(getString(R.string.wait),true);
                        NetService.getInstance().uploadFile(this, new File(path), new NetUpload.OnUploadCompletedListener() {
                            @Override
                            public void uploadSuccess(List<String> images,List<String> hash,JSONObject response) {
                                hideProgress();
                                if (images != null && images.size() > 0) {
                                    ConentUtils.httpEidtProfile(CoverActivity.this, "",
                                            "", "", "", "", "", images.get(0), new ConentUtils.AddCollect() {
                                                @Override
                                                public void Collect(boolean is) {
                                                    if (is) {
                                                        finish();
                                                    }
                                                }
                                            });

                                } else {
                                    hideProgress();
                                    showToast("上传失败");
                                }
                            }
                            @Override
                            public void uploadFailed(ApiException ex) {
                                hideProgress();
                                showToast("上传失败");
                            }
                        }, false);
                    }catch (Exception e){
                        LogUtils.e(e.toString());
                    }
                    break;
            }
        }
    }
}

package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.adapterone.UploadImageAdapter;
import com.kingyon.elevator.uis.widgets.FullyGridLayoutManager;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DealScrollRecyclerView;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.kingyon.elevator.utils.PictureSelectorUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class FeedBackEditActivity extends BaseSwipeBackActivity implements BaseAdapterWithHF.OnItemClickListener<Object>, NetUpload.OnUploadCompletedListener {
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.tv_length)
    TextView tvLength;
    @BindView(R.id.rv_images)
    RecyclerView rvImages;
    @BindView(R.id.tv_create)
    TextView tvCreate;
    @BindView(R.id.pre_v_back)
    ImageView preVBack;
    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.pre_v_right)
    TextView preVRight;

    private UploadImageAdapter uploadAdapter;

    @Override
    protected String getTitleText() {
        return "意见反馈";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_feed_back_edit;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        preVRight.setText("我的反馈");
        preVRight.setTextColor(Color.parseColor("#FF1330"));
        rvImages.addItemDecoration(new GridSpacingItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.spacing_small), false));
        uploadAdapter = new UploadImageAdapter(this);
        uploadAdapter.
                setMaxCount(12);
        DealScrollRecyclerView.getInstance().dealAdapter(uploadAdapter, rvImages, new FullyGridLayoutManager(this, 3));
        uploadAdapter.setOnItemClickListener(this);
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvLength.setText(String.format("%s/400", s.length()));
            }
        });
    }

    private void publishRequest(String pictures) {
        showProgressDialog(getString(R.string.wait));
        tvCreate.setEnabled(false);
        NetService.getInstance().createFeedBack(etTitle.getText().toString(), pictures)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        tvCreate.setEnabled(true);
                        hideProgress();
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        tvCreate.setEnabled(true);
                        showToast("提交成功");
                        hideProgress();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position, Object entity, BaseAdapterWithHF<Object> baseAdaper) {
        if (view.getId() == R.id.img_delete) {
            uploadAdapter.deleteItemData(entity);
        } else {
            if (position >= uploadAdapter.getItemCount() - uploadAdapter.getFooterCount()) {
                PictureSelectorUtil.showPictureSelectorNoCrop(this, CommonUtil.REQ_CODE_1, uploadAdapter.getMaxCount() - uploadAdapter.getItemRealCount());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode && data != null) {
            ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            if (mSelectPath != null && mSelectPath.size() > 0) {
                uploadAdapter.addDatas(mSelectPath);
            }
        }
    }

    @Override
    public void uploadSuccess(final List<String> images, List<String> hash, JSONObject response) {
        LogUtils.e(images, hash, response);
        List<String> result = new ArrayList<>();
        int index = 0;
        for (Object object : uploadAdapter.getDatas()) {
            String link = object.toString();
            if (!TextUtils.isEmpty(link) && !link.startsWith("http")) {
                result.add(images.get(index++));
            } else {
                result.add(link);
            }
        }
        publishRequest(NetService.getInstance().getUploadResultString(result));
    }

    @Override
    public void uploadFailed(ApiException ex) {
        hideProgress();
        showToast(ex.getDisplayMessage());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.pre_v_back, R.id.pre_v_right, R.id.tv_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_back:
                finish();
                break;
            case R.id.pre_v_right:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.tv_create:
                if (TextUtils.isEmpty(CommonUtil.getEditText(etTitle))) {
                    showToast("请输入评价内容");
                    return;
                }
                if (uploadAdapter.getItemRealCount() > 0) {
                    List<File> uploadFiles = uploadAdapter.getUploadDatas();
                    if (uploadFiles.size() > 0) {
                        showProgressDialog(getString(R.string.wait));
                        NetService.getInstance().uploadFiles(this, uploadFiles, this);
                    } else {
                        publishRequest(NetService.getInstance().getUploadResultString(uploadAdapter.getAllDatas()));
                    }
                } else {
                    publishRequest(null);
                }
                break;
        }
    }
}

package com.kingyon.elevator.uis.activities.advertising;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.AdTempletEntity;
import com.kingyon.elevator.entities.LocalMaterialEntity;
import com.kingyon.elevator.entities.MateriaEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.dialogs.TipDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DBUtils;
import com.kingyon.elevator.utils.PictureSelectorUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import me.kareluo.imaging.IMGEditActivity;
import me.nereo.multi_image_selector.MultiImageSelector;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class ImageChooseActivity extends BaseStateRefreshingLoadingActivity<LocalMaterialEntity> implements TipDialog.OnOperatClickListener<String> {
    @BindView(R.id.tv_ratio_tip)
    TextView tvRatioTip;

    private boolean splitScreen;
    private TipDialog<String> tipDialog;

    @Override
    protected String getTitleText() {
        splitScreen = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_1, false);
        return "选择图片";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_image_choose;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        tvRatioTip.setText(String.format("建议%s", getString(splitScreen ? R.string.ad_ratio_split_image : R.string.ad_ratio_full)).replaceAll("[\r|\n]", ""));
    }

    @Override
    protected MultiItemTypeAdapter<LocalMaterialEntity> getAdapter() {
        return new BaseAdapter<LocalMaterialEntity>(this, R.layout.activity_video_choose_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, LocalMaterialEntity item, int position) {
                View view = holder.getView(R.id.cv_cover);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (layoutParams != null) {
                    float property = splitScreen ? Constants.adImageProperty : Constants.adScreenProperty;
                    layoutParams.height = (int) (layoutParams.width / property);
                    view.setLayoutParams(layoutParams);
                }
                holder.setTextNotHide(R.id.tv_name, item.getName());
                holder.setImage(R.id.img_cover, item.getUrl());
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, LocalMaterialEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            returnResult(item.getUrl());
        }
    }

    private void returnResult(String path) {
        Intent intent = new Intent();
        intent.putExtra(CommonUtil.KEY_VALUE_1, path);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void loadData(final int page) {
        Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<String, Observable<PageListEntity<LocalMaterialEntity>>>() {
                    @Override
                    public Observable<PageListEntity<LocalMaterialEntity>> call(String s) {
                        String screenType = splitScreen ? Constants.AD_SCREEN_TYPE.VIDEO_IMAGE : Constants.AD_SCREEN_TYPE.FULL_IMAGE;
                        return Observable.just(DBUtils.getInstance().getLocalMateriasImageChoose(screenType, page));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomApiCallback<PageListEntity<LocalMaterialEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<LocalMaterialEntity> localMaterialEntityPageListEntity) {
                        if (localMaterialEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (localMaterialEntityPageListEntity.getContent() != null) {
                            mItems.addAll(localMaterialEntityPageListEntity.getContent());
                        }
                        loadingComplete(true, localMaterialEntityPageListEntity.getTotalPages());
                    }
                });
//        NetService.getInstance().getMaterials("", splitScreen ? Constants.MATERIAL_SCREEN_TYPE.SPLIT : Constants.MATERIAL_SCREEN_TYPE.FULL
//                , Constants.Materia_Type.IMAGE, page)
//                .compose(this.<PageListEntity<MateriaEntity>>bindLifeCycle())
//                .subscribe(new CustomApiCallback<PageListEntity<MateriaEntity>>() {
//                    @Override
//                    protected void onResultError(ApiException ex) {
//                        showToast(ex.getDisplayMessage());
//                        loadingComplete(false, 100000);
//                    }
//
//                    @Override
//                    public void onNext(PageListEntity<MateriaEntity> materiaEntityPageListEntity) {
//                        if (materiaEntityPageListEntity == null) {
//                            throw new ResultException(9001, "返回参数异常");
//                        }
//                        if (FIRST_PAGE == page) {
//                            mItems.clear();
//                        }
//                        if (materiaEntityPageListEntity.getContent() != null) {
//                            mItems.addAll(materiaEntityPageListEntity.getContent());
//                        }
//                        loadingComplete(true, materiaEntityPageListEntity.getTotalPages());
//                    }
//                });
    }

    @OnClick({R.id.tv_template, R.id.tv_memory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_template:
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean(CommonUtil.KEY_VALUE_1, splitScreen);
//                    startActivityForResult(TemplateListActivity.class, CommonUtil.REQ_CODE_2, bundle);

                Bundle bundle = new Bundle();
                bundle.putBoolean(CommonUtil.KEY_VALUE_1, splitScreen);
                startActivityForResult(TemplateListActivity.class, CommonUtil.REQ_CODE_3, bundle);
                break;
            case R.id.tv_memory:
                PictureSelectorUtil.showPictureSelectorCropProperty(this, CommonUtil.REQ_CODE_1, splitScreen ? 2 * Constants.adScreenProperty : Constants.adScreenProperty);
                break;
        }
    }

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .drawable(R.drawable.white_margin_sixteen_divider)
                .sizeResId(R.dimen.spacing_divider)
                .build());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case CommonUtil.REQ_CODE_1:
                    ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    if (mSelectPath != null && mSelectPath.size() > 0) {
//                        showEditDialog(mSelectPath.get(0));
                        returnResult(mSelectPath.get(0));
                    }
                    break;
                case CommonUtil.REQ_CODE_2:
                    AdTempletEntity templet = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (templet != null) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(CommonUtil.KEY_VALUE_1, templet);
                        bundle.putBoolean(CommonUtil.KEY_VALUE_2, splitScreen);
                        startActivityForResult(TemplateDetailsActivity.class, CommonUtil.REQ_CODE_3, bundle);
                    }
                    break;
                case CommonUtil.REQ_CODE_3:
                    String templateResultPath = data.getStringExtra(CommonUtil.KEY_VALUE_1);
                    if (!TextUtils.isEmpty(templateResultPath) && new File(templateResultPath).exists()) {
                        returnResult(templateResultPath);
                    }
                    break;
                case CommonUtil.REQ_CODE_4:
                    String savedPath = data.getStringExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH);
                    if (!TextUtils.isEmpty(savedPath) && new File(savedPath).exists()) {
                        returnResult(savedPath);
                    }
                    break;
            }
        }
    }

    private void showEditDialog(String path) {
        if (tipDialog == null) {
            tipDialog = new TipDialog<>(this, this);
        }
        tipDialog.show("是否要编辑图片？", "继续编辑", "直接使用", path);
    }

    @Override
    public void onEnsureClick(String param) {
        File imageFile = new File(param);
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

    @Override
    public void onCancelClick(String param) {
        returnResult(param);
    }
}
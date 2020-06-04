package com.kingyon.elevator.uis.activities.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.AdTempletEntity;
import com.kingyon.elevator.entities.NormalElemEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adapterone.TemplateListAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class TemplateListActivity extends BaseStateRefreshingLoadingActivity<Object> {
    @BindView(R.id.pre_v_right)
    TextView preVRight;
    @BindView(R.id.img_classify)
    ImageView imgClassify;
    @BindView(R.id.img_newest)
    ImageView imgNewest;
    @BindView(R.id.tv_classify)
    TextView tvClassify;
    @BindView(R.id.fl_classify)
    FrameLayout flClassify;
    @BindView(R.id.fl_newest)
    FrameLayout flNewest;

    private boolean splitScreen;

    private NormalElemEntity type;

    @Override
    protected String getTitleText() {
        splitScreen = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_1, false);
        return "选择模板";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_template_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setText("分类");
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        return new TemplateListAdapter(this, mItems, splitScreen);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null && item instanceof AdTempletEntity) {
//            Intent intent = new Intent();
//            intent.putExtra(CommonUtil.KEY_VALUE_1, (AdTempletEntity) item);
//            setResult(RESULT_OK, intent);
//            finish();

            Bundle bundle = new Bundle();
            bundle.putParcelable(CommonUtil.KEY_VALUE_1, (AdTempletEntity) item);
            bundle.putBoolean(CommonUtil.KEY_VALUE_2, splitScreen);
            startActivityForResult(TemplateDetailsActivity.class, CommonUtil.REQ_CODE_2, bundle);
        }
    }

    @Override
    protected void loadData(final int page) {
        updateClassifyUi();
        NetService.getInstance().getPicAdTemplet(splitScreen ? Constants.MATERIAL_SCREEN_TYPE.SPLIT : Constants.MATERIAL_SCREEN_TYPE.FULL
                , Constants.TEMPLATE_SORT.NEWEST
                , type != null ? type.getObjectId() : null, page)
                .compose(this.<ConentEntity<AdTempletEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<AdTempletEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 10000);
                    }

                    @Override
                    public void onNext(ConentEntity<AdTempletEntity> adTempletEntityPageListEntity) {
                        if (adTempletEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (adTempletEntityPageListEntity.getContent() != null) {
                            mItems.addAll(adTempletEntityPageListEntity.getContent());
                        }
                        loadingComplete(true, adTempletEntityPageListEntity.getTotalPages());
                    }
                });
//        NetService.getInstance().getPicAdTemplet(splitScreen ? Constants.MATERIAL_SCREEN_TYPE.SPLIT : Constants.MATERIAL_SCREEN_TYPE.FULL
//                , type == null ? (flNewest.isSelected() ? Constants.TEMPLATE_SORT.NEWEST : Constants.TEMPLATE_SORT.RECOMMEND) : ""
//                , type != null ? type.getObjectId() : null, page)
//                .compose(this.<PageListEntity<AdTempletEntity>>bindLifeCycle())
//                .subscribe(new CustomApiCallback<PageListEntity<AdTempletEntity>>() {
//                    @Override
//                    protected void onResultError(ApiException ex) {
//                        showToast(ex.getDisplayMessage());
//                        loadingComplete(false, 10000);
//                    }
//
//                    @Override
//                    public void onNext(PageListEntity<AdTempletEntity> adTempletEntityPageListEntity) {
//                        if (adTempletEntityPageListEntity == null) {
//                            throw new ResultException(9001, "返回参数异常");
//                        }
//                        if (FIRST_PAGE == page) {
//                            mItems.clear();
//                            if (!flNewest.isSelected() && type == null) {
//                                mItems.add("tip");
//                            }
//                        }
//                        if (adTempletEntityPageListEntity.getContent() != null) {
//                            mItems.addAll(adTempletEntityPageListEntity.getContent());
//                        }
//                        loadingComplete(true, adTempletEntityPageListEntity.getTotalPages());
//                    }
//                });
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position >= 0 && position < mItems.size()) ? (mItems.get(position) instanceof String ? 2 : 1) : 2;
            }
        });
        return gridLayoutManager;
    }

    @OnClick({R.id.fl_classify, R.id.fl_newest, R.id.pre_v_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_right:
            case R.id.fl_classify:
                Bundle bundle = new Bundle();
                if (type != null) {
                    bundle.putParcelable(CommonUtil.KEY_VALUE_1, type);
                }
                startActivityForResult(TemplateListClassifyActivity.class, CommonUtil.REQ_CODE_1, bundle);
                break;
            case R.id.fl_newest:
                flNewest.setSelected(!flNewest.isSelected());
                imgNewest.setSelected(flNewest.isSelected());
                autoRefresh();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case CommonUtil.REQ_CODE_1:
                    NormalElemEntity entity = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (entity != null) {
                        if (type == null || type.getObjectId() != entity.getObjectId()) {
                            type = entity;
                            autoRefresh();
                        }
                    } else {
                        if (type != null) {
                            type = null;
                            autoRefresh();
                        }
                    }
                    break;
                case CommonUtil.REQ_CODE_2:
                    String templetResultPath = data.getStringExtra(CommonUtil.KEY_VALUE_1);
                    if (!TextUtils.isEmpty(templetResultPath) && new File(templetResultPath).exists()) {
                        Intent intent = new Intent();
                        intent.putExtra(CommonUtil.KEY_VALUE_1, templetResultPath);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
            }
        }
    }

    private void updateClassifyUi() {
        flClassify.setSelected(type != null);
        imgClassify.setSelected(type != null);
        tvClassify.setText(type != null ? type.getName() : "分类");
        if (tvTitle != null) {
            tvTitle.setText(type != null ? type.getName() : "最新");
        }
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }
}

package com.kingyon.elevator.uis.fragments.user;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gerry.scaledelete.DeletedImageScanDialog;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ImageScan;
import com.kingyon.elevator.entities.LocalMaterialEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.uis.activities.advertising.NetVideoPlayActivity;
import com.kingyon.elevator.uis.adapters.MateriaLisAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DBUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshLoadingFragment;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.leo.afbaselibrary.widgets.lazyViewPager.LazyFragmentPagerAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class MateriaListFragment extends BaseStateRefreshLoadingFragment<Object> implements LazyFragmentPagerAdapter.Laziable {

    private String type;

    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.tv_delete_all)
    TextView tvDeleteAll;
    @BindView(R.id.tv_delete_number)
    TextView tvDeleteNumber;

    private List<LocalMaterialEntity> datas = new ArrayList<>();
    private boolean editMode;
    private int editNum;

    public static MateriaListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(CommonUtil.KEY_VALUE_1, type);
        MateriaListFragment fragment = new MateriaListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_material_list;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            type = getArguments().getString(CommonUtil.KEY_VALUE_1);
        }
        super.init(savedInstanceState);
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        return new MateriaLisAdapter(getContext(), mItems);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null && item instanceof LocalMaterialEntity) {
            LocalMaterialEntity entity = (LocalMaterialEntity) item;
            if (editMode) {
                onEditChooseClick(entity);
            } else {
                onNormalClick(entity);
            }
        }
    }

    private void onNormalClick(LocalMaterialEntity entity) {
        switch (type) {
            case Constants.Materia_Type.IMAGE:
                DeletedImageScanDialog deletedImageScanDialog = new DeletedImageScanDialog(getContext(), new ImageScan(entity.getUrl()), null);
                deletedImageScanDialog.showOne();
                break;
            case Constants.Materia_Type.VIDEO:
                Bundle bundle = new Bundle();
                bundle.putString(CommonUtil.KEY_VALUE_1, entity.getUrl());
                startActivity(NetVideoPlayActivity.class, bundle);
                break;
        }
    }

    private void onEditChooseClick(LocalMaterialEntity entity) {
        entity.setDelete(!entity.isDelete());
        mAdapter.notifyDataSetChanged();
        updateEditUI();
    }

//    private void showDeleteDialog() {
//        if (getContext() == null) {
//            return;
//        }
//        new AlertDialog.Builder(getContext())
//                .setTitle("提示")
//                .setMessage("确定要删除本素材吗？")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        try {
////                            item.delete();
//                            requestLocalDelete();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Logger.e("删除本地素材失败");
//                        }
//                        onfresh();
//                    }
//                })
//                .setNegativeButton("取消", null)
//                .show();
//    }

    @Override
    protected void loadData(final int page) {
        Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<String, Observable<PageListEntity<LocalMaterialEntity>>>() {
                    @Override
                    public Observable<PageListEntity<LocalMaterialEntity>> call(String s) {
                        return Observable.just(DBUtils.getInstance().getLocalMateriasByType(type, page));
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
                            datas.clear();
                        }
                        List<LocalMaterialEntity> content = localMaterialEntityPageListEntity.getContent();
                        if (content != null) {
                            datas.addAll(content);
                            for (LocalMaterialEntity entity : content) {
                                entity.setDelete(false);
                                String lastGroup = getLastTimeGroup(entity);
                                String myGroup = TimeUtil.getYMdTime(entity.getCreateTime());
                                if (!TextUtils.equals(lastGroup, myGroup)) {
                                    mItems.add(myGroup);
                                }
                                mItems.add(entity);
                            }
                        }
                        loadingComplete(true, localMaterialEntityPageListEntity.getTotalPages());
                        if (editMode) {
                            updateEditUI();
                        }
                    }
                });
//        NetService.getInstance().getMaterials("", Constants.MATERIAL_SCREEN_TYPE.ALL, type, page)
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
//                            datas.clear();
//                        }
//                        List<MateriaEntity> content = materiaEntityPageListEntity.getContent();
//                        if (content != null) {
//                            datas.addAll(content);
//                            for (MateriaEntity entity : content) {
//                                String lastGroup = getLastTimeGroup(entity);
//                                String myGroup = TimeUtil.getYMdTime(entity.getCreateTime());
//                                if (!TextUtils.equals(lastGroup, myGroup)) {
//                                    mItems.add(myGroup);
//                                }
//                                mItems.add(entity);
//                            }
//                        }
//                        loadingComplete(true, materiaEntityPageListEntity.getTotalPages());
//                    }
//                });
    }

    private String getLastTimeGroup(LocalMaterialEntity entity) {
        int index = datas.indexOf(entity) - 1;
        String lastGroup;
        if (index >= 0 && index < datas.size()) {
            lastGroup = TimeUtil.getYMdTime(datas.get(index).getCreateTime());
        } else {
            lastGroup = "";
        }
        return lastGroup;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position >= 0 && position < mItems.size()) ? (mItems.get(position) instanceof String ? 3 : 1) : 3;
            }
        });
        return gridLayoutManager;
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        updateMode();
    }

    public void updateMode() {
        if (mAdapter == null || mInnerAdapter == null)
            return;
        llDelete.setVisibility(editMode ? View.VISIBLE : View.GONE);
        ((MateriaLisAdapter) mInnerAdapter).setEditMode(editMode);
        mAdapter.notifyDataSetChanged();
        if (editMode) {
            updateEditUI();
        }
    }

    private void updateEditUI() {
        boolean allEdit = true;
        editNum = 0;
        for (Object obj : mItems) {
            if (obj instanceof LocalMaterialEntity) {
                LocalMaterialEntity item = (LocalMaterialEntity) obj;
                if (item.isDelete()) {
                    editNum++;
                } else {
                    allEdit = false;
                }
            }
        }
        tvDeleteAll.setSelected(editNum > 0 && allEdit);
        tvDeleteNumber.setText(String.format("已选%s个", editNum));
    }

    @OnClick({R.id.tv_delete, R.id.tv_delete_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_delete:
//                showDeleteDialog();
                if (editNum <= 0) {
                    showToast("清先选择要删除的素材");
                } else {
                    requestLocalDelete();
                }
                break;
            case R.id.tv_delete_all:
                setAllLomatEdit(!tvDeleteAll.isSelected());
                break;
        }
    }

    private void setAllLomatEdit(boolean edit) {
        for (Object obj : mItems) {
            if (obj instanceof LocalMaterialEntity) {
                ((LocalMaterialEntity) obj).setDelete(edit);
            }
        }
        mAdapter.notifyDataSetChanged();
        updateEditUI();
    }

    private void requestLocalDelete() {
        try {
            for (Object obj : mItems) {
                if (obj instanceof LocalMaterialEntity) {
                    LocalMaterialEntity entity = (LocalMaterialEntity) obj;
                    if (entity.isDelete()) {
                        entity.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("删除本地素材失败");
        }
        autoRefresh();
    }
}

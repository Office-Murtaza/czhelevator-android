package com.kingyon.elevator.uis.activities.advertising;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;
import com.kingyon.elevator.entities.LocalMaterialEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
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
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class VideoChooseActivity extends BaseStateRefreshingLoadingActivity<LocalMaterialEntity> {

    @BindView(R.id.tv_ratio_tip)
    TextView tvRatioTip;

    private boolean splitScreen;
    private String planType;
    private long videoDuration;

    @Override
    protected String getTitleText() {
        splitScreen = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_1, false);
        planType = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        videoDuration = (TextUtils.equals(Constants.PLAN_TYPE.DIY, planType) ? 60 : 15) * 1000;
        return "选择视频";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_video_choose;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        EventBus.getDefault().register(this);
        tvRatioTip.setText(String.format("建议%s", getString(splitScreen ? R.string.ad_ratio_split_video : R.string.ad_ratio_full)).replaceAll("[\r|\n]", ""));
    }

    @Override
    protected MultiItemTypeAdapter<LocalMaterialEntity> getAdapter() {
        return new BaseAdapter<LocalMaterialEntity>(this, R.layout.activity_video_choose_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, LocalMaterialEntity item, int position) {
                View view = holder.getView(R.id.cv_cover);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (layoutParams != null) {
                    float property = splitScreen ? Constants.adVideoProperty : Constants.adScreenProperty;
                    layoutParams.height = (int) (layoutParams.width / property);
                    view.setLayoutParams(layoutParams);
                }
                holder.setTextNotHide(R.id.tv_name, item.getName());
                holder.setVideoImage(R.id.img_cover, item.getUrl());
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
                        String screenType = splitScreen ? Constants.AD_SCREEN_TYPE.VIDEO_IMAGE : Constants.AD_SCREEN_TYPE.FULL_VIDEO;
                        return Observable.just(DBUtils.getInstance().getLocalMateriasVideoChoose(planType, screenType, page));
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
//        NetService.getInstance().getMaterials(planType, splitScreen ? Constants.MATERIAL_SCREEN_TYPE.SPLIT : Constants.MATERIAL_SCREEN_TYPE.FULL
//                , Constants.Materia_Type.VIDEO, page)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventHandler(EventBusObjectEntity eventBusObjectEntity) {

    }

    @OnClick({R.id.tv_create, R.id.tv_local, R.id.tv_memory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_create:
                // TODO: 2019/2/18 录制视频
                if (splitScreen) {
                } else {
                }
                break;
            case R.id.tv_local:
                PictureSelectorUtil.showVideoSelector(this, CommonUtil.REQ_CODE_3);
                break;
            case R.id.tv_memory:
                // TODO: 2019/2/18 选择视频
                String[] locationPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
                checkPermission(new CheckPermListener() {
                    @Override
                    public void agreeAllPermission() {
                        tvTitle.post(new Runnable() {
                            @Override
                            public void run() {
                                jumpToGallery();
                            }
                        });
                    }
                }, "制作视频需要以下权限", locationPermission);
                break;
        }
    }

    private void jumpToGallery() {
        Bundle bundle = new Bundle();
//        bundle.putInt(VideoRecordActivity.PREVIEW_SIZE_RATIO, 1);
//        bundle.putInt(VideoRecordActivity.PREVIEW_SIZE_LEVEL, 5);
//        bundle.putInt(VideoRecordActivity.ENCODING_MODE, 0);
//        bundle.putInt(VideoRecordActivity.ENCODING_SIZE_LEVEL, 14);
//        bundle.putInt(VideoRecordActivity.ENCODING_BITRATE_LEVEL, 4);
//        bundle.putInt(VideoRecordActivity.AUDIO_CHANNEL_NUM, 0);
//        bundle.putLong(VideoRecordActivity.RECORD_MAX_DURATION, videoDuration);
//        startActivityForResult(VideoRecordActivity.class, CommonUtil.REQ_CODE_4, bundle);
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
        switch (requestCode) {
            case CommonUtil.REQ_CODE_1:
                break;
            case CommonUtil.REQ_CODE_2:
                break;
            case CommonUtil.REQ_CODE_3:
                if (RESULT_OK == resultCode && data != null) {
                    List<String> localPathes = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    if (localPathes != null && localPathes.size() > 0) {
                        String localPath = localPathes.get(0);
//                        showLocalEditDialog(localPath);
                        LogUtils.d("当前视频的路径：", localPath);
                        returnResult(localPath);
                    }
                }
                break;
//            case VideoRecordActivity.RECORD_EDIT_REQUEST:
//            case CommonUtil.REQ_CODE_5:
//            case CommonUtil.REQ_CODE_4:
//                if (RESULT_OK == resultCode && data != null) {
//                    String recordPath = data.getStringExtra(VideoRecordActivity.RECORD_RESULT);
//                    if (!TextUtils.isEmpty(recordPath)) {
//                        returnResult(recordPath);
//                    }
//                }
//                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void showLocalEditDialog(String localPath) {
        Bundle bundle = new Bundle();
//        if (MediaUtils.getInstance().getVideoDuring(localPath) > videoDuration) {
//            bundle.putString(VideoTrimActivity.VIDEO_FILE_PATH, localPath);
//            bundle.putLong(VideoTrimActivity.VIDEO_TRIM_DURATION, videoDuration);
//            startActivityForResult(VideoTrimActivity.class, CommonUtil.REQ_CODE_5, bundle);
//        } else {
//            bundle.putString(VideoEditActivity.MP4_PATH, localPath);
//            bundle.putInt(VideoEditActivity.PREVIOUS_ORIENTATION, 1);
//            bundle.putInt(VideoRecordActivity.RECORD_REQUEST, VideoRecordActivity.RECORD_EDIT_REQUEST);
//            startActivityForResult(VideoEditActivity.class, VideoRecordActivity.RECORD_EDIT_REQUEST, bundle);
//        }
    }
}

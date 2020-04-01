package com.kingyon.elevator.uis.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.photopicker.MediaData;
import com.kingyon.elevator.photopicker.MediaDirectory;
import com.kingyon.elevator.photopicker.MimeType;
import com.kingyon.elevator.presenter.PhotoPickerPresenter;
import com.kingyon.elevator.uis.activities.advertising.PreviewVideoActivity;
import com.kingyon.elevator.uis.adapters.PhotoPickerAdapter;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.videocrop.EditVideoActivity;
import com.kingyon.elevator.view.PhotoPickerView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片选择界面
 */
public class PhotoPickerFragment extends MvpBaseFragment<PhotoPickerPresenter> implements PhotoPickerView {

    private MediaDirectory mediaDirectory;
    @BindView(R.id.photo_grid_view)
    GridView photo_grid_view;
    @BindView(R.id.no_data_tips)
    TextView no_data_tips;
    PhotoPickerAdapter pickerAdapter;
    private int showType = 1;//1表示显示视频 2表示图片
    private ArrayList<MediaData> mediaDataArrayList;//处理过后的数据
    ExecutorService executorService;
    private ArrayList<MediaData> noHanlderMediaDataArrayList;//未处理的数据
    private float cropProperty = 1;
    private boolean splitScreen;
    private long videoTime = 15000;
    private int fromType = Constants.FROM_TYPE_TO_SELECT_MEDIA.PLAN;//来自于哪个界面
    private String planType = "";

    @Override
    public PhotoPickerPresenter initPresenter() {
        return new PhotoPickerPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        mediaDirectory = RuntimeUtils.currentMediaDirectory;
        showType = getArguments().getInt("showType");
        fromType = getArguments().getInt("fromType");
        planType = getArguments().getString("planType");
        if (planType.equals(Constants.PLAN_TYPE.BUSINESS)) {
            videoTime = 15000;
        } else if (planType.equals(Constants.PLAN_TYPE.DIY)) {
            videoTime = 60000;
        }
        mediaDataArrayList = new ArrayList<>();
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        photo_grid_view.setOnItemClickListener((parent, view, position, id) -> {
            MediaData mediaData = mediaDataArrayList.get(position);
            if (showType == 1) {
                if (mediaData.getDuration() > videoTime) {
                    RuntimeUtils.selectVideoPath = mediaData.getOriginalPath();
                    MyActivityUtils.goVideoEditorActivity(getActivity(), fromType, planType);
                    getActivity().finish();
                } else {
                    if (fromType == Constants.FROM_TYPE_TO_SELECT_MEDIA.PLAN) {
                        LogUtils.d("+++++++++++++++++++++++",mediaData.getOriginalPath(),mediaData.getDuration());
//                        MyActivityUtils.goPreviewVideoActivity(getActivity(), PreviewVideoActivity.class, mediaData.getOriginalPath(), mediaData.getDuration());
                        Intent intent = new Intent(getActivity(), EditVideoActivity.class);
                        intent.putExtra("path",mediaData.getOriginalPath());
                        intent.putExtra("fromType",fromType);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        //来自于我的广告，发送通知 告诉选择成功
//                        EventBus.getDefault().post(new EventBusObjectEntity(EventBusConstants.VideoOrImageSelectSuccess, mediaData));
                        Intent intent = new Intent(getActivity(), EditVideoActivity.class);
                        intent.putExtra("path",mediaData.getOriginalPath());
                        intent.putExtra("fromType",fromType);
                        startActivity(intent);
                        getActivity().finish();

                    }
                }
            } else {
                openCrop(mediaDataArrayList.get(position).getOriginalPath());
            }
        });
        if (mediaDirectory != null) {
            noHanlderMediaDataArrayList = mediaDirectory.getMediaData();
            handlerData();
        } else {
            showNoDataTips();
        }
    }

    private void hideNoDataTips() {
        no_data_tips.post(() -> {
            no_data_tips.setVisibility(View.GONE);
            if (pickerAdapter != null) {
                pickerAdapter.reflashData(mediaDataArrayList);
                photo_grid_view.smoothScrollToPosition(0);
            } else {
                pickerAdapter = new PhotoPickerAdapter(getActivity(), showType, mediaDataArrayList);
                photo_grid_view.setAdapter(pickerAdapter);
            }
        });
    }


    private void openCrop(String path) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(Color.parseColor("#21282C"));
        options.setStatusBarColor(Color.parseColor("#21282C"));
        UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(new File(getContext().getCacheDir(), System.currentTimeMillis() + ".png")))
                .withAspectRatio(Constants.adScreenProperty, 1)
//                .withMaxResultSize(50000, (int) (50000 / cropProperty))
                .withOptions(options)
                .start(getActivity());
    }

    private void showNoDataTips() {
        no_data_tips.post(() -> {
            if (showType == 1) {
                no_data_tips.setText("当前目录下暂无视频");
            } else {
                no_data_tips.setText("当前目录下暂无图片");
            }
            no_data_tips.setVisibility(View.VISIBLE);
        });
    }


    Runnable handlerDataRunnable = new Runnable() {
        @Override
        public void run() {
            ArrayList<MediaData> mediaDataArrayLists = new ArrayList<>();
            if (showType == 1) {
                for (MediaData md : noHanlderMediaDataArrayList) {
                    if (MimeType.isVideo(md.getImageType())) {
                        mediaDataArrayLists.add(md);
                    }
                }
            } else {
                for (MediaData md : noHanlderMediaDataArrayList) {
                    if (!MimeType.isVideo(md.getImageType())) {
                        mediaDataArrayLists.add(md);
                    }
                }
            }
            mediaDataArrayList = mediaDataArrayLists;
            if (mediaDataArrayList.size() > 0) {
                hideNoDataTips();
            } else {
                showNoDataTips();
            }
        }
    };


    /**
     * 排除不是当前类型的数据
     */
    private void handlerData() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        executorService.execute(handlerDataRunnable);
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_photo_picker;
    }

    public static PhotoPickerFragment newInstance(int showType, int fromType, String planType) {
        Bundle args = new Bundle();
        args.putInt("showType", showType);
        args.putInt("fromType", fromType);
        args.putString("planType", planType);
        PhotoPickerFragment fragment = new PhotoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void loadFolderListSuccess(List<MediaDirectory> mResultFolder) {

    }

    @Override
    public void loadFolderListFailed() {

    }

    @Override
    public void loadAdInfoSuccess(String coverPath, String count) {

    }

    @Override
    public void loadAdInfoFailed() {

    }

    /**
     * 修改为选中文件夹下的数据
     *
     * @param mediaDirectory
     */
    public void changeData(MediaDirectory mediaDirectory) {
        if (mediaDirectory != null) {
            mediaDataArrayList.clear();
            if (pickerAdapter != null) {
                pickerAdapter.reflashData(mediaDataArrayList);
            }
            this.mediaDirectory = mediaDirectory;
            noHanlderMediaDataArrayList = mediaDirectory.getMediaData();
            handlerData();
        } else {
            showNoDataTips();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
    }
}

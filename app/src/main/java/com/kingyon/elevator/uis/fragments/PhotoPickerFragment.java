package com.kingyon.elevator.uis.fragments;


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.photopicker.MediaData;
import com.kingyon.elevator.photopicker.MediaDirectory;
import com.kingyon.elevator.photopicker.MimeType;
import com.kingyon.elevator.presenter.PhotoPickerPresenter;
import com.kingyon.elevator.uis.adapters.PhotoPickerAdapter;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.MyToastUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.videocrop.VideoEditorActivity;
import com.kingyon.elevator.view.PhotoPickerView;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

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

    @Override
    public PhotoPickerPresenter initPresenter() {
        return new PhotoPickerPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        mediaDirectory = RuntimeUtils.currentMediaDirectory;
        showType = getArguments().getInt("showType");
        if (RuntimeUtils.goPlaceAnOrderEntity != null) {
            if (RuntimeUtils.goPlaceAnOrderEntity.getPlanType().equals(Constants.PLAN_TYPE.BUSINESS)) {
                videoTime = 15000;
            } else if (RuntimeUtils.goPlaceAnOrderEntity.getPlanType().equals(Constants.PLAN_TYPE.DIY)) {
                videoTime = 60000;
            }
        }
        mediaDataArrayList = new ArrayList<>();
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        photo_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaData mediaData = mediaDataArrayList.get(position);
                if (showType == 1) {
                    MyToastUtils.showShort("点击了第：" + (position + 1) + "个视频");
                    if (mediaData.getDuration() > videoTime) {
                        RuntimeUtils.selectVideoPath = mediaData.getOriginalPath();
                        MyActivityUtils.goActivity(getActivity(), VideoEditorActivity.class);
                    } else {
                        //returnResult(localPath);
                    }
                } else {
                    MyToastUtils.showShort("点击了第：" + (position + 1) + "张图片");
                    openCrop(mediaDataArrayList.get(position).getOriginalPath());
                }
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

    public static PhotoPickerFragment newInstance(int showType) {
        Bundle args = new Bundle();
        args.putInt("showType", showType);
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

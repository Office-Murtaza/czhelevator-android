package com.kingyon.elevator.presenter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.FolderEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.photopicker.ExternalStorage;
import com.kingyon.elevator.photopicker.LocalMediaLoader;
import com.kingyon.elevator.photopicker.MediaData;
import com.kingyon.elevator.photopicker.MediaDirectory;
import com.kingyon.elevator.photopicker.MimeType;
import com.kingyon.elevator.uis.activities.PhotoPickerActivity;
import com.kingyon.elevator.view.PhotoPickerView;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiFilterType;
import me.nereo.multi_image_selector.bean.Image;

/**
 * Created By SongPeng  on 2019/12/17
 * Email : 1531603384@qq.com
 */
public class PhotoPickerPresenter extends BasePresenter<PhotoPickerView> {
    List<MediaDirectory> directories;

    public PhotoPickerPresenter(Context mContext) {
        super(mContext);
        directories = new ArrayList<>();
    }


    public void loadPhotoFolderList() {
        if (isViewAttached()) {
            getView().showProgressDialog("正在加载数据...", false);
        }
        ((PhotoPickerActivity) mContext).getSupportLoaderManager().initLoader(MimeType.TYPE_ALL,
                null, new PhotoDirLoaderCallbacks(mContext, MimeType.TYPE_ALL, new PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<FolderEntity> directories) {

                    }
                }));
        LogUtils.d("加载图片目录执行-------------------");
    }

    private boolean fileExist(String path) {
        if (!TextUtils.isEmpty(path)) {
            return new File(path).exists();
        }
        return false;
    }

    public void loadAdInfo() {
        NetService.getInstance().myAdList("", 1)
                .subscribe(new CustomApiCallback<PageListEntity<ADEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().loadAdInfoFailed();
                        }
                    }

                    @Override
                    public void onNext(PageListEntity<ADEntity> adEntityPageListEntity) {
                        if (isViewAttached()) {
                            if (adEntityPageListEntity == null || adEntityPageListEntity.getContent() == null) {
                                getView().loadAdInfoFailed();
                            } else {
                                if (adEntityPageListEntity.getContent().size()>0) {
                                    ADEntity adEntity = adEntityPageListEntity.getContent().get(0);
                                    if (!adEntity.getVideoUrl().isEmpty()) {
                                        getView().loadAdInfoSuccess(adEntity.getVideoUrl()+"?vframe/jpg/offset/1",adEntityPageListEntity.getTotalElements()+"");
                                    }else if (!adEntity.getImageUrl().isEmpty()){
                                        getView().loadAdInfoSuccess(adEntity.getImageUrl(),adEntityPageListEntity.getTotalElements()+"");
                                    }else {
                                        getView().loadAdInfoSuccess("",adEntityPageListEntity.getTotalElements()+"");
                                    }
                                }else {
                                    getView().loadAdInfoFailed();
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 异步加载数据
     *
     * @param data
     */
    private void parseData(Cursor data, int mineType) {
        if (data == null || data.isClosed()) return;
        directories = new ArrayList<>();
        MediaDirectory photoDirectoryAll = new MediaDirectory();
        while (data.moveToNext()) {
            int media_id = data.getInt(data.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[0]));
            int media_width = data.getInt(data.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[1]));
            int media_height = data.getInt(data.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[2]));
            // 使用DURATION获取的时长不准确
            // media_duration = data.getLong(data.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[3]));
            String media_path = data.getString(data.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[4]));
            String media_type = data.getString(data.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[5]));
            long media_size = data.getLong(data.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[6]));
            String media_dirId = data.getString(data.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[7]));
            String media_dirName = data.getString(data.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[8]));
            String media_directoryPath = media_path.substring(0, media_path.lastIndexOf(File.separator));

            long media_duration = MimeType.isVideo(media_type) ? MimeType.getVideoDuration(media_path) : 0;

            //判断文件是否损坏
            boolean isDamage = ExternalStorage.getInstance().checkImageIsDamage(media_width, media_path);
            if (isDamage) continue;
            MediaData mediaData = getMediaData(media_id, media_path, media_size, media_duration, mineType, media_type, media_width, media_height);
            MediaDirectory mediaDirectory = new MediaDirectory();
            mediaDirectory.setId(media_dirId);
            mediaDirectory.setDirPath(media_directoryPath);
            mediaDirectory.setName(media_dirName);

            if (!directories.contains(mediaDirectory)) {
                mediaDirectory.setCoverPath(media_path);
                mediaDirectory.addMediaData(mediaData);
                directories.add(mediaDirectory);
            } else {
                directories.get(directories.indexOf(mediaDirectory)).addMediaData(mediaData);
            }
            photoDirectoryAll.addMediaData(mediaData);
        }

        photoDirectoryAll.setName("所有图片");
        photoDirectoryAll.setId("ALL");
        if (photoDirectoryAll.getPhotoPaths().size() > 0) {
            photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
        }
        directories.add(0, photoDirectoryAll);
        data.close();
        getView().loadFolderListSuccess(directories);
    }

    private MediaData getMediaData(int mediaId, String mediaPath, long mediaSize, long duration, int mimeType, String mediaType, int mediaWidth, int mediaHeight) {
        MediaData mediaData = new MediaData();
        mediaData.setMediaId(mediaId);
        mediaData.setOriginalPath(mediaPath);
        mediaData.setOriginalSize(mediaSize);
        mediaData.setDuration(duration);
        mediaData.setMimeType(mimeType);
        mediaData.setImageType(mediaType);
        mediaData.setImageWidth(mediaWidth);
        mediaData.setImageHeight(mediaHeight);
        return mediaData;
    }

    class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;
        private boolean showGif;        //是否展示gif
        private int mineType;           //文件类型
        private PhotosResultCallback resultCallback;

        public PhotoDirLoaderCallbacks(Context context, int type, PhotosResultCallback resultCallback) {
            this.context = context;
            this.resultCallback = resultCallback;
            this.showGif = false;
            this.mineType = type;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new LocalMediaLoader(context, mineType, showGif);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        parseData(data, mineType);
                    }catch (Exception e){
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().loadFolderListFailed();
                        }
                    }
                }
            }).start();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    public List<MediaDirectory> getDirectories() {
        return directories;
    }


    public interface PhotosResultCallback {
        void onResultCallback(List<FolderEntity> directories);
    }

}

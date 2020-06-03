package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.PictureChooseEntity;
import com.kingyon.elevator.entities.entities.VideoInfo;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.photopicker.ExternalStorage;
import com.kingyon.elevator.photopicker.LocalMediaLoader;
import com.kingyon.elevator.photopicker.MediaData;
import com.kingyon.elevator.photopicker.MediaDirectory;
import com.kingyon.elevator.presenter.presenter2.VoideChoosePresenter;
import com.kingyon.elevator.uis.activities.advertising.VideoChooseActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.ImageDialoAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.PictureChooseImgAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.PictureChooseVideoAdapter;
import com.kingyon.elevator.videocrop.EditVideoActivity;
import com.kingyon.elevator.videocrop.VideoEditorActivity;
import com.leo.afbaselibrary.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ACTIVITYUTILS_VIDEO_CHOOSE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDE_RELEASETY;
import static com.kingyon.elevator.presenter.PhotoPickerPresenter.eqVideo;
import static com.kingyon.elevator.uis.adapters.adaptertwo.PictureChooseImgAdapter.listimage;
import static com.kingyon.elevator.uis.adapters.adaptertwo.PictureChooseVideoAdapter.videopath;

/**
 * @Created By Admin  on 2020/4/24
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
@Route(path = ACTIVITY_ACTIVITYUTILS_VIDEO_CHOOSE)
public class VoideChooseActiivty extends MvpBaseActivity<VoideChoosePresenter> {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.img_image_dx)
    ImageView imgImageDx;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rcv_voide)
    RecyclerView rcvVoide;
    private List<VideoInfo> mVideoInfos;
    VideoInfo pictureChooseEntity;
    List<VideoInfo> list = new ArrayList<>();
    private static final String[] sLocalVideoColumns = {
            MediaStore.Video.Media._ID, // 视频id
            MediaStore.Video.Media.DATA, // 视频路径
            MediaStore.Video.Media.SIZE, // 视频字节大小
            MediaStore.Video.Media.DISPLAY_NAME, // 视频名称 xxx.mp4
            MediaStore.Video.Media.TITLE, // 视频标题
            MediaStore.Video.Media.DATE_ADDED, // 视频添加到MediaProvider的时间
            MediaStore.Video.Media.DATE_MODIFIED, // 上次修改时间，该列用于内部MediaScanner扫描，外部不要修改
            MediaStore.Video.Media.MIME_TYPE, // 视频类型 video/mp4
            MediaStore.Video.Media.DURATION, // 视频时长
            MediaStore.Video.Media.ARTIST, // 艺人名称
            MediaStore.Video.Media.ALBUM, // 艺人专辑名称
            MediaStore.Video.Media.RESOLUTION, // 视频分辨率 X x Y格式
            MediaStore.Video.Media.DESCRIPTION, // 视频描述
            MediaStore.Video.Media.IS_PRIVATE,
            MediaStore.Video.Media.TAGS,
            MediaStore.Video.Media.CATEGORY, // YouTube类别
            MediaStore.Video.Media.LANGUAGE, // 视频使用语言
            MediaStore.Video.Media.LATITUDE, // 拍下该视频时的纬度
            MediaStore.Video.Media.LONGITUDE, // 拍下该视频时的经度
            MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.MINI_THUMB_MAGIC,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BOOKMARK // 上次视频播放的位置
    };
    private static final String[] sLocalVideoThumbnailColumns = {
            MediaStore.Video.Thumbnails.DATA, // 视频缩略图路径
            MediaStore.Video.Thumbnails.VIDEO_ID, // 视频id
            MediaStore.Video.Thumbnails.KIND,
            MediaStore.Video.Thumbnails.WIDTH, // 视频缩略图宽度
            MediaStore.Video.Thumbnails.HEIGHT // 视频缩略图高度
    };
    private PictureChooseVideoAdapter pictureChooseVideoAdapter;

    List<MediaDirectory> directories;
    @Override
    public VoideChoosePresenter initPresenter() {
        return new VoideChoosePresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voide_choose);
        ButterKnife.bind(this);
        tvTopTitle.setText("全部");
        initVideoData();

    }

    private void initVideoData() {
        mVideoInfos = new ArrayList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, sLocalVideoColumns,
                null, null, null);
        directories = new ArrayList<>();
        MediaDirectory photoDirectoryAll = new MediaDirectory();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                VideoInfo videoInfo = new VideoInfo();

                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                long dateAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                long dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
                String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
                String resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION));
                String description = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DESCRIPTION));
                int isPrivate = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.IS_PRIVATE));
                String tags = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TAGS));
                String category = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.CATEGORY));
                double latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE));
                int dateTaken = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));
                int miniThumbMagic = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.MINI_THUMB_MAGIC));
                String bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID));
                String bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                String media_directoryPath = data.substring(0, data.lastIndexOf(File.separator));
                int bookmark = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.BOOKMARK));

                Cursor thumbnailCursor = getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, sLocalVideoThumbnailColumns,
                        MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id, null, null);

                if (thumbnailCursor != null && thumbnailCursor.moveToFirst()) {

                    do {
                        String thumbnailData = thumbnailCursor.getString(thumbnailCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                        int kind = thumbnailCursor.getInt(thumbnailCursor.getColumnIndex(MediaStore.Video.Thumbnails.KIND));
                        long width = thumbnailCursor.getLong(thumbnailCursor.getColumnIndex(MediaStore.Video.Thumbnails.WIDTH));
                        long height = thumbnailCursor.getLong(thumbnailCursor.getColumnIndex(MediaStore.Video.Thumbnails.HEIGHT));

                        videoInfo.thumbnailData = thumbnailData;
                        videoInfo.kind = kind;
                        videoInfo.width = width;
                        videoInfo.height = height;

                        MediaData mediaData =getMediaData(id, data, size, duration, mimeType, 0, 0,thumbnailData);
                        MediaDirectory mediaDirectory = new MediaDirectory();
                        mediaDirectory.setId(String.valueOf(id));
                        mediaDirectory.setDirPath(media_directoryPath);
                        mediaDirectory.setName(bucketDisplayName);

                        if (!directories.contains(mediaDirectory)) {
                            mediaDirectory.setCoverPath(data);
                            mediaDirectory.addMediaData(mediaData);
                            directories.add(mediaDirectory);
                        } else {
                            directories.get(directories.indexOf(mediaDirectory)).addMediaData(mediaData);
                        }
                        photoDirectoryAll.addMediaData(mediaData);

                    } while (thumbnailCursor.moveToNext());

                    thumbnailCursor.close();
                }
                videoInfo.id = id;
                videoInfo.data = data;
                videoInfo.size = size;
                videoInfo.displayName = displayName;
                videoInfo.title = title;
                videoInfo.dateAdded = dateAdded;
                videoInfo.dateModified = dateModified;
                videoInfo.mimeType = mimeType;
                videoInfo.duration = duration;
                videoInfo.artist = artist;
                videoInfo.album = album;
                videoInfo.resolution = resolution;
                videoInfo.description = description;
                videoInfo.isPrivate = isPrivate;
                videoInfo.tags = tags;
                videoInfo.category = category;
                videoInfo.latitude = latitude;
                videoInfo.longitude = longitude;
                videoInfo.dateTaken = dateTaken;
                videoInfo.miniThumbMagic = miniThumbMagic;
                videoInfo.bucketId = bucketId;
                videoInfo.bucketDisplayName = bucketDisplayName;
                videoInfo.bookmark = bookmark;


                mVideoInfos.add(videoInfo);

            } while (cursor.moveToNext());
            photoDirectoryAll.setName("全部");
            photoDirectoryAll.setId("ALL");
            if (photoDirectoryAll.getPhotoPaths().size() > 0) {
                photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
            }
            directories.add(0, photoDirectoryAll);

            cursor.close();

        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvVoide.setLayoutManager(gridLayoutManager);
        pictureChooseVideoAdapter = new PictureChooseVideoAdapter(this, mVideoInfos);
        rcvVoide.setAdapter(pictureChooseVideoAdapter);
        pictureChooseVideoAdapter.notifyDataSetChanged();

    }

    @OnClick({R.id.img_top_back, R.id.tv_right,R.id.tv_top_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_right:
                LogUtils.e("地址"+videopath);
                if (videopath.isEmpty()){
                    ToastUtils.showToast(VoideChooseActiivty.this,"请选择视频",1000);
                }else {
                    Intent intent = new Intent(VoideChooseActiivty.this, EditVideoActivity.class);
                    intent.putExtra("path",videopath);
                    intent.putExtra("fromType",456);
                    startActivity(intent);
                }
                break;
            case R.id.tv_top_title:
                shwoTop();
                break;
                default:
        }
    }
    public List<MediaDirectory> getDirectories() {
        return directories;
    }

    private void shwoTop() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        /* 添加对话框自定义布局 */
        alertDialog.setContentView(R.layout.layout_file_name);
        /* 获取对话框窗口 */
        Window window = alertDialog.getWindow();
        /* 设置显示窗口的宽高 */
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, 800);
        /* 设置窗口显示位置 */
        window.setGravity(Gravity.TOP);
        window.setBackgroundDrawable(null);
        ListView lv_listview =window.findViewById(R.id.lv_listview);
        ImageDialoAdapter imageDialoAdapter = new ImageDialoAdapter(this,getDirectories());
        lv_listview.setAdapter(imageDialoAdapter);
        imageDialoAdapter.notifyDataSetChanged();
        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.e( getDirectories().get(position),
                        getDirectories().get(position).getId(),
                        getDirectories().get(position).getName(),
                        getDirectories().get(position).getMediaData().toString()

                );
                tvTopTitle.setText( getDirectories().get(position).getName());
                mVideoInfos.clear();list.clear();
                for (int c = 0;c<getDirectories().get(position).getMediaData().size();c++){
                    pictureChooseEntity = new VideoInfo();
                    pictureChooseEntity.thumbnailData =  getDirectories().get(position).getMediaData().get(c).getCompressionPath();
                    pictureChooseEntity.data = getDirectories().get(position).getMediaData().get(c).getOriginalPath();
                    list.add(pictureChooseEntity);
                }
                GridLayoutManager gridLayoutManager = new GridLayoutManager(VoideChooseActiivty.this, 4, GridLayoutManager.VERTICAL, false);
                gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rcvVoide.setLayoutManager(gridLayoutManager);
                pictureChooseVideoAdapter = new PictureChooseVideoAdapter(VoideChooseActiivty.this, list);
                rcvVoide.setAdapter(pictureChooseVideoAdapter);
                pictureChooseVideoAdapter.notifyDataSetChanged();

                alertDialog.dismiss();
            }
        });

    }


    private MediaData getMediaData(int mediaId, String mediaPath, long mediaSize, long duration,  String mediaType, int mediaWidth, int mediaHeight,String compressionPath) {
        MediaData mediaData = new MediaData();
        mediaData.setMediaId(mediaId);
        mediaData.setOriginalPath(mediaPath);
        mediaData.setOriginalSize(mediaSize);
        mediaData.setDuration(duration);
        mediaData.setImageType(mediaType);
        mediaData.setImageWidth(mediaWidth);
        mediaData.setImageHeight(mediaHeight);
        mediaData.setCompressionPath(compressionPath);
        return mediaData;
    }
}

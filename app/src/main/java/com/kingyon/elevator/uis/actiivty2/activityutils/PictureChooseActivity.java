package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.app.ActionBar;
import android.app.AlertDialog;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.PictureChooseEntity;
import com.kingyon.elevator.photopicker.ExternalStorage;
import com.kingyon.elevator.photopicker.LocalMediaLoader;
import com.kingyon.elevator.photopicker.MediaData;
import com.kingyon.elevator.photopicker.MediaDirectory;
import com.kingyon.elevator.uis.adapters.adaptertwo.ImageDialoAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.PictureChooseImgAdapter;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ACTIVITYUTILS_PICTURE_CHOOSE;
import static com.kingyon.elevator.presenter.PhotoPickerPresenter.eqVideo;
import static com.kingyon.elevator.uis.adapters.adaptertwo.PictureChooseImgAdapter.SCROLL_STATE_IDLE;
import static com.kingyon.elevator.uis.adapters.adaptertwo.PictureChooseImgAdapter.listimage;

/**
 * @Created By Admin  on 2020/4/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:图片选择
 */
@Route(path = ACTIVITY_ACTIVITYUTILS_PICTURE_CHOOSE)
public class PictureChooseActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.img_image_dx)
    ImageView imgImageDx;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rcv_image)
    RecyclerView rcvImage;
    PictureChooseImgAdapter pictureChooseImgAdapter;
    PictureChooseEntity pictureChooseEntity;
    List<PictureChooseEntity> list = new ArrayList<>();
    List<MediaDirectory> directories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);

    }




    @Override
    public int getContentViewId() {
        return R.layout.activity_picture_choose;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("全部");

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        int c = 0;
        MediaDirectory photoDirectoryAll = new MediaDirectory();
        directories = new ArrayList<>();
        while (cursor.moveToNext()) {
            pictureChooseEntity = new PictureChooseEntity();
            int media_id = cursor.getInt(cursor.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[0]));
            int media_width = cursor.getInt(cursor.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[1]));
            int media_height = cursor.getInt(cursor.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[2]));
            // 使用DURATION获取的时长不准确
//            long media_duration = cursor.getLong(cursor.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[3]));
////            long media_duration = 1;
//            LogUtils.e(media_duration);
            String media_path = cursor.getString(cursor.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[4]));
            String media_type = cursor.getString(cursor.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[5]));
            long media_size = cursor.getLong(cursor.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[6]));
            String media_dirId = cursor.getString(cursor.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[7]));
            String media_dirName = cursor.getString(cursor.getColumnIndexOrThrow(LocalMediaLoader.FILE_PROJECTION[8]));
            String media_directoryPath = media_path.substring(0, media_path.lastIndexOf(File.separator));
            // long media_duration = MimeType.isVideo(media_type) ? MimeType.getVideoDuration(media_path) : 0;
            if (eqVideo(media_type)) {
//                if (media_duration==0) {
//                    //如果视频时长为0，就跳过这一条
//                    continue;
//                }
            }else {
                //判断文件是否损坏
                boolean isDamage = ExternalStorage.getInstance().checkImageIsDamage(media_width, media_path);
                if (isDamage) continue;
            }
            MediaData mediaData =getMediaData(media_id, media_path, media_size, 0, media_type, media_width, media_height);
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
            pictureChooseEntity.setName(media_dirName);
            pictureChooseEntity.setPath(media_path);
            pictureChooseEntity.isCheck = false;
            c++;
            pictureChooseEntity.itemId = c + "";
            list.add(pictureChooseEntity);

            photoDirectoryAll.addMediaData(mediaData);
        }
        photoDirectoryAll.setName("全部");
        photoDirectoryAll.setId("ALL");
        if (photoDirectoryAll.getPhotoPaths().size() > 0) {
            photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
        }
        directories.add(0, photoDirectoryAll);
        cursor.close();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvImage.setLayoutManager(gridLayoutManager);
        pictureChooseImgAdapter = new PictureChooseImgAdapter(this, list);
        rcvImage.setAdapter(pictureChooseImgAdapter);
        pictureChooseImgAdapter.notifyDataSetChanged();
        rcvImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    pictureChooseImgAdapter.setScrolling(false);
                    pictureChooseImgAdapter.notifyDataSetChanged();
                } else {
                    pictureChooseImgAdapter.setScrolling(true);
                }
            }
        });
        getpic();
    }

    public List<MediaDirectory> getDirectories() {
        return directories;
    }
    @OnClick({R.id.img_top_back, R.id.tv_top_title, R.id.img_image_dx, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_top_title:
                shwoTop();
                break;
            case R.id.img_image_dx:
                break;
            case R.id.tv_right:
                if (listimage.size()>0) {
                    LogUtils.e(listimage.toString());
                    Intent intent=new Intent();
                    intent.putExtra("imagePath", listimage.toString());
                    setResult(RESULT_OK,intent);
                    finish();
//                    OrdinaryActivity.communityReleasetyActivity.finish();
//                    ARouter.getInstance().build(ACTIVITY_MAIN2_COMMUNITY_RELEASETY).withString("imagePath",listimage.toString()).navigation();
//                    finish();
                }else {
                    ToastUtils.showShort("还没有选择图片");
                }
                break;
        }
    }
    private MediaData getMediaData(int mediaId, String mediaPath, long mediaSize, long duration,  String mediaType, int mediaWidth, int mediaHeight) {
        MediaData mediaData = new MediaData();
        mediaData.setMediaId(mediaId);
        mediaData.setOriginalPath(mediaPath);
        mediaData.setOriginalSize(mediaSize);
        mediaData.setDuration(duration);
        mediaData.setImageType(mediaType);
        mediaData.setImageWidth(mediaWidth);
        mediaData.setImageHeight(mediaHeight);
        return mediaData;
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
                        getDirectories().get(position).getName()

                );
                tvTopTitle.setText( getDirectories().get(position).getName());
                list.clear();
                listimage.clear();
                for (int c = 0;c<getDirectories().get(position).getMediaData().size();c++){
                    pictureChooseEntity = new PictureChooseEntity();
                    pictureChooseEntity.setName(getDirectories().get(position).getMediaData().get(c).getCameraImagePath());
                    pictureChooseEntity.setPath(getDirectories().get(position).getMediaData().get(c).getOriginalPath());
                    pictureChooseEntity.isCheck = false;
                    c++;
                    pictureChooseEntity.itemId = c + "";
                    list.add(pictureChooseEntity);
                }
                GridLayoutManager gridLayoutManager = new GridLayoutManager(PictureChooseActivity.this, 4, GridLayoutManager.VERTICAL, false);
                gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rcvImage.setLayoutManager(gridLayoutManager);
                pictureChooseImgAdapter = new PictureChooseImgAdapter(PictureChooseActivity.this, list);
                rcvImage.setAdapter(pictureChooseImgAdapter);
                pictureChooseImgAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });

    }

    public void getpic() {

    }

}

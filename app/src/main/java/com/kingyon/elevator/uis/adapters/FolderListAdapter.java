package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.entities.DateGridEntity;
import com.kingyon.elevator.entities.FolderEntity;
import com.kingyon.elevator.entities.SelectDateEntity;
import com.kingyon.elevator.photopicker.MediaDirectory;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.MyToastUtils;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.MultiFilterType;
import me.nereo.multi_image_selector.bean.Folder;
import me.nereo.multi_image_selector.bean.Image;

/**
 * Created By SongPeng  on 2019/12/13
 * Email : 1531603384@qq.com
 */
public class FolderListAdapter extends BaseAdapter {

    private List<MediaDirectory> folderEntityList;
    private LayoutInflater mInflater;
    Context context;
    private MediaDirectory myAdMediaDirectory;
    private int fromType;

    public FolderListAdapter(Context context, int fromType, List<MediaDirectory> folderEntityList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.folderEntityList = folderEntityList;
        this.fromType = fromType;
        if (fromType == Constants.FROM_TYPE_TO_SELECT_MEDIA.PLAN) {
            addAllFolderAndMyAd();
            if (myAdMediaDirectory != null) {
                folderEntityList.add(0, myAdMediaDirectory);
            }
        }

    }

    public void reflashData(List<MediaDirectory> folderEntityList) {
        this.folderEntityList = folderEntityList;
        if (fromType == Constants.FROM_TYPE_TO_SELECT_MEDIA.PLAN) {
            if (myAdMediaDirectory != null) {
                this.folderEntityList.add(0, myAdMediaDirectory);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 添加我的广告和所有照片
     */
    private void addAllFolderAndMyAd() {
        myAdMediaDirectory = new MediaDirectory();
        myAdMediaDirectory.setId("我的广告");
        myAdMediaDirectory.setCoverPath("");
        myAdMediaDirectory.setDirPath("0");
        myAdMediaDirectory.setName("我的广告");
    }


    /**
     * 刷新我的广告数据
     *
     * @param coverPath
     * @param count
     */
    public void updateMyAdInfo(String coverPath, String count) {
        if (myAdMediaDirectory != null) {
            myAdMediaDirectory.setCoverPath(coverPath);
            myAdMediaDirectory.setDirPath(count);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return folderEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return folderEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.folder_list_item_layout, null);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(85));
            convertView.setLayoutParams(layoutParams);
            holder.folder_preview_img = convertView.findViewById(R.id.folder_preview_img);
            holder.my_advertisment_tips = convertView.findViewById(R.id.my_advertisment_tips);
            holder.image_count = convertView.findViewById(R.id.image_count);
            holder.folder_name = convertView.findViewById(R.id.folder_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MediaDirectory folderEntity = folderEntityList.get(position);
        if (folderEntity.getId().equals("我的广告")) {
            holder.folder_name.setText(folderEntity.getName());
            holder.my_advertisment_tips.setVisibility(View.VISIBLE);
            holder.image_count.setText(folderEntity.getDirPath());
            if (!folderEntity.getCoverPath().isEmpty()) {
                GlideUtils.loadImage(context, folderEntity.getCoverPath(), holder.folder_preview_img);
            } else {
                holder.folder_preview_img.setImageResource(R.mipmap.suoyoutupian_tubioayi);
            }
        } else {
            holder.folder_name.setText(folderEntity.getName());
            holder.my_advertisment_tips.setVisibility(View.GONE);
            if (folderEntity.getMediaData() != null) {
                holder.image_count.setText(folderEntity.getMediaData().size() + "");
            } else {
                holder.image_count.setText("*");
            }
            if (folderEntity.getCoverPath() != null) {
                GlideUtils.loadLocalFrame(context, folderEntity.getCoverPath(), holder.folder_preview_img);
            } else {
                holder.folder_preview_img.setImageResource(R.mipmap.suoyoutupian_tubioayi);
            }
        }
        return convertView;
    }

    public final class ViewHolder {
        ImageView folder_preview_img;
        TextView my_advertisment_tips;
        TextView image_count;
        TextView folder_name;
    }

}

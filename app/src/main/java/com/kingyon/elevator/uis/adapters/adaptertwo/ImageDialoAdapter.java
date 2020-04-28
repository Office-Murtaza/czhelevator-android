package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.photopicker.MediaDirectory;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;

/**
 * @Created By Admin  on 2020/4/24
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class ImageDialoAdapter extends BaseAdapter {
    Context context;
    private List<MediaDirectory> folderEntityList;

    public ImageDialoAdapter(Context context, List<MediaDirectory> folderEntityList){
        this.context = context;
        this.folderEntityList = folderEntityList;
    }

    @Override
    public int getCount() {
        return folderEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Myclass myclass;
        if (convertView == null){
            convertView  = View.inflate(context, R.layout.itme_image_dialo,null);
            myclass = new Myclass();
            myclass.image = convertView.findViewById(R.id.img_slt);
            myclass.imgxz = convertView.findViewById(R.id.img_xz);
            myclass.tv_name = convertView.findViewById(R.id.tv_file_name);
            myclass.tvnumber = convertView.findViewById(R.id.tv_file_num);
            convertView.setTag(myclass);
        }else {
          myclass = (Myclass) convertView.getTag();
        }
        MediaDirectory folderEntity = folderEntityList.get(position);
        GlideUtils.loadImage(context, folderEntity.getCoverPath(), myclass.image);
        myclass.tv_name.setText(folderEntity.getName());
        myclass.tvnumber.setText(folderEntity.getMediaData().size() + "");
        return convertView;
    }
    class Myclass{
        TextView tv_name,tvnumber;
        ImageView image,imgxz;
    }
}

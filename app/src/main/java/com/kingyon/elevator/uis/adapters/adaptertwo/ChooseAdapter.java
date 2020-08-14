package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.litao.android.lib.entity.PhotoEntry;
import com.muzhi.camerasdk.FilterImageActivity;
import com.muzhi.camerasdk.model.CameraSdkParameterInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.kareluo.imaging.IMGEditActivity;

import static com.czh.myversiontwo.utils.Constance.IMAGER_EDITOR_ACTIVITY;
import static com.kingyon.elevator.uis.adapters.adaptertwo.PictureChooseImgAdapter.listimage;

/**
 * Created by 李涛 on 16/4/30.
 */
public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {

    private List<PhotoEntry> list;

    private BaseActivity mContext;

    private LayoutInflater mInflater;

    private OnItmeClickListener mlistener;

    private EditOniclk editOniclk;

    ArrayList<String> listPath = new ArrayList<>();
    private CameraSdkParameterInfo  mCameraSdkParameterInfo;
    public  interface OnItmeClickListener{
        void onItemClicked(int position);

    }

    public ChooseAdapter(BaseActivity mContext,CameraSdkParameterInfo mCameraSdkParameterInfo) {
        this.mContext = mContext;
        this.mCameraSdkParameterInfo = mCameraSdkParameterInfo;
        mlistener = (OnItmeClickListener) mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LogUtils.e("1*****");
    }


    public void getDataList(List<PhotoEntry> list){
        LogUtils.e("2*****");
        this.list = list;
        list.add(createAddEntry());
        for (int i=0;i<list.size();i++){
            if (list.get(i).getPath()!=null) {
                listPath.add(list.get(i).getPath());
            }
        }
    }

    public List<PhotoEntry> getData(){
        return list.subList(0,list.size());
    }

    private PhotoEntry createAddEntry(){
        return new PhotoEntry();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder vh = new ViewHolder(mInflater.inflate(R.layout.item_selected_photo, viewGroup, false), i);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        LogUtils.e("123123123123123123123123123123123",i);
        if (i==list.size()-1){
            viewHolder.mImageView.setImageResource(R.mipmap.btn_image_upload);
            viewHolder.ima_delete.setVisibility(View.GONE);
            viewHolder.tvEdit.setVisibility(View.GONE);
        }else {
            PhotoEntry entry = list.get(i);
            if (entry.getPath()!=null){
                GlideUtils.loadImage(mContext, new File(entry.getPath().replaceAll(" ", "")), viewHolder.mImageView);
                viewHolder.ima_delete.setVisibility(View.VISIBLE);
                viewHolder.tvEdit.setVisibility(View.VISIBLE);
            }

            viewHolder.ima_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(i);
                    listPath.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i,list.size()-i);
                    mCameraSdkParameterInfo.getImage_list().remove(i);
//                    删除
                }
            });
            viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    编辑
                    mCameraSdkParameterInfo.setImage_list(listPath);
                    if (editOniclk!=null) {
                        editOniclk.editOniclk(listPath,i);
                    }

                }
            });
        }
    }

    public void OnciclkEdit(EditOniclk editOniclk){
        this.editOniclk = editOniclk;
    }

    public interface  EditOniclk{
        void editOniclk(ArrayList<String> listPath,int num);
    }

    @Override
    public int getItemCount() {
        if (list.size()>6) {
            return 6;
        }else {
            return list.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView,ima_delete;
        private TextView tvEdit;

        private int position;

        public ViewHolder(View itemView, int position) {
            super(itemView);
            this.position = position;
            mImageView = itemView.findViewById(R.id.image);
            ima_delete =  itemView.findViewById(R.id.img_deldte);
            tvEdit =  itemView.findViewById(R.id.tv_image_edit);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mlistener.onItemClicked(position);
            listimage .clear();
        }
    }

}

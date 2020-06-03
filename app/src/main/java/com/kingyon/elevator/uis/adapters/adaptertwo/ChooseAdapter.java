package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.litao.android.lib.entity.PhotoEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.kareluo.imaging.IMGEditActivity;

import static com.kingyon.elevator.uis.adapters.adaptertwo.PictureChooseImgAdapter.listimage;

/**
 * Created by 李涛 on 16/4/30.
 */
public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {

    private List<PhotoEntry> list;

    private BaseActivity mContext;

    private LayoutInflater mInflater;

    private OnItmeClickListener mlistener;

    public  interface OnItmeClickListener{
        void onItemClicked(int position);

    }

    public ChooseAdapter(BaseActivity mContext, List<PhotoEntry> list) {
        this.mContext = mContext;
        this.list = list;
        mlistener = (OnItmeClickListener) mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list.add(createAddEntry());
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
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i,list.size()-i);
//                    删除
                }
            });
            viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    编辑
                    LogUtils.e(i);
                File imageFile = new File(entry.getPath());
                Bundle bundle = new Bundle();
                bundle.putParcelable(IMGEditActivity.EXTRA_IMAGE_URI, Uri.fromFile(imageFile));
                bundle.putInt("number",i);
                bundle.putString(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, new File(mContext.getExternalCacheDir(), String.format("%s.jpg", UUID.randomUUID().toString())).getAbsolutePath());
                mContext.startActivityForResult(IMGEditActivity.class, CommonUtil.REQ_CODE_4, bundle);

                }
            });
        }
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

package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.view.RoundImageView;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.litao.android.lib.entity.PhotoEntry;
import com.muzhi.camerasdk.model.CameraSdkParameterInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created By Admin  on 2020/8/14
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class ChoosetAdapter extends RecyclerView.Adapter<ChoosetAdapter.ViewHolder> {

    private BaseActivity mContext;
    ArrayList<String> list = new ArrayList<>();
    private CameraSdkParameterInfo mCameraSdkParameterInfo;
    EditOniclk editOniclk;
    private OnItmeClickListener mlistener;
    public ChoosetAdapter(BaseActivity mContext, CameraSdkParameterInfo mCameraSdkParameterInfo) {
        this.mContext = mContext;
        this.mCameraSdkParameterInfo = mCameraSdkParameterInfo;
        LogUtils.e("1*****");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_selected_photo, parent, false);
        return new ViewHolder(view);
    }

    public void getDataList(ArrayList<String> list) {
        this.list = list;
    }
    public List<String> getData(){
        return list.subList(0,list.size());
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogUtils.e(position,list.size());
        if (position == list.size() - 1) {
            holder.image.setImageResource(R.mipmap.btn_image_upload);
            holder.imgDeldte.setVisibility(View.GONE);
            holder.tvImageEdit.setVisibility(View.GONE);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onItemClicked(position);
                }
            });
        } else {
            if (list.get(position) != null) {
                GlideUtils.loadImage(mContext, new File(list.get(position).replaceAll(" ", "")), holder.image);
                holder.imgDeldte.setVisibility(View.VISIBLE);
                holder.tvImageEdit.setVisibility(View.VISIBLE);
            }
            holder.imgDeldte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,list.size()-position);
                    mCameraSdkParameterInfo.getImage_list().remove(position);
//                    删除
                }
            });
            holder.tvImageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    编辑
                    mCameraSdkParameterInfo.setImage_list(list);
                    if (editOniclk!=null) {
                        editOniclk.editOniclk(list,position);
                    }

                }
            });
        }


    }
    public void OnciclkEdit(EditOniclk editOniclk){
        this.editOniclk = editOniclk;
    }
    public void OnItmeClickListener(OnItmeClickListener mlistener){
        this.mlistener = mlistener;
    }

    public interface  EditOniclk{
        void editOniclk(ArrayList<String> listPath,int num);
    }

    public  interface OnItmeClickListener{
        void onItemClicked(int position);

    }
    @Override
    public int getItemCount() {
        if (list.size()>6) {
            return 6;
        }else {
            return list.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        RoundImageView image;
        @BindView(R.id.img_deldte)
        ImageView imgDeldte;
        @BindView(R.id.tv_image_edit)
        TextView tvImageEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

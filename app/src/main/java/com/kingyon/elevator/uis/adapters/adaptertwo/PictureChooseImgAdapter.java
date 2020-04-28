package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.PictureChooseEntity;
import com.leo.afbaselibrary.utils.FileUtil;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created By Admin  on 2020/4/24
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:相片选择
 */
public class PictureChooseImgAdapter extends RecyclerView.Adapter<PictureChooseImgAdapter.ViewHodle> {

    List<PictureChooseEntity> list;

    private List<PictureChooseEntity> mData;
    private Context mContext;
    public static List<String> listimage = new ArrayList<>();
    private boolean isScrolling = false;
    //正在滚动
    public static final int SCROLL_STATE_IDLE = 0;

    //正在被外部拖拽,一般为用户正在用手指滚动
    public static final int SCROLL_STATE_DRAGGING = 1;

    //自动滚动开始
    public static final int SCROLL_STATE_SETTLING = 2;

    private int number = 0;
    private int maximum = 6;

    public PictureChooseImgAdapter(Context context, List<PictureChooseEntity> list) {
        this.mContext = context;
        this.list = list;
        this.mData = list;
        this.mData = new ArrayList<>();
        if (mData != null) {
            this.mData.addAll(list);
        }
    }

    /**
     * 刷新数据
     *
     * @param list
     */
    public void nodfiyData(List<PictureChooseEntity> list) {
        if (list != null) {
            this.mData.clear();
            this.mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setScrolling(boolean scrolling){
        isScrolling = scrolling;
    }

    @NonNull
    @Override
    public ViewHodle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.itme_picture_image,parent,false);
        return new PictureChooseImgAdapter.ViewHodle(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodle holder, int position) {
        if (holder!=null) {
//            if (!isScrolling) {
                final PictureChooseEntity datainfo = mData.get(position);
                final boolean isCheck = datainfo.isCheck;
                GlideUtils.loadImage(mContext, mData.get(position).getPath(), holder.img_image);
                    if (isCheck) {
                        //被选
                        Resources resources = mContext.getResources();
                        Drawable drawable = resources.getDrawable(R.mipmap.bg_choose_class);
                        holder.tv_xznum.setBackgroundDrawable(drawable);
                        holder.tv_xznum.setText(list.get(position).number+"");
                    } else {
                        //未被选
                        Resources resources = mContext.getResources();
                        Drawable drawable = resources.getDrawable(R.mipmap.bg_choose_class_off);
                        holder.tv_xznum.setBackgroundDrawable(drawable);
                        holder.tv_xznum.setText("");
                    }
                holder.img_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newId = datainfo.itemId;
                            for (PictureChooseEntity datainfo1 : list) {
                                String oldId = datainfo1.itemId;
                                if (newId.equals(oldId)) {
                                    boolean isCheck1 = datainfo1.isCheck;
                                    if (isCheck1) {
                                        number--;
                                        datainfo1.isCheck = false;
                                        listimage.remove(mData.get(position).getPath());
                                    } else {
                                        if (number < maximum) {
                                            number++;
                                            listimage.add(mData.get(position).getPath());
                                            datainfo1.isCheck = true;
                                            datainfo1.number = number;
                                        }else {
                                            ToastUtils.showToast(mContext,"最多只能选择"+maximum+"张",1000);
                                        }
                                    }
                                    break;
                                }
                            }
                            //刷新数据
                            nodfiyData(list);
                    }
                });
            }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHodle extends RecyclerView.ViewHolder {
        ImageView img_image;
        TextView tv_xznum;
        public ViewHodle(View itemView) {
            super(itemView);
            img_image = itemView.findViewById(R.id.img_image);
            tv_xznum = itemView.findViewById(R.id.tv_xznum);
        }
    }

}

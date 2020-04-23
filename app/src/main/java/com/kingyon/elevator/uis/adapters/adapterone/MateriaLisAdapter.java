package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.text.TextUtils;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.LocalMaterialEntity;
import com.kingyon.elevator.entities.MateriaEntity;
import com.leo.afbaselibrary.uis.adapters.ItemViewDelegate;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.TimeUtil;

import java.util.List;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class MateriaLisAdapter extends MultiItemTypeAdapter<Object> {

    private boolean editMode;

    private boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public MateriaLisAdapter(Context context, List<Object> mItems) {
        super(context, mItems);
        addItemViewDelegate(1, new LocalMateriaDelegate());
        addItemViewDelegate(2, new MateriaDelegate());
        addItemViewDelegate(3, new GroupDelegate());
    }

    private class MateriaDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.fragment_material_list_materia;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof MateriaEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            MateriaEntity item = (MateriaEntity) o;
            boolean video = TextUtils.equals(item.getType(), Constants.Materia_Type.VIDEO);
            holder.setAgateImage(R.id.img_cover, item.getPath(), video);
            holder.setVisible(R.id.tv_duration, video);
            holder.setTextNotHide(R.id.tv_duration, TimeUtil.getRemainingTimeFormat(item.getDuration()));
        }
    }

    private class GroupDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.fragment_material_list_group;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof String;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            holder.setTextNotHide(R.id.tv_name, o.toString());
        }
    }

    private class LocalMateriaDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.fragment_material_list_materia;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof LocalMaterialEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            LocalMaterialEntity item = (LocalMaterialEntity) o;
            boolean video = TextUtils.equals(item.getType(), Constants.Materia_Type.VIDEO);
            holder.setAgateImage(R.id.img_cover, item.getUrl(), video);
            holder.setVisible(R.id.tv_duration, video);
            holder.setSelected(R.id.img_choose_delete,item.isDelete());
            holder.setVisible(R.id.img_choose_delete, isEditMode());
            holder.setTextNotHide(R.id.tv_duration, TimeUtil.getRemainingTimeFormat(item.getDuration()));
        }
    }
}

package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.leo.afbaselibrary.utils.ScreenUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gongli on 2017/2/7 09:05
 * email: lc824767150@163.com
 */

public abstract class BaseAdapterWithHF<T> extends RecyclerView.Adapter<BaseAdapterWithHF.ViewHolder> {
    protected final static int TYPE_CONTENT = 0;
    protected final static int TYPE_HEADER = 1;
    protected final static int TYPE_FOOTER = 2;
    protected final static int TYPE_CONTENT_2 = 3;
    protected final static int TYPE_CONTENT_3 = 4;

    protected List<T> datas;
    protected LayoutInflater inflater;
    protected Context context;

    public BaseAdapterWithHF(Context context) {
        this.datas = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    /**
     * 向Adapter添加数据
     *
     * @param datas items
     */
    public void addDatas(List<? extends T> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        } else {
            Logger.e("BaseAdapterWithHF ---> addDatas(List<T> datas) has null parameter");
            notifyDataSetChanged();
        }
    }

    /**
     * 向Adapter添加数据
     *
     * @param data item
     */
    public void addData(T data) {
        if (data != null) {
            this.datas.add(data);
            notifyDataSetChanged();
        } else {
            Logger.e("BaseAdapterWithHF ---> addDatas(List<T> datas) has null parameter");
        }
    }

    public int getItemRealCount() {
        return datas.size();
    }


    /**
     * Adapter刷新数据
     *
     * @param datas items
     */
    public void refreshDatas(List<? extends T> datas) {
        this.datas.clear();
        if (datas != null) {
            addDatas(datas);
        } else {
            Logger.e("BaseAdapterWithHF ---> refreshDatas(List<T> datas) has null parameter");
            notifyDataSetChanged();
        }
    }

    public void clearDatas() {
        this.datas.clear();
        notifyDataSetChanged();
    }

    /**
     * Adapter刷新数据
     *
     * @param data item数据
     */
    public void refreshData(T data) {
        this.datas.clear();
        if (data != null) {
            addData(data);
        } else {
            Logger.e("BaseAdapterWithHF ---> refreshDatas(List<T> datas) has null parameter");
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + getHeaderCount() + getFooterCount();
    }

    public abstract int getFooterCount();

    public abstract int getHeaderCount();

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderCount()) {
            return TYPE_HEADER;
        }
        if (position >= getItemCount() - getFooterCount()) {
            return TYPE_FOOTER;
        }
        return TYPE_CONTENT;
    }

    /**
     * 获取指定位置的数据
     *
     * @param position position
     * @return item
     */
    public T getItemData(int position) {
        if (position - getHeaderCount() >= 0 && datas.size() > position - getHeaderCount()) {
            return datas.get(position - getHeaderCount());
        } else {
            return null;
        }
    }


    /**
     * 删除指定位置的数据
     *
     * @param position position
     */
    public void deleteItemData(int position) {
        deleteItemData(getItemData(position));
    }

    /**
     * 删除指定数据
     *
     * @param t item
     */
    public void deleteItemData(T t) {
        if (t != null) {
            datas.remove(t);
        }
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return datas;
    }

    public interface OnItemClickListener<K> {
        void onItemClick(View view, int position, K entity, BaseAdapterWithHF<K> baseAdaper);
    }

    public interface OnItemLongClickListener<K> {
        void onItemLongClick(View view, int position, K entity, BaseAdapterWithHF<K> baseAdaper);
    }

    protected OnItemClickListener<T> onItemClickListener;
    protected OnItemLongClickListener<T> onItemLongClickListener;

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener<T> getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    //    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
//
//    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
//        this.onItemLongClickListener = onItemLongClickListener;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public View itemRoot;

        public ViewHolder(View view) {
            super(view);
            itemRoot = view;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                onItemClickListener.onItemClick(v, position, getItemData(position), BaseAdapterWithHF.this);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null) {
                int position = getAdapterPosition();
                onItemLongClickListener.onItemLongClick(v, position, getItemData(position), BaseAdapterWithHF.this);
            }
            return true;
        }

        public void setVisibility(boolean visible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemRoot.getLayoutParams();
            if (visible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                param.setMargins(0, ScreenUtil.dp2px(4), 0, ScreenUtil.dp2px(4));
                itemView.setVisibility(View.VISIBLE);
            } else {
                param.height = 0;
                param.width = 0;
                param.setMargins(0, 0, 0, 0);
                itemView.setVisibility(View.GONE);
            }
            itemView.setLayoutParams(param);
        }
    }
}

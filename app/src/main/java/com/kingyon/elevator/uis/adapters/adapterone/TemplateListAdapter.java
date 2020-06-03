package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.AdTempletEntity;
import com.kingyon.elevator.uis.activities.advertising.TemplateListActivity;
import com.leo.afbaselibrary.uis.adapters.ItemViewDelegate;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.ScreenUtil;

import java.util.ArrayList;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class TemplateListAdapter extends MultiItemTypeAdapter<Object> {

    private boolean splitScreen;

    public TemplateListAdapter(Context context, ArrayList<Object> mItems, boolean splitScreen) {
        super(context, mItems);
        this.splitScreen = splitScreen;
        addItemViewDelegate(1, new TemplateDelegate());
        addItemViewDelegate(2, new TipDelegate());
    }

    private class TemplateDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_template_list_template;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof AdTempletEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {

            View view = holder.getView(R.id.img_cover);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null) {
                float property = splitScreen ? Constants.adImageProperty : Constants.adScreenProperty;
                layoutParams.height = (int) ((ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dp2px(48)) / 2f / property);
                view.setLayoutParams(layoutParams);
            }

            AdTempletEntity item = (AdTempletEntity) o;
            holder.setTextNotHide(R.id.tv_name, item.getName());
            holder.setImage(R.id.img_cover, item.getCover());
        }
    }

    private class TipDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_template_list_tip;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof String;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {

        }
    }
}

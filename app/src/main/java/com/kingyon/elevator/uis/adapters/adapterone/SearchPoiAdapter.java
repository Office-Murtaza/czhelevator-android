package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.amap.api.services.core.PoiItem;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.adapters.ItemViewDelegate;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import java.util.List;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class SearchPoiAdapter extends MultiItemTypeAdapter<Object> {

    private String keyWord;

    public SearchPoiAdapter(Context context, List<Object> mItems) {
        super(context, mItems);
        addItemViewDelegate(1, new PoiDelegate());
        addItemViewDelegate(2, new TipDelegate());
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    private class PoiDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.fragment_search_poi_poi;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof PoiItem;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            PoiItem item = (PoiItem) o;
            holder.setTextNotHide(R.id.tv_name, getSearchSpan(keyWord, item.getTitle()));
        }

        private CharSequence getSearchSpan(String keyWord, String title) {
            if (TextUtils.isEmpty(keyWord) || TextUtils.isEmpty(title)) {
                return title;
            }
            int index1 = title.indexOf(keyWord);
            int index2 = index1 + keyWord.length();
            if (index1 < 0 || index1 >= title.length()) {
                return title;
            }
            if (index2 < 0 || index2 > title.length()) {
                return title;
            }
            SpannableString spannableString = new SpannableString(title);
            spannableString.setSpan(new ForegroundColorSpan(0xFFEC7A12), index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    }

    private class TipDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.fragment_search_poi_tip;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof String;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            holder.setTextNotHide(R.id.tv_name, String.valueOf(o));
        }
    }
}

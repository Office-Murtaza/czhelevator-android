package com.zhaoss.weixinrecorded.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zhaoss.weixinrecorded.R;

import java.util.List;

/**
 * Created By Admin  on 2020/3/20
 * Email : 1531603384@qq.com
 */
class MyiconAdapter extends BaseAdapter {
    Context context;
    List<Integer> integers;

    private LayoutInflater layoutInflater;
    public MyiconAdapter(Context context, List<Integer> integers) {
        this.context =context;
        this.integers =integers;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return integers.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.imag_item_layout, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(integers.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }

}

package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kingyon.elevator.R;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;


/**
 * Created by GongLi on 2017/11/2.
 * Email：lc824767150@163.com
 */

public class BannerAdaper<T> extends PagerAdapter {

    private Context context;
    private List<T> bannerEntities;
    private OnPagerClickListener<T> onPagerClickListener;

    public void setOnPagerClickListener(OnPagerClickListener<T> onPagerClickListener) {
        this.onPagerClickListener = onPagerClickListener;
    }

    public BannerAdaper(Context context, List<T> bannerEntities) {
        this.context = context;
        this.bannerEntities = bannerEntities;
    }

    public void setBannerEntities(List<T> bannerEntities) {
        this.bannerEntities = bannerEntities;
    }

    @Override
    public int getCount() {
        return bannerEntities == null ? 0 : bannerEntities.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        Object item = bannerEntities.get(position % getCount());
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.layout_banner, container, false);
        GlideUtils.loadImage(context, item.toString(), imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPagerClickListener != null && bannerEntities != null && bannerEntities.size() > 0) {
                    onPagerClickListener.onBannerClickListener(position, bannerEntities.get(position % bannerEntities.size()), bannerEntities);
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }

    public interface OnPagerClickListener<K> {
        void onBannerClickListener(int position, K item, List<K> datas);
    }
}

package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.utils.FormatUtils;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GongLi on 2018/12/29.
 * Email：lc824767150@163.com
 */

public class SearchCellsAdapter extends PagerAdapter {

    private Context mContext;
    private List<CellItemEntity> mItems;
    private OnPagerClickListener onPagerClickListener;

    public SearchCellsAdapter(Context context, List<CellItemEntity> bannerEntities) {
        mContext = context;
        mItems = bannerEntities;
    }

    public void setBannerEntities(List<CellItemEntity> bannerEntities) {
        mItems = bannerEntities;
    }

    public void setOnPagerClickListener(OnPagerClickListener onPagerClickListener) {
        this.onPagerClickListener = onPagerClickListener;
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        CellItemEntity item = mItems.get(position % getCount());

        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_search_cell_item, container, false);
        ViewHolder holder = new ViewHolder(view);
        GlideUtils.loadImage(mContext, item.getCellLogo(), holder.imgCover);
        holder.tvName.setText(item.getCellName());
        holder.tvAddress.setText(item.getAddress());
        holder.tvInstance.setText(FormatUtils.getInstance().getCellDistance(item.getLongitude(), item.getLatitude(), item.getDistance()));
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPagerClickListener != null && mItems != null && mItems.size() > 0) {
                    onPagerClickListener.onBannerClickListener(v, position, mItems.get(position % mItems.size()), mItems);
                }
            }
        };
        holder.llRoot.setOnClickListener(onClickListener);
        holder.tvChoose.setOnClickListener(onClickListener);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public long getSelectedId(int position) {
        long result;
        if (mItems != null && position >= 0 && position < mItems.size()) {
            result = mItems.get(position).getObjctId();
        } else {
            result = 0;
        }
        return result;
    }

    public Integer getIdIndex(long clickCellId) {
        Integer index = null;
        if (mItems != null) {
            for (int i = 0; i < mItems.size(); i++) {
                if (clickCellId == mItems.get(i).getObjctId()) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public class ViewHolder {
        @BindView(R.id.img_cover)
        ImageView imgCover;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_instance)
        TextView tvInstance;
        @BindView(R.id.tv_choose)
        TextView tvChoose;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnPagerClickListener {
        void onBannerClickListener(View view, int position, CellItemEntity item, List<CellItemEntity> datas);
    }
}

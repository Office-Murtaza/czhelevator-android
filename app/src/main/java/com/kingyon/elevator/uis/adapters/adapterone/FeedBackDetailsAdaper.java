package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.view.View;

import com.gerry.scaledelete.DeletedImageScanDialog;
import com.gerry.scaledelete.ScanleImageUrl;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.FeedBackEntity;
import com.kingyon.elevator.entities.FeedBackMessageEntity;
import com.kingyon.elevator.entities.ImageScan;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.widgets.BlankRecyclerView;
import com.kingyon.elevator.uis.widgets.FullyGridLayoutManager;
import com.kingyon.elevator.utils.DealScrollRecyclerView;
import com.leo.afbaselibrary.uis.adapters.ItemViewDelegate;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class FeedBackDetailsAdaper extends MultiItemTypeAdapter<Object> {
    public FeedBackDetailsAdaper(Context context, ArrayList<Object> mItems) {
        super(context, mItems);
        addItemViewDelegate(1, new MessageDelegate());
        addItemViewDelegate(2, new FeedBackDelegate());
    }

    private class MessageDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_feed_back_details_message;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof FeedBackMessageEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            FeedBackMessageEntity item = (FeedBackMessageEntity) o;
            holder.setTextNotHide(R.id.tv_from, item.isMe() ? "我的回复" : "平台回复");
            holder.setTextNotHide(R.id.tv_time, TimeUtil.getRecentlyTime(item.getTime()));
            holder.setTextNotHide(R.id.tv_content, item.getContent());
            holder.setVisible(R.id.v_line, position != mItems.size() - 1);
        }
    }

    private class FeedBackDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_feed_back_details_feedback;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof FeedBackEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            FeedBackEntity item = (FeedBackEntity) o;
            holder.setTextNotHide(R.id.tv_name, item.getTitile());
            BlankRecyclerView recyclerView = holder.getView(R.id.rv_images);
            ImageShowAdapter imageAdapter = (ImageShowAdapter) recyclerView.getAdapter();
            if (imageAdapter == null) {
                imageAdapter = new ImageShowAdapter(mContext);
                imageAdapter.setShowSize(true);
                DealScrollRecyclerView.getInstance().dealAdapter(imageAdapter, recyclerView, new FullyGridLayoutManager(mContext, 3));
                imageAdapter.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<Object>() {
                    @Override
                    public void onItemClick(View view, int position, Object object, BaseAdapterWithHF<Object> baseAdaper) {
                        showImageScan(position, baseAdaper);
                    }
                });
            }
            imageAdapter.refreshDatas(item.getImages());
            holder.setTextNotHide(R.id.tv_time, TimeUtil.getRecentlyTime(item.getTime()));
        }
    }

    private void showImageScan(int position, BaseAdapterWithHF<Object> baseAdaper) {
        if (baseAdaper.getItemRealCount() > 0) {
            List<ScanleImageUrl> urls = new ArrayList<>();
            List<Object> datas = baseAdaper.getDatas();
            for (Object obj : datas) {
                urls.add(new ImageScan(obj.toString()));
            }
            if (urls.size() > 0 && urls.size() == datas.size()) {
                DeletedImageScanDialog dialog = new DeletedImageScanDialog(mContext, urls, position, null);
                dialog.show(position, false, false);
            }
        }
    }
}

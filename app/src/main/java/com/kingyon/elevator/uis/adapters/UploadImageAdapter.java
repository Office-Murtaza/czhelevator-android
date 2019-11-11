package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.widgets.ProportionFrameLayout;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lc on 2017/11/11.
 */

public class UploadImageAdapter extends BaseAdapterWithHF<Object> {

    private boolean showMainImage;
    private boolean ratio;

    public boolean isShowMainImage() {
        return showMainImage;
    }

    public void setShowMainImage(boolean showMainImage) {
        this.showMainImage = showMainImage;
    }

    private int maxCount = 12;
//    private boolean deleted;

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

//    public boolean isDeleted() {
//        return deleted;
//    }
//
//    public void setDeleted(boolean deleted) {
//        if (this.deleted != deleted) {
//            this.deleted = deleted;
//            notifyDataSetChanged();
//        }
//    }

    public UploadImageAdapter(Context context) {
        super(context);
    }

    public UploadImageAdapter(Context context, boolean ratio) {
        super(context);
        this.ratio = ratio;
    }

    @Override
    public int getFooterCount() {
        return datas.size() < maxCount ? 1 : 0;
    }

    @Override
    public int getHeaderCount() {
        return 0;
    }

    @NonNull
    @Override
    public BaseAdapterWithHF.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        BaseAdapterWithHF.ViewHolder viewHolder;
        if (viewType == TYPE_FOOTER) {
            view = inflater.inflate(R.layout.activity_upload_footer, parent, false);
            viewHolder = new BaseAdapterWithHF.ViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.activity_upload_content, parent, false);
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    public List<File> getUploadDatas() {
        List<File> files = new ArrayList<>();
        for (Object object : getDatas()) {
            String url = object.toString();
            if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
                files.add(new File(url));
            }
        }
        return files;
    }

    public List<String> getAllDatas() {
        List<String> result = new ArrayList<>();
        for (Object object : getDatas()) {
            String url = object.toString();
            if (!TextUtils.isEmpty(url)) {
                result.add(url);
            }
        }
        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapterWithHF.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_CONTENT) {
            ViewHolder holder = (ViewHolder) viewHolder;

            if (ratio) {
                holder.pflProportion.setProporty(1);
            }

            Object item = getItemData(position);
            String path = item.toString();
            if (path != null && path.startsWith("http")) {
                GlideUtils.loadImage(context, path, holder.imgSrc);
            } else {
                GlideUtils.loadImage(context, String.format("file://%s", path), holder.imgSrc);
            }
//            if (deleted){
            holder.imgDelete.setVisibility(View.VISIBLE);
//            }else {
//                holder.imgDelete.setVisibility(View.GONE);
//            }
//            holder.tvMainImage.setVisibility((showMainImage && position == 0) ? View.VISIBLE : View.GONE);
        } else {
            if (ratio) {
                ProportionFrameLayout proportionFrameLayout = viewHolder.itemRoot.findViewById(R.id.pfl_proportion);
                proportionFrameLayout.setProporty(1);
            }
        }
    }

    public class ViewHolder extends BaseAdapterWithHF.ViewHolder {
        @BindView(R.id.tv_main_image)
        TextView tvMainImage;
        @BindView(R.id.img_src)
        ImageView imgSrc;
        @BindView(R.id.img_delete)
        ImageView imgDelete;
        @BindView(R.id.pfl_proportion)
        ProportionFrameLayout pflProportion;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            imgDelete.setOnClickListener(this);
        }
    }
}

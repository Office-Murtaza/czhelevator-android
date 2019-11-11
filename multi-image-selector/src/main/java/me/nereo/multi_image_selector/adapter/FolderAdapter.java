package me.nereo.multi_image_selector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.afbaselibrary.utils.GlideUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiFilterType;
import me.nereo.multi_image_selector.R;
import me.nereo.multi_image_selector.bean.Folder;
import me.nereo.multi_image_selector.bean.Image;
import me.nereo.multi_image_selector.utils.CollectionUtils;

/**
 * 文件夹Adapter
 * Created by Nereo on 2015/4/7.
 * Updated by nereo on 2016/1/19.
 */
public class FolderAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<Folder> mFolders = new ArrayList<>();

    int mImageSize;

    int lastSelected = 0;
    private int filterType;

    public FolderAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageSize = mContext.getResources().getDimensionPixelOffset(R.dimen.mis_folder_cover_size);
    }

    /**
     * 设置数据集
     *
     * @param folders
     */
    public void setData(List<Folder> folders) {
        if (folders != null && folders.size() > 0) {
            mFolders = folders;
            switch (filterType) {
                case MultiFilterType.IMAGE:
                    List<Image> totalImages = new ArrayList<>();
                    for (Folder item1 : folders) {
                        if (item1.images != null) {
                            totalImages.addAll(item1.images);
                        }
                    }
                    Folder folder1 = new Folder();
                    folder1.images = totalImages;
                    folder1.name = mContext.getString(R.string.mis_folder_image_all);
                    folder1.cover = totalImages.size() > 0 ? totalImages.get(0) : null;
                    folder1.path = "/sdcard";
                    folder1.custom = true;
                    folder1.type = MultiFilterType.IMAGE;
                    folders.add(0, folder1);
                    break;
                case MultiFilterType.VIDEO:
                    List<Image> totalVideos = new ArrayList<>();
                    for (Folder item2 : folders) {
                        if (item2.images != null) {
                            totalVideos.addAll(item2.images);
                        }
                    }
                    Folder folder2 = new Folder();
                    folder2.images = totalVideos;
                    folder2.name = mContext.getString(R.string.mis_folder_video_all);
                    folder2.cover = totalVideos.size() > 0 ? totalVideos.get(0) : null;
                    folder2.path = "/sdcard";
                    folder2.custom = true;
                    folder2.type = MultiFilterType.VIDEO;
                    folders.add(0, folder2);
                    break;
                case MultiFilterType.ALL:
                    List<Image> totals = new ArrayList<>();
                    List<Image> videos = new ArrayList<>();
                    List<Image> images = new ArrayList<>();
                    for (Folder item3 : folders) {
                        if (item3.images != null) {
//                            item3.images.removeAll(CollectionUtils.nullCollection());
                            for (Image image : item3.images) {
                                if (image.video) {
                                    videos.add(image);
                                } else {
                                    images.add(image);
                                }
                                totals.add(image);
                            }
                        }
                    }

                    Folder videoFolder = new Folder();
                    videoFolder.images = videos;
                    videoFolder.name = mContext.getString(R.string.mis_folder_video_all);
                    videoFolder.cover = videos.size() > 0 ? videos.get(0) : null;
                    videoFolder.path = "/sdcard";
                    videoFolder.custom = true;
                    videoFolder.type = MultiFilterType.VIDEO;
                    folders.add(0, videoFolder);

                    Folder imagesFolder = new Folder();
                    imagesFolder.images = images;
                    imagesFolder.name = mContext.getString(R.string.mis_folder_image_all);
                    imagesFolder.cover = images.size() > 0 ? images.get(0) : null;
                    imagesFolder.path = "/sdcard";
                    imagesFolder.custom = true;
                    imagesFolder.type = MultiFilterType.IMAGE;
                    folders.add(0, imagesFolder);

                    Folder totalFolder = new Folder();
                    totalFolder.images = totals;
                    totalFolder.name = mContext.getString(R.string.mis_folder_all);
                    totalFolder.cover = totals.size() > 0 ? totals.get(0) : null;
                    totalFolder.path = "/sdcard";
                    totalFolder.custom = true;
                    totalFolder.type = MultiFilterType.ALL;
                    folders.add(0, totalFolder);
                    break;
            }
        } else {
            mFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
//        return mFolders.size() + 1;
        return mFolders.size();
    }

    @Override
    public Folder getItem(int i) {
//        Folder folder;
//        switch (filterType) {
//            case MultiFilterType.IMAGE:
//                if (i < 1) {
//                    folder = null;
//                } else {
//                    folder = mFolders.get(i - 1);
//                }
//                break;
//            case MultiFilterType.VIDEO:
//                if (i < 1) {
//                    folder = null;
//                } else {
//                    folder = mFolders.get(i - 1);
//                }
//                break;
//            case MultiFilterType.ALL:
//                if (i < 3) {
//                    folder = null;
//                } else {
//                    folder = mFolders.get(i - 3);
//                }
//                break;
//            default:
//                folder = mFolders.get(i);
//                break;
//        }
//        return folder;
        return mFolders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.mis_list_item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            Folder item = getItem(i);
            if (item == null) {
                String categoryText;
                if (i == 0) {
                    switch (filterType) {
                        case MultiFilterType.IMAGE:
                            categoryText = mContext.getString(R.string.mis_folder_image_all);
                            break;
                        case MultiFilterType.VIDEO:
                            categoryText = mContext.getString(R.string.mis_folder_video_all);
                            break;
                        default:
                            categoryText = mContext.getString(R.string.mis_folder_all);
                            break;
                    }
                } else if (i == 1) {
                    categoryText = mContext.getString(R.string.mis_folder_image_all);
                } else {
                    categoryText = mContext.getString(R.string.mis_folder_video_all);
                }
                holder.name.setText(categoryText);
                holder.path.setText("/sdcard");
                holder.size.setText(String.format("%d%s",
                        getTotalImageSize(), mContext.getResources().getString(R.string.mis_photo_unit)));
                if (mFolders.size() > 0) {
                    Folder f = mFolders.get(0);
                    if (f != null) {
//                        Picasso.with(mContext)
//                                .load(new File(f.cover.path))
//                                .error(R.drawable.mis_default_error)
//                                .resizeDimen(R.dimen.mis_folder_cover_size, R.dimen.mis_folder_cover_size)
//                                .centerCrop()
//                                .into(holder.cover);
                        if (f.cover.video) {
                            GlideUtils.loadLocalFrame(mContext, f.cover.path, holder.cover);
                        } else {
                            GlideUtils.loadImage(mContext, new File(f.cover.path), holder.cover);
                        }
                    } else {
                        holder.cover.setImageResource(R.drawable.mis_default_error);
                    }
                }
            } else {
                holder.bindData(item);
            }
            if (lastSelected == i) {
                holder.indicator.setVisibility(View.VISIBLE);
            } else {
                holder.indicator.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private int getTotalImageSize() {
        int result = 0;
        if (mFolders != null && mFolders.size() > 0) {
            for (Folder f : mFolders) {
                result += f.images.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    class ViewHolder {
        ImageView cover;
        TextView name;
        TextView path;
        TextView size;
        ImageView indicator;

        ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            path = (TextView) view.findViewById(R.id.path);
            size = (TextView) view.findViewById(R.id.size);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bindData(Folder data) {
            if (data == null) {
                return;
            }
            name.setText(data.name);
            path.setText(data.path);
            if (data.images != null) {
                size.setText(String.format("%d%s", data.images.size(), mContext.getResources().getString(R.string.mis_photo_unit)));
//                size.setText(String.format("数量：%d", data.images.size()));
            } else {
                size.setText("*" + mContext.getResources().getString(R.string.mis_photo_unit));
//                size.setText("数量：*");
            }
            if (data.cover != null) {
                // 显示图片
//                Picasso.with(mContext)
//                        .load(new File(data.cover.path))
//                        .placeholder(R.drawable.mis_default_error)
//                        .resizeDimen(R.dimen.mis_folder_cover_size, R.dimen.mis_folder_cover_size)
//                        .centerCrop()
//                        .into(cover);
                if (data.cover.video) {
                    GlideUtils.loadLocalFrame(mContext, data.cover.path, cover);
                } else {
                    GlideUtils.loadImage(mContext, new File(data.cover.path), cover);
                }
            } else {
                cover.setImageResource(R.drawable.mis_default_error);
            }
        }
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }
}

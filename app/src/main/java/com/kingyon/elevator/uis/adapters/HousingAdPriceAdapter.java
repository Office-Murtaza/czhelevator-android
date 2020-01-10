package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.photopicker.MediaDirectory;
import com.kingyon.elevator.utils.DensityUtil;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;

/**
 * Created By SongPeng  on 2019/12/13
 * Email : 1531603384@qq.com
 * 订单页明细对话框小区数据适配器
 */
public class HousingAdPriceAdapter extends BaseAdapter {

    private List<CellItemEntity> cellItemEntityList;
    private LayoutInflater mInflater;
    Context context;
    private MediaDirectory myAdMediaDirectory;
    private String planType;
    private int totalCount;

    public HousingAdPriceAdapter(Context context, int totalCount, String planType, List<CellItemEntity> cellItemEntityList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.cellItemEntityList = cellItemEntityList;
        this.planType = planType;
        this.totalCount = totalCount;
    }

    public void reflashData(List<CellItemEntity> cellItemEntityList) {
        this.cellItemEntityList = cellItemEntityList;
        notifyDataSetChanged();
    }

    /**
     * 添加我的广告和所有照片
     */
    private void addAllFolderAndMyAd() {
        myAdMediaDirectory = new MediaDirectory();
        myAdMediaDirectory.setId("我的广告");
        myAdMediaDirectory.setCoverPath("");
        myAdMediaDirectory.setDirPath("0");
        myAdMediaDirectory.setName("我的广告");
    }


    /**
     * 刷新我的广告数据
     *
     * @param coverPath
     * @param count
     */
    public void updateMyAdInfo(String coverPath, String count) {
        if (myAdMediaDirectory != null) {
            myAdMediaDirectory.setCoverPath(coverPath);
            myAdMediaDirectory.setDirPath(count);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cellItemEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cellItemEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.housing_ad_price_item_layout, null);
            holder.housing_name = convertView.findViewById(R.id.housing_name);
            holder.all_price = convertView.findViewById(R.id.all_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CellItemEntity cellItemEntity = cellItemEntityList.get(position);
        holder.housing_name.setText(cellItemEntity.getCellName());
        String pointText = "";
        if (planType.equals(Constants.PLAN_TYPE.BUSINESS)) {
            pointText = cellItemEntity.getChoosedScreenNum() + "/台x" +
                    totalCount + "/天x¥" + cellItemEntity.getBusinessAdPrice()
                    + "=" + cellItemEntity.getChoosedScreenNum() * cellItemEntity.getBusinessAdPrice() * totalCount;
        } else if (planType.equals(Constants.PLAN_TYPE.DIY)) {
            pointText = cellItemEntity.getChoosedScreenNum() + "/台x" +
                    totalCount + "/天x¥" + cellItemEntity.getDiyAdPrice()
                    + "=" + cellItemEntity.getChoosedScreenNum() * cellItemEntity.getDiyAdPrice() * totalCount;
        } else {
            pointText = cellItemEntity.getChoosedScreenNum() + "/台x" +
                    totalCount + "/天x¥" + cellItemEntity.getInformationAdPrice()
                    + "=" + cellItemEntity.getChoosedScreenNum() * cellItemEntity.getInformationAdPrice() * totalCount;
        }
        holder.all_price.setText(pointText);
        return convertView;
    }

    public final class ViewHolder {
        TextView housing_name;
        TextView all_price;
    }

}

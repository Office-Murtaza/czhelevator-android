package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.customview.MyGridView;
import com.kingyon.elevator.date.CustomDatePickerView;
import com.kingyon.elevator.entities.HorizontalSelectDateEntity;
import com.kingyon.elevator.entities.SelectDateEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 * 水平滑动日期选择适配器
 */
public class HorizontalSelectDateAdapter extends RecyclerView.Adapter<HorizontalSelectDateAdapter.ViewHolder> {

    private List<HorizontalSelectDateEntity> horizontalSelectDateEntityList;
    private Context context;
    protected LayoutInflater inflater;
    private BaseOnItemClick<HorizontalSelectDateEntity> baseOnItemClick;
    public SelectDateEntity startSelectDateEntity;
    public SelectDateEntity endSelectDateEntity;
    public SparseArray<GridDateAdapter> gridDateAdapterMap;
    public int  totalDayCount=0;//总共多少天

    public HorizontalSelectDateAdapter(Context context, List<HorizontalSelectDateEntity> horizontalSelectDateEntityList) {
        this.context = context;
        this.horizontalSelectDateEntityList = horizontalSelectDateEntityList;
        this.inflater = LayoutInflater.from(context);
        startSelectDateEntity = getLastDay();
        setHasStableIds(true);
        gridDateAdapterMap = new SparseArray<GridDateAdapter>();
        for (int i = 0; i < horizontalSelectDateEntityList.size(); i++) {
            HorizontalSelectDateEntity horizontalSelectDateEntity = horizontalSelectDateEntityList.get(i);
            gridDateAdapterMap.put(i, new GridDateAdapter(context, this,
                    horizontalSelectDateEntity.getYear(), horizontalSelectDateEntity.getMonth(),
                    horizontalSelectDateEntity.getDayCount(), horizontalSelectDateEntity.getStartPosition()));
        }
    }

    public void setBaseOnItemClick(BaseOnItemClick<HorizontalSelectDateEntity> baseOnItemClick) {
        this.baseOnItemClick = baseOnItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.horizontal_date_partner_item_layout, parent, false);
        return new ViewHolder(view);
    }


    public void reflashData(List<HorizontalSelectDateEntity> horizontalSelectDateEntityList) {
        this.horizontalSelectDateEntityList = horizontalSelectDateEntityList;
        notifyDataSetChanged();
    }

    public void reflashGridData(int position){
        gridDateAdapterMap.get(position).notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.custom_date_picker_view.getTag() == null) {
            LogUtils.d("设置数据执行-------------------------------",position);
            holder.custom_date_picker_view.setTag(position);
            HorizontalSelectDateEntity horizontalSelectDateEntity = horizontalSelectDateEntityList.get(position);
            holder.custom_date_picker_view.initData(this,horizontalSelectDateEntity.getYear(),
                    horizontalSelectDateEntity.getMonth(),horizontalSelectDateEntity.getDayCount());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return horizontalSelectDateEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CustomDatePickerView custom_date_picker_view;


        public ViewHolder(View itemView) {
            super(itemView);
            custom_date_picker_view = itemView.findViewById(R.id.custom_date_picker_view);
        }
    }

    private SelectDateEntity getLastDay() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return new SelectDateEntity(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

}

package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.customview.MyGridView;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.entities.DateGridEntity;
import com.kingyon.elevator.entities.HorizontalSelectDateEntity;
import com.kingyon.elevator.entities.SelectDateEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 * 宫格日期选择适配器
 */
public class RecyclerviewGridDateAdapter extends RecyclerView.Adapter<RecyclerviewGridDateAdapter.ViewHolder> {
    private List<DateGridEntity> dateGridEntities;
    protected LayoutInflater inflater;
    Context context;
    HorizontalSelectDateAdapter horizontalSelectDateAdapter;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleBeforeDateFormat;
    Date currentDate;

    public RecyclerviewGridDateAdapter(Context context, HorizontalSelectDateAdapter horizontalSelectDateAdapter, int year, int month, int dayCount, int startPosition) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        dateGridEntities = new ArrayList<>();
        currentDate = new Date();
        this.horizontalSelectDateAdapter = horizontalSelectDateAdapter;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleBeforeDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            currentDate = simpleBeforeDateFormat.parse(simpleDateFormat.format(currentDate) + " 00:00:00");
        } catch (ParseException e) {
            currentDate = new Date();
        }
        for (int i = 0; i < startPosition; i++) {
            dateGridEntities.add(new DateGridEntity(year, month, 50, 0));
        }
        for (int i = 1; i <= dayCount; i++) {
            dateGridEntities.add(new DateGridEntity(year, month, i, 1));
        }
    }

//    public void setBaseOnItemClick(BaseOnItemClick<HorizontalSelectDateEntity> baseOnItemClick) {
//        this.baseOnItemClick = baseOnItemClick;
//    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.grid_date_item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateGridEntity dateGridEntity = dateGridEntities.get(position);
        if (dateGridEntity.getType() == 0) {
            holder.tv_date_day.setText("");
        } else {
            holder.tv_date_day.setText(dateGridEntity.getTimeDay() + "");
        }
        if (isBeforeDate(dateGridEntity)) {
            holder.tv_date_day.setTextColor(Color.parseColor("#E1DFDF"));
        } else {
            // holder.tv_date_day.setTextColor(Color.parseColor("#404040"));
            if (dateGridEntity.getType() == 1) {
                if (horizontalSelectDateAdapter.startSelectDateEntity != null) {
                    if (horizontalSelectDateAdapter.endSelectDateEntity != null) {
                        if (isWhetherInInterval(horizontalSelectDateAdapter.startSelectDateEntity
                                , horizontalSelectDateAdapter.endSelectDateEntity, dateGridEntity)) {
                            holder.tv_date_day.setBackground(null);
                            holder.end_split_line.setVisibility(View.GONE);
                            holder.tv_total_count.setVisibility(View.GONE);
                            holder.end_time_bg.setVisibility(View.VISIBLE);
                            holder.start_time_bg.setVisibility(View.VISIBLE);
                            holder.tv_date_day.setTextColor(Color.parseColor("#ffffff"));
                        } else {
                            if (isStartTime(horizontalSelectDateAdapter.startSelectDateEntity, dateGridEntity)) {
                                holder.tv_date_day.setBackgroundResource(R.drawable.shape_select_start_date_bg);
                                holder.start_time_bg.setVisibility(View.VISIBLE);
                                holder.end_split_line.setVisibility(View.GONE);
                                holder.tv_total_count.setVisibility(View.GONE);
                                holder.end_time_bg.setVisibility(View.INVISIBLE);
                                holder.tv_date_day.setTextColor(Color.parseColor("#ffffff"));
                            } else {
                                if (isStartTime(horizontalSelectDateAdapter.endSelectDateEntity, dateGridEntity)) {
                                    holder.tv_date_day.setBackgroundResource(R.drawable.shape_select_start_date_bg);
                                    holder.start_time_bg.setVisibility(View.INVISIBLE);
                                    holder.end_time_bg.setVisibility(View.VISIBLE);
                                    holder.end_split_line.setVisibility(View.VISIBLE);
                                    holder.tv_total_count.setVisibility(View.VISIBLE);
                                    holder.tv_date_day.setTextColor(Color.parseColor("#ffffff"));
                                    holder.tv_total_count.setText("共计" + getDiffDay() + "天");
                                } else {
                                    holder.tv_date_day.setBackground(null);
                                    holder.start_time_bg.setVisibility(View.INVISIBLE);
                                    holder.end_time_bg.setVisibility(View.INVISIBLE);
                                    holder.end_split_line.setVisibility(View.GONE);
                                    holder.tv_total_count.setVisibility(View.GONE);
                                    holder.tv_date_day.setTextColor(Color.parseColor("#404040"));
                                }
                            }
                        }
                    } else {
                        holder.start_time_bg.setVisibility(View.INVISIBLE);
                        holder.end_time_bg.setVisibility(View.INVISIBLE);
                        holder.end_split_line.setVisibility(View.GONE);
                        holder.tv_total_count.setVisibility(View.GONE);
                        if (isStartTime(horizontalSelectDateAdapter.startSelectDateEntity, dateGridEntity)) {
                            holder.tv_date_day.setBackgroundResource(R.drawable.shape_select_start_date_bg);
                            holder.tv_date_day.setTextColor(Color.parseColor("#ffffff"));
                        } else {
                            holder.tv_date_day.setTextColor(Color.parseColor("#404040"));
                            holder.tv_date_day.setBackground(null);
                        }
                    }
                } else {
                    holder.start_time_bg.setVisibility(View.INVISIBLE);
                    holder.end_time_bg.setVisibility(View.INVISIBLE);
                    holder.end_split_line.setVisibility(View.GONE);
                    holder.tv_total_count.setVisibility(View.GONE);
                    holder.tv_date_day.setTextColor(Color.parseColor("#404040"));
                    holder.tv_date_day.setBackground(null);
                }
            } else {
                holder.start_time_bg.setVisibility(View.INVISIBLE);
                holder.end_time_bg.setVisibility(View.INVISIBLE);
                holder.end_split_line.setVisibility(View.GONE);
                holder.tv_total_count.setVisibility(View.GONE);
                holder.tv_date_day.setTextColor(Color.parseColor("#404040"));
                holder.tv_date_day.setBackground(null);
            }
        }
      //  ViewHolder finalHolder = holder;
        holder.tv_date_day.setOnClickListener(v -> {
            if (dateGridEntity.getType() == 1) {
                if (horizontalSelectDateAdapter.startSelectDateEntity == null) {
                    if (!isBeforeDate(dateGridEntity)) {
                        horizontalSelectDateAdapter.startSelectDateEntity = new SelectDateEntity(dateGridEntity.getYear(), dateGridEntity.getMonth(), dateGridEntity.getTimeDay());
                        holder.tv_date_day.setBackgroundResource(R.drawable.shape_select_start_date_bg);
                    }
                } else {
                    if (isStartTime(horizontalSelectDateAdapter.startSelectDateEntity, dateGridEntity)) {
                        if (horizontalSelectDateAdapter.endSelectDateEntity == null) {
                            horizontalSelectDateAdapter.startSelectDateEntity = null;
                        }
                    } else {
                        if (horizontalSelectDateAdapter.endSelectDateEntity == null) {
                            if (isAfterStartDate(dateGridEntity)) {
                                horizontalSelectDateAdapter.endSelectDateEntity = new SelectDateEntity(dateGridEntity.getYear(), dateGridEntity.getMonth(), dateGridEntity.getTimeDay());
                                holder.tv_date_day.setBackgroundResource(R.drawable.shape_select_start_date_bg);
                            } else {
                                if (!isBeforeDate(dateGridEntity)) {
                                    horizontalSelectDateAdapter.startSelectDateEntity = new SelectDateEntity(dateGridEntity.getYear(), dateGridEntity.getMonth(), dateGridEntity.getTimeDay());
                                    holder.tv_date_day.setBackgroundResource(R.drawable.shape_select_start_date_bg);
                                    // MyToastUtils.showShort("结束日期应大于开始日期");
                                }
                            }
                        } else {
                            if (isStartTime(horizontalSelectDateAdapter.endSelectDateEntity, dateGridEntity)) {
                                //取消选中的结束时间---------
                                horizontalSelectDateAdapter.endSelectDateEntity = null;
                            } else {
                                if (isBeforeStartDate(dateGridEntity)) {
                                    horizontalSelectDateAdapter.startSelectDateEntity = new SelectDateEntity(dateGridEntity.getYear(), dateGridEntity.getMonth(), dateGridEntity.getTimeDay());
                                    holder.tv_date_day.setBackgroundResource(R.drawable.shape_select_start_date_bg);
                                }else {
                                    horizontalSelectDateAdapter.endSelectDateEntity = new SelectDateEntity(dateGridEntity.getYear(), dateGridEntity.getMonth(), dateGridEntity.getTimeDay());
                                    holder.tv_date_day.setBackgroundResource(R.drawable.shape_select_start_date_bg);
                                }
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dateGridEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date_day;
        View end_time_bg;
        View start_time_bg;
        TextView tv_total_count;
        View end_split_line;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_date_day = itemView.findViewById(R.id.tv_date_day);
            end_time_bg = itemView.findViewById(R.id.end_time_bg);
            start_time_bg = itemView.findViewById(R.id.start_time_bg);
            tv_total_count = itemView.findViewById(R.id.tv_total_count);
            end_split_line = itemView.findViewById(R.id.end_split_line);
        }
    }


    private Boolean isStartTime(SelectDateEntity selectDateEntity, DateGridEntity dateGridEntity) {
        if (selectDateEntity.getYear() == dateGridEntity.getYear() &&
                selectDateEntity.getMonth() == dateGridEntity.getMonth()
                && selectDateEntity.getDay() == dateGridEntity.getTimeDay()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 是否在开始和结束日期之间
     *
     * @return
     */
    private Boolean isWhetherInInterval(SelectDateEntity startSelectDateEntity, SelectDateEntity endSelectDateEntity, DateGridEntity dateGridEntity) {
        try {
            if (DateUtils.belongCalendar(simpleDateFormat.parse(dateGridEntity.getDate()),
                    simpleDateFormat.parse(startSelectDateEntity.getDate()),
                    simpleDateFormat.parse(endSelectDateEntity.getDate()))) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }


    /**
     * 是否在开始日期之后
     *
     * @return
     */
    private Boolean isAfterStartDate(DateGridEntity dateGridEntity) {
        try {
            if (DateUtils.afterCalendar(simpleDateFormat.parse(dateGridEntity.getDate()),
                    simpleDateFormat.parse(horizontalSelectDateAdapter.startSelectDateEntity.getDate()))) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }


    /**
     * 是否在今日开始日期之前，如果在之前，则全部置灰，不可点击
     *
     * @return
     */
    private Boolean isBeforeDate(DateGridEntity dateGridEntity) {
        try {
            if (DateUtils.beforeCalendar(simpleDateFormat.parse(dateGridEntity.getDate()), currentDate)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 是否在选择开始日期之前
     * @param dateGridEntity
     * @return
     */
    private Boolean isBeforeStartDate(DateGridEntity dateGridEntity) {
        try {
            if (DateUtils.beforeCalendar(simpleDateFormat.parse(dateGridEntity.getDate()), simpleDateFormat.parse(horizontalSelectDateAdapter.startSelectDateEntity.getDate()))) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    private int getDiffDay() {
        try {
            Date start = simpleDateFormat.parse(horizontalSelectDateAdapter.startSelectDateEntity.getDate());
            Date end = simpleDateFormat.parse(horizontalSelectDateAdapter.endSelectDateEntity.getDate());
            return DateUtils.getDatePoorDay(end, start) + 1;
        } catch (ParseException e) {
            return 0;
        }
    }

}

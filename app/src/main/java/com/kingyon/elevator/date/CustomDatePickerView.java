package com.kingyon.elevator.date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.DateGridEntity;
import com.kingyon.elevator.entities.SelectDateEntity;
import com.kingyon.elevator.uis.adapters.HorizontalSelectDateAdapter;
import com.kingyon.elevator.utils.DensityUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created By SongPeng  on 2019/12/25
 * Email : 1531603384@qq.com
 */
public class CustomDatePickerView extends View {
    // 画笔
    private Paint paint;
    /**
     * 日期的列数
     */
    private static final int Column = 7;
    /**
     * 日期的行数
     */
    private static final int Rows = 6;
    /**
     * 在开始和结束日期之间的日期的行背景颜色
     */
    private int betweenStartAndEndDateLineBg = Color.parseColor("#F8DABE");

    /**
     * 选中日期的文字颜色
     */
    private int selectedDateTextColor = Color.parseColor("#ffffff");

    /**
     * 可选日期的文字颜色
     */
    private int optionalDateTextColor = Color.parseColor("#404040");

    /**
     * 不可选日期的文字颜色
     */
    private int notOptionalDateTextColor = Color.parseColor("#E1DFDF");

    /**
     * 选中的开始和结束日期背景颜色
     */
    private int startAndEndDateViewBg = Color.parseColor("#FF8003");

    /**
     * 总共多少天的文字颜色
     */
    private int totalCountTextColor = Color.parseColor("#EB7A12");


    /**
     * 总共多少天的分割线颜色
     */
    private int totalCountLineColor = Color.parseColor("#FF8003");

    // 日期字体大小
    private int dateTextSize = 14;


    // DisplayMetrics对象
    private DisplayMetrics displayMetrics;

    // 当前年
    private int mCurrentYear;
    // 当前月
    private int mCurrentMonth;

    /**
     * 一号日期的坐标，计算星期几来获取位置
     */
    private int[] oneDatePosition;
    /**
     * 每一列的宽度
     */
    private int columnWidth;
    /**
     * 每一行的高度
     */
    private int rowHeight;

    /**
     * 选中的开始和结束日期的圆圈半径
     */
    private float selectedBgCircleR;


    /**
     * 本月有多少天
     */
    private int monthOfDayCount;
    private float lineWidth = DensityUtil.dip2px(0.5f);

    /**
     * 存放每个日期的位置，日期最大行数最多为6行，也就是天数为31天时，1号刚好为周五，则会出现6行日期
     */
    private DateGridEntity[][] dateDays = new DateGridEntity[Rows][Column];
    private SimpleDateFormat simpleDateFormat;
    private Date numberOneDate;//一号的日期
    private Boolean isSetData = false;//是否已经设置数据
    private HorizontalSelectDateAdapter horizontalSelectDateAdapter;
    private List<DateGridEntity> dateGridEntities;
    SimpleDateFormat simpleBeforeDateFormat;
    Date currentDate;


    public CustomDatePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    private void initView(int mCurrentYear, int mCurrentMonth) {
        displayMetrics = getResources().getDisplayMetrics();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 获取当前年份
        this.mCurrentYear = mCurrentYear;
        // 获取当前月份
        this.mCurrentMonth = mCurrentMonth;
        monthOfDayCount = dateGridEntities.size();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (mCurrentMonth < 10) {
                numberOneDate = simpleDateFormat.parse(this.mCurrentYear + "-0" + this.mCurrentMonth + "-01");
            } else {
                numberOneDate = simpleDateFormat.parse(this.mCurrentYear + "-" + this.mCurrentMonth + "-01");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isSetData) {
            return;
        }
        columnWidth = getWidth() / Column;
        // 计算每一行高度
        rowHeight = getHeight() / Rows;
        drawDayText(canvas);
    }

    /**
     * 绘制本月日期(若有背景，需要先绘制背景，否则会覆盖文字)
     *
     * @param canvas
     */
    private void drawDayText(Canvas canvas) {
        int firstDayPosition = DateUtils.getWeekOfDatePosition(numberOneDate);
        // 写入文字
        for (int i = 0; i < dateGridEntities.size(); i++) {
            DateGridEntity dateGridEntity = dateGridEntities.get(i);
            //一行中的第几个
            int column = (i + firstDayPosition) % 7;
            //第几行
            int row = (i + firstDayPosition) / 7;
            dateDays[row][column] = dateGridEntities.get(i);
            paint.setColor(optionalDateTextColor);
            if (isBeforeDate(dateGridEntity)) {
                // holder.tv_date_day.setTextColor(Color.parseColor("#E1DFDF"));
                paint.setColor(notOptionalDateTextColor);
            } else {
                // holder.tv_date_day.setTextColor(Color.parseColor("#404040"));
                if (dateGridEntity.getType() == 1) {
                    if (horizontalSelectDateAdapter.startSelectDateEntity != null) {
                        if (horizontalSelectDateAdapter.endSelectDateEntity != null) {
                            if (isWhetherInInterval(horizontalSelectDateAdapter.startSelectDateEntity
                                    , horizontalSelectDateAdapter.endSelectDateEntity, dateGridEntity)) {
                                drawSelectedBetweenStartAndEndBackground(canvas, row, column);
                                paint.setColor(selectedDateTextColor);
                            } else {
                                if (isStartTime(horizontalSelectDateAdapter.startSelectDateEntity, dateGridEntity)) {
                                    drawSelectedStartDateBackground(canvas, row, column);
                                    drawSelectedBackground(canvas, row, column);
                                    paint.setColor(selectedDateTextColor);
                                } else {
                                    if (isStartTime(horizontalSelectDateAdapter.endSelectDateEntity, dateGridEntity)) {
                                        drawSelectedEndDateBackground(canvas, row, column);
                                        drawSelectedBackground(canvas, row, column);
                                        //绘制总共多少天
                                        drawTotalCount(canvas, row, column);
                                        paint.setColor(selectedDateTextColor);
                                    } else {
                                        paint.setColor(optionalDateTextColor);
                                    }
                                }
                            }
                        } else {
                            paint.setColor(notOptionalDateTextColor);
                            if (isStartTime(horizontalSelectDateAdapter.startSelectDateEntity, dateGridEntity)) {
                                // drawSelectedStartDateBackground(canvas, row, column);
                                drawSelectedBackground(canvas, row, column);
                                //绘制总共多少天
                                drawTotalCount(canvas, row, column);
                                paint.setColor(selectedDateTextColor);
                            } else {
                                paint.setColor(optionalDateTextColor);
                            }
                        }
                    } else {
                        paint.setColor(optionalDateTextColor);
                    }
                } else {
                    paint.setColor(optionalDateTextColor);
                }
            }
            paint.setTextSize(dateTextSize * displayMetrics.scaledDensity);
            int startX = (int) (columnWidth * column + (columnWidth - paint.measureText(dateGridEntity.getTimeDay() + "")) / 2);
            int startY = (int) (rowHeight * row + rowHeight / 2 - (paint.ascent() + paint.descent()) / 2);
            //  paint.setTextSize(dateTextSize * displayMetrics.scaledDensity);
            // 设置画笔颜色
            canvas.drawText(dateGridEntity.getTimeDay() + "", startX, startY, paint);
        }
    }


    /**
     * 绘制总共有多少天
     *
     * @param canvas
     * @param row
     * @param column
     */
    private void drawTotalCount(Canvas canvas, int row, int column) {
        paint.setColor(totalCountTextColor);
        paint.setTextSize(10 * displayMetrics.scaledDensity);
        selectedBgCircleR = (float) (columnWidth / 2 * 0.7);
        int startX = (int) (columnWidth * column + (columnWidth - (columnWidth - selectedBgCircleR * 2) / 2 - paint.measureText("共计" + getDiffDay() + "天")));
        int startY = (int) (rowHeight * row + rowHeight - (paint.ascent() + paint.descent()));
        // 设置画笔颜色
        canvas.drawText("共计" + getDiffDay() + "天" + "", startX, startY, paint);
        int startLineX = (int) (columnWidth * (column + 1));
        int startLineY = (int) (rowHeight * row);
        paint.setColor(totalCountLineColor);
        canvas.drawLine(startLineX, startLineY, startLineX + lineWidth, startLineY + rowHeight, paint);
    }

    /**
     * 绘制已选日期背景
     *
     * @param canvas
     * @param row
     * @param column
     */
    private void drawSelectedBackground(Canvas canvas, int row, int column) {
        // 画笔颜色
        paint.setColor(startAndEndDateViewBg);
        // 圆心位置
        float cX = (float) (columnWidth * column + columnWidth / 2);
        float cY = (float) (rowHeight * row + rowHeight / 2);
        // 圆形半径
        selectedBgCircleR = (float) (columnWidth / 2 * 0.7);
        // 绘制圆形背景
        canvas.drawCircle(cX, cY, selectedBgCircleR, paint);
    }


    /**
     * 绘制再开始和结束之间的日期的背景
     *
     * @param canvas
     * @param row
     * @param column
     */
    private void drawSelectedBetweenStartAndEndBackground(Canvas canvas, int row, int column) {
        // 画笔颜色
        paint.setColor(betweenStartAndEndDateLineBg);
        // 圆心位置
        selectedBgCircleR = (float) (columnWidth / 2 * 0.7);
        int left = (int) (columnWidth * column);
        int top = (int) ((rowHeight * row) + (rowHeight - selectedBgCircleR * 2) / 2);
        // 圆形半径
        // 绘制圆形背景
        RectF rectF = new RectF(left, top, left + columnWidth, top + selectedBgCircleR * 2);
        canvas.drawRect(rectF, paint);
    }


    /**
     * 绘制再开始日期衔接处矩形背景
     *
     * @param canvas
     * @param row
     * @param column
     */
    private void drawSelectedStartDateBackground(Canvas canvas, int row, int column) {
        // 画笔颜色
        paint.setColor(betweenStartAndEndDateLineBg);
        // 圆的半径
        selectedBgCircleR = (float) (columnWidth / 2 * 0.7);
        float cX = (float) (columnWidth * column + columnWidth / 2);
        float cY = (float) (rowHeight * row + rowHeight / 2);
        int left = (int) cX;
        int top = (int) ((rowHeight * row) + (rowHeight - selectedBgCircleR * 2) / 2);
        RectF rectF = new RectF(left, top, left + columnWidth, top + selectedBgCircleR * 2);
        canvas.drawRect(rectF, paint);
    }

    /**
     * 绘制结束日期衔接处矩形背景
     *
     * @param canvas
     * @param row
     * @param column
     */
    private void drawSelectedEndDateBackground(Canvas canvas, int row, int column) {
        // 画笔颜色
        paint.setColor(betweenStartAndEndDateLineBg);
        // 圆的半径
        selectedBgCircleR = (float) (columnWidth / 2 * 0.7);
        float cX = (float) (columnWidth * column + columnWidth / 2);
        int left = (int) (columnWidth * column);
        int top = (int) ((rowHeight * row) + (rowHeight - selectedBgCircleR * 2) / 2);
        RectF rectF = new RectF(left, top, cX, top + selectedBgCircleR * 2);
        canvas.drawRect(rectF, paint);
    }


    private int downX = 0, downY = 0, upX = 0, upY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            // 若是按下，则获取坐标
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;

            // 若是点击后放开
            case MotionEvent.ACTION_UP:
                upX = (int) event.getX();
                upY = (int) event.getY();
                if (Math.abs(downX - upX) < 10 && Math.abs(downY - upY) < 10) {
                    // （点击事件）因为这里返回true，导致事件不会往上传，因此“手动”上传
                    performClick();
                    // 处理点击事件
                    handleClick((upX + downX) / 2, (upY + downY) / 2);
                }
                break;

            default:
                break;

        }
        // 返回true表示已经消费此事件，不上传了（这样才能监听所有动作，而不是只有ACTION_DOWN）
        return true;
    }

    public void initData(HorizontalSelectDateAdapter horizontalSelectDateAdapter, int year, int month, int dayCount) {
        isSetData = true;
        dateGridEntities = new ArrayList<>();
        this.horizontalSelectDateAdapter = horizontalSelectDateAdapter;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleBeforeDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDate = new Date();
        try {
            currentDate = simpleBeforeDateFormat.parse(simpleDateFormat.format(currentDate) + " 00:00:00");
        } catch (ParseException e) {
            currentDate = new Date();
        }
        for (int i = 1; i <= dayCount; i++) {
            dateGridEntities.add(new DateGridEntity(year, month, i, 1));
        }
        initView(year, month);
    }

    /**
     * 处理点击事件
     *
     * @param x
     * @param y
     */
    private void handleClick(int x, int y) {
        // 获取行
        int row = y / rowHeight;
        // 获取列
        int column = x / columnWidth;
        // 获取点击的日期
        DateGridEntity dateGridEntity = dateDays[row][column];
        if (dateGridEntity == null) {
            return;
        }
        if (dateGridEntity.getType() == 1) {
            if (horizontalSelectDateAdapter.startSelectDateEntity == null) {
                if (!isBeforeDate(dateGridEntity)) {
                    horizontalSelectDateAdapter.startSelectDateEntity = new SelectDateEntity(dateGridEntity.getYear(), dateGridEntity.getMonth(), dateGridEntity.getTimeDay());
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
                        } else {
                            if (!isBeforeDate(dateGridEntity)) {
                                horizontalSelectDateAdapter.startSelectDateEntity = new SelectDateEntity(dateGridEntity.getYear(), dateGridEntity.getMonth(), dateGridEntity.getTimeDay());
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
                            } else {
                                horizontalSelectDateAdapter.endSelectDateEntity = new SelectDateEntity(dateGridEntity.getYear(), dateGridEntity.getMonth(), dateGridEntity.getTimeDay());
                            }
                        }
                    }
                }
            }
            invalidate();
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
     *
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
            if (horizontalSelectDateAdapter.endSelectDateEntity == null) {
                return 1;
            }
            Date start = simpleDateFormat.parse(horizontalSelectDateAdapter.startSelectDateEntity.getDate());
            Date end = simpleDateFormat.parse(horizontalSelectDateAdapter.endSelectDateEntity.getDate());
            return DateUtils.getDatePoorDay(end, start) + 1;
        } catch (ParseException e) {
            return 0;
        }
    }
}

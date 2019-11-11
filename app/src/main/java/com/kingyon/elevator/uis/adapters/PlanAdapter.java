package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.PlanItemEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.entities.StateHolder;
import com.kingyon.elevator.entities.TimeHolder;
import com.kingyon.elevator.uis.widgets.EditCountViewInList;
import com.kingyon.elevator.utils.FormatUtils;
import com.leo.afbaselibrary.listeners.OnClickWithObjects;
import com.leo.afbaselibrary.uis.adapters.ItemViewDelegate;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.leo.afbaselibrary.widgets.emptyprovider.FadeViewAnimProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.mob.tools.utils.Strings.getString;

/**
 * Created by GongLi on 2019/1/2.
 * Email：lc824767150@163.com
 */

public class PlanAdapter extends MultiItemTypeAdapter<Object> {

    private boolean editMode;
    private OnOperateClickListener onOperateClickListener;

    public PlanAdapter(Context context, List<Object> mItems, OnOperateClickListener onOperateClickListener) {
        super(context, mItems);
        this.onOperateClickListener = onOperateClickListener;
        addItemViewDelegate(1, new PlanDelegate());
        addItemViewDelegate(2, new CellDelegate());
        addItemViewDelegate(3, new TimeDelegate());
        addItemViewDelegate(4, new EmptyDelegate());
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    private class PlanDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.fragment_plan_plan;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof PlanItemEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            PlanItemEntity item = (PlanItemEntity) o;
            holder.setTextNotHide(R.id.tv_name, item.getPlanName());
            holder.setTextNotHide(R.id.tv_type, FormatUtils.getInstance().getPlanType(item.getPlanType()));
            holder.setSelected(R.id.tv_name, editMode ? item.isDeleteCache() : item.isChoosed());
        }
    }

    private class CellDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.fragment_plan_cell;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof CellItemEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            CellItemEntity item = (CellItemEntity) o;
            holder.setSelected(R.id.item_root, item.getPlanPosition() % 2 == 1);
            holder.setImage(R.id.img_cover, item.getCellLogo());
            holder.setTextNotHide(R.id.tv_name, item.getCellName());
            List<PointItemEntity> points = item.getPoints();
            holder.setTextNotHide(R.id.tv_lift, FormatUtils.getInstance().getCellLift(points == null ? item.getLiftNum() : FormatUtils.getInstance().getPointsListNumber(points)));
            holder.setTextNotHide(R.id.tv_screen, String.format("%s(%s可用)", FormatUtils.getInstance().getCellScreen(item.getTotalScreenNum()), item.getTargetScreenNum()));
//            holder.setTextNotHide(R.id.tv_screen, String.format("%s(%s可用)", FormatUtils.getInstance().getCellScreen(item.getTotalScreenNum()), 10000));
            holder.setTextNotHide(R.id.tv_price, FormatUtils.getInstance().getCellPrice(getTypePrice(item)));
            holder.setSelected(R.id.img_choose, item.isChoosed());
            holder.setSelected(R.id.img_choose_delete, item.isDeleteCache());
            holder.setVisible(R.id.img_choose, !editMode);
            holder.setVisible(R.id.img_choose_delete, editMode);
            holder.setVisible(R.id.v_delete, editMode);

            OnClickWithObjects onClickWithObjects = new OnClickWithObjects(item) {
                @Override
                public void onClick(View view, Object[] objects) {
                    if (onOperateClickListener != null) {
                        onOperateClickListener.onCellAssignClick((CellItemEntity) objects[0]);
                    }
                }
            };
            holder.setVisible(R.id.tv_assign, !editMode);
            holder.setOnClickListener(R.id.tv_assign, onClickWithObjects);

            EditCountViewInList editCountView = holder.getView(R.id.ecv_count);
            editCountView.removeOnNumberChange();
            editCountView.setOnNumberChange(new EditCountViewInList.OnNumberChange() {
                @Override
                public void onChange(int num, int position, EditText text) {
                    if (position >= 0 && mItems.size() > position) {
                        Object obj = mItems.get(position);
                        if (obj instanceof CellItemEntity) {
                            CellItemEntity cell = (CellItemEntity) obj;
                            cell.setChoosedScreenNum(num);
                            ArrayList<PointItemEntity> cellPoints = cell.getPoints();
                            if (cellPoints != null && cellPoints.size() > cell.getChoosedScreenNum()) {
                                ArrayList<PointItemEntity> entities = new ArrayList<>();
                                entities.addAll(cellPoints.subList(0, cell.getChoosedScreenNum()));
                                cell.setPoints(entities);
                            } else if (cellPoints != null && cellPoints.size() < cell.getChoosedScreenNum()) {
                                ArrayList<PointItemEntity> entities = new ArrayList<>();
                                entities.addAll(cellPoints);
                                int needNum = item.getChoosedScreenNum() - cellPoints.size();

                                List<PointItemEntity> unusedDatas = getUnusedDatas(cell.getAllPoints(), entities);
                                List<PointItemEntity> solution = solution(unusedDatas, needNum);

                                entities.addAll(solution);
                                cell.setPoints(entities);
                            }
                            if (onOperateClickListener != null) {
                                onOperateClickListener.onNumberChange(cell);
                            }
                        }
                    }
                }
            }, position);
            editCountView.setCanEdit(false);
            editCountView.setMinCount(item.getTargetScreenNum() <= 0 ? 0 : 1);
            editCountView.setMaxCount(item.getTargetScreenNum());
            editCountView.setCurrentCount(item.getChoosedScreenNum());
            holder.setVisible(R.id.ecv_count, !editMode);

//            holder.setVisible(R.id.v_line, !lastIsPlan(position));
//            holder.setVisible(R.id.v_line, false);
        }

        private List<PointItemEntity> getUnusedDatas(List<PointItemEntity> all, List<PointItemEntity> useds) {
            ArrayList<PointItemEntity> unUseds = new ArrayList<>();
            if (all != null) {
                for (PointItemEntity src : all) {
                    if (TextUtils.equals(Constants.DELIVER_STATE.USABLE, src.getDeliverState())) {
                        boolean unUse = true;
                        for (PointItemEntity used : useds) {
                            if (src.getObjectId() == used.getObjectId()) {
                                unUse = false;
                            }
                        }
                        if (unUse) {
                            unUseds.add(src);
                        }
                    }
                }
            }
            return unUseds;
        }

        private List<PointItemEntity> solution(List<PointItemEntity> entities, int length) {
            List<PointItemEntity> result;
            Collections.shuffle(entities);
            if (entities.size() >= length) {
                result = entities.subList(0, length);
            } else {
                result = entities;
            }
            return result;
        }

        private float getTypePrice(CellItemEntity item) {
            float result;
            if (item.getPlanTypeCache() == null) {
                item.setPlanTypeCache("");
            }
            switch (item.getPlanTypeCache()) {
                case Constants.PLAN_TYPE.BUSINESS:
                    result = item.getBusinessAdPrice();
                    break;
                case Constants.PLAN_TYPE.DIY:
                    result = item.getDiyAdPrice();
                    break;
                case Constants.PLAN_TYPE.INFORMATION:
                    result = item.getInformationAdPrice();
                    break;
                default:
                    result = 0;
                    break;
            }
            return result;
        }
    }


    private class TimeDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.fragment_plan_time;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof TimeHolder;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            TimeHolder item = (TimeHolder) o;
            holder.setTextNotHide(R.id.tv_start_time, com.leo.afbaselibrary.utils.TimeUtil.getYMdTime(item.getStartTime()));
            holder.setTextNotHide(R.id.tv_end_time, com.leo.afbaselibrary.utils.TimeUtil.getYMdTime(item.getEndTime()));
        }
    }

    private boolean lastIsPlan(int position) {
        Object item = null;
        int index = position + 1;
        if (index >= 0 && index < mItems.size()) {
            item = mItems.get(index);
        }
        return item == null || item instanceof PlanItemEntity;
    }

    private class EmptyDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.fragment_plan_empty;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof StateHolder;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            StateHolder item = (StateHolder) o;
            StateLayout stateLayout = holder.getView(R.id.sl_state);
            ViewGroup.LayoutParams layoutParams = stateLayout.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.height = item.getHeight() <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : item.getHeight();
                stateLayout.setLayoutParams(layoutParams);
            }
            stateLayout.setViewSwitchAnimProvider(new FadeViewAnimProvider());
            stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOperateClickListener != null) {
                        stateLayout.showProgressView(getString(R.string.loading));
                        onOperateClickListener.onErrorAndEmptyAction();
                    }
                }
            });
            switch (item.getState()) {//STATE_CONTENT = 0、STATE_EMPTY = 1、STATE_PROGRESS = 2、STATE_ERROR = 3;
                case 0:
                    stateLayout.showContentView();
                    break;
                case 1:
                    stateLayout.showEmptyView(mContext.getString(R.string.empty));
                    break;
                case 2:
                    stateLayout.showProgressView(mContext.getString(R.string.loading));
                    break;
                case 3:
                    stateLayout.showErrorView(mContext.getString(R.string.error));
                    break;
            }
        }
    }

    public interface OnOperateClickListener {
        void onCellAssignClick(CellItemEntity item);

        void onNumberChange(CellItemEntity item);

        void onErrorAndEmptyAction();
    }
}

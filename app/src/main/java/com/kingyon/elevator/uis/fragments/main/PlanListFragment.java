package com.kingyon.elevator.uis.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.entities.StateHolder;
import com.kingyon.elevator.entities.TimeHolder;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.password.LoginActivity;
import com.kingyon.elevator.uis.activities.plan.AssignNewActivity;
import com.kingyon.elevator.uis.activities.plan.OrderEditActivity;
import com.kingyon.elevator.uis.adapters.PlanAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshLoadingFragment;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.leo.afbaselibrary.widgets.emptyprovider.FadeViewAnimProvider;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/2/19.
 * Email：lc824767150@163.com
 */

public class PlanListFragment extends BaseStateRefreshLoadingFragment<Object> implements PlanAdapter.OnOperateClickListener {
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_cell_num)
    TextView tvCellNum;
    @BindView(R.id.tv_screen_num)
    TextView tvScreenNum;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;
    @BindView(R.id.ll_order)
    LinearLayout llOrder;
    @BindView(R.id.tv_delete_all)
    TextView tvDeleteAll;
    @BindView(R.id.tv_delete_number)
    TextView tvDeleteNumber;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.ll_bar)
    LinearLayout llBar;

    private String planType;
    private Long startTime;
    private Long endTime;
    private boolean editMode;

    private PlanAdapter planAdapter;

    private DatePickerDialog startDialog;
    private DatePickerDialog endDialog;
    private Long startTimeCache;

    public static PlanListFragment newInstance(String planType) {
        Bundle args = new Bundle();
        args.putString(CommonUtil.KEY_VALUE_1, planType);
        PlanListFragment fragment = new PlanListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_plan_list;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            planType = getArguments().getString(CommonUtil.KEY_VALUE_1);
        }
        super.init(savedInstanceState);
        long curTime = System.currentTimeMillis();
        startTime = TimeUtil.getDayStartTimeMilliseconds(curTime);
//        startTime = curTime;
        endTime = TimeUtil.getDayEndTimeMilliseconds(curTime);
        updateTimeUI();
        tvPrice.setText(getPriceSpan(CommonUtil.getTwoFloat(0)));
        updateMode();
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        planAdapter = new PlanAdapter(getContext(), mItems, this);
        return planAdapter;
    }

    @Override
    protected void loadData(int page) {
        updateBarVisiable();
        if (!TextUtils.isEmpty(Net.getInstance().getToken())) {
            NetService.getInstance().plansList(planType, startTime, endTime, page)
                    .compose(this.<PageListEntity<CellItemEntity>>bindLifeCycle())
                    .subscribe(new CustomApiCallback<PageListEntity<CellItemEntity>>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                            loadingComplete(false, 100000);
                            updateBarVisiable();
                        }

                        @Override
                        public void onNext(PageListEntity<CellItemEntity> cellItemEntityPageListEntity) {
                            if (cellItemEntityPageListEntity == null) {
                                throw new ResultException(9000, "返回参数异常");
                            }
                            List<CellItemEntity> cellItemEntities = cellItemEntityPageListEntity.getContent();
                            if (FIRST_PAGE == page) {
                                mItems.clear();
//                                mItems.add(new TimeHolder(startTime, endTime));
                            }
                            if (cellItemEntities != null && cellItemEntities.size() > 0) {
                                for (CellItemEntity item : cellItemEntities) {
                                    item.setPlanTypeCache(planType);
                                    //改为默认只选择一台设备
//                                    item.setChoosedScreenNum(item.getTargetScreenNum());
                                    item.setChoosedScreenNum(1);
                                }
                                mItems.addAll(cellItemEntities);
                            } else {
                                mItems.add(new StateHolder(STATE_EMPTY, ScreenUtil.getScreenHeight(getContext()) - ScreenUtil.getStatusBarHeight() - ScreenUtil.dp2px(49) - ScreenUtil.dp2px(48) - ScreenUtil.dp2px(46) - ScreenUtil.dp2px(60) - ScreenUtil.dp2px(43) - ScreenUtil.dp2px(6)));
                            }
                            int planPosition = 0;
                            for (Object obj : mItems) {
                                if (obj instanceof CellItemEntity) {
                                    ((CellItemEntity) obj).setPlanPosition(planPosition);
                                    planPosition++;
                                } else {
                                    planPosition = 0;
                                }
                            }
                            planAdapter.setEditMode(editMode);
                            loadingComplete(true, cellItemEntityPageListEntity.getTotalPages());
                            updateBarVisiable();
                            updatePriceUI();
                            updateEditUI();
                        }
                    });
        } else {
            mCurrPage = FIRST_PAGE;
            mItems.clear();
            loadingComplete(true, 1);
            updateBarVisiable();
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            if (item instanceof CellItemEntity) {
                if (editMode) {
                    onEditChooseClick((CellItemEntity) item);
                } else {
                    onOrderChooseClick((CellItemEntity) item);
                }
            } else if (item instanceof TimeHolder) {
                showStartPicker();
            }
        }
    }

    private void onOrderChooseClick(CellItemEntity cell) {
        if (startTime == null || endTime == null) {
            showToast("请先选择时间");
            return;
        }
        if (cell.getTargetScreenNum() > 0) {
            cell.setChoosed(!cell.isChoosed());
        } else {
            showToast(String.format("无法选择，总共%s面屏已被全部占用", cell.getTotalScreenNum()));
        }
        mAdapter.notifyDataSetChanged();
        updatePriceUI();
    }

    private void onEditChooseClick(CellItemEntity cell) {
        cell.setDeleteCache(!cell.isDeleteCache());
        mAdapter.notifyDataSetChanged();
        updateEditUI();
    }

    private void updateBarVisiable() {
        llBar.setVisibility(TextUtils.isEmpty(Net.getInstance().getToken()) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onCellAssignClick(CellItemEntity item) {
        if (editMode) {
            onEditChooseClick(item);
            return;
        }
        if (startTime == null || endTime == null) {
            showToast("请先选择时间");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong(CommonUtil.KEY_VALUE_1, item.getObjctId());
        bundle.putString(CommonUtil.KEY_VALUE_2, planType);
        bundle.putLong(CommonUtil.KEY_VALUE_3, startTime);
        bundle.putLong(CommonUtil.KEY_VALUE_4, endTime);
        if (item.getPoints() != null) {
            bundle.putParcelableArrayList(CommonUtil.KEY_VALUE_6, item.getPoints());
        } else {
            bundle.putInt(CommonUtil.KEY_VALUE_7, item.getChoosedScreenNum());
        }
        startActivityForResult(AssignNewActivity.class, 8100, bundle);

//        Bundle bundle = new Bundle();
//        bundle.putLong(CommonUtil.KEY_VALUE_1, item.getObjctId());
//        bundle.putString(CommonUtil.KEY_VALUE_2, planType);
//        bundle.putLong(CommonUtil.KEY_VALUE_3, startTime);
//        bundle.putLong(CommonUtil.KEY_VALUE_4, endTime);
//        bundle.putLong(CommonUtil.KEY_VALUE_5, item.getPlanIdCache());
//        if (item.getPoints() != null) {
//            bundle.putParcelableArrayList(CommonUtil.KEY_VALUE_6, item.getPoints());
//        }
//        startActivityForResult(AssignActivity.class, 8100, bundle);
    }

    @Override
    public void onNumberChange(CellItemEntity item) {
        updatePriceUI();
    }

    @Override
    public void onErrorAndEmptyAction() {
        onfresh();
    }

    public void updateMode() {
        if (llDelete != null) {
            llDelete.setVisibility(editMode ? View.VISIBLE : View.GONE);
        }
        if (planAdapter != null) {
            planAdapter.setEditMode(editMode);
        }
        mAdapter.notifyDataSetChanged();
        if (editMode) {
            updateEditUI();
        } else {
            updatePriceUI();
        }
    }

    private void updatePriceUI() {
        float sum = 0;
        int cellNum = 0;
        int screenNum = 0;
        for (Object obj : mItems) {
            if (obj instanceof CellItemEntity) {
                CellItemEntity cell = (CellItemEntity) obj;
                if (cell.isChoosed()) {
                    cellNum++;
                    int cellScreenNum = cell.getChoosedScreenNum();
                    screenNum += cellScreenNum;
                    float cellScreenPrice = 0;
                    switch (planType) {
                        case Constants.PLAN_TYPE.BUSINESS:
                            cellScreenPrice = cell.getBusinessAdPrice() * cellScreenNum;
                            break;
                        case Constants.PLAN_TYPE.DIY:
                            cellScreenPrice = cell.getDiyAdPrice() * cellScreenNum;
                            break;
                        case Constants.PLAN_TYPE.INFORMATION:
                            cellScreenPrice = cell.getInformationAdPrice() * cellScreenNum;
                            break;
                    }
                    sum += cellScreenPrice;
                }
            }
        }
        tvCellNum.setText(String.format("覆盖%s个小区", cellNum));
        tvScreenNum.setText(String.format("%s面屏", screenNum));
        tvPrice.setText(getPriceSpan(CommonUtil.getTwoFloat(sum * FormatUtils.getInstance().getTimeDays(startTime, endTime))));
    }

    private void updateEditUI() {
        boolean allEdit = true;
        int editNum = 0;
        for (Object obj : mItems) {
            if (obj instanceof CellItemEntity) {
                CellItemEntity item = (CellItemEntity) obj;
                if (item.isDeleteCache()) {
                    editNum++;
                } else {
                    allEdit = false;
                }
            }
        }
        tvDeleteAll.setSelected(allEdit);
        tvDeleteNumber.setText(String.format("已选%s个小区", editNum));
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        updateMode();
    }

    private CharSequence getPriceSpan(String price) {
        SpannableString spannableString = new SpannableString(price);
        int index = price.indexOf(".");
        spannableString.setSpan(new AbsoluteSizeSpan(18, true), index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(13, true), index + 1, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @OnClick({R.id.ll_time, R.id.tv_ensure, R.id.tv_delete_all, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_time:
                showStartPicker();
                break;
            case R.id.tv_ensure:
                toCommitOrder();
                break;
            case R.id.tv_delete_all:
                setAllCellEdit(!tvDeleteAll.isSelected());
                break;
            case R.id.tv_delete:
                requestDelete();
                break;
        }
    }

    private void toCommitOrder() {
        ArrayList<CellItemEntity> orderCells = getOrderCells();
        if (orderCells.size() < 1) {
            showToast("至少需要一个小区一面屏");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(CommonUtil.KEY_VALUE_1, planType);
        bundle.putLong(CommonUtil.KEY_VALUE_2, startTime);
        bundle.putLong(CommonUtil.KEY_VALUE_3, endTime);
        bundle.putParcelableArrayList(CommonUtil.KEY_VALUE_4, orderCells);
        startActivityForResult(OrderEditActivity.class, 8101, bundle);
    }

    private ArrayList<CellItemEntity> getOrderCells() {
        ArrayList<CellItemEntity> cells = new ArrayList<>();
        for (Object obj : mItems) {
            if (obj instanceof CellItemEntity) {
                CellItemEntity entity = (CellItemEntity) obj;
                if (entity.isChoosed() && entity.getChoosedScreenNum() > 0) {
                    cells.add(entity);
                }
            }
        }
        return cells;
    }

    private void setAllCellEdit(boolean edit) {
        for (Object obj : mItems) {
            if (obj instanceof CellItemEntity) {
                ((CellItemEntity) obj).setDeleteCache(edit);
            }
        }
        mAdapter.notifyDataSetChanged();
        updateEditUI();
    }

    private void requestDelete() {
        String cellIds = getDeleteParams();
        if (TextUtils.isEmpty(cellIds)) {
            showToast("需要至少选择一个小区");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        tvDelete.setEnabled(false);
        NetService.getInstance().plansRemoveCells(planType, cellIds)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                        tvDelete.setEnabled(true);
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("删除成功");
                        autoRefresh();
                        tvDelete.setEnabled(true);
                        hideProgress();
                    }
                });
    }

    private String getDeleteParams() {
        StringBuilder builder = new StringBuilder();
        for (Object obj : mItems) {
            if (obj instanceof CellItemEntity) {
                CellItemEntity cell = (CellItemEntity) obj;
                if (cell.isDeleteCache()) {
                    builder.append(cell.getObjctId()).append(",");
                }
            }
        }
        return builder.length() > 1 ? builder.substring(0, builder.length() - 1) : null;
    }

    private void showStartPicker() {
        if (getActivity() == null) {
            return;
        }
        Calendar now = Calendar.getInstance();
        if (startDialog == null) {
            startDialog = DatePickerDialog.newInstance(
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            long startResult = TimeUtil.getDayStartTimeMilliseconds(com.kingyon.elevator.utils.TimeUtil.getLongTime(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
//                            long curTime = System.currentTimeMillis();
//                            if (TextUtils.equals(TimeUtil.getYMdTime(startResult), TimeUtil.getYMdTime(curTime))) {
//                                startResult = curTime;
//                            }
                            startTimeCache = startResult;
                            showEndPicker();
                        }
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            startDialog.setTitle("请选择开始时间");
        }
        startDialog.setMinDate(now);
        startDialog.show(getActivity().getFragmentManager(), "Datepickerdialog_start");
    }

    private void showEndPicker() {
        if (getActivity() == null) {
            return;
        }
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startTimeCache);
        if (endDialog == null) {
            endDialog = DatePickerDialog.newInstance(
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            long endResult = TimeUtil.getDayEndTimeMilliseconds(com.kingyon.elevator.utils.TimeUtil.getLongTime(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                            if (endResult > startTimeCache) {
                                startTime = startTimeCache;
                                endTime = endResult;
                                if (mItems != null && mItems.size() > 0) {
                                    Object obj = mItems.get(0);
                                    if (obj instanceof TimeHolder) {
                                        TimeHolder timeHolder = (TimeHolder) obj;
                                        timeHolder.setStartTime(startTime);
                                        timeHolder.setEndTime(endTime);
                                        mAdapter.notifyItemChanged(0);
                                    }
                                }
                                updateTimeUI();
                                autoRefresh();
                            } else {
                                showToast("结束时间不能大于开始时间");
                            }
                        }
                    },
                    start.get(Calendar.YEAR),
                    start.get(Calendar.MONTH),
                    start.get(Calendar.DAY_OF_MONTH)
            );
            endDialog.setTitle("请选择结束时间");
        }
        endDialog.setMinDate(start);
        endDialog.show(getActivity().getFragmentManager(), "Datepickerdialog_end");
    }

    private void updateTimeUI() {
        tvStartTime.setText(TimeUtil.getYMdTime(startTime));
        tvEndTime.setText(TimeUtil.getYMdTime(endTime));
    }

    @Override
    protected String getEmptyTip() {
        boolean notLogin = TextUtils.isEmpty(Net.getInstance().getToken());
        String tip = notLogin ? "请先登录/注册" : "暂无数据";
        if (notLogin) {
            stateLayout.setEmptyViewTip("点击登录/注册");
        } else {
            stateLayout.setEmptyViewTip("");
        }
        return tip;
    }

    protected void initStateLayout() {
        stateLayout.setViewSwitchAnimProvider(new FadeViewAnimProvider());
        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Net.getInstance().getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    autoLoading();
                }
            }
        });
        stateLayout.showProgressView(getString(com.leo.afbaselibrary.R.string.loading));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (BaseActivity.RESULT_OK == resultCode && data != null) {
            String type = data.getStringExtra(CommonUtil.KEY_VALUE_2);
            if (TextUtils.equals(this.planType, type)) {
                switch (requestCode) {
                    case 8100:
                        long cellId = data.getLongExtra(CommonUtil.KEY_VALUE_1, 0);
                        long startTime = data.getLongExtra(CommonUtil.KEY_VALUE_3, 0);
                        long endTime = data.getLongExtra(CommonUtil.KEY_VALUE_4, 0);
                        ArrayList<PointItemEntity> points = data.getParcelableArrayListExtra(CommonUtil.KEY_VALUE_6);
                        ArrayList<PointItemEntity> allPoints = data.getParcelableArrayListExtra(CommonUtil.KEY_VALUE_7);
                        for (Object object : mItems) {
                            if (object instanceof CellItemEntity) {
                                CellItemEntity cell = (CellItemEntity) object;
                                if (cell.getObjctId() == cellId) {
                                    cell.setAllPoints(allPoints);
                                    cell.setPoints(points);
                                    cell.setChoosedScreenNum(points.size());
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        updatePriceUI();
                        break;
                    case 8101:
                        autoRefresh();
                        break;
                }
            }
        }
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }
}

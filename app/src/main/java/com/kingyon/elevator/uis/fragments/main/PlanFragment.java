package com.kingyon.elevator.uis.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.entities.PlanCellDeleteEntity;
import com.kingyon.elevator.entities.PlanItemEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.uis.actiivty2.login.LoginActiivty;
import com.kingyon.elevator.uis.activities.plan.AssignActivity;
import com.kingyon.elevator.uis.activities.plan.OrderEditActivity;
import com.kingyon.elevator.uis.adapters.adapterone.PlanAdapter;
import com.kingyon.elevator.uis.dialogs.PlanParamsDialog;
import com.kingyon.elevator.uis.pops.PlanParamsWindow;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshLoadingFragment;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.leo.afbaselibrary.widgets.emptyprovider.FadeViewAnimProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/12/24.
 * Email：lc824767150@163.com
 */

@Deprecated
public class PlanFragment extends BaseStateRefreshLoadingFragment<Object> implements PlanAdapter.OnOperateClickListener, PlanParamsWindow.OnResultListener {

    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_params)
    LinearLayout llParams;
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
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.ll_bar)
    LinearLayout llBar;

    private String type;
    private Long startTime;
    private Long endTime;

    private boolean editMode;

    private PlanAdapter planAdapter;
    //    private PlanParamsWindow planParamsDialog;
    private PlanParamsDialog planParamsDialog;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_plan;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        mLayoutRefresh.setEnabled(false);
        StatusBarUtil.setHeadViewPadding(getActivity(), flTitle);
        tvMode.setVisibility(View.GONE);
        updateMode();
        tvPrice.setText(getPriceSpan(CommonUtil.getTwoFloat(0)));
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        planAdapter = new PlanAdapter(getContext(), mItems, this);
        return planAdapter;
    }

    @Override
    protected void loadData(final int page) {
        updateBarVisiable();
        if (!TextUtils.isEmpty(Net.getInstance().getToken())) {
//            NetService.getInstance().plansList(type, startTime, endTime)
//                    .compose(this.<List<PlanItemEntity>>bindLifeCycle())
//                    .subscribe(new CustomApiCallback<List<PlanItemEntity>>() {
//                        @Override
//                        protected void onResultError(ApiException ex) {
//                            showToast(ex.getDisplayMessage());
//                            loadingComplete(false, 1);
//                            updateBarVisiable();
//                        }
//
//                        @Override
//                        public void onNext(List<PlanItemEntity> planItemEntities) {
//                            if (FIRST_PAGE == page) {
//                                mItems.clear();
//                            }
//                            if (planItemEntities != null) {
//                                for (PlanItemEntity plan : planItemEntities) {
//                                    mItems.add(plan);
//                                    List<CellItemEntity> cells = plan.getCells();
//                                    if (cells != null) {
//                                        for (CellItemEntity cell : cells) {
//                                            cell.setPlanTypeCache(plan.getPlanType());
//                                            cell.setPlanIdCache(plan.getObjectId());
//                                            mItems.add(cell);
//                                        }
//                                    }
//                                }
//                            }
//                            int planPosition = 0;
//                            for (Object obj : mItems) {
//                                if (obj instanceof CellItemEntity) {
//                                    ((CellItemEntity) obj).setPlanPosition(planPosition);
//                                    planPosition++;
//                                } else {
//                                    planPosition = 0;
//                                }
//                            }
//                            loadingComplete(true, 1);
//                            updateBarVisiable();
//                            tvMode.setVisibility(View.VISIBLE);
//                            updatePriceUI();
//                        }
//                    });
        } else {
            mCurrPage = FIRST_PAGE;
            mItems.clear();
            loadingComplete(true, 1);
            updateBarVisiable();
        }
    }

    private void updateBarVisiable() {
        llBar.setVisibility(TextUtils.isEmpty(Net.getInstance().getToken()) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            if (editMode) {
                onEditChooseClick(item);
            } else {
                onOrderChooseClick(item);
            }
        }
    }

    private void onOrderChooseClick(Object entity) {
        if (TextUtils.isEmpty(type) || startTime == null || endTime == null) {
            showToast("请先选择类型和时间等参数");
            return;
        }
        if (entity instanceof PlanItemEntity) {
            PlanItemEntity plan = (PlanItemEntity) entity;
            if (plan.isChoosed()) {
                setPlanChoosed(plan, false);
            } else {
                Long planChoosedId = getPlanChoosedId();
                if (planChoosedId == null || planChoosedId == plan.getObjectId()) {
                    setPlanChoosed(plan, true);
                } else {
                    showToast("只能选中一个点位计划下单");
                }
            }
        } else if (entity instanceof CellItemEntity) {
//            CellItemEntity cell = (CellItemEntity) entity;
//            if (cell.isChoosed()) {
//                cell.setChoosed(false);
//                updatePlanChoosed(cell.getPlanIdCache());
//            } else {
//                Long planChoosedId = getPlanChoosedId();
//                if (planChoosedId == null || planChoosedId.longValue() == cell.getPlanIdCache()) {
//                    cell.setChoosed(true);
//                    updatePlanChoosed(cell.getPlanIdCache());
//                } else {
//                    showToast("只能选中一个点位计划下单");
//                }
//            }
        }
        mAdapter.notifyDataSetChanged();
        updatePriceUI();
    }

    private void onEditChooseClick(Object entity) {
        if (entity instanceof PlanItemEntity) {
            PlanItemEntity plan = (PlanItemEntity) entity;
            setPlanEdit(plan, !plan.isDeleteCache());
        } else if (entity instanceof CellItemEntity) {
            CellItemEntity cell = (CellItemEntity) entity;
            cell.setDeleteCache(!cell.isDeleteCache());
//            updatePlanEdit(cell.getPlanIdCache());
        }
        mAdapter.notifyDataSetChanged();
        updateEditUI();
    }

    private void setPlanEdit(PlanItemEntity plan, boolean deleteCache) {
        plan.setDeleteCache(deleteCache);
        List<CellItemEntity> cells = plan.getCells();
        if (cells != null) {
            for (CellItemEntity cell : cells) {
                cell.setDeleteCache(deleteCache);
            }
        }
    }

    @OnClick({R.id.tv_mode, R.id.ll_params, R.id.tv_ensure, R.id.tv_delete_all, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mode:
                editMode = !editMode;
                updateMode();
                break;
            case R.id.ll_params:
                showPlanParamsDialog();
                break;
            case R.id.tv_ensure:
                toCommitOrder();
                break;
            case R.id.tv_delete_all:
                setAllPlanEdit(!tvDeleteAll.isSelected());
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
        bundle.putString(CommonUtil.KEY_VALUE_1, type);
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
                if (entity.isChoosed() && ((entity.getPoints() == null && entity.getTargetScreenNum() > 0) || (entity.getPoints() != null && entity.getPoints().size() > 0))) {
                    cells.add(entity);
                }
            }
        }
        return cells;
    }

    private void showPlanParamsDialog() {
        if (getContext() == null || getActivity() == null) {
            return;
        }
//        if (planParamsDialog == null) {
//            planParamsDialog = new PlanParamsWindow((BaseActivity) getActivity(), getContext(), this);
//        }
//        planParamsDialog.show(llRoot, type, startTime, endTime);
        if (planParamsDialog == null) {
            planParamsDialog = new PlanParamsDialog((BaseActivity) getActivity(), getContext(), this);
        }
        planParamsDialog.show(type, startTime, endTime);
    }

    @Override
    public void onResultReturn(NormalParamEntity cellType, Long startTime, Long endTime) {
        boolean needRefresh = !TextUtils.equals(cellType != null ? cellType.getType() : "", type);
        needRefresh = needRefresh || (this.startTime == null ? startTime != null : (startTime == null || this.startTime.longValue() != startTime.longValue()));
        needRefresh = needRefresh || (this.endTime == null ? endTime != null : (endTime == null || this.endTime.longValue() != endTime.longValue()));
        this.type = cellType != null ? cellType.getType() : null;
        tvType.setText(cellType != null ? cellType.getName() : "");
        this.startTime = (startTime != null && endTime != null) ? startTime : null;
        this.endTime = (startTime != null && endTime != null) ? endTime : null;
        tvTime.setText((startTime != null && endTime != null) ? String.format("%s至%s", TimeUtil.getYMdTime(startTime), TimeUtil.getYMdTime(endTime)) : "");
        if (needRefresh) {
            autoRefresh();
        }
    }

    private void requestDelete() {
        List<PlanCellDeleteEntity> deleteParams = getDeleteParams();
        if (deleteParams.size() < 1) {
            showToast("需要至少选择一个小区");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        tvDelete.setEnabled(false);
//        NetService.getInstance().plansRemoveCells(AppContent.getInstance().getGson().toJson(deleteParams))
//                .compose(this.<String>bindLifeCycle())
//                .subscribe(new CustomApiCallback<String>() {
//                    @Override
//                    protected void onResultError(ApiException ex) {
//                        showToast(ex.getDisplayMessage());
//                        hideProgress();
//                        tvDelete.setEnabled(true);
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        showToast("删除成功");
//                        autoRefresh();
//                        tvDelete.setEnabled(true);
//                        hideProgress();
//                    }
//                });
    }

    private List<PlanCellDeleteEntity> getDeleteParams() {
        HashMap<Long, StringBuilder> map = new HashMap<>();
        for (Object obj : mItems) {
            if (obj instanceof CellItemEntity) {
                CellItemEntity cell = (CellItemEntity) obj;
                if (cell.isDeleteCache()) {
//                    StringBuilder builder;
//                    if (map.containsKey(cell.getPlanIdCache())) {
//                        builder = map.get(cell.getPlanIdCache());
//                    } else {
//                        builder = new StringBuilder();
//                    }
//                    builder.append(cell.getObjctId()).append(",");
//                    map.put(cell.getPlanIdCache(), builder);
                }
            }
        }
        Set<Map.Entry<Long, StringBuilder>> entries = map.entrySet();
        List<PlanCellDeleteEntity> params = new ArrayList<>();
        for (Map.Entry<Long, StringBuilder> entry : entries) {
            StringBuilder stringBuilder = entry.getValue();
            String cellIds = stringBuilder.length() > 1 ? stringBuilder.substring(0, stringBuilder.length() - 1) : null;
            if (!TextUtils.isEmpty(cellIds)) {
                params.add(new PlanCellDeleteEntity(entry.getKey(), cellIds));
            }
        }
        return params;
    }

    private void updateMode() {
        tvMode.setText(editMode ? "取消" : "编辑");
        llParams.setVisibility(editMode ? View.GONE : View.VISIBLE);
        llDelete.setVisibility(editMode ? View.VISIBLE : View.GONE);
        planAdapter.setEditMode(editMode);
        mAdapter.notifyDataSetChanged();
        if (editMode) {
            updateEditUI();
        } else {
            updatePriceUI();
        }
    }

    @Override
    public void onCellAssignClick(CellItemEntity item) {
        if (TextUtils.isEmpty(type) || startTime == null || endTime == null) {
            showToast("请先选择类型和时间等参数");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong(CommonUtil.KEY_VALUE_1, item.getObjctId());
        bundle.putString(CommonUtil.KEY_VALUE_2, type);
        bundle.putLong(CommonUtil.KEY_VALUE_3, startTime);
        bundle.putLong(CommonUtil.KEY_VALUE_4, endTime);
//        bundle.putLong(CommonUtil.KEY_VALUE_5, item.getPlanIdCache());
        if (item.getPoints() != null) {
            bundle.putParcelableArrayList(CommonUtil.KEY_VALUE_6, item.getPoints());
        }
        startActivityForResult(AssignActivity.class, 8100, bundle);
    }

    @Override
    public void onNumberChange(CellItemEntity item) {

    }

    @Override
    public void onErrorAndEmptyAction() {

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
                    ArrayList<PointItemEntity> points = cell.getPoints();
                    int cellScreenNum = points == null ? cell.getTargetScreenNum() : points.size();
                    screenNum += cellScreenNum;
                    float cellScreenPrice = 0;
                    switch (type) {
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

    private CharSequence getPriceSpan(String price) {
        SpannableString spannableString = new SpannableString(price);
        int index = price.indexOf(".");
        spannableString.setSpan(new AbsoluteSizeSpan(18, true), index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(13, true), index + 1, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private void updatePlanChoosed(long planId) {
        PlanItemEntity plan = null;
        for (Object obj : mItems) {
            if (obj instanceof PlanItemEntity && ((PlanItemEntity) obj).getObjectId() == planId) {
                plan = (PlanItemEntity) obj;
                break;
            }
        }
        if (plan != null) {
            updatePlanChoosed(plan);
        }
    }

    private void updatePlanChoosed(PlanItemEntity plan) {
        boolean allChoosed = true;
        List<CellItemEntity> cells = plan.getCells();
        for (CellItemEntity cell : cells) {
            if (!cell.isChoosed()) {
                allChoosed = false;
                break;
            }
        }
        plan.setChoosed(allChoosed);
    }

    private void setPlanChoosed(PlanItemEntity plan, boolean choosed) {
        plan.setChoosed(choosed);
        List<CellItemEntity> cells = plan.getCells();
        if (cells != null) {
            for (CellItemEntity cell : cells) {
                cell.setChoosed(choosed);
            }
        }
    }

    private Long getPlanChoosedId() {
        Long result = null;
        for (Object obj : mItems) {
//            if (obj instanceof PlanItemEntity && ((PlanItemEntity) obj).isChoosed()) {
//                result = ((PlanItemEntity) obj).getObjectId();
//                break;
//            }
            if (obj instanceof CellItemEntity && ((CellItemEntity) obj).isChoosed()) {
//                result = ((CellItemEntity) obj).getPlanIdCache();
                break;
            }
        }
        return result;
    }

    private void updatePlanEdit(long planId) {
        PlanItemEntity plan = null;
        for (Object obj : mItems) {
            if (obj instanceof PlanItemEntity && ((PlanItemEntity) obj).getObjectId() == planId) {
                plan = (PlanItemEntity) obj;
                break;
            }
        }
        if (plan != null) {
            updatePlanEdit(plan);
        }
    }

    private void updatePlanEdit(PlanItemEntity plan) {
        boolean allEdit = true;
        List<CellItemEntity> cells = plan.getCells();
        for (CellItemEntity cell : cells) {
            if (!cell.isDeleteCache()) {
                allEdit = false;
                break;
            }
        }
        plan.setDeleteCache(allEdit);
    }

    private void setAllPlanEdit(boolean edit) {
        for (Object obj : mItems) {
            if (obj instanceof PlanItemEntity) {
                setPlanEdit((PlanItemEntity) obj, edit);
            }
        }
        mAdapter.notifyDataSetChanged();
        updateEditUI();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (BaseActivity.RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case 8100:
                    long cellId = data.getLongExtra(CommonUtil.KEY_VALUE_1, 0);
                    String type = data.getStringExtra(CommonUtil.KEY_VALUE_2);
                    long startTime = data.getLongExtra(CommonUtil.KEY_VALUE_3, 0);
                    long endTime = data.getLongExtra(CommonUtil.KEY_VALUE_4, 0);
                    long planId = data.getLongExtra(CommonUtil.KEY_VALUE_5, 0);
                    ArrayList<PointItemEntity> points = data.getParcelableArrayListExtra(CommonUtil.KEY_VALUE_6);
                    for (Object object : mItems) {
                        if (object instanceof CellItemEntity) {
                            CellItemEntity cell = (CellItemEntity) object;
//                            if (cell.getObjctId() == cellId && cell.getPlanIdCache() == planId) {
//                                cell.setPoints(points);
//                            }
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
                    startActivity(LoginActiivty.class);
                } else {
                    autoLoading();
                }
            }
        });
        stateLayout.showProgressView(getString(com.leo.afbaselibrary.R.string.loading));
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

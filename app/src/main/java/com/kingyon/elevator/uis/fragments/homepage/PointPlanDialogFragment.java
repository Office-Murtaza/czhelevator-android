package com.kingyon.elevator.uis.fragments.homepage;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.PlanItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.PointPlanAddAdapter;
import com.kingyon.elevator.uis.dialogs.CellAdSuccessDialog;
import com.kingyon.elevator.uis.dialogs.PlanEditDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseStateLoadingDialogFragment;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.widgets.emptyprovider.FadeViewAnimProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/12/29.
 * Email：lc824767150@163.com
 */

public class PointPlanDialogFragment extends BaseStateLoadingDialogFragment implements BaseAdapterWithHF.OnItemClickListener<PlanItemEntity>, PlanEditDialog.OnResultListener {

    @BindView(R.id.tv_business)
    TextView tvBusiness;
    @BindView(R.id.tv_diy)
    TextView tvDiy;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.rv_plans)
    RecyclerView rvPlans;
    @BindView(R.id.fl_content)
    FrameLayout flContent;

    private String type;
    private long cellId;

    private TextView[] adTypes;
    private PointPlanAddAdapter planAddAdapter;
    private List<PlanItemEntity> caches = new ArrayList<>();
    private PlanEditDialog editDialog;

    public static PointPlanDialogFragment newInstance(String type, long cellId) {
        Bundle args = new Bundle();
        args.putString(CommonUtil.KEY_VALUE_1, type);
        args.putLong(CommonUtil.KEY_VALUE_2, cellId);
        PointPlanDialogFragment fragment = new PointPlanDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.BL_PopupAnimation);
            }
        }
        return R.layout.fragment_dialog_point_plan;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.normal_dialog_question);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            type = getArguments().getString(CommonUtil.KEY_VALUE_1, "");
            cellId = getArguments().getLong(CommonUtil.KEY_VALUE_2);
        }
        super.init(savedInstanceState);
        planAddAdapter = new PointPlanAddAdapter(getContext());
        rvPlans.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlans.setAdapter(planAddAdapter);
        planAddAdapter.setOnItemClickListener(this);
        adTypes = new TextView[]{tvBusiness, tvDiy, tvInfo};
        initTypeSelected();
        loadData();
    }

    @Override
    public void loadData() {
        caches.clear();
        caches.addAll(planAddAdapter.getAllChoosed());
//        NetService.getInstance().plansList(type, null, null)
//                .compose(this.<List<CellItemEntity>>bindLifeCycle())
//                .subscribe(new CustomApiCallback<List<CellItemEntity>>() {
//                    @Override
//                    protected void onResultError(ApiException ex) {
//                        showToast(ex.getDisplayMessage());
//                        loadingComplete(STATE_ERROR);
//                        if (ex.getCode() == ApiException.NO_LOGIN || ex.getCode() == ApiException.RE_LOGIN) {
//                            dismiss();
//                        }
//                    }
//
//                    @Override
//                    public void onNext(List<CellItemEntity> cellItemEntities) {
//                        if (planItemEntities != null) {
//                            for (PlanItemEntity newItem : planItemEntities) {
//                                boolean hasChoosed = false;
//                                for (PlanItemEntity oldItem : caches) {
//                                    if (newItem.getObjectId() == oldItem.getObjectId()) {
//                                        hasChoosed = true;
//                                        break;
//                                    }
//                                }
//                                newItem.setChoosed(hasChoosed);
//                            }
//                            caches.clear();
//                        }
//                        planAddAdapter.refreshDatas(planItemEntities);
//                        loadingComplete(STATE_CONTENT);
//                    }
//                });
    }

    @Override
    public void onStart() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            if (attributes != null) {
                attributes.width = ScreenUtil.getScreenWidth(getContext()) - ScreenUtil.dp2px(32);
                attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                window.setAttributes(attributes);
            }
            window.setGravity(Gravity.CENTER);
        }
        super.onStart();
    }

    @OnClick({R.id.tv_ensure, R.id.tv_business, R.id.tv_diy, R.id.tv_info})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tv_ensure) {
            String planIds = planAddAdapter.getAllChoosedParam();
            if (TextUtils.isEmpty(planIds)) {
                showToast("请选择至少一个点位计划");
            } else {
                requestAddToPlan(planIds);
            }
        } else {
            if (adTypes == null) {
                return;
            }
            for (TextView tvType : adTypes) {
                if (view.getId() == tvType.getId()) {
                    tvType.setSelected(!tvType.isSelected());
                } else {
                    tvType.setSelected(false);
                }
            }
            updatePlanType();
        }
    }

    private void requestAddToPlan(String planIds) {
        NetService.getInstance().plansAddCells(planIds, String.valueOf(cellId))
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showSuccessDialog();
                        dismiss();
                    }
                });
    }

    private void showSuccessDialog() {
        if (getActivity() != null) {
            CellAdSuccessDialog successDialog = new CellAdSuccessDialog(getActivity());
            successDialog.show("", "已添加", "去下单", "继续添加");
        } else {
            showToast("操作成功");
        }
    }

    private void updatePlanType() {
        int selectId = 0;
        for (TextView tvType : adTypes) {
            if (tvType.isSelected()) {
                selectId = tvType.getId();
                break;
            }
        }
        switch (selectId) {
            case R.id.tv_business:
                type = Constants.PLAN_TYPE.BUSINESS;
                break;
            case R.id.tv_diy:
                type = Constants.PLAN_TYPE.DIY;
                break;
            case R.id.tv_info:
                type = Constants.PLAN_TYPE.INFORMATION;
                break;
            default:
                type = "";
                break;
        }
        loadData();
    }

    private void initTypeSelected() {
        int targetId;
        switch (type) {
            case Constants.PLAN_TYPE.BUSINESS:
                targetId = R.id.tv_business;
                break;
            case Constants.PLAN_TYPE.DIY:
                targetId = R.id.tv_diy;
                break;
            case Constants.PLAN_TYPE.INFORMATION:
                targetId = R.id.tv_info;
                break;
            default:
                targetId = 0;
                break;
        }
        for (TextView tvType : adTypes) {
            tvType.setSelected(targetId == tvType.getId());
        }
    }

    @Override
    public void onItemClick(View view, int position, PlanItemEntity entity, BaseAdapterWithHF<PlanItemEntity> baseAdaper) {
        if (entity == null) {
            if (!TextUtils.isEmpty(type)) {
                showAddPlanDialog(type);
            } else {
                showToast("请先选择计划类型");
            }
        } else {
            entity.setChoosed(!entity.isChoosed());
            planAddAdapter.notifyDataSetChanged();
        }
    }

    private void showAddPlanDialog(String type) {
        if (editDialog == null) {
            editDialog = new PlanEditDialog(getContext(), this);
        }
        editDialog.showType((BaseActivity) getActivity(), type);
        editDialog.setCanceledOnTouchOutside(false);
    }


    protected void initStateLayout() {
        stateLayout.setViewSwitchAnimProvider(new FadeViewAnimProvider());
        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoLoading();
            }
        });
        stateLayout.showProgressView(getString(R.string.loading));
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }

    @Override
    public boolean onResultListner(final PlanItemEntity entity, final String name, final String type) {
        NetService.getInstance().plansAdd(entity != null ? entity.getObjectId() : null, name, type)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("添加成功");
                        loadData();
                        if (editDialog != null && editDialog.isShowing()) {
                            editDialog.dismiss();
                        }
                    }
                });
        return false;
    }
}

package com.kingyon.elevator.others;

import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.dialogs.CellAdSuccessDialog;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import java.util.ArrayList;

/**
 * Created by GongLi on 2019/2/19.
 * Email：lc824767150@163.com
 */

public class AddCellToPlanPresenter {
    private BaseActivity baseActivity;
    private long cellId;

    private OptionsPickerView planPicker;
    private ArrayList<NormalParamEntity> planOptions;
    private CellAdSuccessDialog successDialog;

    public AddCellToPlanPresenter(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public long getCellId() {
        return cellId;
    }

    public void setCellId(long cellId) {
        this.cellId = cellId;
    }

    public void showPlanPicker(long cellId) {
        showPlanPicker(cellId, null);
    }

    public void showPlanPicker(long cellId, String type) {
//        PointPlanDialogFragment planDialogFragment = PointPlanDialogFragment.newInstance("", cellId);
//        planDialogFragment.show(getChildFragmentManager(), "PointPlanDialogFragment");
        if (planPicker == null || planOptions == null) {
            planOptions = new ArrayList<>();
            planOptions.add(new NormalParamEntity(Constants.PLAN_TYPE.BUSINESS, "商业"));
            planOptions.add(new NormalParamEntity(Constants.PLAN_TYPE.DIY, "DIY"));
            planOptions.add(new NormalParamEntity(Constants.PLAN_TYPE.INFORMATION, "便民信息"));
            planPicker = new OptionsPickerView.Builder(baseActivity, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    if (planOptions == null || planOptions.size() <= options1) {
                        return;
                    }
                    NormalParamEntity entity = planOptions.get(options1);
                    addCellToPlan(entity.getType());
                }
            }).setCyclic(false, false, false).build();
            planPicker.setPicker(planOptions);
        }
        this.cellId = cellId;
        KeyBoardUtils.closeKeybord(baseActivity);
        int typeIndex = getTypeIndex(type);
        if (typeIndex >= 0) {
            planPicker.setSelectOptions(getTypeIndex(type));
        }
        planPicker.show();
    }

    private int getTypeIndex(String type) {
        int result = -1;
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case Constants.PLAN_TYPE.BUSINESS:
                    result = 0;
                    break;
                case Constants.PLAN_TYPE.DIY:
                    result = 1;
                    break;
                case Constants.PLAN_TYPE.INFORMATION:
                    result = 2;
                    break;
            }
        }
        return result;
    }

    public void addCellToPlan(final String type) {
        NetService.getInstance().plansAddCells(type, String.valueOf(cellId))
                .compose(baseActivity.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        baseActivity.showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showSuccessDialog(type);
                    }
                });
    }

    public void addCellToPlan(long cellId, final String type) {
        NetService.getInstance().plansAddCells(type, String.valueOf(cellId))
                .compose(baseActivity.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        baseActivity.showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showSuccessDialog(type);
                    }
                });
    }

    private void showSuccessDialog(String type) {
        if (baseActivity != null) {
            if (successDialog == null) {
                successDialog = new CellAdSuccessDialog(baseActivity);
            }
            successDialog.show(type, "已添加", "去下单", "继续添加");
        }
    }

    public void onDestroy() {
        baseActivity = null;
    }
}

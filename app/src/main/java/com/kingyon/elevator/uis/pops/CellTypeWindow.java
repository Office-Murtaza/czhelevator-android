package com.kingyon.elevator.uis.pops;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.uis.adapters.adapterone.SearchCellTypeAdapter;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.leo.afbaselibrary.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class CellTypeWindow extends PopupWindow implements PopupWindow.OnDismissListener {
    @BindView(R.id.rv_cell_types)
    RecyclerView rvCellTypes;

    private OnCellTypeResultListener onCellTypeResultListener;
    private SearchCellTypeAdapter cellTypeAdapter;

    public CellTypeWindow(Context context, OnCellTypeResultListener onCellTypeResultListener) {
        super(context);
        this.onCellTypeResultListener = onCellTypeResultListener;
        View view = LayoutInflater.from(context).inflate(R.layout.activity_search_cell_type, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setOnDismissListener(this);

        rvCellTypes.setLayoutManager(new GridLayoutManager(context, 4));
        rvCellTypes.addItemDecoration(new GridSpacingItemDecoration(4, ScreenUtil.dp2px(16), true));
        cellTypeAdapter = new SearchCellTypeAdapter(context, R.layout.activity_search_cell_type_item);
        rvCellTypes.setAdapter(cellTypeAdapter);
        List<NormalParamEntity> entities = new ArrayList<>();
        entities.add(new NormalParamEntity("", "不限"));
        entities.add(new NormalParamEntity(Constants.CELL_TYPE.COMMERCIAL, "商业"));
        entities.add(new NormalParamEntity(Constants.CELL_TYPE.HOUSE, "住宅"));
        entities.add(new NormalParamEntity(Constants.CELL_TYPE.OFFICE, "写字楼"));
        cellTypeAdapter.refreshDatas(entities);
        cellTypeAdapter.clearSelected();
    }

    @OnClick({R.id.tv_reset, R.id.tv_ensure})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tv_reset) {
            cellTypeAdapter.clearSelected();
        }
        dismiss();
    }

    @Override
    public void onDismiss() {
        if (onCellTypeResultListener != null) {
            String[] choosedParams = cellTypeAdapter.getChoosedParams();
            onCellTypeResultListener.onCellTypResult(choosedParams[0], choosedParams[1]);
        }
    }

    public interface OnCellTypeResultListener {
        void onCellTypResult(String names, String types);
    }
}

package com.kingyon.elevator.uis.pops;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.ADEntity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ADOperatePop extends PopupWindow {

    private ADEntity adEntity;
    private OnOperateClickLister onOperateClickListeer;

    public ADOperatePop(Context context, View target) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_del_edite, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setWidth(target.getWidth());
        setHeight(target.getHeight());
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setOnOperateClickLister(OnOperateClickLister onOperateClickListeer) {
        this.onOperateClickListeer = onOperateClickListeer;
    }

    @OnClick({R.id.btn_del, R.id.btn_edit, R.id.root})
    public void onViewClicked(View view) {
        if (onOperateClickListeer != null) {
            switch (view.getId()) {
                case R.id.btn_del:
                    onOperateClickListeer.onDeleteClick(adEntity);
                    break;
                case R.id.btn_edit:
                    onOperateClickListeer.onEditClick(adEntity);
                    break;
            }
        }
        dismiss();
    }

    public void show(View parent, View view, ADEntity adEntity) {
        this.adEntity = adEntity;
        int[] location = new int[2];
            view.getLocationInWindow(location);
        showAtLocation(parent, Gravity.TOP | Gravity.LEFT, location[0], location[1]);
    }

    public interface OnOperateClickLister {
        void onEditClick(ADEntity entity);

        void onDeleteClick(ADEntity entity);
    }
}

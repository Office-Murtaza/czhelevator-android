package com.kingyon.elevator.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.entities.HorizontalSelectDateEntity;
import com.kingyon.elevator.entities.SelectDateEntity;
import com.kingyon.elevator.interfaces.PlanSelectDateLinsener;
import com.kingyon.elevator.uis.adapters.adapterone.HorizontalSelectDateAdapter;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.MyToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created By SongPeng  on 2019/12/13
 * Email : 1531603384@qq.com
 * 计划单选择日期
 */
public class PlanSelectDateNewDialog extends DialogFragment {

    @BindView(R.id.date_grid_view)
    HorizontalRecyclerView date_grid_view;
    @BindView(R.id.tv_current_date)
    TextView tv_current_date;
    Context mContext;
    private Date initTodayDate;//初始时选中今天的日期
    private SimpleDateFormat simpleDateFormat;
    PlanSelectDateLinsener planSelectDateLinsener;
    HorizontalSelectDateAdapter horizontalSelectDateAdapter;
    private List<HorizontalSelectDateEntity> horizontalSelectDateEntityList;
    private int todayYear;
    private int lastYear;
    private Boolean isInitData = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.plan_select_date_dialog_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyDialog);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCancelable(true);
    }

    public void setPlanSelectDateLinsener(PlanSelectDateLinsener planSelectDateLinsener) {
        this.planSelectDateLinsener = planSelectDateLinsener;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isInitData) {
            isInitData = true;
            mContext = getContext();
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            initTodayDate = new Date();
            todayYear = DateUtils.getCurrentYear();
            lastYear = todayYear + 1;
            initData();
        }
    }

    private void initData() {
        horizontalSelectDateEntityList = new ArrayList<>();
        for (int i = DateUtils.getCurrentMonth(); i <= 12; i++) {
            try {
                if (i < 10) {
                    horizontalSelectDateEntityList.add(new HorizontalSelectDateEntity(DateUtils.getCurrentYear(), i, simpleDateFormat.parse(todayYear + "-0" + i + "-01")));
                } else {
                    horizontalSelectDateEntityList.add(new HorizontalSelectDateEntity(DateUtils.getCurrentYear(), i, simpleDateFormat.parse(todayYear + "-" + i + "-01")));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i < 13; i++) {
            try {
                if (i < 10) {
                    horizontalSelectDateEntityList.add(new HorizontalSelectDateEntity(lastYear, i, simpleDateFormat.parse(lastYear + "-0" + i + "-01")));
                } else {
                    horizontalSelectDateEntityList.add(new HorizontalSelectDateEntity(lastYear, i, simpleDateFormat.parse(lastYear + "-" + i + "-01")));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        horizontalSelectDateAdapter = new HorizontalSelectDateAdapter(mContext, horizontalSelectDateEntityList);
        tv_current_date.setText(DateUtils.getCurrentYear() + "年" + DateUtils.getCurrentMonth() + "月");
        date_grid_view.setAdapter(horizontalSelectDateAdapter);
        date_grid_view.setOnPagerChageListener(position -> {
            tv_current_date.setText(horizontalSelectDateEntityList.get(position).getCurrentYearAndMonth());
        });
        date_grid_view.setOnPagerPosition(0);
        planSelectDateLinsener.dialogShowSuccess();
    }

    @OnClick({R.id.cancel_date, R.id.confirm_date})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_date:
                dismiss();
                break;
            case R.id.confirm_date:
                if (horizontalSelectDateAdapter.startSelectDateEntity == null) {
                    MyToastUtils.showShort("请选择开始日期");
                    return;
                }
                if (horizontalSelectDateAdapter.endSelectDateEntity == null) {
                    SelectDateEntity endSelectDateEntity = new SelectDateEntity(horizontalSelectDateAdapter.startSelectDateEntity.getYear(),
                            horizontalSelectDateAdapter.startSelectDateEntity.getMonth(), horizontalSelectDateAdapter.startSelectDateEntity.getDay());
                    endSelectDateEntity.setDate(horizontalSelectDateAdapter.startSelectDateEntity.getDate() + " 23:59:59.999");
                    horizontalSelectDateAdapter.startSelectDateEntity.setDate(horizontalSelectDateAdapter.startSelectDateEntity.getDate() + " 00:00:00.000");
                    planSelectDateLinsener.confirmSelectDate(horizontalSelectDateAdapter.startSelectDateEntity,
                            endSelectDateEntity);
                } else {
                    horizontalSelectDateAdapter.startSelectDateEntity.setDate(horizontalSelectDateAdapter.startSelectDateEntity.getDate() + " 00:00:00.000");
                    horizontalSelectDateAdapter.endSelectDateEntity.setDate(horizontalSelectDateAdapter.endSelectDateEntity.getDate() + " 23:59:59.999");
                    planSelectDateLinsener.confirmSelectDate(horizontalSelectDateAdapter.startSelectDateEntity,
                            horizontalSelectDateAdapter.endSelectDateEntity);
                }
                dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        //window.setDimAmount(0.5f);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(490));
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        try {
            date_grid_view.removeAllViews();
            horizontalSelectDateAdapter = null;
            date_grid_view = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

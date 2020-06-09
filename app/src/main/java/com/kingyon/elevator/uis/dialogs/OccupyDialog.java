package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adapterone.OccupyAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/2/21.
 * Email：lc824767150@163.com
 */

public class OccupyDialog extends Dialog {

    @BindView(R.id.rv_datas)
    RecyclerView rvDatas;

    private Context mContext;
    private OccupyAdapter occupyAdapter;

    public OccupyDialog(Context context) {
        super(context, R.style.normal_dialog_small);
        mContext = context;
        setContentView(R.layout.dialog_occupy);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(com.kingyon.library.social.R.style.dialog_show_anim);
            window.setGravity(Gravity.BOTTOM);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        rvDatas.setLayoutManager(new LinearLayoutManager(mContext));
        rvDatas.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.black_divider)
                .sizeResId(R.dimen.spacing_divider)
                .build());
        occupyAdapter = new OccupyAdapter(mContext);
        rvDatas.setAdapter(occupyAdapter);
    }

    public void show(List<Long> dates) {
        show();
        if (dates != null && dates.size() > 0) {
            Collections.sort(dates);
        }
        occupyAdapter.refreshDatas(dates);
    }

    @OnClick(R.id.share_btn_cancel)
    public void onViewClicked() {
        dismiss();
    }
}

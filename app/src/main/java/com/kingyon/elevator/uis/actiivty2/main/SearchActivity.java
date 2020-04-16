package com.kingyon.elevator.uis.actiivty2.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donkingliang.labels.LabelsView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adapter2.SearchAdapter;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kingyon.elevator.utils.Constance.ACTIVITY_MAIN2_SEARCH;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
@Route(path = ACTIVITY_MAIN2_SEARCH)
public class SearchActivity extends BaseActivity {
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.tv_bake)
    TextView tvBake;
    @BindView(R.id.img_swarch_delete)
    ImageView imgSwarchDelete;
    @BindView(R.id.labels)
    LabelsView labels;
    @BindView(R.id.rcv_view)
    RecyclerView rcvView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main_search;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ArrayList<String> label = new ArrayList<>();
        label.add("11111111");
        label.add("asddddddddddddddd");
        label.add("122");
        label.add("aaaaaaaaaaaaaa");
        label.add("23423");
        label.add("546456");
        label.add("zzzzzzzzzzzzzzzzz");
        label.add("768678");
        label.add("90809089");
        label.add("rrrrrrrrrrrrrrrrrrrrr");
        label.add("dsds");
        label.add("xcx ");
        label.add("ccccccccccccccc");
        label.add("90809xcxc089");
        label.add("aswqawdsdasd");
        label.add("dsfsfd");
        label.add("sadddddddddd");
        label.add("ssssssssssssss");

        labels.setLabels(label);
        SearchAdapter searchAdapter = new SearchAdapter(this);
        rcvView.setLayoutManager(new LinearLayoutManager(this));
        rcvView.setAdapter(searchAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_bake)
    public void onViewClicked() {
        finish();
    }
}

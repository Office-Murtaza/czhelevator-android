package com.kingyon.elevator.uis.activities.salesman;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/6/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:添加小区内容
 */
public class AddSalesActiviry extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_conent)
    EditText etConent;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    long parentId;
    String superior;
    int type;

    @Override
    public int getContentViewId() {
        return R.layout.activity_add_sales;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        parentId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        superior = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        type = getIntent().getIntExtra(CommonUtil.KEY_VALUE_3,0);
        tvTopTitle.setText(superior);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_add:
                httpAdd();
                break;
        }
    }

    private void httpAdd() {
        if (etConent.getText().toString().isEmpty()){

        }else {
            if (type == 1) {
                /*添加楼栋*/
                NetService.getInstance().addBuilding((long) 0,parentId,etConent.getText().toString())
                        .compose(this.<String>bindLifeCycle())
                        .subscribe(new CustomApiCallback<String>() {
                            @Override
                            protected void onResultError(ApiException ex) {
                                showToast(ex.getDisplayMessage());
                            }
                            @Override
                            public void onNext(String s) {
                                showToast("保存成功");
                                finish();
                            }
                        });
            } else {
                /*添加单元*/
                NetService.getInstance().addUnit((long) 0, parentId, etConent.getText().toString())
                        .compose(this.<String>bindLifeCycle())
                        .subscribe(new CustomApiCallback<String>() {
                            @Override
                            protected void onResultError(ApiException ex) {
                                showToast(ex.getDisplayMessage());
                            }

                            @Override
                            public void onNext(String s) {
                                showToast("保存成功");
//                                if (editDialog != null && editDialog.isShowing()) {
//                                    editDialog.dismiss();
//                                }
//                                autoRefresh();
                                finish();
                            }
                        });

            }

        }

    }


}

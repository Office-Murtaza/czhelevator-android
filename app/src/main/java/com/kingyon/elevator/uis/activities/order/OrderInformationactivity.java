package com.kingyon.elevator.uis.activities.order;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ORDER_INFORMATION_ACTIVITY;

/**
 * @Created By Admin  on 2020/8/4
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
@Route(path = ORDER_INFORMATION_ACTIVITY)
public class OrderInformationactivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @Autowired
    String content;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_informationact;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        tvTopTitle.setText("便民信息预览");
        tvContent.setText(content+"");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_top_back)
    public void onViewClicked() {
        finish();
    }
}

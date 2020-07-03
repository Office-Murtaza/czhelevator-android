package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.activities.user.FeedBackActivity;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.uis.fragments.main2.user.CustomerServiceCenterFragment;
import com.kingyon.elevator.uis.fragments.order.OrderFragment;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_CUSTOMER_SERVICE_CENTER;

/**
 * @Created By Admin  on 2020/7/1
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 客服中心
 */
@Route(path = ACTIVITY_CUSTOMER_SERVICE_CENTER)
public class CustomerServiceCenterActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.tv_feedback)
    TextView tvFeedback;
    @BindView(R.id.vp)
    ViewPager vp;

    @Override
    public int getContentViewId() {
        return R.layout.activity_customer_service_center;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CustomerServiceCenterFragment().setIndex("1注册问题"), "注册问题");
        adapter.addFrag(new CustomerServiceCenterFragment().setIndex("2投放问题"), "投放问题");
        adapter.addFrag(new CustomerServiceCenterFragment().setIndex("3合伙人问题"), "合伙人问题");
        adapter.addFrag(new CustomerServiceCenterFragment().setIndex("4使用教程"), "使用教程");
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(adapter.getCount());
        viewpagertab.setViewPager(vp);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_contact, R.id.tv_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_contact:
                try {
                    AFUtil.toCall(this, getString(R.string.service_phone));
                } catch (Exception e) {
                    ToastUtils.showToast(this, "当前手机不允许拨打电话，请换手机操作", 1000);
                }
                break;
            case R.id.tv_feedback:
                startActivity(FeedBackActivity.class);
                break;
        }
    }
}

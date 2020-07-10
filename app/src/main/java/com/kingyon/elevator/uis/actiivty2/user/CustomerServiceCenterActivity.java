package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.entities.WikipediaEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.user.FeedBackActivity;
import com.kingyon.elevator.uis.dialogs.CallDialog;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.uis.fragments.main2.user.CustomerServiceCenterFragment;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
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
    @BindView(R.id.tv_nickname)
    TextView tvNickname;

    @Override
    public int getContentViewId() {
        return R.layout.activity_customer_service_center;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("客服中心");
        tvNickname.setText("Hi，"+DataSharedPreferences.getNickName());
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().getWikipedia()
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<WikipediaEntiy>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        showToast(ex.getDisplayMessage());
                        finish();
                    }

                    @Override
                    public void onNext(WikipediaEntiy wikipediaEntiy) {
                        hideProgress();
                        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
                        for (int i = 0; i < wikipediaEntiy.getWikipedia().size(); i++) {
                            LogUtils.e(wikipediaEntiy.getWikipedia().get(i).getLabel());
                            adapter.addFrag(new CustomerServiceCenterFragment().setIndex(wikipediaEntiy.getWikipedia().get(i).getItem()), wikipediaEntiy.getWikipedia().get(i).getLabel());
                        }
                        vp.setAdapter(adapter);
                        vp.setOffscreenPageLimit(adapter.getCount());
                        viewpagertab.setViewPager(vp);
                    }
                });
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
                CallDialog callDialog = new CallDialog(this);
                callDialog.show();
                break;
            case R.id.tv_feedback:
                startActivity(FeedBackActivity.class);
                break;
        }
    }
}

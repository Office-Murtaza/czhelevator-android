package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.activities.user.UserProfileActivity;
import com.kingyon.elevator.uis.dialogs.ReportShareDialog;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.HOME_CONTENT;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_CENTER;

/**
 * @Created By Admin  on 2020/6/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:用户中心
 */
@Route(path = ACTIVITY_USER_CENTER)
public class UserCenterActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.img_placeholder)
    ImageView imgPlaceholder;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_idnumber)
    TextView tvIdnumber;
    @BindView(R.id.tv_attention_number)
    TextView tvAttentionNumber;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rv_attention_list)
    RecyclerView rvAttentionList;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rlNotlogin;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @Autowired
    int type;
    @Override
    public int getContentViewId() {
        return R.layout.activity_user_center;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setHeadViewPadding(this, rlTop);
//        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setTransparent(this, false);
        if (type==0){
            imgMore.setImageResource(R.mipmap.ic_personal_edit_white);
            tvAttention.setVisibility(View.GONE);
        }else {
            imgMore.setImageResource(R.mipmap.ic_comm_menu_whiteq);
            tvAttention.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.img_top_back, R.id.tv_attention, R.id.img_more, R.id.rl_error, R.id.rl_notlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_attention:

                break;
            case R.id.img_more:
                if (type==0) {
                    startActivity(UserProfileActivity.class);
                }else {
                    /*举报*/
                    ReportShareDialog reportShareDialog = new ReportShareDialog(this, 1, HOME_CONTENT);
                    reportShareDialog.show();
                }
                break;
            case R.id.rl_error:

                break;
            case R.id.rl_notlogin:
                ActivityUtils.setLoginActivity();
                break;
        }
    }
}

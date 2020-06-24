package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.uis.activities.cooperation.AddNewBankCardActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.MyActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/6/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class AppAccountDialog extends Dialog {

    @BindView(R.id.tv_zfb)
    TextView tvZfb;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.share_btn_cancel)
    TextView shareBtnCancel;
    private Context context;
    private CooperationInfoNewEntity entity;
    public AppAccountDialog(@NonNull Context context,CooperationInfoNewEntity entity) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.context = context;
        this.entity = entity;
        setContentView(getLayoutId());
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(com.kingyon.library.social.R.style.dialog_show_anim);
            window.setGravity(Gravity.BOTTOM);
        }
    }

    protected int getLayoutId() {
        return R.layout.dialog_app_account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    public void setPaddingTop(int top) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = top;
            window.setAttributes(lp);
        }
    }

    @OnClick({R.id.tv_zfb, R.id.tv_wx, R.id.tv_bank, R.id.share_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_zfb:
                Bundle bundle = new Bundle();
                bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
                MyActivityUtils.goActivity(context, AddNewBankCardActivity.class, bundle, "2");
                break;
            case R.id.tv_wx:
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable(CommonUtil.KEY_VALUE_1, entity);
                MyActivityUtils.goActivity(context, AddNewBankCardActivity.class, bundle1, "3");
                break;
            case R.id.tv_bank:
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable(CommonUtil.KEY_VALUE_1, entity);
                MyActivityUtils.goActivity(context, AddNewBankCardActivity.class, bundle2, "1");
                break;
            case R.id.share_btn_cancel:
                dismiss();
                break;
        }
    }
}

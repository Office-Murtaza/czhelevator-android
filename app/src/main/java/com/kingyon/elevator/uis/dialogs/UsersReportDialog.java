package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.PointClassicEntiy;
import com.kingyon.elevator.entities.entities.ReportContent;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.dialog.CommunityTypeAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.dialog.UserReportAdapter;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/8/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class UsersReportDialog extends Dialog {

    @BindView(R.id.rl_list)
    RecyclerView rlList;
    @BindView(R.id.share_btn_cancel)
    TextView shareBtnCancel;
    Context context;
    String beReportAccount;

    public UsersReportDialog(@NonNull Context context,String beReportAccount ) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.context =context;
        this.beReportAccount =beReportAccount;
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
        return R.layout.dialog_users_report;
    }

    public void setPaddingTop(int top) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = top;
            window.setAttributes(lp);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        NetService.getInstance().getReportContent()

                .subscribe(new CustomApiCallback<List<ReportContent>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showToast(context,ex.getDisplayMessage(),1000);
                        dismiss();
                    }

                    @Override
                    public void onNext(List<ReportContent> reportContents) {
                        UserReportAdapter adapter = new UserReportAdapter(context, reportContents, new UserReportAdapter.ItmeOnclick() {
                            @Override
                            public void itmeOnclick(int id) {
                                OrdinaryActivity.HttpreportUser(context, beReportAccount, id, new IsSuccess() {
                                    @Override
                                    public void isSuccess(boolean success) {
                                        dismiss();
                                        if (success){

                                            ToastUtils.showToast(context,"举报成功",1000);
                                        }else {

                                            ToastUtils.showToast(context,"举报成功",1000);
                                        }
                                    }
                                });
                            }
                        });
                        rlList.setAdapter(adapter);
                        rlList.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false));

                    }
                });

    }

    @OnClick(R.id.share_btn_cancel)
    public void onViewClicked() {
        dismiss();
    }
}

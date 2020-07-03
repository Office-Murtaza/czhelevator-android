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
import android.widget.ListView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.UserCashTypeListEnity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.cooperation.WithdrawalWayActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.partnership.WithdrawalWayAdapter;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kingyon.elevator.photopicker.UtilsHelper.getString;

/**
 * @Created By Admin  on 2020/7/3
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class AccountDialog extends Dialog {

    @BindView(R.id.list_item)
    RecyclerView listItem;
    @BindView(R.id.share_btn_cancel)
    TextView shareBtnCancel;
    BaseActivity baseActivity;
    DialogOnClick dialogOnClick;
    public AccountDialog(@NonNull BaseActivity baseActivity) {
        super(baseActivity, com.kingyon.library.social.R.style.ShareDialog);
        this.baseActivity = baseActivity;
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
        return R.layout.dialog_account;
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
        httpData();
    }

    private void httpData() {
        baseActivity.showProgressDialog(getString(R.string.wait));
        NetService.getInstance().steUserCashTypeList(0)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<List<UserCashTypeListEnity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        baseActivity.hideProgress();
                    }

                    @Override
                    public void onNext(List<UserCashTypeListEnity> userCashTypeListEnities) {
                        baseActivity.hideProgress();
                        WithdrawalWayAdapter withdrawalWayAdapter = new WithdrawalWayAdapter(baseActivity, userCashTypeListEnities,null,"2");
                        listItem.setAdapter(withdrawalWayAdapter);
                        listItem.setLayoutManager(new GridLayoutManager(baseActivity, 1, GridLayoutManager.VERTICAL, false));
                        withdrawalWayAdapter.setOnClick(new WithdrawalWayAdapter.OnClick() {
                            @Override
                            public void OnClick(UserCashTypeListEnity enity) {
                                dismiss();
                                dialogOnClick.onClick(enity);
                            }
                        });
                    }
                });

    }

    @OnClick(R.id.share_btn_cancel)
    public void onViewClicked() {
        dismiss();
    }

    public void setDialogOnClick(DialogOnClick dialogOnClick){
        this.dialogOnClick = dialogOnClick;
    }

    public interface DialogOnClick{
        void onClick(UserCashTypeListEnity enity);
    }
}



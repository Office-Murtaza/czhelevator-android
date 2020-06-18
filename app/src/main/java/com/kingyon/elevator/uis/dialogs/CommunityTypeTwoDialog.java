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

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.PointClassicEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.dialog.CommunityTypeAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.dialog.CommunityTypeTwoAdapter;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/6/18
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CommunityTypeTwoDialog extends Dialog {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.share_btn_cancel)
    TextView shareBtnCancel;
    Context context;
    CommunityTypeDialog.OnClickzhi onClickzhi;
    public List<PointClassicEntiy.ChildBean> child;
    public CommunityTypeTwoDialog(@NonNull Context context,List<PointClassicEntiy.ChildBean> child,CommunityTypeDialog.OnClickzhi onClickzhi) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.context = context;
        this.child = child;
        this.onClickzhi = onClickzhi;
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
        return R.layout.dialog_community_type;
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
        initView();
    }

    private void initView() {
        CommunityTypeTwoAdapter communityTypeAdapter = new CommunityTypeTwoAdapter(context, child, new CommunityTypeTwoAdapter.ItmeOnclick() {
            @Override
            public void itmeOnclick(PointClassicEntiy.ChildBean child) {
                dismiss();
                LogUtils.e(child.pointName,child.id);
                onClickzhi.getIdStr(child.pointName,child.id);
            }
        });
        rvList.setAdapter(communityTypeAdapter);
        rvList.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false));

    }

    @OnClick(R.id.share_btn_cancel)
    public void onViewClicked() {
            dismiss();
    }
}

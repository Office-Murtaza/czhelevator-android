package com.kingyon.elevator.uis.fragments.main2.found.utilsf;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.alibaba.android.arouter.launcher.ARouter;
import com.kingyon.elevator.R;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_RELEASETY;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_COMMUNITY_RELEASETY;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDE_RELEASETY;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * @Author:Mrczh
 * Instructions:
 */
public class ConfirmPopWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private View ll_article, ll_voide,ll_community;

    public ConfirmPopWindow(Context context) {
        super(context);
        this.context = context;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.main2_edit_dialog, null);
        ll_community = view.findViewById(R.id.ll_community);
        ll_voide = view.findViewById(R.id.ll_voide);
        ll_article = view.findViewById(R.id.ll_article);
        ll_community.setOnClickListener(this);
        ll_voide.setOnClickListener(this);
        ll_article.setOnClickListener(this);

        setContentView(view);
        initWindow();
    }

    private void initWindow() {
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        this.setWidth((int) (d.widthPixels * 0.35));
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) context, 1.0f);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) context, 1f);
            }
        });
    }

//    设置添加屏幕的背景透明度

    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showAtBottom(View view) {
        //弹窗位置设置
//        showAsDropDown(view, Math.abs((view.getWidth() - getWidth()) / 2), 20);
        showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 35, 150);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_article:
                ARouter.getInstance().build(ACTIVITY_MAIN2_ARTICLE_RELEASETY).navigation();
                break;
            case R.id.ll_community:
                ARouter.getInstance().build(ACTIVITY_MAIN2_COMMUNITY_RELEASETY).navigation();
                break;
            case R.id.ll_voide:
                ARouter.getInstance().build(ACTIVITY_MAIN2_VOIDE_RELEASETY).navigation();
                break;
            default:
                break;
        }
    }

}

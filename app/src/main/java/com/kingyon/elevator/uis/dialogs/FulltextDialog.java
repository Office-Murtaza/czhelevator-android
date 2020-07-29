package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobomee.android.mentions.text.MentionTextView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.uis.actiivty2.input.Parser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/7/28
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class FulltextDialog extends Dialog {
    QueryRecommendEntity commendEntity;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_bt)
    TextView tvBt;
    @BindView(R.id.tv_original)
    TextView tvOriginal;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_content)
    MentionTextView tvContent;

    public FulltextDialog(@NonNull Context context, QueryRecommendEntity commendEntity) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.commendEntity = commendEntity;
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
        return R.layout.dialog_fulltext;
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
        Parser mTagParser = new Parser();
        tvContent.setMovementMethod(new LinkMovementMethod());
        tvContent.setParserConverter(mTagParser);
        tvTitle.setText(commendEntity.title);
        tvContent.setText(commendEntity.content);
        if (!commendEntity.original) {
            tvOriginal.setText("原创");
        } else {
            tvOriginal.setText("转载");
        }
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        dismiss();
    }
}

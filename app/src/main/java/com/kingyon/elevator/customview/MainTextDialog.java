package com.kingyon.elevator.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.AdNoticeWindowEntity;
import com.kingyon.elevator.interfaces.OnItemClick;
import com.kingyon.elevator.uis.activities.WebViewActivity;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.leo.afbaselibrary.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/8/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MainTextDialog extends Dialog {

    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private OnItemClick onItemClick;

    private Context context;

    private AdNoticeWindowEntity adNoticeWindowEntity;

    public MainTextDialog(Context context, AdNoticeWindowEntity adNoticeWindowEntity) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.adNoticeWindowEntity = adNoticeWindowEntity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_main_text);
        ButterKnife.bind(this);
        if (adNoticeWindowEntity != null && adNoticeWindowEntity.type == 1) {
            DataSharedPreferences.saveLong(DataSharedPreferences.LAST_AD_TIME, System.currentTimeMillis());
            DataSharedPreferences.saveInt(DataSharedPreferences.LAST_AD_ID, adNoticeWindowEntity.id);
            tvContent.setText(adNoticeWindowEntity.popText);
        } else {
            DialogUtils.getInstance().hideMainWindowNoticeDialog();
        }
    }



    @Override
    public void dismiss() {
        super.dismiss();

    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        DialogUtils.getInstance().hideMainText();
    }
}
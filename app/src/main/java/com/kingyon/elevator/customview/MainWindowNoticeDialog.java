package com.kingyon.elevator.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
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
 * Date:2018/10/30
 * Time:15:24
 * author:songpeng
 * Email:1531603384@qq.com
 * 首页图片弹窗提示
 */
public class MainWindowNoticeDialog extends Dialog {

    private OnItemClick onItemClick;
    @BindView(R.id.iv_ad_image)
    ImageView iv_ad_image;
    @BindView(R.id.close_dialog1)
    ImageView close_dialog;
    private Context context;

    private AdNoticeWindowEntity adNoticeWindowEntity;

    public MainWindowNoticeDialog(Context context, AdNoticeWindowEntity adNoticeWindowEntity) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.adNoticeWindowEntity = adNoticeWindowEntity;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_main_window_notice_dialog);
        ButterKnife.bind(this);
        close_dialog = findViewById(R.id.close_dialog1);
        close_dialog.bringToFront();
        close_dialog.setClickable(true);
        if (adNoticeWindowEntity != null && adNoticeWindowEntity.type == 0) {
            DataSharedPreferences.saveLong(DataSharedPreferences.LAST_AD_TIME, System.currentTimeMillis());
            DataSharedPreferences.saveInt(DataSharedPreferences.LAST_AD_ID, adNoticeWindowEntity.id);

            GlideUtils.loadRoundCornersImage(context, adNoticeWindowEntity.urlImage, iv_ad_image, 20);
        } else {
            DialogUtils.getInstance().hideMainWindowNoticeDialog();
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();

    }


    @OnClick({R.id.iv_ad_image, R.id.close_dialog1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_ad_image:
                LogUtils.e("pppppppppppppppp");
                if (adNoticeWindowEntity.urlLink != null) {
                    MyActivityUtils.goActivity(context, WebViewActivity.class, adNoticeWindowEntity.urlLink);
                    DialogUtils.getInstance().hideMainWindowNoticeDialog();
                } else {
                    DialogUtils.getInstance().hideMainWindowNoticeDialog();
                }
                break;
            case R.id.close_dialog1:
                LogUtils.e("11111111111111");
                DialogUtils.getInstance().hideMainWindowNoticeDialog();
                break;
        }
    }
}

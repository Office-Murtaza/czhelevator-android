package com.kingyon.elevator.uis.actiivty2.main;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kingyon.elevator.utils.Constance.ACTIVITY_MAIN2_COMMUNITY_RELEASETY;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */

@Route(path = ACTIVITY_MAIN2_COMMUNITY_RELEASETY)
public class CommunityReleasetyActivity extends BaseActivity {
    @BindView(R.id.img_bake)
    ImageView imgBake;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_releaset)
    TextView tvReleaset;
    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.rcv_list_img)
    RecyclerView rcvListImg;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.img_aite)
    ImageView imgAite;
    @BindView(R.id.img_huati)
    ImageView imgHuati;
    @BindView(R.id.tv_zishu)
    TextView tvZishu;

    @Override
    public int getContentViewId() {
        return R.layout.activity_community_releaset;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            tvZishu.setText(s.toString().length()+"/500");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }


    @OnClick({R.id.img_bake, R.id.tv_releaset, R.id.img_add,R.id.img_icon, R.id.img_aite, R.id.img_huati})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.tv_releaset:
//                发布
                break;
            case R.id.img_add:
//                添加图片
                break;
            case R.id.img_icon:
//                表情
                break;
            case R.id.img_aite:
//                @
                break;
            case R.id.img_huati:
//                话题
                break;
        }
    }


    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

}

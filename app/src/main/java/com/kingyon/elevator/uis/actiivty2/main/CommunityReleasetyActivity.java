package com.kingyon.elevator.uis.actiivty2.main;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.adapters.adaptertwo.ChooseAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.litao.android.lib.Configuration;
import com.litao.android.lib.Utils.GridSpacingItemDecoration;
import com.litao.android.lib.entity.PhotoEntry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ACTIVITYUTILS_PICTURE_CHOOSE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_COMMUNITY_RELEASETY;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_TOPIC_DETAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_TOPIC_SEARCH;
import static com.kingyon.elevator.utils.PictureSelectorUtil.showPictureSelectorCropProperty;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * @Author:Mrczh
 * Instructions:社区发布
 */

@Route(path = ACTIVITY_MAIN2_COMMUNITY_RELEASETY)
public class CommunityReleasetyActivity extends BaseActivity implements ChooseAdapter.OnItmeClickListener {
    @BindView(R.id.img_bake)
    ImageView imgBake;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_releaset)
    TextView tvReleaset;
    @BindView(R.id.edit_content)
    EditText editContent;
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
    ChooseAdapter mAdapter;
    @Autowired
    String imagePath;
    @Override
    public int getContentViewId() {
        return R.layout.activity_community_releaset;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
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

        LogUtils.e(imagePath);

        mAdapter = new ChooseAdapter(this);
        rcvListImg.setLayoutManager(new GridLayoutManager(this, 3));
        rcvListImg.setAdapter(mAdapter);
        rcvListImg.addItemDecoration(new GridSpacingItemDecoration(3, 4, true));
    }


    @OnClick({R.id.img_bake, R.id.tv_releaset,R.id.img_icon, R.id.img_aite, R.id.img_huati})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.tv_releaset:
//                发布
                break;
            case R.id.img_icon:
//                表情
                break;
            case R.id.img_aite:
//                @
                break;
            case R.id.img_huati:
//                话题
                ARouter.getInstance().build(ACTIVITY_MAIN2_TOPIC_SEARCH).navigation();

                break;
                default:
        }
    }

    @Override
    public void onItemClicked(int position) {
        if (position == mAdapter.getItemCount()-1) {
            ARouter.getInstance().build(ACTIVITY_ACTIVITYUTILS_PICTURE_CHOOSE).navigation();
        }
    }


}

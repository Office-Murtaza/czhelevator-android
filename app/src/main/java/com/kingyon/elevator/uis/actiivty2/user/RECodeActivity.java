package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.utilstwo.ZXingUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_RE_CODE;

/**
 * @Created By Admin  on 2020/6/3
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 二维码
 */
@Route(path = ACTIVITY_RE_CODE)
public class RECodeActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.img_photo)
    ImageView imgPhoto;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.img_re_code)
    ImageView imgReCode;

    @Override
    public int getContentViewId() {
        return R.layout.activity_re_code;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("我的二维码");
        NetService.getInstance().userProfile()
                .compose(this.<UserEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<UserEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }
                    @Override
                    public void onNext(UserEntity userEntity) {
                        GlideUtils.loadImage(RECodeActivity.this,userEntity.getAvatar(),imgPhoto);
                        tvId.setText(DataSharedPreferences.getCreatateAccount());
                        tvName.setText(userEntity.getNikeName());
                        imgReCode.setImageBitmap(ZXingUtils.createQRImage(DataSharedPreferences.getCreatateAccount(),320,320));
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_top_back)
    public void onViewClicked() {
        finish();
    }
}

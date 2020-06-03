package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.VersionEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.user.FeedBackActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.leo.afbaselibrary.utils.download.DownloadApkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_ABOUT;

/**
 * @Created By Admin  on 2020/6/3
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:关于
 */
@Route(path = ACTIVITY_USER_ABOUT)
public class AboutActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_feed_bak)
    TextView tvFeedBak;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.ll_user_privacy)
    LinearLayout llUserPrivacy;

    @Override
    public int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        requestUpdate(false);
        tvVersion.setText(String.format("V%s", AFUtil.getVersion(this)));
    }

    private void requestUpdate(final boolean update) {
        if (update) {
            showProgressDialog(getString(R.string.wait));
        }
        NetService.getInstance().getLatestVersion(this)
                .compose(this.<VersionEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<VersionEntity>() {
                    @Override
                    public void onResultError(ApiException ex) {
                        if (update) {
                            showToast(ex.getDisplayMessage());
                        }
                        hideProgress();
                    }

                    @Override
                    public void onNext(VersionEntity versionEntity) {
                        hideProgress();
                        if (update) {
                            if (versionEntity == null) {
                                ToastUtils.toast(AboutActivity.this, "已是最新版本");
                                return;
                            }
                            DownloadApkUtil.getInstance(AboutActivity.this).isDownloadNewVersion(AboutActivity.this, versionEntity);
                        } else {
                            if (versionEntity == null) {
                                return;
                            }
                            if (versionEntity.getVersionCode() > AFUtil.getVersionCode(AboutActivity.this)) {
                                tvVersion.setSelected(true);
                            } else {
                                tvVersion.setSelected(false);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_top_title, R.id.tv_right, R.id.tv_feed_bak, R.id.tv_version, R.id.ll_version, R.id.ll_user_privacy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_top_title:
                break;
            case R.id.tv_right:
                break;
            case R.id.tv_feed_bak:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.tv_version:
                break;
            case R.id.ll_version:
                requestUpdate(true);
                break;
            case R.id.ll_user_privacy:
                break;
        }
    }
}

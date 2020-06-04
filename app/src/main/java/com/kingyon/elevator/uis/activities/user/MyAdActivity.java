package com.kingyon.elevator.uis.activities.user;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.advertising.AdEditActivity;
import com.kingyon.elevator.uis.dialogs.TipDialog;
import com.kingyon.elevator.uis.dialogs.WechatDialog;
import com.kingyon.elevator.uis.pops.ADOperatePop;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DownloadUtils;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.kingyon.elevator.utils.JumpUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class MyAdActivity extends BaseStateRefreshingLoadingActivity<ADEntity> implements TipDialog.OnOperatClickListener<BaseDownloadTask> {

    @BindView(R.id.pre_v_right)
    TextView preVRight;
    @BindView(R.id.tv_open_third)
    TextView tvOpenThird;
    @BindView(R.id.ll_root)
    View llRoot;

    private boolean choose;
    private String type;
    private WechatDialog wechatDialog;
    private TipDialog<BaseDownloadTask> tipDialog;
    private ADOperatePop adOperatePop;

    @Override
    protected String getTitleText() {
        choose = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_1, false);
        type = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        if (type == null) {
            type = "";
        }
        return (choose && !TextUtils.isEmpty(type)) ? FormatUtils.getInstance().getPlanType(type) : "我的广告";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_ad;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        preVRight.setText("免费制作");
    }

    @Override
    protected MultiItemTypeAdapter<ADEntity> getAdapter() {
        return new BaseAdapter<ADEntity>(this, R.layout.activity_my_ad_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, ADEntity item, int position) {
                FormatUtils.getInstance().updateAdCoverShow(mContext, item
                        , holder.getView(R.id.fl_full_video), (ImageView) holder.getView(R.id.img_full_video)
                        , (ImageView) holder.getView(R.id.img_full_image)
                        , holder.getView(R.id.ll_incise), (ImageView) holder.getView(R.id.img_top), (ImageView) holder.getView(R.id.img_bottom));
                holder.setTextNotHide(R.id.tv_name, item.getTitle());
//                holder.setOnClickListener(R.id.tv_status, new OnClickWithObjects(item) {
//                    @Override
//                    public void onClick(View view, Object[] objects) {
//                        onStatusClick((ADEntity) (objects[0]));
//                    }
//                });
                holder.setTextNotHide(R.id.tv_status, FormatUtils.getInstance().getAdStatus(item.getAdStatus()));
            }
        };
    }

    private void onStatusClick(ADEntity entity) {
//        if (entity != null && TextUtils.equals(Constants.AD_STATUS.REVIEW_FAILED, entity.getAdStatus())) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(CommonUtil.KEY_VALUE_1, true);
        bundle.putParcelable(CommonUtil.KEY_VALUE_2, entity);
        bundle.putString(CommonUtil.KEY_VALUE_3, type);
        startActivityForResult(AdEditActivity.class, CommonUtil.REQ_CODE_1, bundle);
//        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, ADEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            if (choose) {
//                Intent intent = new Intent();
//                intent.putExtra(CommonUtil.KEY_VALUE_1, item);
//                setResult(RESULT_OK, intent);
//                finish();
                RuntimeUtils.adEntity = item;
                MyActivityUtils.goConfirmOrderActivity(MyAdActivity.this, Constants.FROM_TYPE.MYAD, item.getImageUrl(), "");
                finish();
            } else {
                JumpUtils.getInstance().jumpToAdPreview(this, item);
            }
        }
    }

    private ADOperatePop.OnOperateClickLister onOperateClickListeer = new ADOperatePop.OnOperateClickLister() {
        @Override
        public void onEditClick(ADEntity entity) {
            onStatusClick(entity);
        }

        @Override
        public void onDeleteClick(ADEntity entity) {
            showDeleteDialog(entity);
//            onAdDelete(entity);
        }
    };

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, final ADEntity item, int position) {
        if (item != null) {
//            showDeleteDialog(item);
            if (adOperatePop == null) {
                adOperatePop = new ADOperatePop(this, view.findViewById(R.id.aspf));
                adOperatePop.setOnOperateClickLister(onOperateClickListeer);
            }
            adOperatePop.show(llRoot, view.findViewById(R.id.aspf), item);
        }
        return true;
    }

    private void showDeleteDialog(ADEntity item) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("删除以后无法找回，确定要删除它吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onAdDelete(item);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private void onAdDelete(ADEntity item) {
        NetService.getInstance().deleteAd(item.getObjctId())
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("操作成功");
                        autoRefresh();
                    }
                });
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().myAdList(type, page)
                .compose(this.<ConentEntity<ADEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<ADEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(ConentEntity<ADEntity> adEntityPageListEntity) {
                        if (adEntityPageListEntity == null || adEntityPageListEntity.getContent() == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(adEntityPageListEntity.getContent());
                        loadingComplete(true, adEntityPageListEntity.getTotalPages());
                    }
                });
    }

    @OnClick({R.id.pre_v_right, R.id.tv_open_third, R.id.tv_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_right:
                String wechat = getString(R.string.wechat_official_account);
                showWechatDialog(wechat);
                break;
            case R.id.tv_open_third:
                String pakeageName = getString(R.string.hbgc_package_name);
                if (AFUtil.checkApkExist(this, pakeageName)) {
                    AFUtil.openApp(MyAdActivity.this, getString(R.string.hbgc_package_name), getString(R.string.hbgc_class_name));
                } else {
                    showDownloadEnsureDialog();
                }
                break;
            case R.id.tv_create:
                Bundle bundle = new Bundle();
                bundle.putString(CommonUtil.KEY_VALUE_3, type);
                startActivityForResult(AdEditActivity.class, CommonUtil.REQ_CODE_1, bundle);
                break;
        }
    }

    private void showDownloadEnsureDialog() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "hbgc.apk");
        if (file.exists()) {
            DownloadUtils.getInstance().installAPK();
        } else {
            if (tipDialog == null) {
                tipDialog = new TipDialog<>(this, this);
            }
            tipDialog.show("确认要在后台开始下载海报工厂？", "确定", "不了", null);
        }
    }

    @Override
    protected void onResume() {
        tvOpenThird.setText(String.format("海报工厂可以为您制作精美广告,%s>>", AFUtil.checkApkExist(this, getString(R.string.hbgc_package_name)) ? "去看看" : "点击下载"));
        super.onResume();
    }

    @Override
    public void onEnsureClick(BaseDownloadTask param) {
        DownloadUtils.getInstance().downloadAPK(this, getString(R.string.hbgc_download));
    }

    @Override
    public void onCancelClick(BaseDownloadTask param) {

    }

    private void showWechatDialog(String wechat) {
        if (!TextUtils.isEmpty(wechat)) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText(AFUtil.getAppProcessName(this), wechat);
            if (cm != null) {
                cm.setPrimaryClip(mClipData);
                showCopyDialog(wechat);
            }
        } else {
            showToast("没有微信号");
        }
    }

    private void showCopyDialog(String wechatId) {
        if (wechatDialog == null) {
            wechatDialog = new WechatDialog(this);
            wechatDialog.setOnWeChatOpenClickListener(new WechatDialog.OnWeChatOpenClickListener() {
                @Override
                public void onWeChatClick() {
//                    try {
//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
//                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setComponent(cmp);
//                        startActivity(intent);
//                    } catch (ActivityNotFoundException e) {
//                        showToast("检查到您手机没有安装微信，请安装后使用该功能");
//                    }
                    boolean success = AFUtil.openApp(MyAdActivity.this, "com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                    if (!success) {
                        showToast("检查到您手机没有安装微信，请安装后使用该功能");
                    }
                }
            });
        }
        wechatDialog.show(wechatId);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, ScreenUtil.dp2px(10), true));
        return new GridLayoutManager(this, 2);
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode) {
            autoRefresh();
        }
    }
}

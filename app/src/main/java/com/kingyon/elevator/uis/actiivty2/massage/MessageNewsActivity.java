package com.kingyon.elevator.uis.actiivty2.massage;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.czh.myversiontwo.uiutils.TipItem;
import com.czh.myversiontwo.view.TipView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.MassageListMentiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.cooperation.CooperationActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationWithdrawRecordsActivity;
import com.kingyon.elevator.uis.activities.order.OrderDetailsActivity;
import com.kingyon.elevator.uis.activities.user.IdentitySuccessActivity;
import com.kingyon.elevator.uis.activities.user.MyCouponsActivty;
import com.kingyon.elevator.uis.activities.user.MyInvoiceActivity;
import com.kingyon.elevator.uis.dialogs.DeleMassageDialog;
import com.kingyon.elevator.uis.dialogs.popwindow.MassageDelePopWindow;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.ConfirmPopWindow;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.MassageUtils;
import com.kingyon.elevator.view.DialogOnClick;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CONTENT_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VIDEO_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_MSAGGER;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * @Created By Admin  on 2020/4/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:消息
 */
@Route(path = ACTIVITY_MASSAGE_MSAGGER)
public class MessageNewsActivity extends BaseStateRefreshingLoadingActivity<MassageListMentiy> {
    @BindView(R.id.pre_v_back)
    ImageView preVBack;
    @BindView(R.id.pre_v_right)
    TextView preVRight;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.pre_recycler_view)
    RecyclerView preRecyclerView;


    @Override
    protected MultiItemTypeAdapter<MassageListMentiy> getAdapter() {
        return new BaseAdapter<MassageListMentiy>(this, R.layout.itme_message_news, mItems) {
            @Override
            protected void convert(CommonHolder holder, MassageListMentiy item, int position) {
                holder.setText(R.id.tv_conent, item.content);
                holder.setText(R.id.tv_title, item.title);
                holder.setText(R.id.tv_time, item.createTime);
                if (item.isRead == 1) {
                    holder.setVisible(R.id.img_is, false);
                } else {
                    holder.setVisible(R.id.img_is, true);
                }
            }
        };
    }

    @Override
    protected void loadData(int page) {
        NetService.getInstance().getMessageList(page, 20)
                .subscribe(new CustomApiCallback<ConentEntity<MassageListMentiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(ConentEntity<MassageListMentiy> massageListMentiys) {
                        if (massageListMentiys == null || massageListMentiys.getContent() == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(massageListMentiys.getContent());
                        if (page > 1 && massageListMentiys.getContent().size() <= 0) {
                            showToast("已经没有了");
                        }
                        loadingComplete(true, massageListMentiys.getTotalPages());
                    }
                });
    }

    @Override
    protected String getTitleText() {
        return "消息";
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, MassageListMentiy item, int position) {
        super.onItemClick(view, holder, item, position);
        /**
         * ORDER        订单
         * ACQUIRE     获得优惠劵
         * ADVERTIS    广告审核
         * FEEDBACK   反馈收到平台回复
         * PROMOTE    推广获得奖励
         * PERSON        个人认证
         * PARTNER      合伙人认证
         * INVOICE     发票
         * PARTNER_WITHDRAW    合伙人提现审核
         * USER_COMMENT   用户评论
         * USER_LIKE    用户点赞
         * CONTENT_OFFLINE  内容下线
         * CONTENT     内容审核
         * INTEGRAL     积分兑换
         * */
        ImageView imageView = view.findViewById(R.id.img_is);
        switch (item.typeChild) {
            case "ADVERTIS":
            case "ORDER":
                /*订单*/
                Bundle bundle = new Bundle();
                bundle.putString(CommonUtil.KEY_VALUE_1, item.postId);
                startActivity(OrderDetailsActivity.class, bundle);
                break;
            case "ACQUIRE":
                /*获得优惠卷*/
                if (isToken(this)) {
                    startActivity(MyCouponsActivty.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case "FEEDBACK":
                /*反馈平台反馈*/

                break;
            case "PROMOTE":
                /*推广获得奖励*/

                break;
            case "PERSON":
                /*个人认证*/
                if (item.success || item.auth) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("type", Constants.IDENTITY_STATUS.AUTHED);
                    startActivity(IdentitySuccessActivity.class, bundle2);
                } else {
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("type", Constants.IDENTITY_STATUS.FAILD);
                    startActivity(IdentitySuccessActivity.class, bundle3);
                }

                break;
            case "PARTNER":
                /*合伙人认证*/
                if (isToken(this)) {
                    startActivity(CooperationActivity.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case "INVOICE":
                /*发票*/
                if (isToken(this)) {
                    startActivity(MyInvoiceActivity.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }

                break;
            case "PARTNER_WITHDRAW":
                /*合伙人提现审核*/
                if (isToken(this)) {
                    startActivity(CooperationWithdrawRecordsActivity.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case "USER_COMMENT":
                /*用户评论*/
            case "USER_LIKE":
                /*用户点赞*/
                switch (item.contentType) {
                    case "wsq":
                        /*社区*/
                        LogUtils.e(item.postId);
                        ActivityUtils.setActivity(ACTIVITY_MAIN2_CONTENT_DRTAILS,
                                "contentId", Integer.parseInt(item.postId));
                        break;
                    case "article":
                        /*文章*/
                        LogUtils.e(item.postId);
                        ActivityUtils.setActivity(ACTIVITY_MAIN2_ARTICLE_DRTAILS,
                                "contentId", Integer.parseInt(item.postId));
                        break;
                    case "video":
                        /*视频*/
                        LogUtils.e(item.postId);
                        if (item.videoHorizontalVertical == 0) {
                            ActivityUtils.setActivity(ACTIVITY_MAIN2_VIDEO_DRTAILS,
                                    "contentId", Integer.parseInt(item.postId));
                        } else {
                            ActivityUtils.setActivity(ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS,
                                    "contentId", Integer.parseInt(item.postId));
                        }
                        break;

                }
                break;
            case "CONTENT_OFFLINE":
                /*内容下线*/
                break;
            case "CONTENT":
                /*内容审核*/
                if (item.success) {
                    switch (item.contentType) {
                        case "wsq":
                            /*社区*/
                            LogUtils.e(item.postId);
                            ActivityUtils.setActivity(ACTIVITY_MAIN2_CONTENT_DRTAILS,
                                    "contentId", Integer.parseInt(item.postId));
                            break;
                        case "article":
                            /*文章*/
                            LogUtils.e(item.postId);
                            ActivityUtils.setActivity(ACTIVITY_MAIN2_ARTICLE_DRTAILS,
                                    "contentId", Integer.parseInt(item.postId));
                            break;
                        case "video":
                            /*视频*/
                            LogUtils.e(item.postId);
                            if (item.videoHorizontalVertical == 0) {
                                ActivityUtils.setActivity(ACTIVITY_MAIN2_VIDEO_DRTAILS,
                                        "contentId", Integer.parseInt(item.postId));
                            } else {
                                ActivityUtils.setActivity(ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS,
                                        "contentId", Integer.parseInt(item.postId));
                            }
                            break;

                    }
                }
                break;
            case "INTEGRAL":
                /*积分兑换*/

                break;
            default:

        }


        ConentUtils.httpGetMarkRead(String.valueOf(item.id), "MESSAGE", "SINGLE", new IsSuccess() {
            @Override
            public void isSuccess(boolean success) {
                LogUtils.e(success);
                imageView.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, MassageListMentiy item, int position) {
        LogUtils.e("*****************", item.messageId);

//        LinearLayout layout = view.findViewById(R.id.ll_itme_root);
//
//        new MassageDelePopWindow(this).showAtBottom(layout);

        DeleMassageDialog dialog = new DeleMassageDialog(MessageNewsActivity.this);
        dialog.show();
        dialog.setDialogOnClick(new DialogOnClick() {
            @Override
            public void onClick() {
                showProgressDialog(getString(R.string.wait),true);
                dialog.dismiss();
                MassageUtils.httpremoveMsg(String.valueOf(item.id), new IsSuccess() {
                    @Override
                    public void isSuccess(boolean success) {
                        hideProgress();
                        if (success){
                            autoRefresh();
                            showToast("删除成功");
                        }else {
                            showToast("删除失败");
                        }
                    }
                });
            }
        });

        return true;

    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_message_newstwo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        preVRight.setVisibility(View.GONE);

    }

    @OnClick(R.id.pre_v_back)
    public void onViewClicked() {
        finish();
    }

}

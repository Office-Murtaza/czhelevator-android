package com.kingyon.elevator.uis.actiivty2.massage;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.MassagePushEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.WebViewActivity;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VIDEO_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MESSAGE_PUSH;
import static com.czh.myversiontwo.utils.Constance.WEB_ACTIVITY;

/**
 * @Created By Admin  on 2020/6/1
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 机器人推送消息
 */
@Route(path = ACTIVITY_MESSAGE_PUSH)
public class MessagePushActivity extends BaseStateRefreshingLoadingActivity<MassagePushEntiy> {
    @Autowired
    int robotId;
    @Autowired
    String name;
    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.pre_v_right)
    TextView preVRight;

    @Override
    protected MultiItemTypeAdapter<MassagePushEntiy> getAdapter() {
        return new BaseAdapter<MassagePushEntiy>(this, R.layout.itme_message_push, mItems) {
            @Override
            protected void convert(CommonHolder holder, MassagePushEntiy item, int position) {
                holder.setText(R.id.tv_content, item.pushContent);
                holder.setText(R.id.tv_name, item.robotName);
                holder.setText(R.id.tv_time, item.pushTime);
                holder.setText(R.id.tv_title, item.pushTitle);
                TextView textView = new TextView(MessagePushActivity.this);
                textView = holder.getView(R.id.tv_content);
                if (item.isRead==1){
                    holder.setVisible(R.id.img_is,false);
                }else {
                    holder.setVisible(R.id.img_is,true);
                }
                switch (item.msgType) {
                    case "MSG":
                        /*系统消息*/
                        holder.setVisible(R.id.tv_activity, false);
                        holder.setVisible(R.id.tv_qw, false);
                        holder.setVisible(R.id.tv_xd, false);
                        holder.setVisible(R.id.tv_content, true);
                        holder.setVisible(R.id.tv_mag_image, false);
                        break;
                    case "ARTICLE":
                        /*文章*/
                        holder.setVisible(R.id.tv_activity, true);
                        holder.setText(R.id.tv_activity, "文章");
                        holder.setVisible(R.id.tv_content, true);
                        holder.setVisible(R.id.tv_mag_image, false);
                        holder.setVisible(R.id.tv_qw, true);
                        holder.setVisible(R.id.tv_xd, true);
                        textView.setMaxLines(3);
                        textView.setEllipsize(TextUtils.TruncateAt.END);
                        break;
                    case "VIDEO":
                        /*视频*/
                        holder.setVisible(R.id.tv_activity, true);
                        holder.setText(R.id.tv_activity, "视频");
                        holder.setVisible(R.id.tv_content, true);
                        holder.setVisible(R.id.tv_mag_image, false);
                        holder.setVisible(R.id.tv_qw, true);
                        holder.setVisible(R.id.tv_xd, true);
                        textView.setMaxLines(3);
                        textView.setEllipsize(TextUtils.TruncateAt.END);
                        break;
                    case "ACTIVITY":
                        /*活动*/
                        holder.setVisible(R.id.tv_activity, true);
                        holder.setText(R.id.tv_activity, "活动");
                        holder.setVisible(R.id.tv_content, true);
                        holder.setVisible(R.id.tv_mag_image, false);
                        holder.setVisible(R.id.tv_qw, true);
                        holder.setVisible(R.id.tv_xd, true);
                        textView.setMaxLines(3);
                        textView.setEllipsize(TextUtils.TruncateAt.END);
                        break;
                    case "H5":
                        holder.setVisible(R.id.tv_mag_image, true);
                        holder.setVisible(R.id.tv_content, false);
                        holder.setVisible(R.id.tv_activity, false);
                        holder.setVisible(R.id.tv_qw, false);
                        holder.setVisible(R.id.tv_xd, false);
                        GlideUtils.loadRoundCornersImage(MessagePushActivity.this, item.msgImage, holder.getView(R.id.tv_mag_image), 20);
                        /*H5*/
                        break;
                }
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, MassagePushEntiy item, int position) {
        super.onItemClick(view, holder, item, position);
        ImageView imageView = view.findViewById(R.id.img_is);
        ConentUtils.httpGetMarkRead(String.valueOf(item.pushId), "PUSH_MSG", "SINGLE", new IsSuccess() {
            @Override
            public void isSuccess(boolean success) {
                LogUtils.e(success);
                imageView.setVisibility(View.GONE);
            }
        });
        switch (item.msgType) {
            case "MSG":
                /*系统消息*/
                break;
            case "ARTICLE":
                /*文章*/
                LogUtils.e(item.msgParams);
                ActivityUtils.setActivity(ACTIVITY_MAIN2_ARTICLE_DRTAILS, "contentId", Integer.parseInt(item.msgParams));
                break;
            case "VIDEO":
                /*视频*/
                LogUtils.e(item.msgParams);
                if (item.videoHorizontalVertical.equals("0")) {
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VIDEO_DRTAILS, "contentId", Integer.parseInt(item.msgParams));
                } else {
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS, "contentId", Integer.parseInt(item.msgParams));
                }
                break;
            case "ACTIVITY":
                /*活动*/
                showToast("活动正在开发，敬请期待");
                break;

            case "H5":
                LogUtils.e(item.msgParams);
                ActivityUtils.setActivity(WEB_ACTIVITY, "title", item.pushTitle, "content", item.msgParams, "type", "url");

                break;
        }
    }

    @Override
    protected void loadData(int page) {
        NetService.getInstance().getPushMagList(robotId, page, 20)
                .subscribe(new CustomApiCallback<ConentEntity<MassagePushEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(ConentEntity<MassagePushEntiy> massageListMentiys) {
                        preTvTitle.setText(massageListMentiys.getContent().get(0).robotName);
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
        return name;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_message_push;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        preVRight.setVisibility(View.GONE);
//        preTvTitle.setText(name+"");
    }

    @OnClick(R.id.pre_v_back)
    public void onViewClicked() {
        finish();
    }
}

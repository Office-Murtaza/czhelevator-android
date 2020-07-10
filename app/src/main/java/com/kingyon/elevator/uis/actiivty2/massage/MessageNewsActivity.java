package com.kingyon.elevator.uis.actiivty2.massage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.MassageListMentiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_MSAGGER;

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

    @Override
    protected MultiItemTypeAdapter<MassageListMentiy> getAdapter() {
        return new BaseAdapter<MassageListMentiy>(this,R.layout.itme_message_news,mItems) {
            @Override
            protected void convert(CommonHolder holder, MassageListMentiy item, int position) {
                holder.setText(R.id.tv_conent,item.content);
                holder.setText(R.id.tv_title,item.title);
                holder.setText(R.id.tv_time,"14-45");
            }
        };
    }

    @Override
    protected void loadData(int page) {
        NetService.getInstance().getMessageList(page, 20)
                .subscribe(new CustomApiCallback<List<MassageListMentiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(List<MassageListMentiy> massageListMentiys) {
                        if (massageListMentiys == null ) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(massageListMentiys);
                        if (page>1&&massageListMentiys.size()<=0){
                            showToast("已经没有了");
                        }
                        loadingComplete(true, 100000);
                    }
                });
    }

    @Override
    protected String getTitleText() {
        return "消息";
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

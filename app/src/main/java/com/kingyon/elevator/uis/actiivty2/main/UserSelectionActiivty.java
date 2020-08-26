package com.kingyon.elevator.uis.actiivty2.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.czh.myversiontwo.utils.EditTextUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.input.adapter.UserAdapter;
import com.kingyon.elevator.uis.actiivty2.user.SearchAttentionUserActivity;
import com.kingyon.elevator.uis.dialogs.NotAttentionDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.ATTENTION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_CENTER;

/**
 * @Created By Admin  on 2020/5/19
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:用户选择
 */
public class UserSelectionActiivty extends BaseStateRefreshingLoadingActivity<AttenionUserEntiy> {
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.tv_bake)
    TextView tvBake;
    public static final String RESULT_USER = "RESULT_USER";
    String extend;
    String keyWords = "";
    @Override
    public int getContentViewId() {
        return R.layout.activity_search_attention_user;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyWords = s.toString();
                if (keyWords.length()>0) {
                    httpDataAll(1, keyWords);
                }else {
                    autoRefresh();
                }
            }
        });
    }

    private void httpDataAll(int page, String keyWords) {
        NetService.getInstance().getMatching(page, keyWords)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<AttenionUserEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }
                    @Override
                    public void onNext(ConentEntity<AttenionUserEntiy> attenionUserEntiyConentEntity) {
                        if (attenionUserEntiyConentEntity == null||attenionUserEntiyConentEntity.getContent()==null ) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(attenionUserEntiyConentEntity.getContent());
                        if (page>1&&attenionUserEntiyConentEntity.getContent().size()<=0){
                            showToast("已经没有了");
                        }
                        loadingComplete(true, attenionUserEntiyConentEntity.getTotalPages());
                    }
                });
    }


    @Override
    protected String getTitleText() {
        return "关注";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EditTextUtils.setEditTextInhibitInputSpace(editSearch);
    }

    @OnClick({R.id.tv_bake})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bake:
                finish();
                break;
        }
    }

    @Override
    protected MultiItemTypeAdapter<AttenionUserEntiy> getAdapter() {
        return new BaseAdapter<AttenionUserEntiy>(this,R.layout.itme_user_attent,mItems) {
            @Override
            protected void convert(CommonHolder holder, AttenionUserEntiy item, int position) {
                GlideUtils.loadCircleImage(UserSelectionActiivty.this,item.photo,holder.getView(R.id.image_photo));
                holder.setText(R.id.tv_name,item.nickname);
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, AttenionUserEntiy item, int position) {
        super.onItemClick(view, holder, item, position);
        setResult1(item);
    }

    @Override
    protected void loadData(int page) {
        LogUtils.e(page);
        if (keyWords.length()>0){
            httpDataAll(page, keyWords);
        }else {
            httpData(page, ATTENTION, extend);
        }
    }

    private void httpData(int page,String handlerType,String extend) {
        NetService.getInstance().setAttention(page,handlerType,extend)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<AttenionUserEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);

                    }
                    @Override
                    public void onNext(ConentEntity<AttenionUserEntiy> attenionUserEntiyConentEntity) {
                        if (attenionUserEntiyConentEntity == null||attenionUserEntiyConentEntity.getContent()==null ) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(attenionUserEntiyConentEntity.getContent());
                        if (page>1&&attenionUserEntiyConentEntity.getContent().size()<=0){
                            showToast("已经没有了");
                        }
                        loadingComplete(true, attenionUserEntiyConentEntity.getTotalPages());
                    }
                });

    }
    public static Intent getIntent(Activity activity) {
        return new Intent(activity, UserSelectionActiivty.class);
    }

    private void setResult1(AttenionUserEntiy user) {
        Intent intent = getIntent();
        intent.putExtra(RESULT_USER, user);
        setResult(RESULT_OK, intent);
        finish();
    }

}

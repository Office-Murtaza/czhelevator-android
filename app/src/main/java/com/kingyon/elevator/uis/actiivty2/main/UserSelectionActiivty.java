package com.kingyon.elevator.uis.actiivty2.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.input.adapter.UserAdapter;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.ATTENTION;

/**
 * @Created By Admin  on 2020/5/19
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:用户选择
 */
public class UserSelectionActiivty extends BaseActivity {
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.tv_bake)
    TextView tvBake;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private UserSelectionActiivty mUserSelection;
    UserAdapter mUserAdapter;
    ConentEntity<AttenionUserEntiy> result = new ConentEntity<>();
    public static final String RESULT_USER = "RESULT_USER";

    public static Intent getIntent(Activity activity) {
        return new Intent(activity, UserSelectionActiivty.class);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_uesr_selection;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mUserSelection = this;
        initView();
        LogUtils.e();
    }

    private void initView() {
        provideData(1,ATTENTION);

    }
    private void provideData(int page,String handlerType) {
        NetService.getInstance().setAttention(page,handlerType,"")
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<AttenionUserEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showToast(UserSelectionActiivty.this,ex.getDisplayMessage(),1000);
                    }
                    @Override
                    public void onNext(ConentEntity<AttenionUserEntiy> attenionUserEntiyConentEntity) {
                        LogUtils.e(attenionUserEntiyConentEntity.getContent().toString());
                        result = attenionUserEntiyConentEntity;

                        recycler.setLayoutManager(new LinearLayoutManager(mUserSelection));
                        mUserAdapter = new UserAdapter(UserSelectionActiivty.this,result);
                        recycler.setAdapter(mUserAdapter);

                        mUserAdapter.setOnItemClickListener(new UserAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                ConentEntity<AttenionUserEntiy> data = mUserAdapter.getData();
                                AttenionUserEntiy user = data.getContent().get(position);
                                setResult1(user);
                            }
                        });
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_bake)
    public void onViewClicked() {
        finish();
    }
    private void setResult1(AttenionUserEntiy user) {
        Intent intent = getIntent();
        intent.putExtra(RESULT_USER, user);
        setResult(RESULT_OK, intent);
        finish();
    }

}

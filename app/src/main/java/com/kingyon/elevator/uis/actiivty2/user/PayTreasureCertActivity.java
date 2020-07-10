package com.kingyon.elevator.uis.actiivty2.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.utils.EditTextUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.entities.PlanNumberEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;

import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_PAYTREASURECERT;
import static com.kingyon.elevator.uis.actiivty2.user.CertificationActivity.certificationActivity;

/**
 * @Created By Admin  on 2020/6/12
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:支付宝认证
 */
@Route(path = ACTIVITY_PAYTREASURECERT)
public class PayTreasureCertActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.tv_rz)
    TextView tvRz;

    @Override
    public int getContentViewId() {
        return R.layout.activity_pay_treasurecer;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        EditTextUtils.setEditTextInhibitInputSpace(etName);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            String name = uri.getQueryParameter("id");
            String scheme = uri.getScheme();
            String host = uri.getHost();
            String port = uri.getPort() + "";
            String path = uri.getPath();
            String query = uri.getQuery();
            LogUtils.e("获得的数据name=" + name + "==" + "scheme" + scheme + "==" + "host" +
                    "host" + host + "==" + "port" + port + "==" + "path" + path + "===" + "query" + query);

            LogUtils.e(DataSharedPreferences.getCertifyId()+"====");
            showProgressDialog("请稍后...");
            NetService.getInstance().setAliAuthQuery(DataSharedPreferences.getCertifyId())
                    .compose(this.bindLifeCycle())
                    .subscribe(new CustomApiCallback<String>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                            showToast(ex.getDisplayMessage());
                            hideProgress();
                        }

                        @Override
                        public void onNext(String s) {
                            LogUtils.e(s);
                            finish();
                            certificationActivity.finish();
                            hideProgress();
                        }
                    });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_rz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_rz:
                httpVerify();
                break;
        }
    }

    private void httpVerify() {
        if (etName.getText().toString().isEmpty()||etNumber.getText().toString().isEmpty()){
            ToastUtils.showToast(this,"姓名身份证不能为空",1000);
        }else {
            showProgressDialog("请稍后...");
            NetService.getInstance().setAliIdentityAuth(etName.getText().toString(),etNumber.getText().toString())
                    .compose(this.bindLifeCycle())
                    .subscribe(new CustomApiCallback<PlanNumberEntiy>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            ToastUtils.showToast(PayTreasureCertActivity.this,ex.getDisplayMessage(),1000);
                            hideProgress();
                        }

                        @Override
                        public void onNext(PlanNumberEntiy planNumberEntiy) {
                            LogUtils.e(planNumberEntiy.certifyUrl,planNumberEntiy.certifyId);
                            DataSharedPreferences.saveCertifyId(planNumberEntiy.certifyId);
                            doVerify(planNumberEntiy.certifyUrl);
                            hideProgress();
                        }
                    });
        }
    }

    /**
     * 启动支付宝进行认证
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            StringBuilder builder = new StringBuilder();
            // 这里使用固定
            builder.append("alipays://platformapi/startapp?appId=20000067&url=");
            builder.append(URLEncoder.encode(url));
            action.setData(Uri.parse(builder.toString()));
            startActivity(action);
            finish();
        } else {
            // 处理没有安装支付宝的情况
            new AlertDialog.Builder(this)
                    .setMessage("是否下载并安装支付宝完成认证?")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            startActivity(action);
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /**
     * 判断是否安装了支付宝
     * @return true 为已经安装
     */
    private boolean hasApplication() {
        PackageManager manager = getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }

}

package com.kingyon.elevator.uis.activities.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.InvoiceInfoEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.dialogs.InvoiceSuccessDialog;
import com.kingyon.elevator.uis.widgets.DecimalDigitsInputFilter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateLoadingActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class InvoiceApplyActivity extends BaseStateLoadingActivity {
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_person)
    TextView tvPerson;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_taxpayer)
    EditText etTaxpayer;
    @BindView(R.id.et_bank)
    EditText etBank;
    @BindView(R.id.ll_company_info)
    LinearLayout llCompanyInfo;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.et_sum)
    EditText etSum;
    @BindView(R.id.tv_max)
    TextView tvMax;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private InvoiceSuccessDialog successDialog;
    private Double maxInvoice;

    @Override
    protected String getTitleText() {
        return "开具发票";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_invoice_apply;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        etSum.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(this,10,true,"发票金额超过限制值，请重新输入")});
        updateType(tvCompany);
    }

    @Override
    protected void loadData() {
        NetService.getInstance().invoiceInfo()
                .compose(this.<InvoiceInfoEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<InvoiceInfoEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(InvoiceInfoEntity invoiceInfoEntity) {
                        if (invoiceInfoEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        maxInvoice = invoiceInfoEntity.getWaitting();
                        tvMax.setText(String.format("最多可开具￥%s", CommonUtil.getMayTwoFloat(invoiceInfoEntity.getWaitting())));
                        loadingComplete(STATE_CONTENT);
                    }
                });
    }

    @OnClick({R.id.tv_company, R.id.tv_person, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_company:
            case R.id.tv_person:
                updateType(view);
                break;
            case R.id.tv_submit:
                onSubmitClick();
                break;
        }
    }

    private void onSubmitClick() {
        if (!tvCompany.isSelected() && !tvPerson.isSelected()) {
            showToast("请选择发票类型");
            return;
        }
        String invoiceType = tvCompany.isSelected() ? Constants.INVOICE_TYPE.COMPANY : Constants.INVOICE_TYPE.PERSON;
        boolean companyType = TextUtils.equals(Constants.INVOICE_TYPE.COMPANY, invoiceType);
        if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
            showToast("请填写发票抬头");
            return;
        }
        if (companyType && TextUtils.isEmpty(CommonUtil.getEditText(etTaxpayer))) {
            showToast("请填写税号");
            return;
        }
//        if (companyType && TextUtils.isEmpty(CommonUtil.getEditText(etBank))) {
//            showToast("请填写开户行");
//            return;
//        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etContent))) {
            showToast("请填写发票内容");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etSum))) {
            showToast("请填写开票金额");
            return;
        }
        Float sum;
        try {
            sum = Float.parseFloat(etSum.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            sum = 0f;
        }
        if (sum <= 0) {
            showToast("输入的开票金额不正确");
            return;
        }

        if (maxInvoice != null && sum > maxInvoice) {
            showToast(String.format("最多只能开具￥%s", CommonUtil.getTwoFloat(maxInvoice)));
            return;
        }

        if (TextUtils.isEmpty(CommonUtil.getEditText(etEmail))) {
            showToast("请填写电子邮箱");
            return;
        }
        KeyBoardUtils.closeKeybord(this);
        tvSubmit.setEnabled(false);
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().createInvoice(invoiceType, etName.getText().toString()
                , companyType ? etTaxpayer.getText().toString() : "", companyType ? etBank.getText().toString() : ""
                , sum, etEmail.getText().toString(), etContent.getText().toString())
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                        tvSubmit.setEnabled(true);
                    }

                    @Override
                    public void onNext(String s) {
                        hideProgress();
                        showSuccessDialog();
                    }
                });
    }

    private void showSuccessDialog() {
        if (successDialog == null) {
            successDialog = new InvoiceSuccessDialog(this);
            successDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
//                    loadData();
//                    etSum.setText("");
//                    etSum.setSelection(etSum.getText().length());
//                    tvSubmit.setEnabled(true);
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
        successDialog.show();
    }

    private void updateType(View view) {
        tvCompany.setSelected(view == tvCompany);
        tvPerson.setSelected(view == tvPerson);
        llCompanyInfo.setVisibility(tvCompany.isSelected() ? View.VISIBLE : View.GONE);
    }
}

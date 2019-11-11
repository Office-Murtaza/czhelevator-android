package com.kingyon.elevator.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

import com.kingyon.elevator.entities.InputEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2017/10/27.
 * Email：lc824767150@163.com
 */

public class InputActivity extends BaseSwipeBackActivity {

    @BindView(R.id.et_content)
    EditText etContent;

    private InputEntity entity;

    public static void start(BaseActivity activity, int requestCode, InputEntity entity) {
        if (entity == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        activity.startActivityForResult(InputActivity.class, requestCode, bundle);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_input;
    }

    @Override
    protected String getTitleText() {
        entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        return entity.getTitle();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(entity.getMaxLength() == 0 ? Integer.MAX_VALUE : entity.getMaxLength())}); //最大输入长度
        etContent.setHint(entity.getHint());
        etContent.setText(entity.getText() == null ? "" : entity.getText());
        if (entity.getInputType() != InputType.TYPE_TEXT_FLAG_MULTI_LINE) {
            etContent.setInputType(entity.getInputType());
        }
        etContent.setMinLines(entity.getShowLines() > 1 ? entity.getShowLines() : 1);
        etContent.setSelection(entity.getText() == null ? 0 : entity.getText().length());
    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etContent))) {
            showToast("还没有输入任何内容");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(CommonUtil.KEY_VALUE_1, etContent.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}

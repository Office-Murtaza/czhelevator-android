package com.kingyon.elevator.uis.activities.inputcomment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class InCommunityActivity extends Activity {
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.img_aite)
    ImageView imgAite;
    @BindView(R.id.img_huati)
    ImageView imgHuati;
    @BindView(R.id.tv_zishu)
    TextView tvZishu;
    private static EditorCallback mEditorCallback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_community);
        ButterKnife.bind(this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        mEditorCallback.onAttached((ViewGroup) getWindow().getDecorView());

    }
    public static void openEditor(Context context, EditorCallback editorCallback) {
        Intent intent = new Intent(context, InCommunityActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mEditorCallback = editorCallback;
        context.startActivity(intent);
    }
    @OnClick({R.id.img_icon, R.id.img_aite, R.id.img_huati})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_icon:

                break;
            case R.id.img_aite:

                break;
            case R.id.img_huati:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        mEditorCallback.onCancel();
        super.onDestroy();
    }
}

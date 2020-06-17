package com.kingyon.elevator.uis.activities.inputcomment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 输入框弹出界面
 */
public class InputCommentActivity extends Activity {

    private static EditorCallback mEditorCallback;
    @BindView(R.id.input_comment)
    EditText input_comment;
    @BindView(R.id.btn_send)
    TextView btn_send;
    @BindView(R.id.iv_dianzan)
    ImageView iv_dianzan;
    @BindView(R.id.iv_share_news)
    ImageView iv_share_news;
    @BindView(R.id.img_icon)
    ImageView img_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_comment);
        ButterKnife.bind(this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        mEditorCallback.onAttached((ViewGroup) getWindow().getDecorView());

    }

    public static void openEditor(Context context, EditorCallback editorCallback) {
        Intent intent = new Intent(context, InputCommentActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mEditorCallback = editorCallback;
        context.startActivity(intent);
    }




    @OnClick({R.id.btn_send, R.id.iv_dianzan, R.id.iv_share_news,R.id.img_icon})
    public void OnClick(View view) {
            switch (view.getId()) {
                case R.id.btn_send:
                    if (input_comment.getText().toString().trim().isEmpty()) {
                        ToastUtils.showShort("请输入评论内容");
                        return;
                    }
                    mEditorCallback.onSubmit(input_comment.getText().toString().trim());
                    finish();
                    break;
                case R.id.iv_dianzan:

                    break;
                case R.id.iv_share_news:

                    break;
                case R.id.img_icon:
                    mEditorCallback.onIcon();

                    break;
            }

    }


    @Override
    protected void onDestroy() {
        mEditorCallback.onCancel();
        super.onDestroy();
    }
}

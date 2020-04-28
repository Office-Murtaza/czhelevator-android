package com.kingyon.elevator.uis.actiivty2.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.ArticleEntity;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_RELEASETY;


/**
 * Created By Admin  on 2020/4/17
 * Email : 163235610@qq.com
 *
 * @Author:Mrczh Instructions: 富文本编辑
 */
@Route(path = ACTIVITY_MAIN2_ARTICLE_RELEASETY)
public class ArticleReleaseActivity extends MvpBaseActivity {


    @BindView(R.id.img_bake)
    ImageView imgBake;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_releaset)
    TextView tvReleaset;
    @BindView(R.id.editor)
    RichEditor mEditor;
    private boolean isChanged;

    @Override
    public ArticleEntity initPresenter() {
        return new ArticleEntity(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artice_release);
        ButterKnife.bind(this);

        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("请输入文章内容");
        //mEditor.setInputEnabled(false);

        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
//                preview.setText(text);
                LogUtils.e(text);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.img_bake)
    public void onViewClicked() {
        finish();
    }

//
//    @OnClick({R.id.action_undo, R.id.action_redo, R.id.action_bold, R.id.action_italic, R.id.action_subscript, R.id.action_superscript, R.id.action_strikethrough, R.id.action_underline, R.id.action_heading1, R.id.action_heading2, R.id.action_heading3, R.id.action_heading4, R.id.action_heading5, R.id.action_insert_checkbox
//            , R.id.action_heading6, R.id.action_txt_color, R.id.action_bg_color, R.id.action_indent, R.id.action_outdent, R.id.action_align_left, R.id.action_align_center, R.id.action_align_right, R.id.action_insert_bullets, R.id.action_insert_numbers, R.id.action_blockquote, R.id.action_insert_image, R.id.action_insert_link})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.action_undo:
//                mEditor.undo();
//                break;
//            case R.id.action_redo:
//                mEditor.redo();
//                break;
//            case R.id.action_bold:
//                mEditor.setBold();
//                break;
//            case R.id.action_italic:
//                mEditor.setItalic();
//                break;
//            case R.id.action_subscript:
//                mEditor.setSubscript();
//                break;
//            case R.id.action_superscript:
//                mEditor.setSuperscript();
//                break;
//            case R.id.action_strikethrough:
//                mEditor.setStrikeThrough();
//                break;
//            case R.id.action_underline:
//                mEditor.setUnderline();
//                break;
//            case R.id.action_heading1:
//                mEditor.setHeading(1);
//                break;
//            case R.id.action_heading2:
//                mEditor.setHeading(2);
//                break;
//            case R.id.action_heading3:
//                mEditor.setHeading(3);
//                break;
//            case R.id.action_heading4:
//                mEditor.setHeading(4);
//                break;
//            case R.id.action_heading5:
//                mEditor.setHeading(5);
//                break;
//            case R.id.action_insert_checkbox:
//                mEditor.insertTodo();
//                break;
//            case R.id.action_heading6:
//                mEditor.setHeading(6);
//                break;
//            case R.id.action_txt_color:
//                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
//                isChanged = !isChanged;
//                break;
//            case R.id.action_bg_color:
//                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
//                isChanged = !isChanged;
//                break;
//            case R.id.action_indent:
//                mEditor.setIndent();
//                break;
//            case R.id.action_outdent:
//                mEditor.setOutdent();
//                break;
//            case R.id.action_align_left:
//                mEditor.setAlignLeft();
//                break;
//            case R.id.action_align_center:
//                mEditor.setAlignCenter();
//                break;
//            case R.id.action_align_right:
//                mEditor.setAlignRight();
//                break;
//            case R.id.action_insert_bullets:
//                mEditor.setBullets();
//                break;
//            case R.id.action_insert_numbers:
//                mEditor.setNumbers();
//                break;
//            case R.id.action_blockquote:
//                mEditor.setBlockquote();
//                break;
//            case R.id.action_insert_image:
//                mEditor.insertImage("https://dss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3386247472,87720242&fm=26&gp=0.jpg",
//                        "测试图片");
//                break;
//            case R.id.action_insert_link:
//                mEditor.insertLink("https://www.baidu.com", "百度");
//
//                break;
//            default:
//        }
//    }
}

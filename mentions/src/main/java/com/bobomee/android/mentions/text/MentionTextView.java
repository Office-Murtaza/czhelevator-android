package com.bobomee.android.mentions.text;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.bobomee.android.mentions.edit.MentionEditText;
import com.bobomee.android.mentions.edit.listener.InsertData;
import com.bobomee.android.mentions.model.FormatRange;
import com.bobomee.android.mentions.text.listener.ParserConverter;

/**
 * @Created By Admin  on 2020/4/29
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MentionTextView extends TextView {

  private CharSequence mOriginalText;

  public MentionTextView(Context context) {
    this(context, null);
  }

  public MentionTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MentionTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public MentionTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override public void setText(CharSequence text, BufferType type) {
    mOriginalText = text;
    if (!TextUtils.isEmpty(text) && null != mParserConverter) {
      text = mParserConverter.convert(text);
    }
    text = wrapper(text);
    setLinkTextColor(0xFF4DACEE);
    super.setText(text, type);
  }

  @Override
  public void setTextColor(int color) {
    super.setTextColor(color);
  }

  public CharSequence wrapper(CharSequence text) {
    return text;
  }

  private ParserConverter mParserConverter;

  public void setParserConverter(ParserConverter parserConverter) {
    mParserConverter = parserConverter;
  }

  public CharSequence getOriginalText() {
    return mOriginalText;
  }
//  public void insert(CharSequence charSequence) {
//    insert(new Default(charSequence));
//  }

  class Default implements InsertData {

    private final CharSequence charSequence;

    public Default(CharSequence charSequence) {
      this.charSequence = charSequence;
    }

    @Override
    public CharSequence charSequence() {
      return charSequence;
    }

    @Override
    public FormatRange.FormatData formatData() {
      return new DEFAULT();
    }

    @Override
    public int color() {
      return 0xFF4DACEE;
    }
    class DEFAULT implements FormatRange.FormatData {
      @Override public CharSequence formatCharSequence() {
        return charSequence;
      }
    }
  }


}

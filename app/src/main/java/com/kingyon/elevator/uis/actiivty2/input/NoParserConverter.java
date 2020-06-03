package com.kingyon.elevator.uis.actiivty2.input;

import android.text.SpannableString;
import android.text.Spanned;

import com.bobomee.android.mentions.text.listener.ParserConverter;

/**
 * Created By Admin  on 2020/4/28
 * Email : 163235610@qq.com
 * @Author:Mrczh
 * Instructions:
 */
public class NoParserConverter implements ParserConverter {

  @Override public Spanned convert(CharSequence source) {
    return new SpannableString(source);
  }
}

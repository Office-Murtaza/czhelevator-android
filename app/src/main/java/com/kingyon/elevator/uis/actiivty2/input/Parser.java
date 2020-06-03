package com.kingyon.elevator.uis.actiivty2.input;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import com.bobomee.android.mentions.text.listener.ParserConverter;
import com.kingyon.elevator.uis.actiivty2.input.utils.LinkUtil;

/**
 * Created By Admin  on 2020/4/28
 * Email : 163235610@qq.com
 * @Author:Mrczh
 * Instructions:
 */
public class Parser implements ParserConverter {

  public Parser() {
  }

  @Override public Spanned convert(CharSequence source) {
    if (TextUtils.isEmpty(source)) return new SpannableString("");
    String sourceString = source.toString();
    sourceString = LinkUtil.replaceUrl(sourceString);

    return Html.fromHtml(sourceString, null, new HtmlTagHandler());
  }
}

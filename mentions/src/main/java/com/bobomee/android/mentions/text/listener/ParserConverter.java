package com.bobomee.android.mentions.text.listener;

import android.text.Spanned;

/**
 * @Created By Admin  on 2020/4/29
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public interface ParserConverter {

  Spanned convert(CharSequence source);
}

package com.bobomee.android.mentions.edit.listener;

import com.bobomee.android.mentions.model.FormatRange;

/**
 * @Created By Admin  on 2020/4/29
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public interface InsertData {

  CharSequence charSequence();

  FormatRange.FormatData formatData();

  int color();
}

package com.kingyon.elevator.uis.actiivty2.input.utils;

import com.koudai.styletextview.RichTextView;
import com.koudai.styletextview.styledata.ITextStylePhraseAgent;
import com.koudai.styletextview.textstyle.TextStylePhrase;
import com.koudai.styletextview.utils.WeiBoUrlLinkTextStyleUtils;

import java.util.regex.Pattern;

/**
 * Created By Admin  on 2020/4/28
 * Email : 163235610@qq.com
 * @Author:Mrczh
 * Instructions:
 */
public class TextStylePhraseAgentImp implements ITextStylePhraseAgent {

    private String mLinkText;
    private int mHighlightColorId;
    private Pattern mWebUrlPattern;
    private WeiBoUrlLinkTextStyleUtils.OnLikeWeiboLinkTextClickListener mOnLikeWeiboLinkTextClickListener;

    @Override
    public TextStylePhrase createTextStylePhrase(String content) {
        WeiBoUrlLinkTextStyleUtils mWeiBoUrlLinkTextStyleUtils = new WeiBoUrlLinkTextStyleUtils(content);

        mWeiBoUrlLinkTextStyleUtils.setLinkText(mLinkText); // 设置替换文案
        mWeiBoUrlLinkTextStyleUtils.setHighlightColorId(mHighlightColorId); // 设置高亮颜色
        mWeiBoUrlLinkTextStyleUtils.setWebUrlPattern(mWebUrlPattern); // 设置匹配规则
        mWeiBoUrlLinkTextStyleUtils.setOnLikeWeiboLinkTextClickListener(mOnLikeWeiboLinkTextClickListener); // 设置点击事件

        TextStylePhrase textStylePhrase = mWeiBoUrlLinkTextStyleUtils.getLikeWeiboLinkTextStylePhrase();
        if (textStylePhrase != null){
            return textStylePhrase;
        }
        return new TextStylePhrase(content);
    }

    @Override
    public void setExternalStylePhraseData(RichTextView.IExternalStylePhraseData iExternalStylePhraseData) {

    }

    public String getLinkText() {
        return mLinkText;
    }

    public void setLinkText(String linkText) {
        this.mLinkText = linkText;
    }

    public int getHighlightColorId() {
        return mHighlightColorId;
    }

    public void setHighlightColorId(int highlightColorId) {
        this.mHighlightColorId = highlightColorId;
    }

    public Pattern getWebUrlPattern() {
        return mWebUrlPattern;
    }

    public void setWebUrlPattern(Pattern webUrlPattern) {
        this.mWebUrlPattern = webUrlPattern;
    }

    public WeiBoUrlLinkTextStyleUtils.OnLikeWeiboLinkTextClickListener getOnLikeWeiboLinkTextClickListener() {
        return mOnLikeWeiboLinkTextClickListener;
    }

    public void setOnLikeWeiboLinkTextClickListener(WeiBoUrlLinkTextStyleUtils.OnLikeWeiboLinkTextClickListener onLikeWeiboLinkTextClickListener) {
        this.mOnLikeWeiboLinkTextClickListener = onLikeWeiboLinkTextClickListener;
    }
}

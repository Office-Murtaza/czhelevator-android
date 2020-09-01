package com.czh.myversiontwo.utils;

/**
 * @Created By Admin  on 2020/5/27
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class StringContent {

    public static String STRING_PRICE = "低至   ¥ <font color=\"#FF3049\"><big>%s</big></font> 元/台/天";

    public static String STRING_COMMUNITY_CODE = "%s单元   |   %s电梯";

    public static String STRING_PRICE1 = "¥ <font color=\"#FF3049\"><big>%s</big></font> 元/台/天 ";

    public static String STRING_PRICE2 = "<span style=\"color: #333; font-size: 14px; font-weight: normal;\">合计:</span>¥ <span style=\"color: #ff3049; font-size: 14px;\"><big>%s</big></span> 元";

    public static String AD_ZQSTRING = "%s 点赞    %s 评论    %s播放";

    public static String ORADER_NUMBER = "数量：%s      合计：¥%s";

    public static String ATTENTION_TO_FANS = "关注   %s     |   粉丝   %s";

    public static String REVIEW_STATUS1 = "审核状态: <font color=\"#2D6EF2\"><big>%s</big></font>";

    public static String REVIEW_STATUS2 = "审核状态: <font color=\"#FF3049\"><big>%s</big></font>";

    public static String REVIEW_STATUS3 = "审核状态: <font color=\"#6BAB44\"><big>%s</big></font>";


    /**
     * 加载html标签
     *
     * @param bodyHTML
     * @return
     */
    public static String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important; }font{ word-wrap: break-word;}" +
                "video::-webkit-media-controls-fullscreen-button {display: none;}" +
                "</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


}

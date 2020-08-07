package com.muzhi.camerasdk;

import java.util.List;

/**
 * @Created By Admin  on 2020/8/6
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MessageWrap {
    public final List<String> message;

    public static MessageWrap getInstance(List<String> message) {
        return new MessageWrap(message);
    }

    private MessageWrap(List<String> message) {
        this.message = message;
    }
}

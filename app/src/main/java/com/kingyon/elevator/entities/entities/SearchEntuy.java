package com.kingyon.elevator.entities.entities;

import org.litepal.crud.DataSupport;

/**
 * @Created By Admin  on 2020/8/14
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 搜索内容保存
 */
public class SearchEntuy extends DataSupport {

    public String search;

    @Override
    public String toString() {
        return "SearchEntuy{" +
                "search='" + search + '\'' +
                '}';
    }
}

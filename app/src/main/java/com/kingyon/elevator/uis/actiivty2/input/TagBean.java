package com.kingyon.elevator.uis.actiivty2.input;

/**
 * Created By Admin  on 2020/4/29
 * Email : 163235610@qq.com
 * @Author:Mrczh
 * Instructions:
 */
public class TagBean {

  public String name;
  public String id;

  public TagBean(String name, String id) {
    this.name = name;
    this.id = id;
  }

  @Override public String toString() {
    return "TagBean{" +
        "name='" + name + '\'' +
        ", id='" + id + '\'' +
        '}';
  }
}

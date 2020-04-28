package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/4/24
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class PictureChooseEntity {

    public boolean isCheck;
    public String itemId;
    public int number;
    private String name;
    private String path;
    private String descs;
    private String fileNames;


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PictureChooseEntity{" +
                "name='" + name + '\'' +
                ", poth='" + path + '\'' +
                ", descs='" + descs + '\'' +
                ", fileNames='" + fileNames + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String poth) {
        this.path = poth;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    public String getFileNames() {
        return fileNames;
    }

    public void setFileNames(String fileNames) {
        this.fileNames = fileNames;
    }
}

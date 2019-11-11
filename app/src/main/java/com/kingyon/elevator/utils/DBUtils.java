package com.kingyon.elevator.utils;

import android.content.ContentValues;
import android.text.TextUtils;

import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.LocalMaterialEntity;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by GongLi on 2018/8/23.
 * Email：lc824767150@163.com
 */

public class DBUtils {
    private static DBUtils dbUtils;

    public static DBUtils getInstance() {
        if (dbUtils == null) {
            dbUtils = new DBUtils();
        }
        return dbUtils;
    }

    public void updateLocalMateria(LocalMaterialEntity entity) {
        if (entity == null || TextUtils.isEmpty(entity.getUrl())) {
            return;
        }
        List<LocalMaterialEntity> searchHistoryEntities = DataSupport.where("url = ? and userId = ?"
                , entity.getUrl()
                , String.valueOf(AppContent.getInstance().getMyUserId()))
                .find(LocalMaterialEntity.class);
        if (searchHistoryEntities != null && searchHistoryEntities.size() > 0) {
            ContentValues values = new ContentValues();
            values.put("planType", entity.getPlanType());
            values.put("screenType", entity.getScreenType());
            values.put("adId", entity.getAdId());
            values.put("type", entity.getType());
            values.put("url", entity.getUrl());
            values.put("path", entity.getPath());
            values.put("createTime", entity.getCreateTime());
            values.put("duration", entity.getDuration());
            values.put("name", entity.getName());
            values.put("extendA", entity.getExtendA());
            values.put("extendB", entity.getExtendB());
            values.put("extendC", entity.getExtendC());
            values.put("extendD", entity.getExtendD());
            values.put("extendE", entity.getExtendE());
            DataSupport.updateAll(LocalMaterialEntity.class, values, "url = ? and userId = ?"
                    , entity.getUrl(), String.valueOf(AppContent.getInstance().getMyUserId()));
            Logger.i("更新数据到数据库成功");
        } else {
            try {
                entity.saveThrows();
                Logger.i("插入数据到数据库成功");
            } catch (Exception e) {
                e.printStackTrace();
                Logger.i("插入数据到数据库失败");
            }
        }
    }

//    public List<LocalMaterialEntity> getLocalMateriasByType(String type, String screenSplit, String planType, int page) {
//        return DataSupport.where("type = ? and screenSplit = ? and planType = ?"
//                , type, screenSplit, planType)
//                .order("createTime desc")
//                .limit(30)
//                .offset(page * 30)
//                .find(LocalMaterialEntity.class);
//    }\

    public PageListEntity<LocalMaterialEntity> getLocalMateriasByType(String type, int page) {
        PageListEntity<LocalMaterialEntity> datas = new PageListEntity<>();
        List<LocalMaterialEntity> results = DataSupport.where("type = ? and userId = ?"
                , type, String.valueOf(AppContent.getInstance().getMyUserId()))
                .order("createTime desc")
                .limit(30)
                .offset((page - 1) * 30)
                .find(LocalMaterialEntity.class);
//        List<LocalMaterialEntity> results = DataSupport.findAll(LocalMaterialEntity.class);
        int count = DataSupport.where("type = ? and userId = ?", type, String.valueOf(AppContent.getInstance().getMyUserId()))
                .count(LocalMaterialEntity.class);
        datas.setContent(results);
        datas.setTotalElements(count);
        datas.setTotalPages(count % 30 == 0 ? count / 30 : (count / 30 + 1));
        return datas;
    }

    public PageListEntity<LocalMaterialEntity> getLocalMateriasImageChoose(String screenType, int page) {
        PageListEntity<LocalMaterialEntity> datas = new PageListEntity<>();
        List<LocalMaterialEntity> results = DataSupport.where("type = ? and screenType = ? and userId = ?"
                , Constants.Materia_Type.IMAGE, screenType, String.valueOf(AppContent.getInstance().getMyUserId()))
                .order("createTime desc")
                .limit(30)
                .offset((page - 1) * 30)
                .find(LocalMaterialEntity.class);
//        List<LocalMaterialEntity> results = DataSupport.findAll(LocalMaterialEntity.class);
        int count = DataSupport.where("type = ? and screenType = ? and userId = ?"
                , Constants.Materia_Type.IMAGE, screenType, String.valueOf(AppContent.getInstance().getMyUserId()))
                .count(LocalMaterialEntity.class);
        datas.setContent(results);
        datas.setTotalElements(count);
        datas.setTotalPages(count % 30 == 0 ? count / 30 : (count / 30 + 1));
        return datas;
    }

    public PageListEntity<LocalMaterialEntity> getLocalMateriasVideoChoose(String planType, String screenType, int page) {
        PageListEntity<LocalMaterialEntity> datas = new PageListEntity<>();
        List<LocalMaterialEntity> results = DataSupport.where("type = ? and screenType = ? and planType = ? and userId = ?"
                , Constants.Materia_Type.VIDEO, screenType, planType, String.valueOf(AppContent.getInstance().getMyUserId()))
                .order("createTime desc")
                .limit(30)
                .offset((page - 1) * 30)
                .find(LocalMaterialEntity.class);
//        List<LocalMaterialEntity> results = DataSupport.findAll(LocalMaterialEntity.class);
        int count = DataSupport.where("type = ? and screenType = ? and planType = ? and userId = ?"
                , Constants.Materia_Type.VIDEO, screenType, planType, String.valueOf(AppContent.getInstance().getMyUserId()))
                .count(LocalMaterialEntity.class);
        datas.setContent(results);
        datas.setTotalElements(count);
        datas.setTotalPages(count % 30 == 0 ? count / 30 : (count / 30 + 1));
        return datas;
    }

    public LocalMaterialEntity getLocalMateria(String url) {
        List<LocalMaterialEntity> searchResults = DataSupport.where("url = ? and userId = ?"
                , url, String.valueOf(AppContent.getInstance().getMyUserId()))
                .order("createTime desc")
                .limit(1)
                .find(LocalMaterialEntity.class);
        return (searchResults != null && searchResults.size() > 0) ? searchResults.get(0) : null;
    }

    public void deleteLocalMateria(String url) {
        DataSupport.deleteAll(LocalMaterialEntity.class, "url = ? and userId = ?"
                , url, String.valueOf(AppContent.getInstance().getMyUserId()));
    }
}

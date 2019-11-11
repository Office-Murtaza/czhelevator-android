package com.kingyon.elevator.entities;

import java.util.List;

/**
 * Created by GongLi on 2018/12/25.
 * Email：lc824767150@163.com
 */

public class HomepageDataEntity {
    private List<CellItemEntity> cells;
    private List<BannerEntity> banners;
    private List<AnnouncementEntity> announcements;

    public HomepageDataEntity(List<CellItemEntity> cells, List<BannerEntity> banners, List<AnnouncementEntity> announcements) {
        this.cells = cells;
        this.banners = banners;
        this.announcements = announcements;
    }

    public List<CellItemEntity> getCells() {
        return cells;
    }

    public void setCells(List<CellItemEntity> cells) {
        this.cells = cells;
    }

    public List<BannerEntity> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerEntity> banners) {
        this.banners = banners;
    }

    public List<AnnouncementEntity> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<AnnouncementEntity> announcements) {
        this.announcements = announcements;
    }
}

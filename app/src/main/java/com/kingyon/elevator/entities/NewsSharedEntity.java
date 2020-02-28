package com.kingyon.elevator.entities;

import com.kingyon.elevator.R;
import com.kingyon.library.social.ShareEntity;

/**
 * Created By SongPeng  on 2020/2/26
 * Email : 1531603384@qq.com
 */
public class NewsSharedEntity implements ShareEntity {

    private String shareTitle;
    private String shareContent;
    private String shareLink;

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    @Override
    public String getShareTitle() {
        return shareTitle != null ? shareTitle : "";
    }

    @Override
    public String getShareText() {
        return shareContent != null ? shareContent : "";
    }

    @Override
    public String getShareTitleUrl() {
        return shareLink != null ? shareLink : "";
    }

    @Override
    public String getShareSiteUrl() {
        return shareLink != null ? shareLink : "";
    }

    @Override
    public Integer getShareImageData() {
        return R.mipmap.ic_launcher;
    }

    @Override
    public String getShareImageUrl() {
        return null;
    }

    @Override
    public String[] getShareImageArray() {
        return new String[0];
    }
}

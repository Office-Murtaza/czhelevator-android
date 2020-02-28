package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2020/2/25
 * Email : 1531603384@qq.com
 * 首页新闻实体列表
 */
public class NewsEntity {


    /**
     * id : 1
     * urlPrefix : http://111.85.152.201:1510/promotion/news
     * orgName : 梯联网传媒
     * writerAccount : null
     * category : 新闻
     * title : “中国树立了世界防疫的典范”(患难见真情 共同抗疫情)
     * summary : 中国国家主席习近平2月23日出席统筹推进新冠肺炎疫情防控和经济社会发展工作部署会议并发表重要讲话，深刻分析了中国当前疫情形势和对经济社会发展影响，明确提出了加强党的领导、统筹推进疫情防控和经济社会发展工作的重点任务和重大举措。接受本报记者采访的外国人士纷纷表示，习近平主席的重要讲话向世界传递了中国战胜疫情的必胜信心、推进经济社会发展的坚强意志，他们相信中国将取得疫情防控阻击战的胜利，也看好中国经济发展前景。
     * coverUrl : 1111111
     * readCount : 18
     * createTime : 2020-02-25 16:07
     */

    private int id;
    private String urlPrefix;
    private String orgName;
    private String writerAccount;
    private String category;
    private String title;
    private String summary;
    private String coverUrl;
    private Double readCount;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getWriterAccount() {
        return writerAccount;
    }

    public void setWriterAccount(String writerAccount) {
        this.writerAccount = writerAccount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Double getReadCount() {
        return readCount;
    }

    public void setReadCount(Double readCount) {
        this.readCount = readCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

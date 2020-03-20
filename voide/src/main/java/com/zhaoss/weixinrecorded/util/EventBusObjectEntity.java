package com.zhaoss.weixinrecorded.util;

/**
 * Created By SongPeng  on 2019/12/3
 * Email : 1531603384@qq.com
 */
public class EventBusObjectEntity {

    private int eventCode;
    private Object data;

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public EventBusObjectEntity(int eventCode, Object data) {
        this.eventCode = eventCode;
        this.data = data;
    }
}

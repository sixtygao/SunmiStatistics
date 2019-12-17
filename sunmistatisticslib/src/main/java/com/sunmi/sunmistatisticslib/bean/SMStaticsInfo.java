package com.sunmi.sunmistatisticslib.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SMStaticsInfo {

    @Id(autoincrement = true)
    Long id;
    /**
     * 设备SN
     */
    String sn;
    /**
     * 事件时间 精确到秒
     */
    String eventTime;
    /**
     * app版本号
     */
    String appVersion;
    /**
     * 事件ID
     */
    String eventId;
    /**
     * 事件数据
     */
    String eventData;
    /**
     * 包名
     */
    String packageName;

    public SMStaticsInfo(String eventId) {
        this.eventId = eventId;
    }

    public SMStaticsInfo(String sn, String eventTime, String appVersion, String eventId, String eventData, String packageName) {
        this.sn = sn;
        this.eventTime = eventTime;
        this.appVersion = appVersion;
        this.eventId = eventId;
        this.eventData = eventData;
        this.packageName = packageName;
    }

    @Generated(hash = 861263785)
    public SMStaticsInfo(Long id, String sn, String eventTime, String appVersion,
                         String eventId, String eventData, String packageName) {
        this.id = id;
        this.sn = sn;
        this.eventTime = eventTime;
        this.appVersion = appVersion;
        this.eventId = eventId;
        this.eventData = eventData;
        this.packageName = packageName;
    }

    @Generated(hash = 1324576185)
    public SMStaticsInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "SMStaticsInfo{" +
                "id=" + id +
                ", sn='" + sn + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", eventId='" + eventId + '\'' +
                ", eventData='" + eventData + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}

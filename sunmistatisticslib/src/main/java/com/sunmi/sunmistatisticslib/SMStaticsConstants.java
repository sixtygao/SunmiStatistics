package com.sunmi.sunmistatisticslib;

public class SMStaticsConstants {

    /**
     * test api host
     */
    public static final String DEFAULT_API_HOST = "http://api.dev.sunmi.com/";
    /**
     * 上报事件打点统计
     */
    public static final String DEFAULT_API_REPORT_EVENT = DEFAULT_API_HOST + "api/invoice/app/invoice/1.0/?service=/reporteventlist";


    /**
     * 默认最小上传延迟时间
     * 单位：秒
     */
    public static final int DEFAULT_MIN_UPLOAD_DELAY = 5 * 60;
    /**
     * 默认最大上传延迟时间
     * 单位：秒
     */
    public static final int DEFAULT_MAX_UPLOAD_DELAY = 10 * 60;

    /**
     * 本地默认最大缓存数量
     */
    public static final int DEFAULT_MAX_CACHE_SIZE = 1000;

    /**
     * 失败重试间隔
     * 单位:秒
     */
    public static final int DEFAULT_RETRY_DELAY = 30;

    /**
     * 失败重试次数
     */
    public static final int DEFAULT_RETRY_COUNT = 2;
    /**
     * 是否是省流量模式
     */
    public static boolean saveFlow = false;
    /**
     * 默认数据库存储的最大记录数目为1000条
     */
    public static int maxCacheSize = DEFAULT_MAX_CACHE_SIZE;

    /**
     * 闹钟间隔默认最小时间要大于等于2，否则设置为默认值2
     */
    static int minUploadDelay = DEFAULT_MIN_UPLOAD_DELAY;
    /**
     * 时间间隔将是defaultMinTime<----->defaultMinTime+defaultMaxTime时间范围内
     */
    static int maxUploadDelay = DEFAULT_MAX_UPLOAD_DELAY;

    /**
     * 上传事件信息地址
     */
    public static String eventReportApi = DEFAULT_API_REPORT_EVENT;
}

package com.sunmi.sunmistatistics;

import android.app.Application;
import android.os.Environment;

import com.sunmi.sunmistatisticslib.SMStaticsConstants;
import com.sunmi.sunmistatisticslib.SMStaticsMaster;
import com.sunmi.toolslib.SunmiUtils;
import com.sunmi.toolslib.log.LogUtil;
import com.sunmi.toolslib.sp.SharedPrefUtil;
import com.sunmilib.service.HttpConfig;

public class MyApplication extends Application {
    //TODO only for test
    public static final String DES_KEY = "wywmxxkj";
    public static final String DES_VI = "12345678";
    public static final String MD5_SECRET_KEY = "Woyouxinxi666";
    public static final String isEncrypted = "0";
    /**
     * sharedPreference file name
     */
    public static final String SP_NAME = "sunmi_statistics";
    /**
     * sdcard log file directory
     */
    public static final String LOG_FILE_DIRECTORY = Environment.getExternalStorageDirectory() + "/sunmiStatistics";

    @Override
    public void onCreate() {
        super.onCreate();
        initUtilConfig();
        initHttpConfig();
        SMStaticsMaster.getInstance()
                .init(this)
                //是否是省流量模式
                .configFlowMode(false)
                //延时上传区间
                //最小 1分钟
                //最大 5分钟
                .configUploadDelayRange(SMStaticsConstants.DEFAULT_MIN_UPLOAD_DELAY, SMStaticsConstants.DEFAULT_MAX_UPLOAD_DELAY)
                //配置事件最大缓存限制
                //超过最大限制将采取？策略
                .configCacheSize(SMStaticsConstants.DEFAULT_MAX_CACHE_SIZE);
    }


    private void initUtilConfig() {
        SunmiUtils.init(this);
        SharedPrefUtil.getInstance(this).setPrefFileName(SP_NAME);
        LogUtil.setLogDirectory(LOG_FILE_DIRECTORY);
        LogUtil.setLogLevel(LogUtil.VERBOSE);
        LogUtil.setDumpLevel(LogUtil.VERBOSE);
    }


    private void initHttpConfig() {
        HttpConfig.Builder builder = new HttpConfig.Builder()
                .desKey(DES_KEY)
                .desVI(DES_VI)
                .md5Key(MD5_SECRET_KEY)
                .encrypted(!"0".equals(isEncrypted))
                .log(true)
                .debug(true)
                .appContext(getApplicationContext());
        HttpConfig.initSConfig(builder.build());
    }
}

package com.sunmi.sunmistatisticslib;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sunmi.sunmistatisticslib.bean.SMStaticsInfo;
import com.sunmi.sunmistatisticslib.service.AlarmService;
import com.sunmi.sunmistatisticslib.utils.DaoUtil;
import com.sunmi.toolslib.SunmiUtils;
import com.sunmi.toolslib.application.AppInfoUtils;
import com.sunmi.toolslib.deviceinfo.DeviceUtils;
import com.sunmi.toolslib.log.LogUtil;
import com.sunmi.toolslib.net.NetworkMonitor;
import com.sunmi.toolslib.time.DateUtils;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.sunmi.sunmistatisticslib.SMStaticsConstants.DEFAULT_MAX_UPLOAD_DELAY;
import static com.sunmi.sunmistatisticslib.SMStaticsConstants.DEFAULT_MIN_UPLOAD_DELAY;


public class SMStaticsMaster {

    public static volatile boolean hasAlarm = false;
    private static String TAG = "SMStaticsMaster";
    private static SMStaticsMaster master = null;
    private Context mAppContext;
    private NetworkMonitor.NetworkChangeObserver networkChangeObserver = new NetworkMonitor.NetworkChangeObserver() {
        @Override
        public void onNetworkChange(boolean isNetworkConnected, boolean isWifiOrEthernet) {
            if (isNetworkConnected && (!SMStaticsConstants.saveFlow || SMStaticsConstants.saveFlow && isWifiOrEthernet)) {
                addAlarm(mAppContext);
            }
        }
    };

    private SMStaticsMaster() {

    }

    public static SMStaticsMaster getInstance() {
        synchronized (SMStaticsMaster.class) {
            if (null == master) {
                synchronized (SMStaticsMaster.class) {
                    master = new SMStaticsMaster();
                }
            }
        }
        return master;
    }

    public synchronized SMStaticsMaster init(Context context) {
        if (context == null) {
            throw new InvalidParameterException("context can not be null!");
        }
        if (mAppContext != null) {
            LogUtil.i(TAG, "already init return ");
            return this;
        }
        mAppContext = context.getApplicationContext();
        SunmiUtils.init(mAppContext);
        DaoUtil.getInstance().init(mAppContext);
        NetworkMonitor.get().subscribe(networkChangeObserver);
        return this;
    }

    public SMStaticsMaster configUploadUrl(String eventReportApi) {
        SMStaticsConstants.eventReportApi = eventReportApi;
        return this;
    }

    public SMStaticsMaster configFlowMode(boolean saveFlow) {
        SMStaticsConstants.saveFlow = saveFlow;
        return this;
    }

    public SMStaticsMaster configUploadDelayRange(int minDelay, int maxDelay) {
        if (minDelay >= maxDelay) {
            throw new InvalidParameterException("minDelay must < maxDelay!");
        }
        if (minDelay < DEFAULT_MIN_UPLOAD_DELAY) {
            LogUtil.i(TAG, "minDelay must >= " + DEFAULT_MIN_UPLOAD_DELAY);
            minDelay = DEFAULT_MIN_UPLOAD_DELAY;
        }
        if (maxDelay <= DEFAULT_MIN_UPLOAD_DELAY) {
            LogUtil.i(TAG, "maxDelay must > " + DEFAULT_MIN_UPLOAD_DELAY);
            maxDelay = DEFAULT_MAX_UPLOAD_DELAY;
        }
        SMStaticsConstants.minUploadDelay = minDelay;
        SMStaticsConstants.maxUploadDelay = maxDelay;
        return this;
    }

    public SMStaticsMaster configCacheSize(int cacheSize) {
        if (cacheSize <= 0) {
            throw new InvalidParameterException("cache size must > 0");
        }
        SMStaticsConstants.maxCacheSize = cacheSize;
        return this;
    }

    public synchronized void onEvent(String eventId) {
        onEvent(eventId, null);
    }

    public synchronized void onEvent(String eventId, String eventData) {
        try {
            SMStaticsInfo info = new SMStaticsInfo(
                    DeviceUtils.getMSN(),
                    String.valueOf(System.currentTimeMillis() / 1000),
                    AppInfoUtils.getAppVersionName(mAppContext),
                    eventId,
                    eventData,
                    mAppContext.getPackageName()
            );
            onEvent(info);
        } catch (Exception e) {
            LogUtil.e(TAG, "store event error!", e);
        }
    }

    public synchronized void onEvent(SMStaticsInfo info) {
        if (mAppContext == null) {
            throw new IllegalStateException("should init first");
        }
        onEvent(mAppContext, info);
    }

    /**
     * 埋点
     *
     * @param info
     */
    public synchronized void onEvent(Context context, SMStaticsInfo info) {
        //入库
        Observable.just(1)
                .observeOn(Schedulers.io())
                .subscribe((integer) -> {
                    DaoUtil.getInstance().insert(info);
                });
        addAlarm(context);
    }

    private void addAlarm(Context context) {
        if (!hasAlarm) {
            hasAlarm = true;
            Random df = new Random();
            long time = 1000 * (df.nextInt(SMStaticsConstants.maxUploadDelay - SMStaticsConstants.minUploadDelay) + SMStaticsConstants.minUploadDelay) + System.currentTimeMillis();
            LogUtil.d(TAG, "添加埋点统计上传闹钟:" + DateUtils.dateToStr(new Date(time)));
            Intent intent = new Intent(context, AlarmService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (null != alarmManager) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
        }
    }

    /**
     * 清除本地缓存数据
     */
    public synchronized void clearCache() {
        DaoUtil.getInstance().deleteAll();
    }

}

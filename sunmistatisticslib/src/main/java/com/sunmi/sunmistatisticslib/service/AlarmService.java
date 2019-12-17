package com.sunmi.sunmistatisticslib.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import com.sunmi.sunmistatisticslib.SMStaticsConstants;
import com.sunmi.sunmistatisticslib.SMStaticsMaster;
import com.sunmi.sunmistatisticslib.bean.SMStaticsBean;
import com.sunmi.sunmistatisticslib.bean.SMStaticsInfo;
import com.sunmi.sunmistatisticslib.utils.DaoUtil;
import com.sunmi.sunmistatisticslib.utils.RetryWhenDelay;
import com.sunmi.toolslib.log.LogUtil;
import com.sunmi.toolslib.net.NetworkUtils;
import com.sunmilib.http.BaseResponse;
import com.sunmilib.http.SunmiHttp;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.sunmi.sunmistatisticslib.SMStaticsConstants.DEFAULT_RETRY_COUNT;
import static com.sunmi.sunmistatisticslib.SMStaticsConstants.DEFAULT_RETRY_DELAY;


public class AlarmService extends IntentService {

    public static String TAG = AlarmService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmService(String name) {
        super(name);
    }

    public AlarmService() {
        super("SMStaticsService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "onCreate--------->");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "onStartCommand--------->");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtil.d(TAG, "onHandleIntent--------->");
        //获取所有数据
        List<SMStaticsInfo> infos = DaoUtil.getInstance().getAll();
        /**
         * 1 非省流量模式
         * 2 省流量模式下必须是使用wifi or 有线
         */
        if (infos != null && (!SMStaticsConstants.saveFlow || (SMStaticsConstants.saveFlow && NetworkUtils.isWifiOrEthernet(this)))) {//上传数据
            SMStaticsBean bean = new SMStaticsBean();
            bean.getData().addAll(infos);
            SMStaticsMaster.hasAlarm = false;
            post(bean);
        } else {
            SMStaticsMaster.hasAlarm = false;
        }

    }


    private void post(final SMStaticsBean bean) {
        SunmiHttp.request(SMStaticsConstants.eventReportApi, bean)
                .build()
                .<String>asynHttpToSingle(String.class)
                .retryWhen(new RetryWhenDelay(DEFAULT_RETRY_DELAY, DEFAULT_RETRY_COUNT))
                .observeOn(Schedulers.io())
                .subscribe(new SingleObserver<BaseResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BaseResponse<String> stringBaseResponse) {
                        LogUtil.d(TAG, "上传埋点信息: " + stringBaseResponse);
                        //埋点信息上传成功
                        for (SMStaticsInfo info : bean.getData()) {
                            //清楚数据库中记录
                            DaoUtil.getInstance().delete(info);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //埋点信息上传失败,等待下次触发
                        LogUtil.e(TAG, "上传埋点信息失败！", e);
                    }
                });

    }

}

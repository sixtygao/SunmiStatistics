package com.sunmi.sunmistatisticslib.utils;

import android.content.Context;

import com.sunmi.sunmistatisticslib.SMStaticsConstants;
import com.sunmi.sunmistatisticslib.bean.DaoMaster;
import com.sunmi.sunmistatisticslib.bean.DaoSession;
import com.sunmi.sunmistatisticslib.bean.SMStaticsInfo;
import com.sunmi.sunmistatisticslib.bean.SMStaticsInfoDao;
import com.sunmi.toolslib.log.LogUtil;

import org.greenrobot.greendao.database.Database;

import java.util.List;

public class DaoUtil {
    private static final String TAG = DaoUtil.class.getSimpleName();
    private static DaoUtil mDaoUtil = null;
    private DaoSession mDaoSession = null;

    private DaoUtil() {
    }

    public static DaoUtil getInstance() {
        if (null == mDaoUtil) {
            synchronized (DaoUtil.class) {
                mDaoUtil = new DaoUtil();
            }
        }
        return mDaoUtil;
    }

    public void init(Context context) {
        if (null == mDaoSession) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "SunmiStatics.db");
            Database db = helper.getWritableDb();
            mDaoSession = new DaoMaster(db).newSession();
        }
    }

    public void insert(SMStaticsInfo info) {
        if (null == mDaoSession) {
            throw new RuntimeException("please excute init method first");
        }
        /**
         * 存储记录大于1000条之后，只能删除最前面一条才能插入一条
         */
        long dataCount = mDaoSession.getSMStaticsInfoDao().count();
        if (dataCount >= SMStaticsConstants.maxCacheSize) {
            LogUtil.d(TAG, "埋点数据本地缓存超限: " + SMStaticsConstants.maxCacheSize);
            mDaoSession.getSMStaticsInfoDao().deleteInTx(mDaoSession.getSMStaticsInfoDao().queryBuilder().limit(1).orderAsc(SMStaticsInfoDao.Properties.Id).list());
        }
        mDaoSession.insert(info);
    }


    public List<SMStaticsInfo> getAll() {
        List<SMStaticsInfo> infos = mDaoSession.loadAll(SMStaticsInfo.class);
        return infos;
    }

    public void delete(SMStaticsInfo info) {
        if (null == mDaoSession) {
            throw new RuntimeException("please execute init method first");
        }
        mDaoSession.delete(info);
    }

    public void deleteAll() {
        if (null == mDaoSession) {
            throw new RuntimeException("please execute init method first");
        }
        mDaoSession.deleteAll(SMStaticsInfo.class);
    }
}

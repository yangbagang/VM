package com.ybg.rp.vm.app;

import com.cnpay.tigerbalm.app.TbApplication;
import com.cnpay.tigerbalm.utils.ACache;
import com.cnpay.tigerbalm.utils.GsonUtils;
import com.cnpay.tigerbalm.utils.TbLog;
import com.igexin.sdk.PushManager;
import com.ybg.rp.vm.entity.db.Operator;
import com.ybg.rp.vm.entity.db.util.DbUtils;
import com.ybg.rp.vm.util.Config;

import org.apache.log4j.Level;

import java.io.File;
import java.io.IOException;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * application 控制
 * 包            名:      com.ybg.rp.vm.app
 * 类            名:      OPApplication
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/3
 */
public class OPApplication extends TbApplication {

    private static OPApplication opApplication;

    /*缓存二维码*/
    private static ACache cache;

    public static OPApplication getInstance() {
        if (opApplication == null) {
            opApplication = new OPApplication();
        }
        return opApplication;
    }

    /* 登录的账号*/
    private Operator oper;

    @Override
    public void onCreate() {
        super.onCreate();

        //错误信息，保存为日志
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(mContext);

        PushManager.getInstance().initialize(this.getApplicationContext());
        cache = ACache.get(mContext, Config.CACHE_FILENAME);

        setLog4j();
    }

    /**
     * 获取操作员信息
     */
    public Operator getOper() {
        if (oper == null) {
//            oper = new Operator();
//            oper.setOperId(1000l);
//            oper.setOperName("test");
        }
        return oper;
    }

    public void setOper(Operator oper) {
        this.oper = oper;
    }


    /**
     * 初始化LOG日志保存
     */
    private void setLog4j() {
        TbLog.i("启动LOG 4J 日志记录");
        try {
            File destDir = new File(Config.LOG4J_PATH);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            File file = new File(Config.LOG4J_PATH + "vm_now_log.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Config.LOG4J_PATH + "vm_now_log.txt");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 5); //1MB一个文件
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
    }


    /**
     * 是否打开柜门
     * 默认 没开门-否
     * default: NULL
     *
     * @return 订单号
     */
    public synchronized String getIsOpenTrack(String isOpenTrack) {
        if (null == cache)
            cache = ACache.get(mContext, Config.CACHE_FILENAME);
        return cache.getAsString(isOpenTrack);
        //        return PreferencesUtil.getString(Config.FILE_NAME, mContext, Config.FILE_NAME_ORDER, null);
    }

    /**
     * 设置是否开门
     *
     * @param isOpenTrack 订单号
     */
    public synchronized void setIsOpenTrack(String isOpenTrack) {
        if (null == cache)
            cache = ACache.get(mContext, Config.CACHE_FILENAME);
        cache.put(isOpenTrack, isOpenTrack);
        //        PreferencesUtil.putString(Config.FILE_NAME, mContext, Config.FILE_NAME_ORDER, isOpenTrack);
    }

    /**
     * 清空
     *
     * @param isOpenTrack 订单号
     */
    public synchronized void isOpenTrackRemove(String isOpenTrack) {
        if (null == cache)
            cache = ACache.get(mContext, Config.CACHE_FILENAME);
        cache.remove(isOpenTrack);
    }
}

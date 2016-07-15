package com.ybg.rp.vm.util;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;

import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.service.TtimingDataUpService;

import java.util.List;

/**
 * 定时任务管理工具
 * 包            名:      com.ybg.rp.vm.util
 * 类            名:      AlarmManagerUtil
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/14
 */
public class AlarmManagerUtil {
    
    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    //数据上传
    private static PendingIntent pendingIntentSixHours;

    /**
     * 使用 AlarmManager 来 定时启动服务
     * 定时上传数据
     */
    public static void startPendingIntentSixHours(Context context) {
        if (!isServiceWork(context, "SixHoursService")) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, TtimingDataUpService.class);//启动示例Service
            pendingIntentSixHours = PendingIntent.getService(context, 0, intent, 0);
            long interval = DateUtils.MINUTE_IN_MILLIS * 60 * 3;// 3小时一次
            //long start = DateUtils.MINUTE_IN_MILLIS;// 1分钟后开始
            long firstWake = System.currentTimeMillis() + interval;
            am.setRepeating(AlarmManager.RTC, firstWake, interval, pendingIntentSixHours);
            TbLog.i("-定时上传--服务启动---成功");
        } else {
            TbLog.i("服务已启动");
        }
    }

    /**
     * 停止
     */
    public static void stopPendingIntentSixHours() {
        if (pendingIntentSixHours != null) {
            pendingIntentSixHours.cancel();
        }
    }
}

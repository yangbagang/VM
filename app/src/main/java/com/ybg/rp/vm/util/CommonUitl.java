package com.ybg.rp.vm.util;

import android.app.ActivityManager;
import android.content.Context;

import com.cnpay.tigerbalm.utils.TbLog;

import java.util.List;

/**
 * 包            名:      com.ybg.rp.vm.util
 * 类            名:      CommonUitl
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/14
 */
public class CommonUitl {

    /**
     * 获取当前显示的activity
     *
     * @param context
     * @param cmdName
     * @return
     */
    public boolean isTopActivy(Context context, String cmdName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;

        if (null != runningTaskInfos) {
            cmpNameTemp = (runningTaskInfos.get(0).topActivity).toString();
            TbLog.e("cmpNameTemp:" + cmpNameTemp);
        }

        if (null == cmpNameTemp) return false;
        return cmpNameTemp.equals(cmdName);
    }
}

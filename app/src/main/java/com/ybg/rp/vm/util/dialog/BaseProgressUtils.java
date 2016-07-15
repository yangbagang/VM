package com.ybg.rp.vm.util.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 包            名:      com.ybg.rp.vm.util.view
 * 类            名:      BaseProgressUtils
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/27 0027
 */
public class BaseProgressUtils {
    /**原生*/
    private static ProgressDialog baseDialog;

    /**原生开始*/
    public static void showBaseDialog(Context mContext, String msgName) {

        if (baseDialog == null) {
            baseDialog = new ProgressDialog(mContext);
            baseDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            baseDialog.setMessage(msgName);
            baseDialog.setCanceledOnTouchOutside(false);
            baseDialog.show();
        }


    }
    /**原生取消*/
    public static void cancelBaseDialog() {
        if (null != baseDialog) {
            baseDialog.cancel();
        }
        baseDialog = null;
    }
}

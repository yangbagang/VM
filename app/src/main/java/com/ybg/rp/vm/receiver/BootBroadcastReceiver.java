package com.ybg.rp.vm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ybg.rp.vm.activity.InitActivity;


/**
 * 自动启动程序广播
 * <p/>
 * 包            名:      com.cnpay.ppvending.receiver
 * 类            名:      BootBroadcastReceiver
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/14
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO 开机启动程序
        if (intent.getAction().equals(action_boot)) {
            Intent ootStartIntent = new Intent(context, InitActivity.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
        }
    }
}

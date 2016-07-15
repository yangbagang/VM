package com.ybg.rp.vm.activity.shopping;

import android.app.Activity;
import android.app.Application;

import java.util.Stack;

/**
 * 退出Activity
 *
 * Created by zenghonghua on 2016/6/23 0023.
 */
public class ExitShoppingActivity extends Application {

    private Stack<Activity> mList = new Stack<Activity>();
    private static ExitShoppingActivity instance;

    /** 单列 */
    public synchronized static ExitShoppingActivity getInstance() {
        if (null == instance) {
            instance = new ExitShoppingActivity();
        }
        return instance;
    }



    // 添加要退出的Activity
    public void addActivity(Activity activity) {
        for (int i = 0; i < mList.size(); i++) {
            Activity mAc = mList.get(i);
            if (mAc.equals(activity)) {
                return;
            }
        }
        mList.add(activity);
    }

    // 清除数据
    public void clearAll() {
        mList.clear();
    }

    // 退出
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* 不需要终止程序 */
        // finally {
        // System.exit(0);
        // }
    }

    /*// 退出指定页
    public void exitPayAllActivity() {
        try {
            if (null != payAllActivity) {
                payAllActivity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /* 清理内存 */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}

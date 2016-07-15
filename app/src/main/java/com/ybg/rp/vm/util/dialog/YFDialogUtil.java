package com.ybg.rp.vm.util.dialog;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;

import com.cnpay.tigerbalm.utils.ExitActivity;
import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.InitActivity;

/**
 * 包            名:      com.cnpay.vending.yifeng.util.dialog
 * 类            名:      YFDialogUtil
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/4/15
 */
public class YFDialogUtil {
    /**
     * dialog tag
     */
    private static String mDialogTag = "loadding";


    public static void showLoadding(Context mContext) {
//        Activity activity = (Activity) YFApplication.getInstance().mContext;
        Activity activity = (Activity) mContext;
        if (activity == null) {
            TbLog.e("页面为NULL--不能显示DIALOG");
            return;
        }

         /*为了不重复显示dialog，在显示对话框之前移除正在显示的对话框。*/

//        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
//        Fragment fragment = activity.getFragmentManager().findFragmentByTag(mDialogTag);
//        if (null != fragment) {
//            ft.remove(fragment);
//        }
        removeDialog(mContext);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个过渡动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        LoaddingDialog dialogFragment = LoaddingDialog.newInstance(R.style.Dialog_Fullscreen);
//        dialogFragment.show(ft, mDialogTag);
        ft.add(dialogFragment, mDialogTag).commitAllowingStateLoss();
        // 作为全屏显示,使用“content”作为fragment容器的基本视图,这始终是Activity的基本视图
//        ft.add(android.R.id.content, dialogFragment, mDialogTag).addToBackStack(null).commit();

    }


    /**
     * 显示USB设备列表
     *
     * @param mContext
     */
    public static void showListDeviceDialog(Context mContext) {
        Activity activity = (Activity) mContext;
        if (activity == null) {
            TbLog.e("页面为NULL--不能显示DIALOG");
            return;
        }
        removeDialog(mContext);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个过渡动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        LoaddingDialog dialogFragment = LoaddingDialog.newInstance(R.style.Dialog_Fullscreen);
        DevicesDialog dialogFragment = DevicesDialog.newInstance(R.style.Dialog_Fullscreen);
//        dialogFragment.show(ft, mDialogTag);
        ft.add(dialogFragment, mDialogTag).commitAllowingStateLoss();
    }


    /**
     * 重新启动应用!
     *
     * @param mAbActivity
     * @param msg
     */
    public static void onStartApp(final Activity mAbActivity, String msg) {
        com.cnpay.tigerbalm.utils.dialog.DialogUtil.showAlertDialog(mAbActivity, "提示", msg,
                new com.cnpay.tigerbalm.utils.dialog.fragment.AlertDialogFragment.DialogOnClickListener() {
                    @Override
                    public void onPositiveClick() {
                        //确认
                        TbLog.i("--重启程序-");
                        // 关闭程序
                        ExitActivity.getInstance().exit();
                        //启动程序
                        Intent intent = new Intent();
                        intent.setClass(mAbActivity, InitActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mAbActivity.startActivity(intent);
                    }

                    @Override
                    public void onNegativeClick() {
                        //取消
                        com.cnpay.tigerbalm.utils.dialog.DialogUtil.removeDialog(mAbActivity);
                    }
                });
    }


    /**
     * 描述：移除Fragment.
     */
    public static void removeDialog(Context mContext) {
        try {
            Activity activity = (Activity) mContext;
//            Activity activity = (Activity) YFApplication.getInstance().mContext;
            if (activity == null) {
                TbLog.e("页面为NULL--不能-关闭-DIALOG");
                return;
            }
            FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
            // 指定一个过渡动画
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            Fragment prev = activity.getFragmentManager().findFragmentByTag(mDialogTag);
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            ft.commit();
        } catch (Exception e) {
            // 可能有Activity已经被销毁的异常
//			e.printStackTrace();
        }
    }

}

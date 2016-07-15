package com.ybg.rp.vm.util.dialog;

import android.app.DatePickerDialog;
import android.content.Context;

/**
 * 重写日期选择器
 *
 * 包            名:      com.ybg.rp.vm.util.view
 * 类            名:      MDatePickerDialog
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/22 0022
 */
public class MDatePickerDialog extends DatePickerDialog {

    public MDatePickerDialog(Context context, OnDateSetListener callBack,
                             int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    @Override
    protected void onStop() {
        //super.onStop();
    }
}

package com.ybg.rp.vm.util.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.dialog.DialogUtil;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.listener.ResultCallback;

/**
 * 包            名:      com.ybg.rp.vm.util.view
 * 类            名:      MaxSelectDialog
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/27 0027
 */
public class MaxSelectDialog implements View.OnClickListener {
    private Context mContext;
    private ResultCallback.MaxListener maxListener;
    private View selectLayout;

    public MaxSelectDialog(Context context, ResultCallback.MaxListener maxListener) {
        this.mContext = context;
        this.maxListener = maxListener;
        findLayout();
    }


    private void findLayout() {
        selectLayout = LayoutInflater.from(mContext).inflate(R.layout.view_select_max, null);
        TextView tv_01 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_01);
        TextView tv_02 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_02);
        TextView tv_03 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_03);
        TextView tv_04 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_04);
        TextView tv_05 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_05);
        TextView tv_06 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_06);
        TextView tv_07 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_07);
        TextView tv_08 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_08);
        TextView tv_09 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_09);
        TextView tv_10 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_10);
        TextView tv_11 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_11);
        TextView tv_12 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_12);
        TextView tv_13 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_13);
        TextView tv_14 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_14);
        TextView tv_15 = (TextView) selectLayout.findViewById(R.id.maxSet_rb_change_15);

        tv_01.setOnClickListener(this);
        tv_02.setOnClickListener(this);
        tv_03.setOnClickListener(this);
        tv_04.setOnClickListener(this);
        tv_05.setOnClickListener(this);
        tv_06.setOnClickListener(this);
        tv_07.setOnClickListener(this);
        tv_08.setOnClickListener(this);
        tv_09.setOnClickListener(this);
        tv_10.setOnClickListener(this);
        tv_11.setOnClickListener(this);
        tv_12.setOnClickListener(this);
        tv_13.setOnClickListener(this);
        tv_14.setOnClickListener(this);
        tv_15.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maxSet_rb_change_01:
                maxListener.selectMax(1);
                break;
            case R.id.maxSet_rb_change_02:
                maxListener.selectMax(2);
                break;
            case R.id.maxSet_rb_change_03:
                maxListener.selectMax(3);
                break;
            case R.id.maxSet_rb_change_04:
                maxListener.selectMax(4);
                break;
            case R.id.maxSet_rb_change_05:
                maxListener.selectMax(5);
                break;
            case R.id.maxSet_rb_change_06:
                maxListener.selectMax(6);
                break;
            case R.id.maxSet_rb_change_07:
                maxListener.selectMax(7);
                break;
            case R.id.maxSet_rb_change_08:
                maxListener.selectMax(8);
                break;
            case R.id.maxSet_rb_change_09:
                maxListener.selectMax(9);
                break;
            case R.id.maxSet_rb_change_10:
                maxListener.selectMax(10);
                break;
            case R.id.maxSet_rb_change_11:
                maxListener.selectMax(11);
                break;
            case R.id.maxSet_rb_change_12:
                maxListener.selectMax(12);
                break;
            case R.id.maxSet_rb_change_13:
                maxListener.selectMax(13);
                break;
            case R.id.maxSet_rb_change_14:
                maxListener.selectMax(14);
                break;
            case R.id.maxSet_rb_change_15:
                maxListener.selectMax(15);
                break;
        }
    }

    /**
     * 显示对话框
     */
    public void showDialog() {
        if (selectLayout != null) {
            DialogUtil.showAlertDialog(selectLayout);
        }
    }

    /**
     * 移除对话框
     */
    public void hideDialog() {
        if (selectLayout != null) {
            DialogUtil.removeDialog(selectLayout);
            selectLayout=null;
        }
    }
}

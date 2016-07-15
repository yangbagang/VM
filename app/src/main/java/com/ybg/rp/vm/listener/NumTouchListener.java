package com.ybg.rp.vm.listener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.R;

/**
 * 数字点击监听
 *
 * 包   名:     com.ybg.rp.vm.listener
 * 类   名:     NumTouchListener
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/6 0006 17:18
 * 作   者:     yuyucheng
 */
public class NumTouchListener implements View.OnTouchListener{
    private EditText et_command;
    private TextView input_text;

    public NumTouchListener(EditText et_command, TextView input_text) {
        this.et_command = et_command;
        this.input_text = input_text;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            TbLog.i("[- ACTION_DOWN -]");
        }else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            TbLog.i("[- ACTION_MOVE -]");
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            TbLog.i("[- ACTION_UP -]");
            getView(v);
        }else if (event.getAction() == MotionEvent.ACTION_CANCEL){
            TbLog.i("[- ACTION_CANCEL -]");
            getView(v);
        }
        return false;
    }


    private void getView(View v){

        switch (v.getId()) {
            case R.id.home_bt_delete:
                String text = et_command.getText().toString().trim();
                if (!StrUtil.isEmpty(text)) {
                    text = text.substring(0, text.length() - 1);
                    et_command.setText(text);
                }
                break;
            case R.id.home_bt_deleteAll:
                et_command.setText("");
                input_text.setText("");
                break;
            case R.id.home_bt_num_0:
                setEdText("0");
                break;
            case R.id.home_bt_num_01:
                setEdText("1");
                break;
            case R.id.home_bt_num_02:
                setEdText("2");
                break;
            case R.id.home_bt_num_03:
                setEdText("3");
                break;
            case R.id.home_bt_num_04:
                setEdText("4");
                break;
            case R.id.home_bt_num_05:
                setEdText("5");
                break;
            case R.id.home_bt_num_06:
                setEdText("6");
                break;
            case R.id.home_bt_num_07:
                setEdText("7");
                break;
            case R.id.home_bt_num_08:
                setEdText("8");
                break;
            case R.id.home_bt_num_09:
                setEdText("9");
                break;
        }
    }

    /**
     * 设置文本框的数值
     *
     * @param num
     */
    private void setEdText(String num) {
        String text = et_command.getText().toString().trim();

        if (text.length() == 3) {
            return;
        }
        /** 拼接轨道字符 */
        text += num;
        et_command.setText(text);
    }

}

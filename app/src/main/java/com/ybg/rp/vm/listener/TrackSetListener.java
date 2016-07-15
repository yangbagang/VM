package com.ybg.rp.vm.listener;

import android.widget.EditText;

import com.ybg.rp.vm.util.KeyboardUtil;
import com.cnpay.tigerbalm.utils.TbLog;

/**
 * 轨道设置
 *
 * 包            名:      com.ybg.rp.vm.listener
 * 类            名:      TrackSetListener
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/21 0021
 */
public class TrackSetListener implements KeyboardUtil.InputFinishListener{

    @Override
    public void inputHasOver(int onclickType, EditText editText) {
        TbLog.i("[ --inputHasOver-- ]");
        if (editText!=null){
            TbLog.i("[ onclickType:" + onclickType + " editText:" + editText.getText().toString());
        }
    }
}

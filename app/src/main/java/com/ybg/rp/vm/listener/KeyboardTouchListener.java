package com.ybg.rp.vm.listener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ybg.rp.vm.util.KeyboardUtil;

/**
 * 包            名:      com.cnpay.keybord
 * 类            名:      KeyboardTouchListener
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/24 0024
 */
public class KeyboardTouchListener implements View.OnTouchListener{
    private KeyboardUtil keyboardUtil;
    private int keyboardType = 1;
    private int scrollTo = -1;

    public KeyboardTouchListener(KeyboardUtil util,int keyboardType,int scrollTo){
        this.keyboardUtil = util;
        this.keyboardType = keyboardType;
        this.scrollTo = scrollTo;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (keyboardUtil != null && keyboardUtil.getEd() !=null &&v.getId() != keyboardUtil.getEd().getId())
                keyboardUtil.showKeyBoardLayout((EditText) v,keyboardType,scrollTo);
            else if(keyboardUtil != null && keyboardUtil.getEd() ==null){
                keyboardUtil.showKeyBoardLayout((EditText) v,keyboardType,scrollTo);
            }else{
//                Log.d("KeyboardTouchListener", "v.getId():" + v.getId());
//                Log.d("KeyboardTouchListener", "keyboardUtil.getEd().getId():" + keyboardUtil.getEd().getId());
                if (keyboardUtil != null) {
                    keyboardUtil.setKeyBoardCursorNew((EditText) v);
                }
            }
        }
        return false;
    }
}

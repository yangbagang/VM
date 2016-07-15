package com.ybg.rp.vm.listener;

import android.content.Context;
import android.widget.EditText;

import com.ybg.rp.vm.util.KeyboardUtil;
import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.util.helper.DbSetHelper;

/**
 * 自定义键盘的切换与监听
 * <p>
 * 包            名:      com.ybg.rp.vm.listener
 * 类            名:      KeyBoardStateListener
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/24 0024
 */
public class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {
    private Context mContext;
    private DbSetHelper helper;

    public KeyBoardStateListener(Context context) {
        this.mContext = context;
        helper = DbSetHelper.getInstance(mContext);
    }

    @Override
    public void KeyBoardStateChange(int state, EditText editText) {
        TbLog.i("[state:" + state + " editText:" + editText.getText().toString());
        String num = editText.getText().toString();
        if (StrUtil.isEmpty(num)) {
            editText.setText("8");
            num="8";
        }
        setMainLayer(num, editText);
    }

    private void setMainLayer(String num, EditText editText) {
        int size = Integer.parseInt(num);
        String layerNo = "";

        switch (editText.getId()) {
            /**保存主机的轨道数设置*/
            case R.id.container_edt_main_track_0:
                //layerNo = "00";
                break;
            case R.id.container_edt_main_track_1:
                layerNo = "01";
                break;
            case R.id.container_edt_main_track_2:
                layerNo = "02";
                break;
            case R.id.container_edt_main_track_3:
                layerNo = "03";
                break;
            case R.id.container_edt_main_track_4:
                layerNo = "04";
                break;
            case R.id.container_edt_main_track_5:
                layerNo = "05";
                break;
            case R.id.container_edt_main_track_6:
                layerNo = "06";
                break;
            case R.id.container_edt_main_track_7:
                layerNo = "07";
                break;
            case R.id.container_edt_main_track_8:
                layerNo = "08";
                break;
        }
        /**保存主机设置*/
        helper.setMainTrack(layerNo, size);
    }


}

package com.ybg.rp.vm.serial.yl;

import android.os.SystemClock;

import com.cnpay.tigerbalm.utils.CharacterUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.dwin.navy.serialportapi.SerailPortOpt;
import com.ybg.rp.vm.serial.BeanTrackSet;
import com.ybg.rp.vm.serial.factory.OperaBase;
import com.ybg.rp.vm.util.Config;

/**
 * 以勒
 *
 * 包   名:     com.ybg.rp.vm.serialmanage
 * 类   名:     YLCabinet
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/1 0001 10:30
 * 作   者:     yuyucheng
 */
public class YLCabinet extends OperaBase {
    //private final Logger log = Logger.getLogger(YLCabinet.class);
    public YLCabinet(SerailPortOpt serialPort) {
        super(serialPort);
    }

    @Override
    public BeanTrackSet operaMachines(byte[] command) {
        BeanTrackSet var5 = new BeanTrackSet();
        var5.errorinfo = "格子柜打开失败";
        var5.trackstatus = 0;
        for (int k = 1; k <= 5; k++) {
            synchronized (this) {
                if (this.serialPort.mFd != null) {
                    this.rxByteArray = null;
                    /** 打开格子柜*/
                    serialPort.writeBytes(command);
                    String output = CharacterUtil.bytesToHexString(command, command.length);
                    TbLog.i("发送格子柜(出货)：" + output);
                    for (int i = 0; i < 80; i++) {
                        SystemClock.sleep(10L);
                        if (this.rxByteArray != null) {
                            String str = CharacterUtil.bytesToHexString(this.rxByteArray, this.rxByteArray.length);
                            TbLog.i("接收到数据(打开格子柜)：" + str);
                            if ((this.rxByteArray.length == 8) || (this.rxByteArray.length == 16) || (this.rxByteArray.length == 24)
                                    || (this.rxByteArray.length == 32)) {
                                int var = 3;
                                switch (this.rxByteArray.length) {
                                    case 16:
                                        var = 11;
                                        break;
                                    case 24:
                                        var = 19;
                                        break;
                                    case 32:
                                        var = 27;
                                        break;
                                }
                                if (this.rxByteArray[var] == 98) {
                                    var5.errorinfo = "格子柜打开成功";
                                    var5.trackstatus = 1;
                                    TbLog.i("[-打开格子柜成功- ");
                                    return var5;
                                } else {
                                    TbLog.e("[-串口接收未知数据-错误打开- " + str);
                                    break;
                                }
                            } else {
                                TbLog.i("[-验证通讯收到的数据的长度不对：" + this.rxByteArray.length + ";  --  " + i);
                            }
                        }
                    }
                }
            }
            SystemClock.sleep(Config.CYCLE_INTERVAL);
        }
        return var5;
    }

    @Override
    public byte[] openCommand(String trackNo) {
        return new byte[0];
    }

}

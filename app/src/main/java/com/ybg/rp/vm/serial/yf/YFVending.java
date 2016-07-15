package com.ybg.rp.vm.serial.yf;

import android.content.Context;
import android.os.SystemClock;

import com.cnpay.tigerbalm.utils.CharacterUtil;
import com.cnpay.tigerbalm.utils.PreferencesUtil;
import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.dwin.navy.serialportapi.SerailPortOpt;
import com.ybg.rp.vm.serial.BeanTrackSet;
import com.ybg.rp.vm.serial.factory.OperaBase;
import com.ybg.rp.vm.util.helper.TrackManager;

import org.apache.log4j.Logger;

/**
 * 易丰主机 发送指令操作
 *
 * 包   名:     com.ybg.rp.vm.serial.yf
 * 类   名:     YFVending
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/4 0004 11:32
 * 作   者:     yuyucheng
 */
public class YFVending extends OperaBase{
    private final Logger log = Logger.getLogger(YFVending.class);
    private Context mContext;
    public YFVending(SerailPortOpt serialPort,Context context) {
        super(serialPort);
        this.mContext=context;
    }

    @Override
    public BeanTrackSet operaMachines(byte[] command) {
        BeanTrackSet var5 = new BeanTrackSet();
        var5.trackstatus = 0;
        var5.errorinfo = "发送出货命令，对方无应答。";
        if (command.length==0){//指令长度为0
            var5.trackstatus = 0;
            var5.errorinfo = "轨道不存在-出货";
            return var5;
        }
        /** 发送出货指令是否返回成功*/
        if (this.serialPort.mFd != null) {
            this.rxByteArray = null;
            if (null == serialPort || null == serialPort.mFd) {
                var5.trackstatus = 0;
                var5.errorinfo = "串口未打开-出货";
                return var5;
            }
            //将指定字符串， 以每两个字符分割转换为 16 进制形式
            serialPort.writeBytes(command);
            String strcommand = CharacterUtil.bytesToHexString(command, command.length);
            TbLog.i("发送指令完成(出货)：" + strcommand);
            log.info("发送指令完成(出货)：" + strcommand);
            for (int i = 0; i < 1500; i++) {
                SystemClock.sleep(75L);
                if (this.rxByteArray != null) {
                    String str = CharacterUtil.bytesToHexString(this.rxByteArray, this.rxByteArray.length);
                    TbLog.i("接收到数据(出货)：" + str);
                    if (rxByteArray.length == 16) {
                        if (rxByteArray[13] != 0) {
                            var5.trackstatus = 0;
                            var5.errorinfo = "出货错误";
                            if (rxByteArray[13] != 15) {
                                var5.errorinfo = "没检测到轨道信息";
                            }
                        } else {
                            var5.trackstatus = 1;
                            var5.errorinfo = "出货成功";
                        }
                        break;
                    }
                }
            }
        }
        return var5;
    }

    @Override
    public byte[] openCommand(String track) {
        /**获取真实轨道*/
        String trackNo = new TrackManager().getRealTrack(track);
        if (!StrUtil.isEmpty(trackNo)){
            int column = Integer.parseInt(trackNo);
            return vendingCommand(vendingSN(mContext),column);
        }
        return new byte[0];
    }

    /**
     * 易丰-主机发货
     * @param sn     序列号
     * @param column 柜门地址
     */
    public byte[] vendingCommand(int sn, int column) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) 0xed;
        bytes[1] = (byte) 0x08;
        bytes[2] = (byte) sn;
        bytes[3] = (byte) 0x70;
        bytes[4] = (byte) column;
        bytes[5] = (byte) 0x00;
        bytes[6] = (byte) 0x01; //出货确认 GOC(出货检测) （00：关闭,01：开启）
        bytes[7] = (byte) (bytes[0] ^ bytes[1] ^ bytes[2] ^ bytes[3] ^ bytes[4] ^ bytes[5] ^ bytes[6]);
        return bytes;
    }

    /**
     * 主机-序列号，叠加
     * 1~255之间
     *
     * @param context
     * @return
     */
    public int vendingSN(Context context) {
        int sn = PreferencesUtil.getInt("VENDING_COMMAND_SN", context, "SN", 1);
        if (sn == 255) {
            sn = 1;
        }
        PreferencesUtil.putInt("VENDING_COMMAND_SN", context, "SN", (sn + 1));
        return sn;
    }
}

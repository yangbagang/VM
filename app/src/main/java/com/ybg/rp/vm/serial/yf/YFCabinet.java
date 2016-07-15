package com.ybg.rp.vm.serial.yf;

import android.os.SystemClock;

import com.cnpay.tigerbalm.utils.CharacterUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.dwin.navy.serialportapi.SerailPortOpt;
import com.ybg.rp.vm.serial.BeanTrackSet;
import com.ybg.rp.vm.serial.factory.OperaBase;
import com.ybg.rp.vm.util.Config;

import org.apache.log4j.Logger;

/**
 * 格子柜
 * <p/>
 * 包            名:      com.cnpay.ppvending.comm.sp.serial
 * 类            名:      YFCabinet
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/14
 */
public class YFCabinet extends OperaBase {
    private final Logger log = Logger.getLogger(YFCabinet.class);
    public YFCabinet(SerailPortOpt serialPort) {
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
                    log.info("发送格子柜:(出货)：" + output);
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

    /**
     * 格子柜开启指令
     *
     * @param trackNo 轨道编号
     */
    @Override
    public byte[] openCommand(String trackNo) {
        // 格子柜-主板编号
        int caNo = Integer.parseInt(trackNo.substring(0, 1));
        // 格子柜-柜门编号
        int coNum = Integer.parseInt(trackNo.substring(1, trackNo.length()));

        int crc;
        //发送十六进制
        byte content[] = new byte[9];
        content[0] = (byte) 0xc7;
        content[1] = (byte) 0x07;
        content[2] = (byte) (caNo - 1);
        content[3] = (byte) 0x52;//(开启)
        //content[3] = (byte) 0x51;//(查询)
        content[4] = (byte) (caNo - 1);
        content[5] = (byte) 0x05;
        content[6] = (byte) coNum;
        crc = calc_crc(content, (short) 7); //计算校验值
        content[7] = (byte) ((crc & 0xff00) >> 8);
        content[8] = (byte) (crc & 0x00ff);
        return content;
    }


    /**
     * CRC16 校验
     *
     * @param msg 数据
     * @param len 长度
     * @return 校验值
     */
    private static short calc_crc(byte[] msg, short len) {
        short i, j, crc = 0, current;
        for (i = 0; i < len; i++) {
            current = (short) (msg[i] << 8);
            for (j = 0; j < 8; j++) {
                if ((short) (crc ^ current) < 0)
                    crc = (short) ((crc << 1) ^ 0x1021);
                else
                    crc <<= 1;
                current <<= 1;
            }
        }
        return crc;
    }
}

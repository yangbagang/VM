package com.ybg.rp.vm.serial.factory;

import android.content.Context;

import com.dwin.navy.serialportapi.SerailPortOpt;
import com.ybg.rp.vm.entity.db.SerialBrand;
import com.ybg.rp.vm.serial.yf.YFCabinet;
import com.ybg.rp.vm.serial.yf.YFVending;

/**
 * 包   名:     com.ybg.rp.vm.serial.factory
 * 类   名:     OperaFactory
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/4 0004 11:39
 * 作   者:     yuyucheng
 */
public class OperaFactory {
    private Context mContext;

    public OperaFactory(Context context) {
        this.mContext = context;
    }

    /**
     * 创建主机窜口对象
     */
    public OperaBase createMain(String brand_main) {
        if (brand_main.equals(SerialBrand.yifeng)) {
            return getYFMain();
        } else if (brand_main.equals(SerialBrand.yile)) {

        } else if (brand_main.equals(SerialBrand.fushi)) {

        }
        return getYFMain();
    }

    /**
     * 创建格子柜窜口对象
     */
    public OperaBase createCabinet(String brand_cabinet) {
        if (brand_cabinet.equals(SerialBrand.yifeng)) {
            return getYFCabinet();
        } else if (brand_cabinet.equals(SerialBrand.yile)) {

        } else if (brand_cabinet.equals(SerialBrand.fushi)) {

        }
        return getYFCabinet();
    }


    /**
     * 获取易丰主机
     */
    private OperaBase getYFMain() {

        SerailPortOpt serialPort = new SerailPortOpt();

        /** tty + "S0", "S1", "S2", "S3", "S4", "USB0", "USB1""*/
        serialPort.mDevNum = "S1";// 串口号
        /** "115200", "57600", "38400", "19200", "9600", "4800", "2400", "1200",
         "300", ( 115200, 57600, 38400, 19200, 9600, 4800, 2400, 1200, 300)*/
        serialPort.mSpeed = 9600;// 波特率
        /** "5", "6", "7", "8" ( 5, 6, 7, 8 )*/
        serialPort.mDataBits = 8;// 数据位
        /** "1", "2 (1, 2)*/
        serialPort.mStopBits = 1;// 停止位
        /** "None", "Odd", "Even", "Mark", "Space"  ('n', 'o', 'e', 'm', 's')*/
        serialPort.mParity = 'n';// 检验位

        return new YFVending(serialPort,mContext);
    }

    /**
     * 获取易丰格子柜
     */
    private OperaBase getYFCabinet() {

        SerailPortOpt serialPort = new SerailPortOpt();

        /** tty + "S0", "S1", "S2", "S3", "S4", "USB0", "USB1""*/
        serialPort.mDevNum = "S2";// 串口号
        /** "115200", "57600", "38400", "19200", "9600", "4800", "2400", "1200",
         "300", ( 115200, 57600, 38400, 19200, 9600, 4800, 2400, 1200, 300)*/
        serialPort.mSpeed = 9600;// 波特率
        /** "5", "6", "7", "8" ( 5, 6, 7, 8 )*/
        serialPort.mDataBits = 8;// 数据位
        /** "1", "2 (1, 2)*/
        serialPort.mStopBits = 2;// 停止位
        /** "None", "Odd", "Even", "Mark", "Space"  ('n', 'o', 'e', 'm', 's')*/
        serialPort.mParity = 'n';// 检验位

        return new YFCabinet(serialPort);

    }
}

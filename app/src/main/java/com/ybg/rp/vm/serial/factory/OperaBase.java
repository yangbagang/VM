package com.ybg.rp.vm.serial.factory;

import android.os.SystemClock;

import com.cnpay.tigerbalm.utils.GMethod;
import com.dwin.navy.serialportapi.SerailPortOpt;
import com.ybg.rp.vm.serial.BeanTrackSet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 包   名:     com.ybg.rp.vm.serial.factory
 * 类   名:     OperaBase
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/4 0004 11:36
 * 作   者:     yuyucheng
 */
public abstract class OperaBase {
    private SimpleDateFormat formathms;
    public byte[] rxByteArray;
    public SerailPortOpt serialPort;
    public ReadThread mReadThread;
    /**线程停止标识*/
    private boolean readThreadisRunning;

    public OperaBase(SerailPortOpt serialPort) {
        this.serialPort = serialPort;

        this.formathms = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        this.rxByteArray = null;
        this.readThreadisRunning = false;
    }

    /**
     * 打开串口
     */
    public void openSerialPort() {
        if (this.serialPort.mFd == null) {
            this.serialPort.openDev(this.serialPort.mDevNum);
            this.serialPort.setSpeed(this.serialPort.mFd, this.serialPort.mSpeed);
            this.serialPort.setParity(this.serialPort.mFd, this.serialPort.mDataBits, this.serialPort.mStopBits, this.serialPort.mParity);
            this.readThreadisRunning = true;

            this.mReadThread = new ReadThread();
            this.mReadThread.setName("串口线程：" + this.formathms.format(new Date(System.currentTimeMillis())));
            this.mReadThread.start();
        }
    }

    /**
     * 发送指令和接收
     *
     * @param command 操作指令
     */
    public abstract BeanTrackSet operaMachines(byte[] command);

    /**
     * 拼接指令
     *
     * @param trackNo 指定轨道
     */
    public abstract byte[] openCommand(String trackNo);

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        if (this.mReadThread != null) {
            this.readThreadisRunning = false;
            //SystemClock.sleep(100L);
        }
        if (this.mReadThread != null && !this.mReadThread.isInterrupted()) {
            this.mReadThread.interrupt();
            this.mReadThread = null;
            System.gc();
        }
        if (this.serialPort.mFd != null) {
            this.serialPort.closeDev(this.serialPort.mFd);
            this.serialPort = null;
        }
    }


    /**
     * 接收指令线程
     */
    private class ReadThread extends Thread {
        byte[] buf;
        byte[] rxByteArrayTemp;

        private ReadThread() {
            this.buf = new byte[128];
            this.rxByteArrayTemp = null;
        }

        public void run() {
            super.run();
            for (; readThreadisRunning && this.buf != null; SystemClock.sleep(5L)) {
                int var1 = serialPort.readBytes(this.buf);
                if (var1 > 0) {
                    this.rxByteArrayTemp = GMethod.ArrayAppend(this.rxByteArrayTemp, this.buf, var1);
                } else if (this.rxByteArrayTemp != null) {
                    //rxByteArray = GMethod.ArrayAppend(rxByteArrayTemp, (byte[]) null);
                    rxByteArray = GMethod.ArrayAppend(rxByteArray, rxByteArrayTemp);
                    this.rxByteArrayTemp = null;
                }
            }
        }
    }
}

package com.ybg.rp.vm.serial;

import android.content.Context;

import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.entity.db.SerialBrand;
import com.ybg.rp.vm.serial.factory.OperaBase;
import com.ybg.rp.vm.serial.factory.OperaFactory;
import com.ybg.rp.vm.util.helper.EntityDBUtil;

import org.apache.log4j.Logger;

/**
 * 包   名:     com.ybg.rp.vm.serialmanage
 * 类   名:     SerialManager
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/1 0001 15:42
 * 作   者:     yuyucheng
 */
public class SerialManager {
    private final Logger log = Logger.getLogger(SerialManager.class);
    private static SerialManager manager;
    private Context mContext;
    private EntityDBUtil dbUtil;
    /**
     * 当前使用的串口品牌
     */
    private String brand_main;
    private String brand_cabinet;

    private OperaBase operate;


    public static SerialManager getInstance(Context context) {
        if (null == manager) {
            manager = new SerialManager(context);
        }
        return manager;
    }

    public SerialManager(Context context) {
        this.dbUtil = EntityDBUtil.getInstance();
        this.mContext=context;
        loadData();
    }


    /**
     * 初始化
     * 获取当前设置的品牌
     */
    private void loadData() {
        try {
            /**1：格子柜,0：不是格子柜*/
            /**获取主机选择的品牌*/
            SerialBrand main_b = dbUtil.getDb().selector(SerialBrand.class).where("GRID_MARK", "=", "0").findFirst();
            if (main_b == null) {
                main_b = new SerialBrand();
                main_b.setGridMark(0);
                main_b.setBrand(SerialBrand.yifeng);
                dbUtil.saveOrUpdate(main_b);

                this.brand_main = SerialBrand.yifeng;
            }
            this.brand_main = main_b.getBrand();

            /**获取格子柜的选择品牌*/
            SerialBrand cabinet_b = dbUtil.getDb().selector(SerialBrand.class).where("GRID_MARK", "=", "1").findFirst();
            if (cabinet_b == null) {
                cabinet_b = new SerialBrand();
                cabinet_b.setGridMark(1);
                cabinet_b.setBrand(SerialBrand.yifeng);
                dbUtil.saveOrUpdate(cabinet_b);

                this.brand_cabinet = SerialBrand.yifeng;
            }
            this.brand_cabinet = cabinet_b.getBrand();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 创造串口
     *
     * @param type 1:主机 2: 格子机
     */
    public void createSerial(int type) {
        if (this.operate!=null){
            closeSerial();
        }
        /**打开新串口*/
        if (type == 1) {
            this.operate = new OperaFactory(mContext).createMain(this.brand_main);
        } else if (type == 2) {
            this.operate = new OperaFactory(mContext).createCabinet(this.brand_cabinet);
        }
        /** 打开串口 */
        this.operate.openSerialPort();
    }


    /**
     * 关闭串口
     */
    public void closeSerial() {
        this.operate.closeSerialPort();
        this.operate=null;
    }

    /**
     * 机器开门
     * 发送指令和接收
     *
     * @param track 指定轨道
     */
    public BeanTrackSet openMachineTrack(String track) {
        TbLog.i("打开机器轨道:" + track);
        log.info("打开机器轨道:" + track);
        BeanTrackSet var5 = new BeanTrackSet();
        if (StrUtil.isEmpty(track)) {
            var5.trackstatus = 0;
            var5.errorinfo = "轨道不存在-出货";
            return var5;
        }

        var5 = this.operate.operaMachines(operate.openCommand(track));
        return var5;
    }

}

package com.ybg.rp.vm.util;

import com.cnpay.tigerbalm.utils.SimpleUtil;

/**
 * 包            名:      com.ybg.rp.vm.util
 * 类            名:      Config
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/3
 */
public interface Config {

    /**
     * 通用网络请求 VEMManage/
     */
    String URL_COMM = "http://192.168.12.100:8080/";   //测试

//    String URL_COMM = "http://183.57.41.230/ua/";// 生产地址

    /** 网路请求-返回JSON 关键字*/
    String TRUE = "true";
    String SUCCESS = "success";
    String ERROR = "msg";

    /** 数据库名称*/
    String DB_NAME = "YBG_VM_DB";
    /** 数据库初始版本*/
    int DB_VERSION = 10;

    /**
     * 数据上传不成功，请求次数
     */
    int HTTP_ERROR_REQUEST_COUNT = 5;
    /**
     * 数据上传间隔
     */
    long UO_DATA_INTERVAL = 1000;

    /**
     * 机器打开，循环间隔
     */
    long CYCLE_INTERVAL = 1000;

    /**
     * 缓存文件名
     */
    String CACHE_FILENAME = "CACHE_VM";

    /**
     * 支付查询请求间隔时间 1秒
     */
    long PAY_INTERVAL = 1000;


    String BASE_PATH = SimpleUtil.getSDCardPath() + "/ybg_vm";
    String PATH = BASE_PATH + "/lastestApk/";   //新版apk保存路劲
    String LOG4J_PATH = BASE_PATH + "/log/info/";

    String PREFERENCES = "ybg_vm_preferences";
}
